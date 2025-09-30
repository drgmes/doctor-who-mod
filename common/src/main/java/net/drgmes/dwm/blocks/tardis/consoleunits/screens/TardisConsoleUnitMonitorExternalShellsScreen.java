package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriorEntry;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriors;
import net.drgmes.dwm.network.server.TardisConsoleUnitMonitorExternalShellApplyPacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.base.screens.elements.BaseButton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec2f;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class TardisConsoleUnitMonitorExternalShellsScreen extends BaseTardisConsoleUnitMonitorScreen {
    private final Screen parentScreen;
    private final String tardisId;
    private final String currentExteriorTypeId;

    private boolean isInited;
    private int tick = 180;

    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;

    private ExternalShellsListWidget externalShellsListWidget;
    private ExternalShellsListWidget.ExternalShellEntry selected = null;

    public TardisConsoleUnitMonitorExternalShellsScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, String tardisId, NbtCompound tag, @Nullable Screen parentScreen) {
        super(DWM.TEXTS.MONITOR_EXTERNAL_SHELLS_TITLE, tardisConsoleUnitBlockEntity);

        this.parentScreen = parentScreen;
        this.tardisId = tardisId;
        this.currentExteriorTypeId = tag.getCompound("tardisTag").getString("exteriorType");
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return true;
    }

    @Override
    protected void init() {
        super.init();

        this.externalShellsListWidget = new ExternalShellsListWidget(this, this.getExternalShellsListPos(), this.getExternalShellsListSize());

        Vector2i acceptButtonPos = this.getRightBottomRenderPos(BUTTON_SIZE + SCREEN_MARGIN, BUTTON_SIZE + SCREEN_MARGIN);
        this.acceptButton = new BaseButton(acceptButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_EXTERNAL_SHELLS_ACCEPT, DWM.TEXTURES.GUI.COMMON.ELEMENTS.ACCEPT, (b) -> {
            this.apply();
        });

        Vector2i cancelButtonPos = acceptButtonPos.add(-BUTTON_SIZE - BUTTON_MARGIN, 0);
        this.cancelButton = new BaseButton(cancelButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_EXTERNAL_SHELLS_CANCEL, DWM.TEXTURES.GUI.COMMON.ELEMENTS.CANCEL, (b) -> {
            this.back();
        });

        this.addDrawableChild(this.externalShellsListWidget);
        this.addDrawableChild(this.acceptButton);
        this.addDrawableChild(this.cancelButton);

        this.isInited = true;
        this.update();
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        super.resize(mc, width, height);
        this.externalShellsListWidget.refreshList();
    }

    @Override
    public void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderAdditional(context, mouseX, mouseY, delta);
        if (this.selected == null) return;

        MatrixStack matrixStack = context.getMatrices();
        VertexConsumerProvider buffer = context.getVertexConsumers();
        Vector2i externalShellsListSize = this.getExternalShellsListSize();

        float scale = 40F;
        int modelX = externalShellsListSize.x + (this.getBackgroundSize().x - externalShellsListSize.x) / 2;
        int modelY = this.getBackgroundBorderSize().y + (externalShellsListSize.y - BUTTON_SIZE - SCREEN_MARGIN * 2) / 2;

        Vector2i modelOffset = this.getRenderPos(modelX, modelY);
        Vec2f pos = new Vec2f(modelOffset.x, modelOffset.y).multiply(1 / scale);

        BlockState blockState = this.selected.exterior.getBlock().getDefaultState();
        BlockEntity blockEntity = this.selected.exterior.getBlockEntityType().instantiate(BlockPos.ORIGIN, blockState);
        BlockEntityRenderer<BlockEntity> blockEntityRenderer = this.client.getBlockEntityRenderDispatcher().get(blockEntity);

        matrixStack.push();
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(pos.x, pos.y, 100);
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(22.5F));
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(this.tick));
        matrixStack.translate(-0.5, -1.15, -0.5);
        RenderSystem.enableCull();
        blockEntityRenderer.render(blockEntity, delta, matrixStack, buffer, LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
        RenderSystem.disableCull();
        matrixStack.pop();
    }

    @Override
    public void tick() {
        this.tick = (this.tick + 1) % 360;
        this.externalShellsListWidget.setSelected(this.selected);
    }

    @Override
    public void apply() {
        if (this.selected != null && !this.selected.exterior.name.equals(this.currentExteriorTypeId)) {
            new TardisConsoleUnitMonitorExternalShellApplyPacket(this.tardisId, this.selected.exterior.name).sendToServer();
        }

        super.apply();
    }

    @Override
    public void back() {
        if (this.parentScreen != null) this.client.setScreen(this.parentScreen);
        else super.back();
    }

    private Vector2i getExternalShellsListPos() {
        return this.getLeftTopRenderPos(0, 0);
    }

    private Vector2i getExternalShellsListSize() {
        return new Vector2i(125, this.getBackgroundSize().y - this.getBackgroundBorderSize().y * 2);
    }

    private void update() {
        if (!this.isInited) return;
        this.acceptButton.active = this.selected != null && !this.selected.exterior.name.equals(this.currentExteriorTypeId);
    }

    private void setSelected(ExternalShellsListWidget.ExternalShellEntry entry) {
        this.selected = entry;
        this.update();
    }

    private static class ExternalShellsListWidget extends BaseListWidget {
        private final TardisConsoleUnitMonitorExternalShellsScreen parent;

        public ExternalShellsListWidget(TardisConsoleUnitMonitorExternalShellsScreen parent, Vector2i pos, Vector2i size) {
            super(parent.client, pos, size, LINE_HEIGHT);
            this.parent = parent;
            this.init();
        }

        @Override
        public Vector2f getScale() {
            return this.parent.cachedScale;
        }

        @Override
        public void refreshList() {
            super.refreshList();

            TardisExteriors.TYPES.forEach((key, exterior) -> {
                ExternalShellEntry entry = new ExternalShellEntry(exterior);
                this.addEntry(entry);

                if (Objects.equals(key, this.parent.currentExteriorTypeId)) {
                    this.setSelected(entry);
                    this.parent.setSelected(entry);
                }
            });
        }

        private class ExternalShellEntry extends BaseListEntry {
            private final TardisExteriorEntry exterior;

            public ExternalShellEntry(TardisExteriorEntry exterior) {
                super(Formatting.WHITE, Formatting.GOLD);
                this.exterior = exterior;
            }

            @Override
            public Text getText() {
                return this.exterior.getTitle();
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int delta) {
                ExternalShellsListWidget.this.parent.setSelected(this);
                return super.mouseClicked(mouseX, mouseY, delta);
            }
        }
    }
}

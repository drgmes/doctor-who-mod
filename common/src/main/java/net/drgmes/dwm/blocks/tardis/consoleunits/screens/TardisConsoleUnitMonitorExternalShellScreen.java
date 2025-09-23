package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriorEntry;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriors;
import net.drgmes.dwm.network.server.TardisConsoleUnitMonitorExternalShellApplyPacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.helpers.RenderHelper;
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
import org.joml.Vector2i;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class TardisConsoleUnitMonitorExternalShellScreen extends BaseTardisConsoleUnitMonitorScreen {
    protected static final int LINE_PADDING = 3;

    private final Screen parentScreen;
    private final String tardisId;
    private final String currentExteriorTypeId;

    private int tick = 180;
    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;

    private ExternalShellsListWidget externalShellsListWidget;
    private ExternalShellsListWidget.ExternalShellEntry selected = null;

    public TardisConsoleUnitMonitorExternalShellScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, String tardisId, NbtCompound tag, @Nullable Screen parentScreen) {
        super(DWM.TEXTS.MONITOR_DATA_EXTERIOR, tardisConsoleUnitBlockEntity);

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

        Vector2i externalShellsListSize = this.getExternalShellsListSize();
        Vector2i externalShellsListPos = this.getExternalShellsListPos();
        this.externalShellsListWidget = new ExternalShellsListWidget(this, externalShellsListSize.x, externalShellsListSize.y, externalShellsListPos);

        int buttonWidth = (this.getBackgroundSize().x - externalShellsListSize.x) / 2 - this.getBackgroundBorderSize().x - 2;
        int buttonOffsetY = externalShellsListPos.y + externalShellsListSize.y - BUTTON_HEIGHT - 1;
        int buttonOffsetX = externalShellsListPos.x + externalShellsListSize.x + 2;

        this.cancelButton = RenderHelper.getButtonWidget(buttonOffsetX, buttonOffsetY, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_EXTERNAL_SHELLS_CANCEL, (b) -> {
            if (this.parentScreen != null) this.client.setScreen(this.parentScreen);
            else this.close();
        });

        this.acceptButton = RenderHelper.getButtonWidget(buttonOffsetX + buttonWidth + 1, buttonOffsetY, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.MONITOR_EXTERNAL_SHELLS_ACCEPT, (b) -> {
            this.apply();
        });

        this.addDrawableChild(this.externalShellsListWidget);
        this.addDrawableChild(this.cancelButton);
        this.addDrawableChild(this.acceptButton);

        this.updateAcceptButton();
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        super.resize(mc, width, height);
        this.externalShellsListWidget.refreshList();
    }

    @Override
    public void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderAdditional(context, mouseX, mouseY, delta);

        int externalShellsListBackgroundColor = 0x40000000;
        Vector2i externalShellsListPos = this.getExternalShellsListPos();
        Vector2i externalShellsListSize = this.getExternalShellsListSize();
        context.fillGradient(externalShellsListPos.x, externalShellsListPos.y, externalShellsListPos.x + externalShellsListSize.x, externalShellsListPos.y + externalShellsListSize.y, externalShellsListBackgroundColor, externalShellsListBackgroundColor);

        if (this.selected == null) return;

        MatrixStack matrixStack = context.getMatrices();
        VertexConsumerProvider buffer = context.getVertexConsumers();

        float scale = 40F;
        int modelX = externalShellsListSize.x + (this.getBackgroundSize().x - externalShellsListSize.x) / 2;
        int modelY = this.getBackgroundBorderSize().y + (externalShellsListSize.y - BUTTON_HEIGHT) / 2;

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

    private Vector2i getExternalShellsListPos() {
        return this.getRenderPos(this.getBackgroundBorderSize().x, this.getBackgroundBorderSize().y);
    }

    private Vector2i getExternalShellsListSize() {
        return new Vector2i(125, this.getBackgroundSize().y - this.getBackgroundBorderSize().y * 2);
    }

    protected void setSelected(ExternalShellsListWidget.ExternalShellEntry entry) {
        this.selected = entry;
        this.updateAcceptButton();
    }

    private void updateAcceptButton() {
        this.acceptButton.active = this.selected != null && !this.selected.exterior.name.equals(this.currentExteriorTypeId);
    }

    private static class ExternalShellsListWidget extends BaseListWidget {
        private final TardisConsoleUnitMonitorExternalShellScreen parent;

        public ExternalShellsListWidget(TardisConsoleUnitMonitorExternalShellScreen parent, int width, int height, Vector2i pos) {
            super(parent.client, width, height, LINE_PADDING, pos);
            this.parent = parent;
            this.init();
        }

        public void refreshList() {
            super.refreshList();

            TardisExteriors.TYPES.forEach((key, exterior) -> {
                ExternalShellEntry entry = new ExternalShellEntry(exterior);
                this.addEntry(entry);

                if (Objects.equals(key, this.parent.currentExteriorTypeId)) {
                    this.setSelected(entry);
                    this.parent.selected = entry;
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

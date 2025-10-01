package net.drgmes.dwm.blocks.tardis.misc.tardisteleporter.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.enums.TardisTeleporterEntityTypes;
import net.drgmes.dwm.network.server.TardisTeleporterApplyPacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.base.screens.BaseScreen;
import net.drgmes.dwm.utils.base.screens.elements.BaseButton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class TardisTeleporterScreen extends BaseScreen {
    private BlockPos blockPos;
    private BlockPos destinationBlockPos;
    private boolean isLocked;

    private List<TardisTeleporterEntityTypes> allowedEntityTypes;

    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;

    private TextFieldWidget xField;
    private TextFieldWidget yField;
    private TextFieldWidget zField;

    private EntityTypesListWidget entityTypesListWidget;

    public TardisTeleporterScreen(BlockPos blockPos, BlockPos destinationBlockPos, boolean isLocked, List<TardisTeleporterEntityTypes> allowedEntityTypes) {
        super(DWM.TEXTS.TARDIS_TELEPORTER_INTERFACE_TITLE);

        this.blockPos = blockPos;
        this.destinationBlockPos = destinationBlockPos;
        this.isLocked = isLocked;
        this.allowedEntityTypes = allowedEntityTypes;
    }

    @Override
    public Identifier getBackground() {
        return DWM.TEXTURES.GUI.TARDIS.TELEPORTER.INTERFACE;
    }

    @Override
    public Vector2i getBackgroundOriginSize() {
        return DWM.TEXTURES.GUI.TARDIS.TELEPORTER.INTERFACE_SIZE;
    }

    @Override
    public Vector2i getBackgroundBorderOriginSize() {
        return new Vector2i(14, 27);
    }

    @Override
    public Vector2i getTitleRenderPos() {
        return this.getRenderPos(12, 7);
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return !this.xField.isFocused() && !this.yField.isFocused() && !this.zField.isFocused();
    }

    @Override
    protected void init() {
        int coordsFieldWidth = this.getCoordsFieldWidth();

        Vector2i entityTypesListPos = this.getEntityTypesListPos();
        Vector2i entityTypesListSize = this.getEntityTypesListSize();
        this.entityTypesListWidget = new EntityTypesListWidget(this, entityTypesListPos, entityTypesListSize);

        Vector2i xFieldPos = entityTypesListPos.add(entityTypesListSize.x + SCREEN_MARGIN + 1, this.textRenderer.fontHeight + SCREEN_MARGIN);
        this.xField = new TextFieldWidget(this.textRenderer, xFieldPos.x, xFieldPos.y, coordsFieldWidth, INPUT_HEIGHT, Text.literal("X"));
        this.xField.setText(String.valueOf(this.destinationBlockPos.getX()));
        this.xField.setEditable(!this.isLocked);

        Vector2i yFieldPos = xFieldPos.add(coordsFieldWidth + INPUT_MARGIN, 0);
        this.yField = new TextFieldWidget(this.textRenderer, yFieldPos.x, yFieldPos.y, coordsFieldWidth, INPUT_HEIGHT, Text.literal("Y"));
        this.yField.setText(String.valueOf(this.destinationBlockPos.getY()));
        this.yField.setEditable(!this.isLocked);

        Vector2i zFieldPos = yFieldPos.add(coordsFieldWidth + INPUT_MARGIN, 0);
        this.zField = new TextFieldWidget(this.textRenderer, zFieldPos.x, zFieldPos.y, coordsFieldWidth, INPUT_HEIGHT, Text.literal("Z"));
        this.zField.setText(String.valueOf(this.destinationBlockPos.getZ()));
        this.zField.setEditable(!this.isLocked);

        Vector2i acceptButtonPos = this.getRightBottomRenderPos(BUTTON_SIZE + SCREEN_MARGIN, BUTTON_SIZE + SCREEN_MARGIN);
        this.acceptButton = new BaseButton(acceptButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.TARDIS_TELEPORTER_INTERFACE_BTN_ACCEPT, DWM.TEXTURES.GUI.COMMON.ELEMENTS.ACCEPT, (b) -> {
            this.apply();
        });

        Vector2i cancelButtonPos = acceptButtonPos.add(-BUTTON_SIZE - BUTTON_MARGIN, 0);
        this.cancelButton = new BaseButton(cancelButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.TARDIS_TELEPORTER_INTERFACE_BTN_CANCEL, DWM.TEXTURES.GUI.COMMON.ELEMENTS.CANCEL, (b) -> {
            this.back();
        });

        this.addDrawableChild(this.entityTypesListWidget);
        this.addDrawableChild(this.cancelButton);
        this.addDrawableChild(this.acceptButton);

        if (this.isLocked) {
            this.addDrawable(this.xField);
            this.addDrawable(this.yField);
            this.addDrawable(this.zField);
        }
        else {
            this.addDrawableChild(this.xField);
            this.addDrawableChild(this.yField);
            this.addDrawableChild(this.zField);
        }
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        super.resize(mc, width, height);
        this.entityTypesListWidget.refreshList();
    }

    @Override
    public void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderAdditional(context, mouseX, mouseY, delta);

        int coordsFieldWidth = this.getCoordsFieldWidth();

        Vector2i entityTypesTitlePos = this.getLeftTopRenderPos(SCREEN_MARGIN, SCREEN_MARGIN + 2);
        context.drawText(this.textRenderer, DWM.TEXTS.TARDIS_TELEPORTER_INTERFACE_ENTITY_TYPES, entityTypesTitlePos.x, entityTypesTitlePos.y, 0xE0E0E0, true);

        Vector2i coordsTitlePos = this.getEntityTypesListPos().add(this.getEntityTypesListSize().x + SCREEN_MARGIN + 1, 0);
        context.drawText(this.textRenderer, DWM.TEXTS.TARDIS_TELEPORTER_INTERFACE_COORDS, coordsTitlePos.x, entityTypesTitlePos.y, 0xE0E0E0, true);

        Vector2i xTitlePos = coordsTitlePos.add(0, 3);
        context.drawText(this.textRenderer, Text.literal("X"), xTitlePos.x, xTitlePos.y, 0xE0E0E0, true);

        Vector2i yTitlePos = xTitlePos.add(coordsFieldWidth + INPUT_MARGIN, 0);
        context.drawText(this.textRenderer, Text.literal("Y"), yTitlePos.x, yTitlePos.y, 0xE0E0E0, true);

        Vector2i zTitlePos = yTitlePos.add(coordsFieldWidth + INPUT_MARGIN, 0);
        context.drawText(this.textRenderer, Text.literal("Z"), zTitlePos.x, zTitlePos.y, 0xE0E0E0, true);
    }

    protected void apply() {
        new TardisTeleporterApplyPacket(this.blockPos, this.destinationBlockPos, this.isLocked, this.allowedEntityTypes).sendToServer();
        this.close();
    }

    private Vector2i getEntityTypesListPos() {
        return this.getLeftTopRenderPos(SCREEN_MARGIN, this.client.textRenderer.fontHeight + 10);
    }

    private Vector2i getEntityTypesListSize() {
        return new Vector2i(125, this.getBackgroundSize().y - this.getBackgroundBorderSize().y * 2 - this.client.textRenderer.fontHeight - 10 - SCREEN_MARGIN);
    }

    private int getCoordsFieldWidth() {
        int availableBodyWidth = this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2 - this.getEntityTypesListSize().x - SCREEN_MARGIN * 3;
        return availableBodyWidth / 3 - INPUT_MARGIN / 2;
    }

    private static class EntityTypesListWidget extends BaseListWidget {
        private final TardisTeleporterScreen parent;

        public EntityTypesListWidget(TardisTeleporterScreen parent, Vector2i pos, Vector2i size) {
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
            Arrays.stream(TardisTeleporterEntityTypes.values()).forEach((entityType) -> this.addEntry(new EntityTypeEntry(entityType)));
        }

        private class EntityTypeEntry extends BaseListEntry {
            private final TardisTeleporterEntityTypes entityType;

            public EntityTypeEntry(TardisTeleporterEntityTypes entityType) {
                super(Formatting.GOLD, Formatting.WHITE);
                this.entityType = entityType;
            }

            @Override
            public Text getText() {
                return DWM.TEXTS.TARDIS_TELEPORTER_INTERFACE_ENTITY_TYPE.apply(this.entityType);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (!super.mouseClicked(mouseX, mouseY, button)) return false;

                if (this.isSelected()) EntityTypesListWidget.this.parent.allowedEntityTypes.remove(this.entityType);
                else EntityTypesListWidget.this.parent.allowedEntityTypes.add(this.entityType);
                return true;
            }

            @Override
            public boolean isSelected() {
                return EntityTypesListWidget.this.parent.allowedEntityTypes.contains(this.entityType);
            }

            @Override
            public MutableText getPrependText() {
                return Text.empty().append(Text.literal("[ ] ").formatted(this.chevronFormat, Formatting.BOLD));
            }

            @Override
            public MutableText getSelectedPrependText() {
                return Text.empty().append(Text.literal("[X] ").formatted(this.chevronFormat, Formatting.BOLD));
            }
        }
    }
}

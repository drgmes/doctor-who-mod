package net.drgmes.dwm.blocks.tardis.misc.tardisteleporter.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.enums.TardisTeleporterEntityTypes;
import net.drgmes.dwm.network.server.TardisTeleporterApplyPacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.base.screens.BaseScreen;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.Window;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector2i;

import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class TardisTeleporterScreen extends BaseScreen {
    protected static final int LINE_PADDING = 3;
    protected static final int INPUT_MARGIN = 4;

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
        super(DWM.TEXTS.TARDIS_TELEPORTER_INTERFACE_NAME);

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
    public Vector2i getBackgroundSize() {
        int padding = 5;
        Window window = MinecraftClient.getInstance().getWindow();
        return new Vector2i(window.getScaledWidth() - padding, window.getScaledHeight() - padding);
    }

    @Override
    public Vector2i getBackgroundBorderSize() {
        Vector2i size = this.getBackgroundSize();
        Vector2i originSize = DWM.TEXTURES.GUI.TARDIS.TELEPORTER.INTERFACE_SIZE.div(1 / 0.795F, new Vector2i());
        return new Vector2i((int) Math.floor(12 * ((float) size.x / originSize.x)), (int) Math.floor(22 * ((float) size.y / originSize.y)));
    }

    @Override
    public Vector2i getTitleRenderPos() {
        return this.getRenderPos(20, 7);
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return !this.xField.isFocused() && !this.yField.isFocused() && !this.zField.isFocused();
    }

    @Override
    protected void init() {
        Vector2i entityTypesListSize = this.getEntityTypesListSize();
        Vector2i entityTypesListPos = this.getEntityTypesListPos();
        this.entityTypesListWidget = new EntityTypesListWidget(this, entityTypesListSize.x, entityTypesListSize.y, entityTypesListPos);

        int inputWidth = this.getAvailableBodyWidth() / 3 - INPUT_MARGIN / 2 - 1;
        int inputOffsetY = this.getBackgroundBorderSize().y + this.textRenderer.fontHeight * 4 - 1;
        int inputOffsetX = this.getEntityTypesListPos().x + this.getEntityTypesListSize().x + INPUT_MARGIN;

        Vector2i xFieldPos = this.getRenderPos(inputOffsetX, inputOffsetY);
        this.xField = new TextFieldWidget(this.textRenderer, xFieldPos.x, xFieldPos.y, inputWidth, 18, Text.literal("X"));
        this.xField.setText(String.valueOf(this.destinationBlockPos.getX()));
        this.xField.setEditable(!this.isLocked);

        Vector2i yFieldPos = xFieldPos.add(inputWidth + INPUT_MARGIN, 0);
        this.yField = new TextFieldWidget(this.textRenderer, yFieldPos.x, yFieldPos.y, inputWidth, 18, Text.literal("Y"));
        this.yField.setText(String.valueOf(this.destinationBlockPos.getY()));
        this.yField.setEditable(!this.isLocked);

        Vector2i zFieldPos = yFieldPos.add(inputWidth + INPUT_MARGIN, 0);
        this.zField = new TextFieldWidget(this.textRenderer, zFieldPos.x, zFieldPos.y, inputWidth, 18, Text.literal("Z"));
        this.zField.setText(String.valueOf(this.destinationBlockPos.getZ()));
        this.zField.setEditable(!this.isLocked);

        int buttonWidth = (this.getBackgroundSize().x - entityTypesListSize.x - 6) / 2 - this.getBackgroundBorderSize().x - 2;
        int buttonOffsetY = entityTypesListPos.y + entityTypesListSize.y - BUTTON_HEIGHT + 2;
        int buttonOffsetX = entityTypesListPos.x + entityTypesListSize.x + 3;

        this.cancelButton = RenderHelper.getButtonWidget(buttonOffsetX, buttonOffsetY, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.TARDIS_TELEPORTER_INTERFACE_BTN_CANCEL, (b) -> {
            this.close();
        });

        this.acceptButton = RenderHelper.getButtonWidget(buttonOffsetX + buttonWidth + 1, buttonOffsetY, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.TARDIS_TELEPORTER_INTERFACE_BTN_ACCEPT, (b) -> {
            this.apply();
        });

        this.addDrawableChild(this.entityTypesListWidget);
        this.addDrawableChild(this.cancelButton);
        this.addDrawableChild(this.acceptButton);

        if (this.isLocked) {
            this.addDrawable(this.xField);
            this.addDrawable(this.yField);
            this.addDrawable(this.zField);
        } else {
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

        int inputWidth = this.getAvailableBodyWidth() / 3 - INPUT_MARGIN / 2 - 1;
        int paddingX = this.getBackgroundBorderSize().x + 3;
        int paddingY = this.getBackgroundBorderSize().y + 6;

        Vector2i entityTypesTitlePos = this.getRenderPos(paddingX, paddingY);
        context.drawText(this.textRenderer, DWM.TEXTS.TARDIS_TELEPORTER_INTERFACE_ENTITY_TYPES, entityTypesTitlePos.x, entityTypesTitlePos.y, 0xE0E0E0, true);

        Vector2i coordsTitlePos = this.getRenderPos(this.getEntityTypesListPos().x + this.getEntityTypesListSize().x + INPUT_MARGIN, paddingY);
        context.drawText(this.textRenderer, DWM.TEXTS.TARDIS_TELEPORTER_INTERFACE_COORDS, coordsTitlePos.x, coordsTitlePos.y, 0xE0E0E0, true);

        Vector2i xTitlePos = this.getRenderPos(coordsTitlePos.x - 2, paddingY + this.textRenderer.fontHeight * 2 + 1);
        context.drawText(this.textRenderer, Text.literal("X"), xTitlePos.x, xTitlePos.y, 0xE0E0E0, true);

        Vector2i yTitlePos = xTitlePos.add(inputWidth + INPUT_MARGIN, 0);
        context.drawText(this.textRenderer, Text.literal("Y"), yTitlePos.x, yTitlePos.y, 0xE0E0E0, true);

        Vector2i zTitlePos = yTitlePos.add(inputWidth + INPUT_MARGIN, 0);
        context.drawText(this.textRenderer, Text.literal("Z"), zTitlePos.x, zTitlePos.y, 0xE0E0E0, true);

        int entityTypesListBackgroundColor = 0x40000000;
        Vector2i entityTypesListPos = this.getEntityTypesListPos();
        Vector2i entityTypesListSize = this.getEntityTypesListSize();
        context.fillGradient(entityTypesListPos.x, entityTypesListPos.y, entityTypesListPos.x + entityTypesListSize.x, entityTypesListPos.y + entityTypesListSize.y, entityTypesListBackgroundColor, entityTypesListBackgroundColor);
    }

    protected void apply() {
        new TardisTeleporterApplyPacket(this.blockPos, this.destinationBlockPos, this.isLocked, this.allowedEntityTypes).sendToServer();
        this.close();
    }

    private Vector2i getEntityTypesListPos() {
        int x = this.getBackgroundBorderSize().x + 3;
        int y = this.getBackgroundBorderSize().y + this.client.textRenderer.fontHeight + 10;
        return this.getRenderPos(x, y);
    }

    private Vector2i getEntityTypesListSize() {
        int offset = this.getEntityTypesListPos().y - this.getRenderStartPos().y;
        return new Vector2i(125, this.getBackgroundSize().y - this.getBackgroundBorderSize().y - offset - 5);
    }

    private int getAvailableBodyWidth() {
        return this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2 - this.getEntityTypesListSize().x - INPUT_MARGIN * 2 - 4;
    }

    private static class EntityTypesListWidget extends BaseListWidget {
        private final TardisTeleporterScreen parent;

        public EntityTypesListWidget(TardisTeleporterScreen parent, int width, int height, Vector2i pos) {
            super(parent.client, width, height, LINE_PADDING, pos);
            this.parent = parent;
            this.init();
        }

        public void refreshList() {
            super.refreshList();

            Arrays.stream(TardisTeleporterEntityTypes.values()).forEach((entityType) -> {
                EntityTypesListWidget.EntityTypeEntry entry = new EntityTypesListWidget.EntityTypeEntry(entityType);
                this.addEntry(entry);
            });
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
            public boolean mouseClicked(double mouseX, double mouseY, int delta) {
                if (this.isSelected()) EntityTypesListWidget.this.parent.allowedEntityTypes.remove(this.entityType);
                else EntityTypesListWidget.this.parent.allowedEntityTypes.add(this.entityType);
                return super.mouseClicked(mouseX, mouseY, delta);
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

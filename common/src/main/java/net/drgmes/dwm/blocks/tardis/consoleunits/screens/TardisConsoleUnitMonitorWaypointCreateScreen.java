package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.common.tardis.systems.flight.TardisFlightWaypointEntry;
import net.drgmes.dwm.network.server.TardisConsoleUnitMonitorWaypointCreatePacket;
import net.drgmes.dwm.utils.base.screens.elements.BaseButton;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public class TardisConsoleUnitMonitorWaypointCreateScreen extends BaseTardisConsoleUnitMonitorWaypointsScreen {
    private final Screen parentScreen;
    private final String tardisId;
    private final RegistryKey<World> dimension;
    private final BlockPos originBlockPos;
    private Direction facing;

    private boolean isInited;

    private ButtonWidget resetXButton;
    private ButtonWidget resetYButton;
    private ButtonWidget resetZButton;
    private ButtonWidget acceptButton;
    private ButtonWidget cancelButton;

    public TardisConsoleUnitMonitorWaypointCreateScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, String tardisId, RegistryKey<World> dimension, @Nullable BlockPos blockPos, @Nullable Direction facing, @Nullable Screen parentScreen) {
        super(DWM.TEXTS.MONITOR_WAYPOINTS_CREATE_TITLE, tardisConsoleUnitBlockEntity);

        this.parentScreen = parentScreen;
        this.tardisId = tardisId;
        this.dimension = dimension;
        this.originBlockPos = blockPos;
        this.blockPos = blockPos;
        this.facing = facing;
    }

    @Override
    protected void init() {
        super.init();

        int nameFieldWidth = this.getNameFieldWidth();
        int coordsFieldWidth = this.getCoordsFieldWidth();

        Vector2i nameFieldPos = this.getLeftTopRenderPos(SCREEN_MARGIN, this.textRenderer.fontHeight + SCREEN_MARGIN * 2);
        this.nameField = new TextFieldWidget(this.textRenderer, nameFieldPos.x, nameFieldPos.y, nameFieldWidth, INPUT_HEIGHT, DWM.TEXTS.MONITOR_WAYPOINTS_CREATE_NAME);

        Vector2i xFieldPos = nameFieldPos.add(0, INPUT_HEIGHT + this.textRenderer.fontHeight + 10);
        this.xField = new TextFieldWidget(this.textRenderer, xFieldPos.x, xFieldPos.y, coordsFieldWidth, INPUT_HEIGHT, Text.literal("X"));
        if (this.blockPos != null) this.xField.setText(String.valueOf(this.blockPos.getX()));
        this.xField.setMaxLength(String.valueOf(MAX_XZ_COORDS).length() + 1);
        this.xField.setCursorToStart(false);
        this.xField.setChangedListener((v) -> this.update());

        if (this.originBlockPos != null) {
            Vector2i resetXButtonPos = xFieldPos.add(coordsFieldWidth + BUTTON_MARGIN, 0);
            this.resetXButton = new BaseButton(resetXButtonPos, INPUT_HEIGHT, BUTTON_PADDING, DWM.TEXTS.MONITOR_WAYPOINTS_CREATE_BTN_RESET, DWM.TEXTURES.GUI.COMMON.ELEMENTS.RESET, (b) -> {
                this.xField.setText(String.valueOf(this.originBlockPos.getX()));
                this.xField.setCursorToStart(false);
            });
        }

        Vector2i yFieldPos = xFieldPos.add((this.originBlockPos != null ? INPUT_HEIGHT : coordsFieldWidth) + INPUT_MARGIN, 0);
        this.yField = new TextFieldWidget(this.textRenderer, yFieldPos.x, yFieldPos.y, coordsFieldWidth, INPUT_HEIGHT, Text.literal("Y"));
        if (this.blockPos != null) this.yField.setText(String.valueOf(this.blockPos.getY()));
        this.yField.setMaxLength(String.valueOf(MAX_Y_COORDS).length() + 1);
        this.yField.setCursorToStart(false);
        this.yField.setChangedListener((v) -> this.update());

        if (this.originBlockPos != null) {
            Vector2i resetYButtonPos = yFieldPos.add(coordsFieldWidth + BUTTON_MARGIN, 0);
            this.resetYButton = new BaseButton(resetYButtonPos, INPUT_HEIGHT, BUTTON_PADDING, DWM.TEXTS.MONITOR_WAYPOINTS_CREATE_BTN_RESET, DWM.TEXTURES.GUI.COMMON.ELEMENTS.RESET, (b) -> {
                this.yField.setText(String.valueOf(this.originBlockPos.getY()));
                this.yField.setCursorToStart(false);
            });
        }

        Vector2i zFieldPos = yFieldPos.add((this.originBlockPos != null ? INPUT_HEIGHT : coordsFieldWidth) + INPUT_MARGIN, 0);
        this.zField = new TextFieldWidget(this.textRenderer, zFieldPos.x, zFieldPos.y, coordsFieldWidth, INPUT_HEIGHT, Text.literal("Z"));
        if (this.blockPos != null) this.zField.setText(String.valueOf(this.blockPos.getZ()));
        this.zField.setMaxLength(String.valueOf(MAX_XZ_COORDS).length() + 1);
        this.zField.setCursorToStart(false);
        this.zField.setChangedListener((v) -> this.update());

        if (this.originBlockPos != null) {
            Vector2i resetZButtonPos = zFieldPos.add(coordsFieldWidth + BUTTON_MARGIN, 0);
            this.resetZButton = new BaseButton(resetZButtonPos, INPUT_HEIGHT, BUTTON_PADDING, DWM.TEXTS.MONITOR_WAYPOINTS_CREATE_BTN_RESET, DWM.TEXTURES.GUI.COMMON.ELEMENTS.RESET, (b) -> {
                this.zField.setText(String.valueOf(this.originBlockPos.getZ()));
                this.zField.setCursorToStart(false);
            });
        }

        Vector2i acceptButtonPos = this.getRightBottomRenderPos(BUTTON_SIZE + SCREEN_MARGIN, BUTTON_SIZE + SCREEN_MARGIN);
        this.acceptButton = new BaseButton(acceptButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_WAYPOINTS_CREATE_BTN_ACCEPT, DWM.TEXTURES.GUI.COMMON.ELEMENTS.ACCEPT, (b) -> {
            this.apply();
        });

        Vector2i cancelButtonPos = this.getLeftBottomRenderPos(SCREEN_MARGIN, BUTTON_SIZE + SCREEN_MARGIN);
        this.cancelButton = new BaseButton(cancelButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_WAYPOINTS_CREATE_BTN_CANCEL, DWM.TEXTURES.GUI.COMMON.ELEMENTS.CANCEL, (b) -> {
            this.back();
        });

        this.addDrawableChild(this.nameField);
        this.addDrawableChild(this.xField);
        this.addDrawableChild(this.yField);
        this.addDrawableChild(this.zField);
        this.addDrawableChild(this.acceptButton);
        this.addDrawableChild(this.cancelButton);

        if (this.originBlockPos != null) {
            this.addDrawableChild(this.resetXButton);
            this.addDrawableChild(this.resetYButton);
            this.addDrawableChild(this.resetZButton);
        }

        this.isInited = true;
        this.update();
    }

    @Override
    public void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderAdditional(context, mouseX, mouseY, delta);

        int coordsFieldWidth = this.getCoordsFieldWidth();

        Vector2i nameTitlePos = this.getLeftTopRenderPos(SCREEN_MARGIN, SCREEN_MARGIN + 2);
        context.drawText(this.textRenderer, DWM.TEXTS.MONITOR_WAYPOINTS_CREATE_NAME, nameTitlePos.x, nameTitlePos.y, 0xE0E0E0, true);

        Vector2i xTitlePos = nameTitlePos.add(0, INPUT_HEIGHT + SCREEN_MARGIN * 2 + this.textRenderer.fontHeight + 2);
        context.drawText(this.textRenderer, Text.literal("X"), xTitlePos.x, xTitlePos.y, 0xE0E0E0, true);

        Vector2i yTitlePos = xTitlePos.add(coordsFieldWidth + INPUT_MARGIN + (this.originBlockPos != null ? BUTTON_MARGIN + INPUT_HEIGHT : 0), 0);
        context.drawText(this.textRenderer, Text.literal("Y"), yTitlePos.x, yTitlePos.y, 0xE0E0E0, true);

        Vector2i zTitlePos = yTitlePos.add(coordsFieldWidth + INPUT_MARGIN + (this.originBlockPos != null ? BUTTON_MARGIN + INPUT_HEIGHT : 0), 0);
        context.drawText(this.textRenderer, Text.literal("Z"), zTitlePos.x, zTitlePos.y, 0xE0E0E0, true);
    }

    @Override
    public void apply() {
        if (this.dimension != null && this.blockPos != null && this.facing != null) {
            String name = this.nameField.getText();
            if (name == null || name.isEmpty()) name = this.getGeneratedWaypointName();
            TardisFlightWaypointEntry waypointEntry = new TardisFlightWaypointEntry(this.dimension, this.blockPos, this.facing, name);

            if (this.parentScreen instanceof TardisConsoleUnitMonitorWaypointsScreen tardisConsoleUnitMonitorWaypointsScreen) {
                tardisConsoleUnitMonitorWaypointsScreen.addWaypointEntry(waypointEntry);
            }

            new TardisConsoleUnitMonitorWaypointCreatePacket(this.tardisId, waypointEntry).sendToServer();
        }

        this.back();
    }

    @Override
    public void back() {
        if (this.parentScreen != null) this.client.setScreen(this.parentScreen);
        else super.back();
    }

    private int getNameFieldWidth() {
        return this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2 - SCREEN_MARGIN * 2;
    }

    private int getCoordsFieldWidth() {
        return this.getNameFieldWidth() / 3 - INPUT_MARGIN / 2 - (this.originBlockPos != null ? INPUT_HEIGHT + BUTTON_MARGIN : 0);
    }

    private String getGeneratedWaypointName() {
        String dimensionName = CommonHelper.capitaliseAllWords(this.dimension.getValue().getPath().replace("_", " "));
        if (this.blockPos == null) return String.format("[%s]", dimensionName);
        return String.format("[%s] %s", dimensionName, this.blockPos.toShortString());
    }

    private void update() {
        if (!this.isInited) return;
        this.updateCoordsFields();

        String xText = this.xField.getText();
        String yText = this.yField.getText();
        String zText = this.zField.getText();

        this.nameField.setPlaceholder(Text.literal(this.getGeneratedWaypointName()).formatted(Formatting.GRAY));
        this.acceptButton.active = this.blockPos != null && xText != null && !xText.isEmpty() && yText != null && !yText.isEmpty() && zText != null && !zText.isEmpty();

        if (this.originBlockPos != null) {
            this.resetXButton.active = this.blockPos != null && this.blockPos.getX() != this.originBlockPos.getX();
            this.resetYButton.active = this.blockPos != null && this.blockPos.getY() != this.originBlockPos.getY();
            this.resetZButton.active = this.blockPos != null && this.blockPos.getZ() != this.originBlockPos.getZ();
        }
    }
}

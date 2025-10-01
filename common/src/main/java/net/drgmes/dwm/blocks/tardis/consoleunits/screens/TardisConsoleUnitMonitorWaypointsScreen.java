package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.flight.TardisFlightWaypointEntry;
import net.drgmes.dwm.network.server.TardisConsoleUnitMonitorWaypointApplyPacket;
import net.drgmes.dwm.network.server.TardisConsoleUnitMonitorWaypointUpdatePacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.base.screens.elements.BaseButton;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class TardisConsoleUnitMonitorWaypointsScreen extends BaseTardisConsoleUnitMonitorWaypointsScreen {
    private final Screen parentScreen;
    private final String tardisId;
    private final NbtCompound tag;
    private final List<TardisFlightWaypointEntry> waypoints = new ArrayList<>();

    private boolean isInited;
    private boolean updateLocaled;

    private ButtonWidget resetNameButton;
    private ButtonWidget resetXButton;
    private ButtonWidget resetYButton;
    private ButtonWidget resetZButton;
    private ButtonWidget acceptButton;
    private ButtonWidget removeButton;
    private ButtonWidget updateButton;
    private ButtonWidget createButton;
    private ButtonWidget cancelButton;

    private WaypointsListWidget waypointsListWidget;
    private WaypointsListWidget.WaypointEntry selected = null;

    public TardisConsoleUnitMonitorWaypointsScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, String tardisId, NbtCompound tag, @Nullable Screen parentScreen) {
        super(DWM.TEXTS.MONITOR_WAYPOINTS_TITLE, tardisConsoleUnitBlockEntity);

        this.parentScreen = parentScreen;
        this.tardisId = tardisId;
        this.tag = tag;

        NbtCompound tardisTag = tag.getCompound("tardisTag");
        if (tardisTag.contains(TardisSystemFlight.class.getSimpleName())) {
            NbtCompound tardisSystemFlightTag = tardisTag.getCompound(TardisSystemFlight.class.getSimpleName());

            if (tardisSystemFlightTag.contains("waypoints")) {
                this.waypoints.clear();

                NbtCompound waypointsTag = tardisSystemFlightTag.getCompound("waypoints");
                List<String> keys = new ArrayList<>(waypointsTag.getKeys());

                keys.sort(Comparator.comparing((key) -> key));
                keys.forEach((key) -> this.waypoints.add(TardisFlightWaypointEntry.createFromNbt(waypointsTag.getCompound(key))));
            }
        }
    }

    @Override
    protected void init() {
        super.init();

        Vector2i waypointsListPos = this.getWaypointsListPos();
        Vector2i waypointsListSize = this.getWaypointsListSize();
        this.waypointsListWidget = new WaypointsListWidget(this, waypointsListPos, waypointsListSize);

        int fieldWidth = this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2 - SCREEN_MARGIN * 2 - BUTTON_MARGIN - INPUT_HEIGHT - waypointsListSize.x - 2;

        Vector2i nameFieldPos = this.getLeftTopRenderPos(waypointsListSize.x + SCREEN_MARGIN + 2, this.textRenderer.fontHeight + SCREEN_MARGIN * 2);
        this.nameField = new TextFieldWidget(this.textRenderer, nameFieldPos.x, nameFieldPos.y, fieldWidth, INPUT_HEIGHT, DWM.TEXTS.MONITOR_WAYPOINTS_NAME);
        if (this.selected != null) this.nameField.setText(this.selected.waypointEntry.name());
        this.nameField.setCursorToStart(false);
        this.nameField.setChangedListener((v) -> this.update());

        Vector2i resetNameButtonPos = nameFieldPos.add(fieldWidth + BUTTON_MARGIN, 0, new Vector2i());
        this.resetNameButton = new BaseButton(resetNameButtonPos, INPUT_HEIGHT, BUTTON_PADDING, DWM.TEXTS.MONITOR_WAYPOINTS_CREATE_BTN_RESET, DWM.TEXTURES.GUI.COMMON.ELEMENTS.RESET, (b) -> {
            if (this.selected != null) this.nameField.setText(this.selected.waypointEntry.name());
            this.nameField.setCursorToStart(false);
        });

        Vector2i xFieldPos = nameFieldPos.add(0, INPUT_HEIGHT + this.textRenderer.fontHeight + 10);
        this.xField = new TextFieldWidget(this.textRenderer, xFieldPos.x, xFieldPos.y, fieldWidth, INPUT_HEIGHT, Text.literal("X"));
        if (this.blockPos != null) this.xField.setText(String.valueOf(this.blockPos.getX()));
        this.xField.setMaxLength(String.valueOf(MAX_XZ_COORDS).length() + 1);
        this.xField.setTooltip(Tooltip.of(Text.literal("X")));
        this.xField.setCursorToStart(false);
        this.xField.setChangedListener((v) -> this.update());

        Vector2i resetXButtonPos = xFieldPos.add(fieldWidth + BUTTON_MARGIN, 0, new Vector2i());
        this.resetXButton = new BaseButton(resetXButtonPos, INPUT_HEIGHT, BUTTON_PADDING, DWM.TEXTS.MONITOR_WAYPOINTS_CREATE_BTN_RESET, DWM.TEXTURES.GUI.COMMON.ELEMENTS.RESET, (b) -> {
            if (this.selected != null) this.xField.setText(String.valueOf(this.selected.waypointEntry.blockPos().getX()));
            this.xField.setCursorToStart(false);
        });

        Vector2i yFieldPos = xFieldPos.add(0, INPUT_HEIGHT + INPUT_MARGIN);
        this.yField = new TextFieldWidget(this.textRenderer, yFieldPos.x, yFieldPos.y, fieldWidth, INPUT_HEIGHT, Text.literal("Y"));
        if (this.blockPos != null) this.yField.setText(String.valueOf(this.blockPos.getY()));
        this.yField.setMaxLength(String.valueOf(MAX_Y_COORDS).length() + 1);
        this.yField.setTooltip(Tooltip.of(Text.literal("Y")));
        this.yField.setCursorToStart(false);
        this.yField.setChangedListener((v) -> this.update());

        Vector2i resetYButtonPos = yFieldPos.add(fieldWidth + BUTTON_MARGIN, 0, new Vector2i());
        this.resetYButton = new BaseButton(resetYButtonPos, INPUT_HEIGHT, BUTTON_PADDING, DWM.TEXTS.MONITOR_WAYPOINTS_CREATE_BTN_RESET, DWM.TEXTURES.GUI.COMMON.ELEMENTS.RESET, (b) -> {
            if (this.selected != null) this.yField.setText(String.valueOf(this.selected.waypointEntry.blockPos().getY()));
            this.yField.setCursorToStart(false);
        });

        Vector2i zFieldPos = yFieldPos.add(0, INPUT_HEIGHT + INPUT_MARGIN);
        this.zField = new TextFieldWidget(this.textRenderer, zFieldPos.x, zFieldPos.y, fieldWidth, INPUT_HEIGHT, Text.literal("Z"));
        if (this.blockPos != null) this.zField.setText(String.valueOf(this.blockPos.getZ()));
        this.zField.setMaxLength(String.valueOf(MAX_XZ_COORDS).length() + 1);
        this.zField.setTooltip(Tooltip.of(Text.literal("Z")));
        this.zField.setCursorToStart(false);
        this.zField.setChangedListener((v) -> this.update());

        Vector2i resetZButtonPos = zFieldPos.add(fieldWidth + BUTTON_MARGIN, 0, new Vector2i());
        this.resetZButton = new BaseButton(resetZButtonPos, INPUT_HEIGHT, BUTTON_PADDING, DWM.TEXTS.MONITOR_WAYPOINTS_CREATE_BTN_RESET, DWM.TEXTURES.GUI.COMMON.ELEMENTS.RESET, (b) -> {
            if (this.selected != null) this.zField.setText(String.valueOf(this.selected.waypointEntry.blockPos().getZ()));
            this.zField.setCursorToStart(false);
        });

        Vector2i acceptButtonPos = this.getRightBottomRenderPos(BUTTON_SIZE + SCREEN_MARGIN, BUTTON_SIZE + SCREEN_MARGIN);
        this.acceptButton = new BaseButton(acceptButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_WAYPOINTS_ACCEPT, DWM.TEXTURES.GUI.COMMON.ELEMENTS.ACCEPT, (b) -> {
            this.apply();
        });

        Vector2i removeButtonPos = acceptButtonPos.add(-BUTTON_SIZE - BUTTON_MARGIN, 0);
        this.removeButton = new BaseButton(removeButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_WAYPOINTS_REMOVE, DWM.TEXTURES.GUI.COMMON.ELEMENTS.CROSS, (b) -> {
            if (this.selected == null) return;
            this.client.setScreen(new TardisConsoleUnitMonitorWaypointRemoveConfirmationScreen(this.tardisConsoleUnitBlockEntity, this.tardisId, this.selected.waypointEntry, this));
        });

        Vector2i updateButtonPos = this.getRightBottomRenderPos(BUTTON_SIZE + SCREEN_MARGIN, BUTTON_SIZE * 2 + SCREEN_MARGIN * 3 + 1);
        this.updateButton = new BaseButton(updateButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_WAYPOINTS_UPDATE, DWM.TEXTURES.GUI.COMMON.ELEMENTS.SAVE, (b) -> {
            if (this.selected != null && this.blockPos != null) {
                String name = this.nameField.getText();
                if (name == null || name.isEmpty()) name = this.getGeneratedWaypointName();
                TardisFlightWaypointEntry newWaypointEntry = new TardisFlightWaypointEntry(this.selected.waypointEntry.id(), this.selected.waypointEntry.dimension(), this.blockPos, this.selected.waypointEntry.facing(), name, this.selected.waypointEntry.timestamp());

                this.updateWaypointEntry(this.selected.waypointEntry, newWaypointEntry);
                new TardisConsoleUnitMonitorWaypointUpdatePacket(this.tardisId, this.selected.waypointEntry, newWaypointEntry).sendToServer();
            }
        });

        Vector2i cancelButtonPos = this.getLeftBottomRenderPos(SCREEN_MARGIN, BUTTON_SIZE + SCREEN_MARGIN);
        this.cancelButton = new BaseButton(cancelButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_WAYPOINTS_CANCEL, DWM.TEXTURES.GUI.COMMON.ELEMENTS.CANCEL, (b) -> {
            this.back();
        });

        Vector2i createButtonPos = cancelButtonPos.add(BUTTON_SIZE + BUTTON_MARGIN, 0);
        this.createButton = new BaseButton(createButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_WAYPOINTS_CREATE, DWM.TEXTURES.GUI.COMMON.ELEMENTS.PLUS, (b) -> {
            if (!this.tag.contains("tardisTag")) return;

            NbtCompound tardisTag = this.tag.getCompound("tardisTag");
            if (!tardisTag.contains("currExteriorDimension") || !tardisTag.contains("currExteriorPosition") || !tardisTag.contains("currExteriorFacing")) return;

            RegistryKey<World> dimension = DimensionHelper.getWorldKey(tardisTag.getString("currExteriorDimension"));
            BlockPos blockPos = BlockPos.fromLong(tardisTag.getLong("currExteriorPosition"));
            Direction facing = Direction.byName(tardisTag.getString("currExteriorFacing"));

            this.client.setScreen(new TardisConsoleUnitMonitorWaypointCreateScreen(this.tardisConsoleUnitBlockEntity, this.tardisId, dimension, blockPos, facing, this));
        });

        this.addDrawableChild(this.waypointsListWidget);
        this.addDrawableChild(this.nameField);
        this.addDrawableChild(this.xField);
        this.addDrawableChild(this.yField);
        this.addDrawableChild(this.zField);
        this.addDrawableChild(this.resetNameButton);
        this.addDrawableChild(this.resetXButton);
        this.addDrawableChild(this.resetYButton);
        this.addDrawableChild(this.resetZButton);
        this.addDrawableChild(this.acceptButton);
        this.addDrawableChild(this.removeButton);
        this.addDrawableChild(this.updateButton);
        this.addDrawableChild(this.createButton);
        this.addDrawableChild(this.cancelButton);

        this.isInited = true;
        this.update();
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        super.resize(mc, width, height);
        this.waypointsListWidget.refreshList();
    }

    @Override
    public void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderAdditional(context, mouseX, mouseY, delta);

        int lineWidth = this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2 + 2;
        Vector2i linePos = this.getLeftBottomRenderPos(-1, SCREEN_MARGIN * 2 + BUTTON_SIZE + 1);
        RenderHelper.drawTessellatorRectangle(context.getMatrices(), linePos.x, linePos.y, linePos.x + lineWidth, linePos.y + 1, 0xFF231F26);

        if (this.selected == null) return;

        Vector2i waypointsListSize = this.getWaypointsListSize();
        Vector2i nameTitlePos = this.getLeftTopRenderPos(waypointsListSize.x + SCREEN_MARGIN + 2, SCREEN_MARGIN + 2);
        context.drawText(this.textRenderer, DWM.TEXTS.MONITOR_WAYPOINTS_NAME, nameTitlePos.x, nameTitlePos.y, 0xE0E0E0, true);

        Vector2i coordsTitlePos = nameTitlePos.add(0, INPUT_HEIGHT + SCREEN_MARGIN * 2 + this.textRenderer.fontHeight + 2);
        context.drawText(this.textRenderer, DWM.TEXTS.MONITOR_WAYPOINTS_COORDS, coordsTitlePos.x, coordsTitlePos.y, 0xE0E0E0, true);
    }

    @Override
    public void tick() {
        this.waypointsListWidget.setSelected(this.selected);
    }

    @Override
    public void apply() {
        if (this.selected != null) {
            new TardisConsoleUnitMonitorWaypointApplyPacket(this.tardisId, this.selected.waypointEntry).sendToServer();
        }

        super.apply();
    }

    @Override
    public void back() {
        if (this.parentScreen != null) this.client.setScreen(this.parentScreen);
        else super.back();
    }

    protected void addWaypointEntry(TardisFlightWaypointEntry waypointEntry) {
        this.waypoints.add(waypointEntry);
        this.waypointsListWidget.refreshList();
    }

    protected void updateWaypointEntry(TardisFlightWaypointEntry oldWaypointEntry, TardisFlightWaypointEntry newWaypointEntry) {
        int index = this.waypoints.indexOf(oldWaypointEntry);
        if (index < 0) return;

        this.waypoints.set(index, newWaypointEntry);
        this.waypointsListWidget.refreshList();
    }

    protected void deleteWaypointEntry(TardisFlightWaypointEntry waypointEntry) {
        Optional<TardisFlightWaypointEntry> foundEntryHolder = this.waypoints.stream().filter((entry) -> entry.equals(waypointEntry)).findFirst();
        foundEntryHolder.ifPresent(this.waypoints::remove);

        this.selected = null;
        this.waypointsListWidget.refreshList();
    }

    private Vector2i getWaypointsListPos() {
        return this.getLeftTopRenderPos(0, 0);
    }

    private Vector2i getWaypointsListSize() {
        return new Vector2i(125, this.getBackgroundSize().y - this.getBackgroundBorderSize().y * 2 - SCREEN_MARGIN * 2 - BUTTON_SIZE - 2);
    }

    private String getGeneratedWaypointName() {
        if (this.selected == null) return "";
        String dimensionName = CommonHelper.capitaliseAllWords(this.selected.waypointEntry.dimension().getValue().getPath().replace("_", " "));
        if (this.blockPos == null) return String.format("[%s]", dimensionName);
        return String.format("[%s] %s", dimensionName, this.blockPos.toShortString());
    }

    private void update() {
        if (!this.isInited || this.updateLocaled) return;

        if (this.selected == null) {
            this.nameField.setFocused(false);
            this.xField.setFocused(false);
            this.yField.setFocused(false);
            this.zField.setFocused(false);

            this.nameField.setEditable(false);
            this.xField.setEditable(false);
            this.yField.setEditable(false);
            this.zField.setEditable(false);

            this.nameField.setVisible(false);
            this.xField.setVisible(false);
            this.yField.setVisible(false);
            this.zField.setVisible(false);

            this.updateButton.visible = false;
            this.resetNameButton.visible = false;
            this.resetXButton.visible = false;
            this.resetYButton.visible = false;
            this.resetZButton.visible = false;

            this.updateButton.active = false;
            this.acceptButton.active = false;
            this.removeButton.active = false;

            return;
        }

        this.nameField.setEditable(true);
        this.xField.setEditable(true);
        this.yField.setEditable(true);
        this.zField.setEditable(true);

        this.nameField.setVisible(true);
        this.xField.setVisible(true);
        this.yField.setVisible(true);
        this.zField.setVisible(true);

        this.updateButton.visible = true;
        this.resetNameButton.visible = true;
        this.resetXButton.visible = true;
        this.resetYButton.visible = true;
        this.resetZButton.visible = true;

        this.updateCoordsFields();

        String nameText = this.nameField.getText();
        String xText = this.xField.getText();
        String yText = this.yField.getText();
        String zText = this.zField.getText();

        this.nameField.setPlaceholder(Text.literal(this.getGeneratedWaypointName()).formatted(Formatting.GRAY));
        this.acceptButton.active = true;
        this.removeButton.active = true;

        this.resetNameButton.active = nameText == null || !nameText.equals(this.selected.waypointEntry.name());
        this.resetXButton.active = this.blockPos != null && this.blockPos.getX() != this.selected.waypointEntry.blockPos().getX();
        this.resetYButton.active = this.blockPos != null && this.blockPos.getY() != this.selected.waypointEntry.blockPos().getY();
        this.resetZButton.active = this.blockPos != null && this.blockPos.getZ() != this.selected.waypointEntry.blockPos().getZ();

        boolean isAllInputsValid = xText != null && !xText.isEmpty() && yText != null && !yText.isEmpty() && zText != null && !zText.isEmpty();
        boolean hasChanges = this.resetNameButton.active || this.resetXButton.active || this.resetYButton.active || this.resetZButton.active;
        this.updateButton.active = this.blockPos != null && isAllInputsValid && hasChanges;
    }

    private void setSelected(WaypointsListWidget.WaypointEntry entry) {
        if (this.selected == entry) this.selected = null;
        else this.selected = entry;

        if (this.selected != null) {
            this.updateLocaled = true;
            this.blockPos = new BlockPos(this.selected.waypointEntry.blockPos());

            this.nameField.setText(this.selected.waypointEntry.name());
            this.xField.setText(String.valueOf(this.blockPos.getX()));
            this.yField.setText(String.valueOf(this.blockPos.getY()));
            this.zField.setText(String.valueOf(this.blockPos.getZ()));

            this.nameField.setCursorToStart(false);
            this.xField.setCursorToStart(false);
            this.yField.setCursorToStart(false);
            this.zField.setCursorToStart(false);
            this.updateLocaled = false;
        }

        this.update();
    }

    private static class WaypointsListWidget extends BaseListWidget {
        private final TardisConsoleUnitMonitorWaypointsScreen parent;

        public WaypointsListWidget(TardisConsoleUnitMonitorWaypointsScreen parent, Vector2i pos, Vector2i size) {
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

            this.parent.waypoints.forEach((waypointEntry) -> {
                WaypointEntry entry = new WaypointEntry(waypointEntry);
                this.addEntry(entry);

                if (this.parent.selected != null && entry.waypointEntry.equals(this.parent.selected.waypointEntry)) {
                    this.setSelected(entry);
                    this.parent.setSelected(entry);
                }
            });
        }

        private class WaypointEntry extends BaseListEntry {
            private final TardisFlightWaypointEntry waypointEntry;

            public WaypointEntry(TardisFlightWaypointEntry waypointEntry) {
                super(Formatting.WHITE, Formatting.GOLD);
                this.waypointEntry = waypointEntry;
            }

            @Override
            public Text getText() {
                return Text.literal(this.waypointEntry.name());
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (!super.mouseClicked(mouseX, mouseY, button)) return false;
                WaypointsListWidget.this.parent.setSelected(this);
                return true;
            }
        }
    }
}

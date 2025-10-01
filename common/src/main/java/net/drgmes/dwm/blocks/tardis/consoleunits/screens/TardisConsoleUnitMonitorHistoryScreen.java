package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.flight.TardisFlightHistoryEntry;
import net.drgmes.dwm.network.server.TardisConsoleUnitMonitorHistoryApplyPacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.base.screens.elements.BaseButton;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class TardisConsoleUnitMonitorHistoryScreen extends BaseTardisConsoleUnitMonitorScreen {
    private final Screen parentScreen;
    private final String tardisId;
    private final List<TardisFlightHistoryEntry> history = new ArrayList<>();

    private boolean isInited;

    private ButtonWidget acceptButton;
    private ButtonWidget saveButton;
    private ButtonWidget removeButton;
    private ButtonWidget clearButton;
    private ButtonWidget cancelButton;

    private HistoryListWidget historyListWidget;
    private HistoryListWidget.HistoryEntry selected = null;

    public TardisConsoleUnitMonitorHistoryScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, String tardisId, NbtCompound tag, @Nullable Screen parentScreen) {
        super(DWM.TEXTS.MONITOR_HISTORY_TITLE, tardisConsoleUnitBlockEntity);

        this.parentScreen = parentScreen;
        this.tardisId = tardisId;

        NbtCompound tardisTag = tag.getCompound("tardisTag");
        if (tardisTag.contains(TardisSystemFlight.class.getSimpleName())) {
            NbtCompound tardisSystemFlightTag = tardisTag.getCompound(TardisSystemFlight.class.getSimpleName());

            if (tardisSystemFlightTag.contains("history")) {
                this.history.clear();

                NbtCompound historyTag = tardisSystemFlightTag.getCompound("history");
                List<String> keys = new ArrayList<>(historyTag.getKeys());

                keys.sort(Comparator.comparing((key) -> key));
                keys.forEach((key) -> this.history.add(TardisFlightHistoryEntry.createFromNbt(historyTag.getCompound(key))));
            }
        }
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return true;
    }

    @Override
    protected void init() {
        super.init();

        this.historyListWidget = new HistoryListWidget(this, this.getHistoryListPos(), this.getHistoryListSize());

        Vector2i acceptButtonPos = this.getRightBottomRenderPos(BUTTON_SIZE + SCREEN_MARGIN, BUTTON_SIZE + SCREEN_MARGIN);
        this.acceptButton = new BaseButton(acceptButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_HISTORY_ACCEPT, DWM.TEXTURES.GUI.COMMON.ELEMENTS.ACCEPT, (b) -> {
            this.apply();
        });

        Vector2i saveButtonPos = acceptButtonPos.add(-BUTTON_SIZE - BUTTON_MARGIN, 0);
        this.saveButton = new BaseButton(saveButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_HISTORY_SAVE, DWM.TEXTURES.GUI.COMMON.ELEMENTS.SAVE, (b) -> {
            if (this.selected == null) return;
            this.client.setScreen(new TardisConsoleUnitMonitorWaypointCreateScreen(this.tardisConsoleUnitBlockEntity, this.tardisId, this.selected.historyEntry.dimension(), this.selected.historyEntry.blockPos(), this.selected.historyEntry.facing(), this));
        });

        Vector2i removeButtonPos = saveButtonPos.add(-BUTTON_SIZE - BUTTON_MARGIN, 0);
        this.removeButton = new BaseButton(removeButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_HISTORY_REMOVE, DWM.TEXTURES.GUI.COMMON.ELEMENTS.CROSS, (b) -> {
            if (this.selected == null) return;
            this.client.setScreen(new TardisConsoleUnitMonitorHistoryRemoveConfirmationScreen(this.tardisConsoleUnitBlockEntity, this.tardisId, this.selected.historyEntry, this));
        });

        Vector2i clearButtonPos = removeButtonPos.add(-BUTTON_SIZE - BUTTON_MARGIN, 0);
        this.clearButton = new BaseButton(clearButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_HISTORY_CLEAR, DWM.TEXTURES.GUI.COMMON.ELEMENTS.CLEAR, (b) -> {
            this.client.setScreen(new TardisConsoleUnitMonitorHistoryClearConfirmationScreen(this.tardisConsoleUnitBlockEntity, this.tardisId, this));
        });

        Vector2i cancelButtonPos = this.getLeftBottomRenderPos(SCREEN_MARGIN, BUTTON_SIZE + SCREEN_MARGIN);
        this.cancelButton = new BaseButton(cancelButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.MONITOR_HISTORY_CANCEL, DWM.TEXTURES.GUI.COMMON.ELEMENTS.CANCEL, (b) -> {
            this.back();
        });

        this.addDrawableChild(this.historyListWidget);
        this.addDrawableChild(this.acceptButton);
        this.addDrawableChild(this.saveButton);
        this.addDrawableChild(this.removeButton);
        this.addDrawableChild(this.clearButton);
        this.addDrawableChild(this.cancelButton);

        this.isInited = true;
        this.update();
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        super.resize(mc, width, height);
        this.historyListWidget.refreshList();
    }

    @Override
    public void renderAdditional(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderAdditional(context, mouseX, mouseY, delta);

        int color = this.getTitleBackgroundColor();
        int titleWidth = this.textRenderer.getWidth(this.getCounterTitle().getString());

        Vector2i pos = this.getCounterRenderPos().add(-titleWidth, 0);
        Vector2i pos1 = new Vector2i(-4, 2).add(pos);
        Vector2i pos2 = new Vector2i(titleWidth + 9, 5).add(pos1);

        context.fillGradient(pos1.x, pos1.y, pos2.x, pos2.y, color, color);
        context.drawText(this.textRenderer, this.getCounterTitle(), pos.x, pos.y, 0xE0E0E0, true);
    }

    @Override
    public void tick() {
        this.historyListWidget.setSelected(this.selected);
    }

    @Override
    public void apply() {
        if (this.selected != null) {
            new TardisConsoleUnitMonitorHistoryApplyPacket(this.tardisId, this.selected.historyEntry).sendToServer();
        }

        super.apply();
    }

    @Override
    public void back() {
        if (this.parentScreen != null) this.client.setScreen(this.parentScreen);
        else super.back();
    }

    protected void deleteHistoryEntry(TardisFlightHistoryEntry historyEntry) {
        Optional<TardisFlightHistoryEntry> foundEntryHolder = this.history.stream().filter((entry) -> entry.equals(historyEntry)).findFirst();
        foundEntryHolder.ifPresent(this.history::remove);

        this.selected = null;
        this.historyListWidget.refreshList();
    }

    private Text getCounterTitle() {
        return Text.literal(this.history.size() + " / " + TardisSystemFlight.HISTORY_SIZE);
    }

    private Vector2i getCounterRenderPos() {
        return new Vector2i(this.getBackgroundSize().x - this.getBackgroundBorderSize().x - 24, 0).add(this.getTitleRenderPos());
    }

    private Vector2i getHistoryListPos() {
        return this.getLeftTopRenderPos(0, 0);
    }

    private Vector2i getHistoryListSize() {
        return new Vector2i(this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2, this.getBackgroundSize().y - this.getBackgroundBorderSize().y * 2 - SCREEN_MARGIN * 2 - BUTTON_SIZE - 2);
    }

    private void update() {
        if (!this.isInited) return;

        this.acceptButton.active = this.selected != null;
        this.saveButton.active = this.selected != null;
        this.removeButton.active = this.selected != null;
        this.clearButton.active = !this.history.isEmpty();
    }

    private void setSelected(HistoryListWidget.HistoryEntry entry) {
        if (this.selected == entry) this.selected = null;
        else this.selected = entry;
        this.update();
    }

    private static class HistoryListWidget extends BaseListWidget {
        private final TardisConsoleUnitMonitorHistoryScreen parent;

        public HistoryListWidget(TardisConsoleUnitMonitorHistoryScreen parent, Vector2i pos, Vector2i size) {
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

            this.parent.history.forEach((historyEntry) -> {
                HistoryEntry entry = new HistoryEntry(historyEntry);
                this.addEntry(entry);

                if (this.parent.selected != null && entry.historyEntry.equals(this.parent.selected.historyEntry)) {
                    this.setSelected(entry);
                    this.parent.setSelected(entry);
                }
            });
        }

        private class HistoryEntry extends BaseListEntry {
            private final TardisFlightHistoryEntry historyEntry;

            public HistoryEntry(TardisFlightHistoryEntry historyEntry) {
                super(Formatting.WHITE, Formatting.GOLD);
                this.historyEntry = historyEntry;
            }

            @Override
            public Text getText() {
                String dimensionName = CommonHelper.capitaliseAllWords(this.historyEntry.dimension().getValue().getPath().replace("_", " "));
                MutableText dimensionText = Text.literal(String.format("[%s] ", dimensionName)).formatted(Formatting.AQUA);
                MutableText posText = Text.literal(this.historyEntry.blockPos().toShortString());
                return Text.empty().append(dimensionText).append(posText);
            }

            @Override
            public void render(DrawContext context, int entryIdx, int top, int left, int entryWidth, int height, int mouseX, int mouseY, boolean flag, float partialTick) {
                super.render(context, entryIdx, top, left, entryWidth, height, mouseX, mouseY, flag, partialTick);

                TextRenderer textRenderer = HistoryListWidget.this.parent.textRenderer;
                int offset = LINE_HEIGHT * (HistoryListWidget.this.getMaxScroll() > 0 ? 4 : 2);
                int startPosX = HistoryListWidget.this.parent.getHistoryListSize().x + left - offset;

                SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
                Text timeText = Text.literal(localDateFormat.format(this.historyEntry.timestamp())).formatted(Formatting.DARK_GRAY);
                Vector2i timePos = new Vector2i(startPosX - textRenderer.getWidth(timeText), top + 2);
                context.drawText(textRenderer, timeText, timePos.x, timePos.y, 0xE0E0E0, true);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (!super.mouseClicked(mouseX, mouseY, button)) return false;
                HistoryListWidget.this.parent.setSelected(this);
                return true;
            }
        }
    }
}

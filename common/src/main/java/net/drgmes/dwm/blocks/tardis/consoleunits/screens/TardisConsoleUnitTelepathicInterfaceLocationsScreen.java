package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.network.server.TardisConsoleUnitTelepathicInterfaceLocationApplyPacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class TardisConsoleUnitTelepathicInterfaceLocationsScreen extends BaseTardisConsoleUnitTelepathicInterfaceScreen {
    public enum EDataType {
        BIOME,
        STRUCTURE
    }

    private final List<Entry<Identifier, EDataType>> locations;
    private List<Entry<Identifier, EDataType>> filteredLocations;

    private LocationsListWidget locationsListWidget;
    private LocationsListWidget.LocationEntry selected = null;

    private TextFieldWidget search;
    private String lastSearch;

    public TardisConsoleUnitTelepathicInterfaceLocationsScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, List<Entry<Identifier, EDataType>> locations) {
        super(DWM.TEXTS.TELEPATHIC_INTERFACE_NAME_LOCATIONS, tardisConsoleUnitBlockEntity);

        this.locations = Collections.unmodifiableList(locations);
        this.filteredLocations = this.locations;
    }

    @Override
    protected void init() {
        int locationsListWidth = this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2;
        int locationsListHeight = this.getBackgroundSize().y - this.getBackgroundBorderSize().y * 2 - 20 - BUTTON_HEIGHT - 3;
        int locationsListOffset = this.getBackgroundSize().y - locationsListHeight - this.getBackgroundBorderSize().y - BUTTON_HEIGHT - 2;

        Vector2i searchPos = this.getRenderPos(this.getBackgroundBorderSize().x + 1, this.getBackgroundBorderSize().y + 1);
        this.search = new TextFieldWidget(this.textRenderer, searchPos.x, searchPos.y, locationsListWidth - 2, 18, DWM.TEXTS.TELEPATHIC_INTERFACE_FLD_SEARCH);

        Vector2i locationsListPos = this.getRenderPos(this.getBackgroundBorderSize().x, locationsListOffset);
        this.locationsListWidget = new LocationsListWidget(this, locationsListWidth, locationsListHeight, locationsListPos);

        this.addDrawableChild(this.locationsListWidget);
        this.addDrawableChild(this.search);

        super.init();
        this.update();
    }

    @Override
    protected void apply() {
        if (this.selected != null) {
            new TardisConsoleUnitTelepathicInterfaceLocationApplyPacket(this.selected.entry.getKey(), this.selected.entry.getValue().name()).sendToServer();
        }

        super.apply();
    }

    @Override
    public void tick() {
        this.search.tick();
        this.locationsListWidget.setSelected(this.selected);

        if (!this.search.getText().equals(lastSearch)) {
            this.selected = null;
            this.reloadLocationsList();
            this.locationsListWidget.refreshList();
            this.update();
        }
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        String search = this.search.getText();
        LocationsListWidget.LocationEntry selected = this.selected;

        super.resize(mc, width, height);
        this.search.setText(search);
        this.selected = selected;

        if (!this.search.getText().isEmpty()) {
            this.reloadLocationsList();
        }
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return !this.search.isFocused();
    }

    protected void update() {
        this.lastSearch = this.search.getText();
        this.acceptButton.active = this.selected != null;
    }

    protected void setSelected(LocationsListWidget.LocationEntry entry) {
        this.selected = entry == this.selected ? null : entry;
        this.update();
    }

    protected void reloadLocationsList() {
        if (this.hasSearch()) {
            this.lastSearch = this.search.getText();
            this.filteredLocations = this.locations.stream().filter((str) -> (
                str.getKey().getPath().toLowerCase().contains(this.search.getText().toLowerCase())
            )).toList();

            return;
        }

        this.filteredLocations = this.locations;
    }

    protected boolean hasSearch() {
        return this.search != null && !Objects.equals(this.search.getText(), "");
    }

    private static class LocationsListWidget extends BaseListWidget {
        private final TardisConsoleUnitTelepathicInterfaceLocationsScreen parent;

        public LocationsListWidget(TardisConsoleUnitTelepathicInterfaceLocationsScreen parent, int width, int height, Vector2i pos) {
            super(parent.client, width, height, LINE_PADDING, pos);
            this.parent = parent;
            this.init();
        }

        public void refreshList() {
            super.refreshList();
            this.parent.filteredLocations.forEach((location) -> this.addEntry(new LocationEntry(location)));
        }

        private class LocationEntry extends BaseListEntry {
            private final Map.Entry<Identifier, EDataType> entry;

            public LocationEntry(Map.Entry<Identifier, EDataType> entry) {
                this.entry = entry;
            }

            @Override
            public Text getText() {
                MutableText narration = Text.translatable(CommonHelper.capitaliseAllWords(this.entry.getKey().getPath().replace("_", " ")));
                Formatting format = Formatting.WHITE;
                if (this.entry.getValue() == EDataType.BIOME) format = Formatting.GOLD;
                else if (this.entry.getValue() == EDataType.STRUCTURE) format = Formatting.AQUA;

                narration = narration.formatted(format);
                return narration;
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int delta) {
                LocationsListWidget.this.parent.setSelected(this);
                return super.mouseClicked(mouseX, mouseY, delta);
            }
        }
    }
}

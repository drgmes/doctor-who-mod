package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.enums.TardisTelepathicInterfaceDataType;
import net.drgmes.dwm.network.server.TardisConsoleUnitTelepathicInterfaceLocationApplyPacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class TardisConsoleUnitTelepathicInterfaceLocationsScreen extends BaseTardisConsoleUnitTelepathicInterfaceScreen {
    private final List<Entry<Identifier, TardisTelepathicInterfaceDataType>> locations;
    private List<Entry<Identifier, TardisTelepathicInterfaceDataType>> filteredLocations;

    private LocationsListWidget locationsListWidget;
    private LocationsListWidget.LocationEntry selected = null;

    private TextFieldWidget search;
    private String lastSearch;

    public TardisConsoleUnitTelepathicInterfaceLocationsScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, List<Entry<Identifier, TardisTelepathicInterfaceDataType>> locations) {
        super(DWM.TEXTS.TELEPATHIC_INTERFACE_TITLE_LOCATIONS, tardisConsoleUnitBlockEntity);

        this.locations = Collections.unmodifiableList(locations);
        this.filteredLocations = this.locations;
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return !this.search.isFocused();
    }

    @Override
    public void init() {
        super.init();

        this.locationsListWidget = new LocationsListWidget(this, this.getLocationsListPos(), this.getLocationsListSize());

        Vector2i searchPos = this.getLeftTopRenderPos(1, 1);
        this.search = new TextFieldWidget(this.textRenderer, searchPos.x, searchPos.y, this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2 - 2, 18, DWM.TEXTS.TELEPATHIC_INTERFACE_SEARCH);

        this.addDrawableChild(this.locationsListWidget);
        this.addDrawableChild(this.search);

        this.setInitialFocus(this.search);
        this.update();
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
    public void tick() {
        this.locationsListWidget.setSelected(this.selected);

        if (!this.search.getText().equals(lastSearch)) {
            this.selected = null;
            this.reloadLocationsList();
            this.locationsListWidget.refreshList();
            this.update();
        }
    }

    @Override
    public void apply() {
        if (this.selected != null) {
            new TardisConsoleUnitTelepathicInterfaceLocationApplyPacket(this.selected.entry.getKey().toString(), this.selected.entry.getValue().name()).sendToServer();
        }

        super.apply();
    }

    private boolean hasSearch() {
        return this.search != null && !Objects.equals(this.search.getText(), "");
    }

    private Vector2i getLocationsListPos() {
        return this.getLeftTopRenderPos(0, 22);
    }

    private Vector2i getLocationsListSize() {
        return new Vector2i(this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2, this.getBackgroundSize().y - this.getBackgroundBorderSize().y * 2 - BUTTON_SIZE - 22 - 4);
    }

    private void update() {
        this.lastSearch = this.search.getText();
        this.acceptButton.active = this.selected != null;
    }

    private void setSelected(LocationsListWidget.LocationEntry entry) {
        this.selected = entry == this.selected ? null : entry;
        this.update();
    }

    private void reloadLocationsList() {
        if (this.hasSearch()) {
            this.lastSearch = this.search.getText();
            this.filteredLocations = this.locations.stream().filter((str) -> (
                str.getKey().getPath().toLowerCase().contains(this.search.getText().toLowerCase())
            )).toList();

            return;
        }

        this.filteredLocations = this.locations;
    }

    private static class LocationsListWidget extends BaseListWidget {
        private final TardisConsoleUnitTelepathicInterfaceLocationsScreen parent;

        public LocationsListWidget(TardisConsoleUnitTelepathicInterfaceLocationsScreen parent, Vector2i pos, Vector2i size) {
            super(parent.client, pos, size, LINE_PADDING);
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
            this.parent.filteredLocations.forEach((location) -> this.addEntry(new LocationEntry(location)));
        }

        private class LocationEntry extends BaseListEntry {
            private final Map.Entry<Identifier, TardisTelepathicInterfaceDataType> entry;

            public LocationEntry(Map.Entry<Identifier, TardisTelepathicInterfaceDataType> entry) {
                this.entry = entry;
            }

            @Override
            public Text getText() {
                MutableText narration;

                if (hasShiftDown()) {
                    narration = Text.literal(this.entry.getKey().toString());
                }
                else {
                    narration = Text.translatable(CommonHelper.capitaliseAllWords(this.entry.getKey().getPath().replace("_", " ")));
                }

                Formatting format = Formatting.WHITE;
                if (this.entry.getValue() == TardisTelepathicInterfaceDataType.BIOME) format = Formatting.GOLD;
                else if (this.entry.getValue() == TardisTelepathicInterfaceDataType.STRUCTURE) format = Formatting.AQUA;

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

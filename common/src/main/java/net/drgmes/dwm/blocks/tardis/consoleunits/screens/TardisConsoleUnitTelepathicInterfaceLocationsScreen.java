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

    private boolean isInited;

    private TextFieldWidget searchField;
    private String lastSearch;

    private LocationsListWidget locationsListWidget;
    private LocationsListWidget.LocationEntry selected = null;

    public TardisConsoleUnitTelepathicInterfaceLocationsScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, List<Entry<Identifier, TardisTelepathicInterfaceDataType>> locations) {
        super(DWM.TEXTS.TELEPATHIC_INTERFACE_TITLE_LOCATIONS, tardisConsoleUnitBlockEntity);

        this.locations = Collections.unmodifiableList(locations);
        this.filteredLocations = this.locations;
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return !this.searchField.isFocused();
    }

    @Override
    public void init() {
        super.init();

        this.locationsListWidget = new LocationsListWidget(this, this.getLocationsListPos(), this.getLocationsListSize());

        Vector2i searchFieldPos = this.getLeftTopRenderPos(1, 1);
        this.searchField = new TextFieldWidget(this.textRenderer, searchFieldPos.x, searchFieldPos.y, this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2 - 2, INPUT_HEIGHT, DWM.TEXTS.TELEPATHIC_INTERFACE_SEARCH);

        this.addDrawableChild(this.locationsListWidget);
        this.addDrawableChild(this.searchField);

        this.isInited = true;
        this.setInitialFocus(this.searchField);
        this.update();
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        String search = this.searchField.getText();
        LocationsListWidget.LocationEntry selected = this.selected;

        super.resize(mc, width, height);
        this.searchField.setText(search);
        this.selected = selected;

        if (!this.searchField.getText().isEmpty()) {
            this.reloadLocationsList();
        }
    }

    @Override
    public void tick() {
        this.locationsListWidget.setSelected(this.selected);

        if (!this.searchField.getText().equals(lastSearch)) {
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
        return this.searchField != null && !Objects.equals(this.searchField.getText(), "");
    }

    private Vector2i getLocationsListPos() {
        return this.getLeftTopRenderPos(0, INPUT_HEIGHT + 4);
    }

    private Vector2i getLocationsListSize() {
        return new Vector2i(this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2, this.getBackgroundSize().y - this.getBackgroundBorderSize().y * 2 - SCREEN_MARGIN * 2 - INPUT_HEIGHT - BUTTON_SIZE - 6);
    }

    private void update() {
        if (!this.isInited) return;

        this.lastSearch = this.searchField.getText();
        this.acceptButton.active = this.selected != null;
    }

    private void setSelected(LocationsListWidget.LocationEntry entry) {
        this.selected = entry == this.selected ? null : entry;
        this.update();
    }

    private void reloadLocationsList() {
        if (this.hasSearch()) {
            this.lastSearch = this.searchField.getText();
            if (this.lastSearch == null) this.lastSearch = "";
            final String search = this.lastSearch.toLowerCase().replaceAll("@", "");

            this.filteredLocations = this.locations.stream().filter((str) -> (
                (!this.lastSearch.startsWith("@") && str.getKey().getPath().toLowerCase().contains(search)) ||
                (this.lastSearch.startsWith("@") && str.getKey().getNamespace().toLowerCase().contains(search))
            )).toList();

            return;
        }

        this.filteredLocations = this.locations;
    }

    private static class LocationsListWidget extends BaseListWidget {
        private final TardisConsoleUnitTelepathicInterfaceLocationsScreen parent;

        public LocationsListWidget(TardisConsoleUnitTelepathicInterfaceLocationsScreen parent, Vector2i pos, Vector2i size) {
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
                Identifier id = this.entry.getKey();

                if (hasShiftDown() && hasControlDown()) {
                    narration = Text.literal(id.toString());
                }
                else {
                    narration = Text.literal(CommonHelper.capitaliseAllWords(id.getPath().replace("_", " ")));
                    if (!id.getNamespace().equals("minecraft")) narration = narration.append(Text.literal(String.format(" (%s)", id.getNamespace())).formatted(Formatting.DARK_GRAY));
                }

                Formatting format = Formatting.WHITE;
                if (this.entry.getValue() == TardisTelepathicInterfaceDataType.BIOME) format = Formatting.GOLD;
                else if (this.entry.getValue() == TardisTelepathicInterfaceDataType.STRUCTURE) format = Formatting.AQUA;

                narration = narration.formatted(format);
                return narration;
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (!super.mouseClicked(mouseX, mouseY, button)) return false;
                LocationsListWidget.this.parent.setSelected(this);
                return true;
            }
        }
    }
}

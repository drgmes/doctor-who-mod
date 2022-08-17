package net.drgmes.dwm.blocks.tardis.consoles.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.network.TardisConsoleRemoteCallablePackets;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.PacketHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class TardisConsoleTelepathicInterfaceLocationsScreen extends BaseTardisConsoleTelepathicInterfaceScreen {
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

    public TardisConsoleTelepathicInterfaceLocationsScreen(BaseTardisConsoleBlockEntity tardisConsoleBlockEntity, List<Entry<Identifier, EDataType>> locations) {
        super(tardisConsoleBlockEntity);

        this.locations = Collections.unmodifiableList(locations);
        this.filteredLocations = this.locations;
    }

    @Override
    public Text getTitle() {
        return DWM.TEXTS.TELEPATHIC_INTERFACE_NAME_LOCATIONS;
    }

    @Override
    protected void init() {
        int locationsListGhostSpace = 10;
        int locationsListWidth = (int) this.getBackgroundSize().x - BACKGROUND_BORDERS * 2;
        int locationsListHeight = (int) this.getBackgroundSize().y - BACKGROUND_BORDERS * 2 - 20 - BUTTON_HEIGHT - locationsListGhostSpace;
        int locationsListOffset = (int) this.getBackgroundSize().y - locationsListHeight - BACKGROUND_BORDERS - BUTTON_HEIGHT - locationsListGhostSpace + 1;

        Vec2f searchPos = this.getRenderPos(BACKGROUND_BORDERS + 1, BACKGROUND_BORDERS + 1);
        this.search = new TextFieldWidget(this.textRenderer, (int) searchPos.x, (int) searchPos.y, locationsListWidth - 2, 18, DWM.TEXTS.TELEPATHIC_INTERFACE_FLD_SEARCH);

        Vec2f locationsListPos = this.getRenderPos(BACKGROUND_BORDERS, locationsListOffset);
        this.locationsListWidget = new LocationsListWidget(this, locationsListWidth, locationsListHeight, locationsListPos);

        this.addDrawableChild(this.locationsListWidget);
        this.addDrawableChild(this.search);

        super.init();
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
    protected void apply() {
        if (this.selected != null) {
            PacketHelper.sendToServer(
                TardisConsoleRemoteCallablePackets.class,
                "applyTardisConsoleTelepathicInterfaceLocation",
                this.selected.entry.getKey(), this.selected.entry.getValue().name()
            );
        }

        super.apply();
    }

    @Override
    public void tick() {
        this.search.tick();
        this.locationsListWidget.setSelected(this.selected);

        if (!search.getText().equals(lastSearch)) {
            this.selected = null;
            this.reloadLocationsList();
            this.locationsListWidget.refreshList();
            this.update();
        }
    }

    public void setSelected(LocationsListWidget.LocationEntry entry) {
        this.selected = entry == this.selected ? null : entry;
        this.update();
    }

    public void reloadLocationsList() {
        boolean hasSearch = this.search != null && !Objects.equals(this.search.getText(), "");

        if (hasSearch) {
            this.lastSearch = this.search.getText();
            this.filteredLocations = this.locations.stream().filter((str) -> (
                str.getKey().getPath().toLowerCase().contains(this.search.getText().toLowerCase())
            )).toList();

            return;
        }

        this.filteredLocations = this.locations;
    }

    private void update() {
        this.lastSearch = this.search.getText();
        this.acceptButton.active = this.selected != null;
    }

    private static class LocationsListWidget extends BaseListWidget {
        private final TardisConsoleTelepathicInterfaceLocationsScreen parent;

        public LocationsListWidget(TardisConsoleTelepathicInterfaceLocationsScreen parent, int width, int height, Vec2f pos) {
            super(parent.client, parent.textRenderer, width, height, LINE_PADDING, pos);
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

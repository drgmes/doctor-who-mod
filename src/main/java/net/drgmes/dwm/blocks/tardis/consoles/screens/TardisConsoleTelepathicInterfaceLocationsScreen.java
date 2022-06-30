package net.drgmes.dwm.blocks.tardis.consoles.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.network.ServerboundTardisConsoleTelepathicInterfaceLocationsApplyPacket;
import net.drgmes.dwm.setup.ModPackets;
import net.drgmes.dwm.utils.DWMUtils;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TardisConsoleTelepathicInterfaceLocationsScreen extends BaseTardisConsoleTelepathicInterfaceScreen {
    public enum DataType {
        BIOME,
        STRUCTURE
    }

    private LocationsListWidget locationsListWidget;
    private LocationsListWidget.LocationEntry selected = null;
    private List<Entry<ResourceLocation, DataType>> locations = new ArrayList<>();
    private List<Entry<ResourceLocation, DataType>> filteredLocations = new ArrayList<>();
    private EditBox search;
    private String lastSearch;

    public TardisConsoleTelepathicInterfaceLocationsScreen(BaseTardisConsoleBlockEntity tardisConsoleBlockEntity, List<Entry<ResourceLocation, DataType>> locations) {
        super(tardisConsoleBlockEntity);

        this.locations = Collections.unmodifiableList(locations);
        this.filteredLocations = Collections.unmodifiableList(this.locations);
    }

    @Override
    public Component getTitle() {
        return DWM.TEXTS.TELEPATHIC_INTERFACE_NAME_LOCATIONS;
    }

    @Override
    protected void init() {
        int locationsListGhostSpace = 10;
        int locationsListWidth = (int) this.getBackgroundSize().x - BACKGROUND_BORDERS * 2;
        int locationsListHeight = (int) this.getBackgroundSize().y - BACKGROUND_BORDERS * 2 - 20 - BUTTON_HEIGHT - locationsListGhostSpace;
        int locationsListOffset = (int) this.getBackgroundSize().y - locationsListHeight - BACKGROUND_BORDERS - BUTTON_HEIGHT - locationsListGhostSpace + 1;

        Vec2 searchPos = this.getRenderPos(BACKGROUND_BORDERS + 1, BACKGROUND_BORDERS + 1);
        this.search = new EditBox(this.font, (int) searchPos.x, (int) searchPos.y, locationsListWidth - 2, 18, DWM.TEXTS.TELEPATHIC_INTERFACE_FLD_SEARCH);
        this.search.setFocus(false);
        this.search.setCanLoseFocus(true);

        Vec2 locationsListPos = this.getRenderPos(BACKGROUND_BORDERS, locationsListOffset);
        this.locationsListWidget = new LocationsListWidget(this, locationsListWidth, locationsListHeight, locationsListPos);

        this.addRenderableWidget(this.locationsListWidget);
        this.addRenderableWidget(this.search);

        super.init();
        this.update();
    }

    @Override
    public void resize(Minecraft mc, int width, int height) {
        String search = this.search.getValue();
        LocationsListWidget.LocationEntry selected = this.selected;

        super.resize(mc, width, height);
        this.search.setValue(search);
        this.selected = selected;

        if (!this.search.getValue().isEmpty()) {
            this.reloadLocationsList();
        }
    }

    @Override
    protected void apply() {
        if (this.selected != null) {
            ModPackets.INSTANCE.sendToServer(new ServerboundTardisConsoleTelepathicInterfaceLocationsApplyPacket(this.selected.entry));
        }

        super.apply();
    }

    @Override
    public void tick() {
        this.search.tick();
        this.locationsListWidget.setSelected(this.selected);

        if (!search.getValue().equals(lastSearch)) {
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
        boolean hasSearch = this.search != null && this.search.getValue() != "";

        if (hasSearch) {
            this.lastSearch = this.search.getValue();
            this.filteredLocations = this.locations.stream().filter((str) -> (
                str.getKey().getPath().toLowerCase().contains(this.search.getValue().toLowerCase())
            )).toList();

            return;
        }

        this.filteredLocations = this.locations;
    }

    private void update() {
        this.lastSearch = this.search.getValue();
        this.acceptButton.active = this.selected != null;
    }

    private class LocationsListWidget extends BaseListWidget {
        private final TardisConsoleTelepathicInterfaceLocationsScreen parent;

        public LocationsListWidget(TardisConsoleTelepathicInterfaceLocationsScreen parent, int width, int height, Vec2 pos) {
            super(parent.minecraft, parent.font, width, height, LINE_PADDING, pos);
            this.parent = parent;
            this.init();
        }

        public void refreshList() {
            super.refreshList();
            this.parent.filteredLocations.forEach((location) -> this.addEntry(new LocationEntry(location)));
        }

        private class LocationEntry extends BaseListWidget.BaseListEntry {
            private final Map.Entry<ResourceLocation, DataType> entry;

            public LocationEntry(Map.Entry<ResourceLocation, DataType> entry) {
                this.entry = entry;
            }

            @Override
            public Component getNarration() {
                MutableComponent narration = Component.translatable(DWMUtils.capitaliseAllWords(this.entry.getKey().getPath().replace("_", " ")));
                ChatFormatting format = ChatFormatting.WHITE;
                if (this.entry.getValue() == DataType.BIOME) format = ChatFormatting.GOLD;
                else if (this.entry.getValue() == DataType.STRUCTURE) format = ChatFormatting.AQUA;

                narration.setStyle(narration.getStyle().withColor(format));
                return narration;
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int partialTicks) {
                LocationsListWidget.this.parent.setSelected(this);
                return super.mouseClicked(mouseX, mouseY, partialTicks);
            }
        }
    }
}

package net.drgmes.dwm.blocks.tardis.consoles.screens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;

import org.codehaus.plexus.util.StringUtils;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.network.ServerboundTardisConsoleTelepathicInterfaceLocationsApplyPacket;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.drgmes.dwm.setup.ModPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.core.Registry;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

public class TardisConsoleTelepathicInterfaceLocationsScreen extends BaseTardisConsoleTelepathicInterfaceScreen {
    public static enum DataType {
        BIOME,
        STRUCTURE
    }

    private LocationsListWidget locationsListWidget;
    private LocationsListWidget.LocationEntry selected = null;
    private List<Entry<ResourceLocation, DataType>> locations = new ArrayList<>();
    private List<Entry<ResourceLocation, DataType>> filteredLocations = new ArrayList<>();

    private EditBox search;
    private String lastSearch;

    public TardisConsoleTelepathicInterfaceLocationsScreen(BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
        super(tardisConsoleBlockEntity);

        this.locations = Collections.unmodifiableList(this.createLocationsList());
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
    public void tick() {
        this.search.tick();
        this.locationsListWidget.setSelected(this.selected);

        if (!search.getValue().equals(lastSearch)) {
            this.reloadLocationsList();
            this.locationsListWidget.refreshList();
            this.update();
        }
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

    private void update() {
        this.lastSearch = this.search.getValue();
        this.acceptButton.active = this.selected != null;
    }

    public void setSelected(LocationsListWidget.LocationEntry entry) {
        this.selected = entry == this.selected ? null : entry;
        this.update();
    }

    public List<Entry<ResourceLocation, DataType>> createLocationsList() {
        List<Entry<ResourceLocation, DataType>> list = new ArrayList<>();
        this.getLocations(Registry.BIOME_REGISTRY, DataType.BIOME).forEach(list::add);
        this.getLocations(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, DataType.STRUCTURE).forEach(list::add);

        return list;
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

    public <T extends ObjectSelectionList.Entry<T>> void buildLocationsList(Consumer<T> consumer, Function<Entry<ResourceLocation, DataType>, T> entry) {
        this.filteredLocations.forEach((location) -> consumer.accept(entry.apply(location)));
    }

    private <T> List<Entry<ResourceLocation, DataType>> getLocations(ResourceKey<Registry<T>> registryKey, DataType type) {
        Minecraft mc = Minecraft.getInstance();
        Registry<T> registry = mc.level.registryAccess().registryOrThrow(registryKey);
        List<Entry<ResourceLocation, DataType>> list = new ArrayList<>(
            registry.keySet().stream()
            .filter((res) -> !res.equals(ModDimensionTypes.TARDIS.location()))
            .map((res) -> Map.entry(res, type)).toList()
        );

        if (list.size() > 0) {
            Collections.sort(list, new Comparator<Entry<ResourceLocation, DataType>>() {
                @Override
                public int compare(Entry<ResourceLocation, DataType> a, Entry<ResourceLocation, DataType> b) {
                    return a.getKey().getPath().compareTo(b.getKey().getPath());
                }
            });
        }

        return list;
    }

    private class LocationsListWidget extends ObjectSelectionList<LocationsListWidget.LocationEntry> {
        private final TardisConsoleTelepathicInterfaceLocationsScreen parent;

        public LocationsListWidget(TardisConsoleTelepathicInterfaceLocationsScreen parent, int width, int height, Vec2 pos) {
            super(parent.minecraft, width, height, (int) pos.y, (int) pos.y + height, parent.font.lineHeight + LINE_PADDING * 2);
            this.parent = parent;

            this.refreshList();
            this.setLeftPos((int) pos.x);
            this.setRenderBackground(false);
            this.setRenderTopAndBottom(false);
        }

        @Override
        protected int getScrollbarPosition() {
            return this.getRight() - 5;
        }

        @Override
        public int getRowWidth() {
            return this.width;
        }

        public void refreshList() {
            this.clearEntries();
            this.parent.buildLocationsList(this::addEntry, (item) -> new LocationEntry(item, this.parent));
        }

        @Override
        public NarratableEntry.NarrationPriority narrationPriority() {
            return NarratableEntry.NarrationPriority.NONE;
        }

        private class LocationEntry extends ObjectSelectionList.Entry<LocationEntry> {
            private final TardisConsoleTelepathicInterfaceLocationsScreen parent;
            private final Map.Entry<ResourceLocation, DataType> entry;

            public LocationEntry(Map.Entry<ResourceLocation, DataType> entry, TardisConsoleTelepathicInterfaceLocationsScreen parent) {
                this.entry = entry;
                this.parent = parent;
            }

            @Override
            public Component getNarration() {
                return new TranslatableComponent(StringUtils.capitaliseAllWords(this.entry.getKey().getPath().replace("_", " ")));
            }

            @Override
            public void render(PoseStack poseStack, int entryIdx, int top, int left, int entryWidth, int height, int mouseX, int mouseY, boolean flag, float partialTick) {
                Component name = this.getNarration();
                font.drawShadow(poseStack, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(name, width))), left + LINE_PADDING, top + 2, 0xFFFFFF);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int partialTicks) {
                this.parent.setSelected(this);
                LocationsListWidget.this.setSelected(this);
                return false;
            }
        }
    }
}

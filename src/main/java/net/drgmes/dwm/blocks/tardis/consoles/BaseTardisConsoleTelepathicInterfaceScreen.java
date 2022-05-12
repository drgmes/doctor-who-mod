package net.drgmes.dwm.blocks.tardis.consoles;

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
import net.drgmes.dwm.network.ServerboundTardisConsoleTelepathicInterfaceUpdatePacket;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.drgmes.dwm.setup.ModPackets;
import net.drgmes.dwm.utils.base.screens.IBaseScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Registry;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.gui.GuiUtils;

public class BaseTardisConsoleTelepathicInterfaceScreen extends Screen implements IBaseScreen {
    public static enum DataType {
        BIOME,
        STRUCTURE
    }

    private final static TranslatableComponent TELEPATHIC_INTERFACE_NAME = new TranslatableComponent("screen." + DWM.MODID + ".telepathic_interface");
    private final static TranslatableComponent TELEPATHIC_INTERFACE_SEARCH = new TranslatableComponent("screen." + DWM.MODID + ".telepathic_interface.search");
    private final static TranslatableComponent TELEPATHIC_INTERFACE_CANCEL = new TranslatableComponent("screen." + DWM.MODID + ".telepathic_interface.cancel");
    private final static TranslatableComponent TELEPATHIC_INTERFACE_ACCEPT = new TranslatableComponent("screen." + DWM.MODID + ".telepathic_interface.accept");
    private final static int LINE_PADDING = 3;

    private final BaseTardisConsoleBlockEntity tardisConsoleBlockEntity;

    private LocationsListWidget locationsListWidget;
    private LocationsListWidget.LocationEntry selected = null;
    private List<Entry<ResourceLocation, DataType>> locations = new ArrayList<>();
    private List<Entry<ResourceLocation, DataType>> filteredLocations = new ArrayList<>();

    private EditBox search;
    private String lastSearch;

    private Button cancelButton;
    private Button acceptButton;

    public BaseTardisConsoleTelepathicInterfaceScreen(BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
        super(TELEPATHIC_INTERFACE_NAME);

        this.tardisConsoleBlockEntity = tardisConsoleBlockEntity;
        this.locations = Collections.unmodifiableList(this.createLocationsList());
        this.filteredLocations = Collections.unmodifiableList(this.locations);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public Font getFont() {
        return this.font;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public ResourceLocation getBackground() {
        return DWM.TEXTURES.GUI.TARDIS.CONSOLE.TELEPATHIC_INTERFACE;
    }

    @Override
    public Vec2 getBackgroundSize() {
        return DWM.TEXTURES.GUI.TARDIS.CONSOLE.TELEPATHIC_INTERFACE_SIZE.scale(0.795F);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void blit(PoseStack poseStack, int x, int y, int textureX, int textureY, int textureWidth, int textureHeight, int textureClipX, int textureClipY) {
        super.blit(poseStack, x, y, textureX, textureY, textureWidth, textureHeight, textureClipX, textureClipY);
    }

    @Override
    public Vec2 getTitleRenderPos() {
        return this.getRenderPos(23, 8);
    }

    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

        int backgroundBorders = 24;
        int buttonHeight = 20;
        int buttonWidth = (int) (this.getBackgroundSize().x - backgroundBorders * 2) / 2;

        int locationsListGhostSpace = 10;
        int locationsListWidth = (int) this.getBackgroundSize().x - backgroundBorders * 2;
        int locationsListHeight = (int) this.getBackgroundSize().y - backgroundBorders * 2 - 20 - buttonHeight - locationsListGhostSpace;
        int locationsListOffset = (int) this.getBackgroundSize().y - locationsListHeight - backgroundBorders - buttonHeight - locationsListGhostSpace + 1;

        Vec2 searchPos = this.getRenderPos(backgroundBorders + 1, backgroundBorders + 1);
        this.search = new EditBox(this.font, (int) searchPos.x, (int) searchPos.y, locationsListWidth - 2, 18, TELEPATHIC_INTERFACE_SEARCH);
        this.search.setFocus(false);
        this.search.setCanLoseFocus(true);

        Vec2 structuresListPos = this.getRenderPos(backgroundBorders, locationsListOffset);
        this.locationsListWidget = new LocationsListWidget(this, locationsListWidth, locationsListHeight, structuresListPos);

        Vec2 cancelButtonPos = this.getRenderPos(backgroundBorders - 1, locationsListHeight + locationsListOffset + locationsListGhostSpace);
        this.cancelButton = new Button((int) cancelButtonPos.x, (int) cancelButtonPos.y, buttonWidth, buttonHeight, TELEPATHIC_INTERFACE_CANCEL, (b) -> this.onClose());

        Vec2 acceptButtonPos = this.getRenderPos(backgroundBorders + buttonWidth + 1, locationsListHeight + locationsListOffset + locationsListGhostSpace);
        this.acceptButton = new Button((int) acceptButtonPos.x, (int) acceptButtonPos.y, buttonWidth, buttonHeight, TELEPATHIC_INTERFACE_ACCEPT, (b) -> this.apply());
        this.acceptButton.active = false;

        this.addRenderableWidget(this.locationsListWidget);
        this.addRenderableWidget(this.search);
        this.addRenderableWidget(this.cancelButton);
        this.addRenderableWidget(this.acceptButton);

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
    public boolean mouseClicked(double mouseX, double mouseY, int frame) {
        if (this.onButtonCloseClick(mouseX, mouseY)) this.onDone();
        return super.mouseClicked(mouseX, mouseY, frame);
    }

    @Override
    public void removed() {
        assert this.minecraft != null;
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(poseStack);
        this.renderBackground(poseStack, mouseX, mouseY);

        Vec2 pos = new Vec2(19, 5);
        Vec2 pos1 = this.getRenderPos(pos.x, pos.y);
        Vec2 pos2 = this.getRenderPos(pos.x + this.font.width(this.title.getString()) + 9, pos.y + 15);
        int color = 0xFF4F5664;

        GuiUtils.drawGradientRect(poseStack.last().pose(), 0, (int) pos1.x, (int) pos1.y, (int) pos2.x, (int) pos2.y, color, color);
        this.renderTitle(poseStack);

        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void resize(Minecraft mc, int width, int height) {
        String search = this.search.getValue();
        LocationsListWidget.LocationEntry selected = this.selected;

        this.init(mc, width, height);
        this.search.setValue(search);
        this.selected = selected;

        if (!this.search.getValue().isEmpty()) {
            this.reloadLocationsList();
        }
    }

    @Override
    public void onClose() {
        this.onDone();
    }

    private void onDone() {
        this.tardisConsoleBlockEntity.setChanged();
        this.minecraft.setScreen((Screen) null);
    }

    private void apply() {
        if (this.selected != null) {
            ModPackets.INSTANCE.sendToServer(new ServerboundTardisConsoleTelepathicInterfaceUpdatePacket(this.selected.entry));
        }

        this.onDone();
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
        private final BaseTardisConsoleTelepathicInterfaceScreen parent;

        public LocationsListWidget(BaseTardisConsoleTelepathicInterfaceScreen parent, int width, int height, Vec2 pos) {
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
            private final BaseTardisConsoleTelepathicInterfaceScreen parent;
            private final Map.Entry<ResourceLocation, DataType> entry;

            public LocationEntry(Map.Entry<ResourceLocation, DataType> entry, BaseTardisConsoleTelepathicInterfaceScreen parent) {
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

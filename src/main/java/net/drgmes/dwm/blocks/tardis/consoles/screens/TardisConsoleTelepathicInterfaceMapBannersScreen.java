package net.drgmes.dwm.blocks.tardis.consoles.screens;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;

import org.codehaus.plexus.util.StringUtils;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.network.ServerboundTardisConsoleTelepathicInterfaceMapBannersApplyPacket;
import net.drgmes.dwm.setup.ModPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.saveddata.maps.MapBanner;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec2;

public class TardisConsoleTelepathicInterfaceMapBannersScreen extends BaseTardisConsoleTelepathicInterfaceScreen {
    private final MapItemSavedData mapData;
    private final Collection<MapBanner> banners;

    private BannersListWidget bannersListWidget;
    private BannersListWidget.BannerEntry selected = null;

    public TardisConsoleTelepathicInterfaceMapBannersScreen(BaseTardisConsoleBlockEntity tardisConsoleBlockEntity, MapItemSavedData mapData) {
        super(tardisConsoleBlockEntity);
        this.mapData = mapData;
        this.banners = mapData.getBanners();
    }

    @Override
    public Component getTitle() {
        return DWM.TEXTS.TELEPATHIC_INTERFACE_NAME_BANNERS;
    }

    @Override
    protected void init() {
        int bannersListGhostSpace = 10;
        int bannersListWidth = (int) this.getBackgroundSize().x - BACKGROUND_BORDERS * 2;
        int bannersListHeight = (int) this.getBackgroundSize().y - BACKGROUND_BORDERS * 2 - BUTTON_HEIGHT - bannersListGhostSpace;

        Vec2 bannersListPos = this.getRenderPos(BACKGROUND_BORDERS, BACKGROUND_BORDERS);
        this.bannersListWidget = new BannersListWidget(this, bannersListWidth, bannersListHeight, bannersListPos);
        this.addRenderableWidget(this.bannersListWidget);

        super.init();
        this.update();
    }

    @Override
    public void tick() {
        this.bannersListWidget.setSelected(this.selected);
    }

    @Override
    public void resize(Minecraft mc, int width, int height) {
        BannersListWidget.BannerEntry selected = this.selected;
        super.resize(mc, width, height);
        this.selected = selected;
    }

    @Override
    protected void apply() {
        if (this.selected != null) {
            ModPackets.INSTANCE.sendToServer(new ServerboundTardisConsoleTelepathicInterfaceMapBannersApplyPacket(this.mapData, this.selected.banner));
        }

        this.onDone();
    }

    private void update() {
        this.acceptButton.active = this.selected != null;
    }

    public void setSelected(BannersListWidget.BannerEntry entry) {
        this.selected = entry == this.selected ? null : entry;
        this.update();
    }

    public <T extends ObjectSelectionList.Entry<T>> void buildBannersList(Consumer<T> consumer, Function<MapBanner, T> entry) {
        this.banners.forEach((banner) -> consumer.accept(entry.apply(banner)));
    }

    private class BannersListWidget extends ObjectSelectionList<BannersListWidget.BannerEntry> {
        private final TardisConsoleTelepathicInterfaceMapBannersScreen parent;

        public BannersListWidget(TardisConsoleTelepathicInterfaceMapBannersScreen parent, int width, int height, Vec2 pos) {
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
            this.parent.buildBannersList(this::addEntry, (item) -> new BannerEntry(item, this.parent));
        }

        @Override
        public NarratableEntry.NarrationPriority narrationPriority() {
            return NarratableEntry.NarrationPriority.NONE;
        }

        private class BannerEntry extends ObjectSelectionList.Entry<BannerEntry> {
            private final TardisConsoleTelepathicInterfaceMapBannersScreen parent;
            private final MapBanner banner;

            public BannerEntry(MapBanner banner, TardisConsoleTelepathicInterfaceMapBannersScreen parent) {
                this.banner = banner;
                this.parent = parent;
            }

            @Override
            public Component getNarration() {
                return new TranslatableComponent(StringUtils.capitaliseAllWords(this.banner.getColor().getName().replace("_", " ")));
            }

            @Override
            public void render(PoseStack poseStack, int entryIdx, int top, int left, int entryWidth, int height, int mouseX, int mouseY, boolean flag, float partialTick) {
                Component name = this.getNarration();
                font.drawShadow(poseStack, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(name, width))), left + LINE_PADDING, top + 2, 0xFFFFFF);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int partialTicks) {
                this.parent.setSelected(this);
                BannersListWidget.this.setSelected(this);
                return false;
            }
        }
    }
}

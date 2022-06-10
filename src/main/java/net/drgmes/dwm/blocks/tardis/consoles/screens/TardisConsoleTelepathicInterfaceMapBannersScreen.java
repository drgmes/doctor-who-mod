package net.drgmes.dwm.blocks.tardis.consoles.screens;

import java.util.Collection;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.network.ServerboundTardisConsoleTelepathicInterfaceMapBannersApplyPacket;
import net.drgmes.dwm.setup.ModPackets;
import net.drgmes.dwm.utils.DWMUtils;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
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

    private class BannersListWidget extends BaseListWidget {
        private final TardisConsoleTelepathicInterfaceMapBannersScreen parent;

        public BannersListWidget(TardisConsoleTelepathicInterfaceMapBannersScreen parent, int width, int height, Vec2 pos) {
            super(parent.minecraft, parent.font, width, height, LINE_PADDING, pos);
            this.parent = parent;
            this.init();
        }

        public void refreshList() {
            super.refreshList();
            this.parent.banners.forEach((banner) -> this.addEntry(new BannerEntry(banner)));
        }

        private class BannerEntry extends BaseListWidget.BaseListEntry {
            private final MapBanner banner;

            public BannerEntry(MapBanner banner) {
                this.banner = banner;
            }

            @Override
            public Component getNarration() {
                return Component.translatable(DWMUtils.capitaliseAllWords(this.banner.getColor().getName().replace("_", " ")));
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int partialTicks) {
                BannersListWidget.this.parent.setSelected(this);
                return super.mouseClicked(mouseX, mouseY, partialTicks);
            }
        }
    }
}

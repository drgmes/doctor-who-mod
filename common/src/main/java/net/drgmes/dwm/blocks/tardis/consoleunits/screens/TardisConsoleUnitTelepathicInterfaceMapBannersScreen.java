package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.network.server.TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.map.MapBannerMarker;
import net.minecraft.item.map.MapState;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;

import java.util.Collection;

public class TardisConsoleUnitTelepathicInterfaceMapBannersScreen extends BaseTardisConsoleUnitTelepathicInterfaceScreen {
    private final Collection<MapBannerMarker> banners;

    private BannersListWidget bannersListWidget;
    private BannersListWidget.BannerEntry selected = null;

    public TardisConsoleUnitTelepathicInterfaceMapBannersScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, MapState mapData) {
        super(DWM.TEXTS.TELEPATHIC_INTERFACE_NAME_BANNERS, tardisConsoleUnitBlockEntity);
        this.banners = mapData.getBanners();
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        BannersListWidget.BannerEntry selected = this.selected;
        super.resize(mc, width, height);
        this.selected = selected;
    }

    @Override
    public void tick() {
        this.bannersListWidget.setSelected(this.selected);
    }

    public void setSelected(BannersListWidget.BannerEntry entry) {
        this.selected = entry == this.selected ? null : entry;
        this.update();
    }

    @Override
    protected void init() {
        int bannersListGhostSpace = 2;
        int bannersListWidth = (int) this.getBackgroundSize().x - BACKGROUND_BORDERS * 2;
        int bannersListHeight = (int) this.getBackgroundSize().y - BACKGROUND_BORDERS * 2 - BUTTON_HEIGHT - bannersListGhostSpace;

        Vec2f bannersListPos = this.getRenderPos(BACKGROUND_BORDERS, BACKGROUND_BORDERS);
        this.bannersListWidget = new BannersListWidget(this, bannersListWidth, bannersListHeight, bannersListPos);
        this.addDrawableChild(this.bannersListWidget);

        super.init();
        this.update();
    }

    @Override
    protected void apply() {
        if (this.selected != null) {
            new TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket(this.selected.banner.getNbt()).sendToServer();
        }

        this.close();
    }

    private void update() {
        this.acceptButton.active = this.selected != null;
    }

    private static class BannersListWidget extends BaseListWidget {
        private final TardisConsoleUnitTelepathicInterfaceMapBannersScreen parent;

        public BannersListWidget(TardisConsoleUnitTelepathicInterfaceMapBannersScreen parent, int width, int height, Vec2f pos) {
            super(parent.client, parent.textRenderer, width, height, LINE_PADDING, pos);
            this.parent = parent;
            this.init();
        }

        public void refreshList() {
            super.refreshList();
            this.parent.banners.forEach((banner) -> this.addEntry(new BannerEntry(banner)));
        }

        private class BannerEntry extends BaseListEntry {
            private final MapBannerMarker banner;

            public BannerEntry(MapBannerMarker banner) {
                this.banner = banner;
            }

            @Override
            public Text getText() {
                return Text.translatable(CommonHelper.capitaliseAllWords(this.banner.getColor().getName().replace("_", " ")));
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int delta) {
                BannersListWidget.this.parent.setSelected(this);
                return super.mouseClicked(mouseX, mouseY, delta);
            }
        }
    }
}

package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.network.server.TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket;
import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.map.MapBannerMarker;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.Collection;

@Environment(EnvType.CLIENT)
public class TardisConsoleUnitTelepathicInterfaceMapBannersScreen extends BaseTardisConsoleUnitTelepathicInterfaceScreen {
    private final Collection<MapBannerMarker> banners;
    private final RegistryKey<World> dimension;

    private BannersListWidget bannersListWidget;
    private BannersListWidget.BannerEntry selected = null;

    public TardisConsoleUnitTelepathicInterfaceMapBannersScreen(BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity, MapState mapData) {
        super(DWM.TEXTS.TELEPATHIC_INTERFACE_TITLE_BANNERS, tardisConsoleUnitBlockEntity);
        this.banners = mapData.getBanners();
        this.dimension = mapData.dimension;
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return true;
    }

    @Override
    public void init() {
        super.init();

        this.bannersListWidget = new BannersListWidget(this, this.getBannersListPos(), this.getBannersListSize());
        this.addDrawableChild(this.bannersListWidget);
        this.update();
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

    @Override
    public void apply() {
        if (this.selected != null) {
            new TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket(this.dimension, this.selected.banner.color(), this.selected.banner.pos()).sendToServer();
        }

        this.close();
    }

    private Vector2i getBannersListPos() {
        return this.getLeftTopRenderPos(0, 0);
    }

    private Vector2i getBannersListSize() {
        return new Vector2i(this.getBackgroundSize().x - this.getBackgroundBorderSize().x * 2, this.getBackgroundSize().y - this.getBackgroundBorderSize().y * 2 - BUTTON_SIZE - 4);
    }

    private void update() {
        this.acceptButton.active = this.selected != null;
    }

    private void setSelected(BannersListWidget.BannerEntry entry) {
        this.selected = entry == this.selected ? null : entry;
        this.update();
    }

    private static class BannersListWidget extends BaseListWidget {
        private final TardisConsoleUnitTelepathicInterfaceMapBannersScreen parent;

        public BannersListWidget(TardisConsoleUnitTelepathicInterfaceMapBannersScreen parent, Vector2i pos, Vector2i size) {
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
            this.parent.banners.forEach((banner) -> this.addEntry(new BannerEntry(banner)));
        }

        private class BannerEntry extends BaseListEntry {
            private final MapBannerMarker banner;

            public BannerEntry(MapBannerMarker banner) {
                this.banner = banner;
            }

            @Override
            public Text getText() {
                return Text.translatable(CommonHelper.capitaliseAllWords(this.banner.color().getName().replace("_", " ")));
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int delta) {
                BannersListWidget.this.parent.setSelected(this);
                return super.mouseClicked(mouseX, mouseY, delta);
            }
        }
    }
}

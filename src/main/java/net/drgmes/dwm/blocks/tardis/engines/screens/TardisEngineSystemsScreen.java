package net.drgmes.dwm.blocks.tardis.engines.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.engines.containers.TardisEngineSystemsContainer;
import net.drgmes.dwm.utils.base.screens.BaseContainerScreen;
import net.drgmes.dwm.utils.base.screens.IBaseScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.phys.Vec2;

public class TardisEngineSystemsScreen extends BaseContainerScreen<TardisEngineSystemsContainer> implements IBaseScreen {
    public TardisEngineSystemsScreen(TardisEngineSystemsContainer menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public ResourceLocation getBackground() {
        return DWM.TEXTURES.GUI.TARDIS.ENGINE.SYSTEMS_INTERFACE;
    }

    @Override
    public Vec2 getBackgroundSize() {
        return DWM.TEXTURES.GUI.TARDIS.ENGINE.SYSTEMS_INTERFACE_SIZE;
    }

    @Override
    public Vec2 getTitleRenderPos() {
        return this.getRenderPos(10, 5);
    }
}

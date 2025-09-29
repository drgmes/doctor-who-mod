package net.drgmes.dwm.blocks.tardis.engines.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.engines.screens.handlers.TardisEngineSystemsScreenHandler;
import net.drgmes.dwm.utils.base.screens.BaseContainerScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public class TardisEngineSystemsScreen extends BaseContainerScreen<TardisEngineSystemsScreenHandler> {
    public TardisEngineSystemsScreen(TardisEngineSystemsScreenHandler menu, PlayerInventory playerInventory, Text text) {
        super(menu, playerInventory, text);
    }

    @Override
    public Identifier getBackground() {
        return DWM.TEXTURES.GUI.TARDIS.ENGINE.SYSTEMS_INTERFACE;
    }

    @Override
    public Vector2i getBackgroundOriginSize() {
        return DWM.TEXTURES.GUI.TARDIS.ENGINE.SYSTEMS_INTERFACE_SIZE;
    }

    @Override
    public Vector2i getTitleRenderPos() {
        return this.getRenderPos(8, 6);
    }
}

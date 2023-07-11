package net.drgmes.dwm.utils.helpers.forge;

import net.minecraft.server.MinecraftServer;

public class DimensionHelperImpl {
    public static void setChanged(MinecraftServer server) {
        server.markWorldsDirty();
    }
}

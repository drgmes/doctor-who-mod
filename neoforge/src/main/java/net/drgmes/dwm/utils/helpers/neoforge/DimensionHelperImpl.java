package net.drgmes.dwm.utils.helpers.neoforge;

import net.minecraft.server.MinecraftServer;

public class DimensionHelperImpl {
    public static void setChanged(MinecraftServer server) {
        server.markWorldsDirty();
    }
}

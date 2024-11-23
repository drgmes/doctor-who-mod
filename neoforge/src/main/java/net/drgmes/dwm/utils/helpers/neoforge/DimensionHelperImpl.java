package net.drgmes.dwm.utils.helpers.neoforge;

import net.minecraft.server.MinecraftServer;

public class DimensionHelperImpl {
    @SuppressWarnings("deprecation")
    public static void setChanged(MinecraftServer server) {
        server.markWorldsDirty();
    }
}

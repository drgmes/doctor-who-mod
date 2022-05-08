package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.caps.ITardisChunkLoader;
import net.drgmes.dwm.caps.ITardisChunkLoader.TardisChunkLoaderProvider;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.caps.ITardisLevelData.TardisLevelDataProvider;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkTracker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.Bindings;

public class ModCapabilities {
    public static final ResourceLocation TARDIS_LOC = new ResourceLocation(DWM.MODID, "tardis_data");
    public static final Capability<ITardisLevelData> TARDIS_DATA = CapabilityManager.get(new CapabilityToken<>() {});

    public static final ResourceLocation TARDIS_CHUNK_LOADER_LOC = new ResourceLocation(DWM.MODID, "tardis_chunk_loader");
    public static final Capability<ITardisChunkLoader> TARDIS_CHUNK_LOADER = CapabilityManager.get(new CapabilityToken<>() {});

    public static void init() {
        Bindings.getForgeBus().get().addListener(ModCapabilities::register);
        Bindings.getForgeBus().get().addGenericListener(Level.class, ModCapabilities::attachWorldCaps);
    }

    public static void register(RegisterCapabilitiesEvent e) {
        e.register(ChunkTracker.class);
    }

    public static void attachWorldCaps(AttachCapabilitiesEvent<Level> event) {
        Level level = event.getObject();

        if (TardisHelper.isTardisDimension(level)) {
            TardisLevelDataProvider tardisLevelDataProvider = new TardisLevelDataProvider(level);
            event.addCapability(TARDIS_LOC, tardisLevelDataProvider);
            event.addListener(tardisLevelDataProvider.holder::invalidate);
        }

        if (level instanceof ServerLevel serverLevel) {
            TardisChunkLoaderProvider tardisChunkLoaderProvider = new TardisChunkLoaderProvider(serverLevel);
            event.addCapability(TARDIS_CHUNK_LOADER_LOC, tardisChunkLoaderProvider);
            event.addListener(tardisChunkLoaderProvider.holder::invalidate);
        }
    }
}

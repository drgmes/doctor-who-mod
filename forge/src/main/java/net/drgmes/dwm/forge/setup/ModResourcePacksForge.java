package net.drgmes.dwm.forge.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModResourcePacks;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.profiler.Profiler;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mod.EventBusSubscriber(modid = DWM.MODID)
public class ModResourcePacksForge {
    @SubscribeEvent
    public static void onDataPackRegistry(AddReloadListenerEvent event) {
        event.addListener(new ResourceReloader() {
            @Override
            public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
                ModResourcePacks.setup(manager);
                return synchronizer.whenPrepared(null);
            }

            @Override
            public String getName() {
                return DWM.getIdentifier("resources").toString();
            }
        });
    }
}

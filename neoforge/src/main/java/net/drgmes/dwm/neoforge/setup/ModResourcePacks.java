package net.drgmes.dwm.neoforge.setup;

import net.drgmes.dwm.DWM;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.profiler.Profiler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@EventBusSubscriber(modid = DWM.MODID)
public class ModResourcePacks {
    @SubscribeEvent
    public static void onDataPackRegistry(AddReloadListenerEvent event) {
        event.addListener(new ResourceReloader() {
            @Override
            public String getName() {
                return DWM.getIdentifier("resources").toString();
            }

            @Override
            public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
                net.drgmes.dwm.setup.ModResourcePacks.setup(manager);
                return synchronizer.whenPrepared(null);
            }
        });
    }
}

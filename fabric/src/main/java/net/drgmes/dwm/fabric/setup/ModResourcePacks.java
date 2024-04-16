package net.drgmes.dwm.fabric.setup;

import net.drgmes.dwm.DWM;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class ModResourcePacks {
    public static void setup() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return DWM.getIdentifier("resources");
            }

            @Override
            public void reload(ResourceManager manager) {
                net.drgmes.dwm.setup.ModResourcePacks.setup(manager);
            }
        });
    }
}

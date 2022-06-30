package net.drgmes.dwm.setup;

import net.drgmes.dwm.blocks.tardis.engines.containers.TardisEngineSystemsContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;

public class ModContainers {
    public static final RegistryObject<MenuType<TardisEngineSystemsContainer>> TARDIS_ENGINE = Registration.registerContainer("tardis_engine", TardisEngineSystemsContainer::new);

    public static void init() {
    }
}

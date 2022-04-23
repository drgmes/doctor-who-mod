package net.drgmes.dwm.setup;

import net.drgmes.dwm.blocks.consoles.tardisconsoletoyota.TardisConsoleToyotaBlockEntity;
import net.drgmes.dwm.blocks.tardisdoor.TardisDoorBlockEntity;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final RegistryObject<BlockEntityType<TardisExteriorBlockEntity>> TARDIS_EXTERIOR = Registration.registerBlockEntity(
        "tardis",
        TardisExteriorBlockEntity::new,
        ModBlocks.TARDIS_EXTERIOR.blockObject
    );

    public static final RegistryObject<BlockEntityType<TardisDoorBlockEntity>> TARDIS_DOOR = Registration.registerBlockEntity(
        "tardis_door",
        TardisDoorBlockEntity::new,
        ModBlocks.TARDIS_DOOR.blockObject
    );

    /////////////////////
    // Tardis Consoles //
    /////////////////////

    public static final RegistryObject<BlockEntityType<TardisConsoleToyotaBlockEntity>> TARDIS_CONSOLE_TOYOTA = Registration.registerBlockEntity(
        "tardis_console_toyota",
        TardisConsoleToyotaBlockEntity::new,
        ModBlocks.TARDIS_CONSOLE_TOYOTA.blockObject
    );

    public static void init() {
    }
}

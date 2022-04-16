package net.drgmes.dwm.setup;

import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final RegistryObject<BlockEntityType<TardisExteriorBlockEntity>> TARDIS_EXTERIOR = Registration.registerBlockEntity(
        "tardis",
        TardisExteriorBlockEntity::new,
        ModBlocks.TARDIS_EXTERIOR.blockObject
    );

    public static void init() {
    }
}

package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorcapsule;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class TardisExteriorCapsuleBlockEntity extends BaseTardisExteriorBlockEntity {
    public TardisExteriorCapsuleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_EXTERIOR_CAPSULE.getBlockEntityType(), blockPos, blockState);
    }
}

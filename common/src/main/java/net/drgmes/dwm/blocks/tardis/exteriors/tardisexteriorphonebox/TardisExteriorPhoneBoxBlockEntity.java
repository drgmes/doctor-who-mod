package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorphonebox;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class TardisExteriorPhoneBoxBlockEntity extends BaseTardisExteriorBlockEntity {
    public TardisExteriorPhoneBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_EXTERIOR_PHONE_BOX.getBlockEntityType(), blockPos, blockState);
    }
}

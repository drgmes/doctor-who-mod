package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class TardisExteriorPoliceBoxBlockEntity extends BaseTardisExteriorBlockEntity {
    public TardisExteriorPoliceBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_EXTERIOR_POLICE_BOX, blockPos, blockState);
    }
}

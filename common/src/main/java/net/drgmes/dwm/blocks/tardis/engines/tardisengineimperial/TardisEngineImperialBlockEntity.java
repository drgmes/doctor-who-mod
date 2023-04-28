package net.drgmes.dwm.blocks.tardis.engines.tardisengineimperial;

import net.drgmes.dwm.blocks.tardis.engines.BaseTardisEngineBlockEntity;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class TardisEngineImperialBlockEntity extends BaseTardisEngineBlockEntity {
    public TardisEngineImperialBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_ENGINE_IMPERIAL.getBlockEntityType(), blockPos, blockState);
    }
}

package net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota;

import net.drgmes.dwm.blocks.tardis.engines.BaseTardisEngineBlockEntity;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TardisEngineToyotaBlockEntity extends BaseTardisEngineBlockEntity {
    public TardisEngineToyotaBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_ENGINE_TOYOTA.get(), blockPos, blockState);
    }
}

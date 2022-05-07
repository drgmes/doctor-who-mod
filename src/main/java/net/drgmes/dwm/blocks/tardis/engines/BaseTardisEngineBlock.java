package net.drgmes.dwm.blocks.tardis.engines;

import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedEntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class BaseTardisEngineBlock extends BaseRotatableWaterloggedEntityBlock {
    public BaseTardisEngineBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
}

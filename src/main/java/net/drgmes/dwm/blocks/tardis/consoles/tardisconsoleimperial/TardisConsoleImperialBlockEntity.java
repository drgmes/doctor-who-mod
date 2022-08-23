package net.drgmes.dwm.blocks.tardis.consoles.tardisconsoleimperial;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.common.tardis.consoles.TardisConsoleTypes;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class TardisConsoleImperialBlockEntity extends BaseTardisConsoleBlockEntity {
    public TardisConsoleImperialBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_CONSOLE_IMPERIAL, TardisConsoleTypes.IMPERIAL, blockPos, blockState);
    }
}

package net.drgmes.dwm.blocks.tardis.consoles.tardisconsoletoyota;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.common.tardis.consoles.TardisConsoleType;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TardisConsoleToyotaBlockEntity extends BaseTardisConsoleBlockEntity {
    public TardisConsoleToyotaBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_CONSOLE_TOYOTA.get(), TardisConsoleType.TOYOTA, blockPos, blockState);
    }
}

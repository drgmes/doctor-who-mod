package net.drgmes.dwm.blocks.tardis.consoles.tardisconsoletoyota;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.common.tardis.consoles.TardisConsoleType;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class TardisConsoleToyotaBlockEntity extends BaseTardisConsoleBlockEntity {
    public TardisConsoleToyotaBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_CONSOLE_TOYOTA, TardisConsoleType.TOYOTA, blockPos, blockState);
    }
}

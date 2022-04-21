package net.drgmes.dwm.blocks.consoles.tardisconsoletoyota;

import net.drgmes.dwm.common.tardis.TardisConsoleType;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.utils.base.blockentities.BaseTardisConsoleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TardisConsoleToyotaBlockEntity extends BaseTardisConsoleBlockEntity {
    public TardisConsoleToyotaBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_CONSOLE_TOYOTA.get(), TardisConsoleType.TOYOTA, blockPos, blockState);
    }
}
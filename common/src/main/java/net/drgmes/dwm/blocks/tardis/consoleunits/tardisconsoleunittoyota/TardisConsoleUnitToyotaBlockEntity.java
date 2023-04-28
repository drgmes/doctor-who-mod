package net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.common.tardis.consoleunits.TardisConsoleUnitTypes;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class TardisConsoleUnitToyotaBlockEntity extends BaseTardisConsoleUnitBlockEntity {
    public TardisConsoleUnitToyotaBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_CONSOLE_UNIT_TOYOTA.getBlockEntityType(), TardisConsoleUnitTypes.TOYOTA, blockPos, blockState);
    }
}

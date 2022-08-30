package net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.common.tardis.consoleunits.TardisConsoleUnitTypes;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class TardisConsoleUnitImperialBlockEntity extends BaseTardisConsoleUnitBlockEntity {
    public TardisConsoleUnitImperialBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_CONSOLE_UNIT_IMPERIAL, TardisConsoleUnitTypes.IMPERIAL, blockPos, blockState);
    }
}

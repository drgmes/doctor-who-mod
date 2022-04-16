package net.drgmes.dwm.data.client;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, DWM.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            blockBuilder.registerBlockStateAndModel(this);
        }
    }
}

package net.drgmes.dwm.data.client;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.drgmes.dwm.utils.builders.item.ItemBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, DWM.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            blockBuilder.registerItemModel(this);
        }

        for (ItemBuilder itemBuilder : ModItems.ITEM_BUILDERS) {
            itemBuilder.registerItemModel(this);
        }
    }
}

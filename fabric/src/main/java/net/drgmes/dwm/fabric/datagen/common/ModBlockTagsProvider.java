package net.drgmes.dwm.fabric.datagen.common;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            for (TagKey<Block> tag : blockBuilder.tags) {
                this.getOrCreateTagBuilder(tag).add(DWM.getIdentifier(blockBuilder.getName()));
            }
        }
    }
}

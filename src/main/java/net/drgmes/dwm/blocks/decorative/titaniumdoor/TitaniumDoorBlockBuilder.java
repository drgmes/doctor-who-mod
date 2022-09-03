package net.drgmes.dwm.blocks.decorative.titaniumdoor;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModLootTableProvider;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.BlockStateHelper;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.minecraft.block.AbstractBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumDoorBlockBuilder extends BlockBuilder {
    public TitaniumDoorBlockBuilder(String name) {
        super(name, new TitaniumDoorBlock(getBlockSettings()), ModCreativeTabs.DECORATIONS);
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return BlockBuilder.getBlockSettings().strength(3.0F).nonOpaque();
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + this.getName());
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void registerCustomRender() {
        BlockRenderLayerMapImpl.INSTANCE.putBlock(this.getBlock(), RenderLayer.getTranslucent());
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateHelper.createDoorBlockStateAndModel(blockStateModelGenerator, this, this.getTexture(), ModBlocks.TITANIUM_PANEL.getTexture());
        ModelHelper.createGeneratedItemModel(blockStateModelGenerator, DWM.getIdentifier("item/decorative/" + this.getName()), DWM.getIdentifier("item/" + this.getName()));
    }

    @Override
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getBlock(), 3)
            .input('#', ModItems.TITANIUM_INGOT.getItem())
            .pattern("##")
            .pattern("##")
            .pattern("##")
            .criterion("has_item", RecipeProvider.conditionsFromItem(ModItems.TITANIUM_INGOT.getItem()))
            .offerTo(exporter);
    }

    @Override
    public void registerDrop(ModLootTableProvider modLootTableProvider) {
        modLootTableProvider.addDrop(this.getBlock(), BlockLootTableGenerator.doorDrops(this.getBlock()));
    }

    @Override
    public void registerTags() {
        super.registerTags();
        this.tags.add(BlockTags.IMPERMEABLE);
        this.tags.add(BlockTags.DOORS);
    }
}

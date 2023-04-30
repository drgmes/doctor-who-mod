package net.drgmes.dwm.blocks.decorative.titaniumdoor;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockLootDataBuilder;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumDoorBlockBuilder extends BlockBuilder {
    public TitaniumDoorBlockBuilder(String name) {
        super(name, () -> new TitaniumDoorBlock(getBlockSettings()), ModCreativeTabs.DECORATIONS);
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
    public RenderLayer getRenderLayer() {
        return RenderLayer.getTranslucent();
    }

    @Override
    public BlockLootDataBuilder getBlockLootDataBuilder() {
        return new BlockLootDataBuilder(this, BlockLootDataBuilder.LootType.DOOR);
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.DOOR, BlockModelDataBuilder.ItemType.GENERATED)
            .addBlockTexture(TextureKey.TOP, this.getTexture())
            .addBlockTexture(TextureKey.BOTTOM, ModBlocks.TITANIUM_PANEL.getTexture())
            .addItemTexture(TextureKey.ALL, DWM.getIdentifier("item/decorative/" + this.getName()))
            .addItemTexture(TextureKey.CONTENT, DWM.getIdentifier("item/" + this.getName()));
    }

    @Override
    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock(), 3)
            .input('#', ModItems.TITANIUM_INGOT.getItem())
            .pattern("##")
            .pattern("##")
            .pattern("##")
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModItems.TITANIUM_INGOT.getItem()))
            .offerTo(exporter);
    }

    @Override
    public void registerTags() {
        super.registerTags();
        this.tags.add(BlockTags.IMPERMEABLE);
        this.tags.add(BlockTags.DOORS);
    }
}

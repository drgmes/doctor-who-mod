package net.drgmes.dwm.blocks.decorative.titaniumglass;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.GlassBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumGlassBlockBuilder extends BlockBuilder {
    public TitaniumGlassBlockBuilder(String name) {
        super(name, () -> new GlassBlock(getBlockSettings()));
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return AbstractBlock.Settings.copy(Blocks.GLASS).strength(1.0F);
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
    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock())
            .input('#', ModItems.TITANIUM_PLATE.getItem())
            .input('0', Blocks.GLASS)
            .pattern(" # ")
            .pattern("#0#")
            .pattern(" # ")
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModItems.TITANIUM_PLATE.getItem()))
            .offerTo(exporter);
    }

    @Override
    public void registerTags() {
        super.registerTags();
        this.tags.add(BlockTags.IMPERMEABLE);
    }
}

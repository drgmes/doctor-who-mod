package net.drgmes.dwm.blocks.tardis.misc.tardisroundel;

import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.models.TardisRoundelModel;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.BlockStateHelper;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.tag.BlockTags;

public class TardisRoundelBlockBuilder extends BlockBuilder {
    public TardisRoundelBlockBuilder(String name) {
        super(name, new TardisRoundelBlock(getBlockSettings()));
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return BlockBuilder.getBlockSettings().nonOpaque().luminance((blockState) -> (
            blockState.get(TardisRoundelBlock.LIT) ? 16 : 0
        ));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void registerCustomRender() {
        EntityModelLayerRegistry.registerModelLayer(TardisRoundelModel.LAYER_LOCATION_DARK, TardisRoundelModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(TardisRoundelModel.LAYER_LOCATION_LIGHT, TardisRoundelModel::getTexturedModelData);
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_ROUNDEL, TardisRoundelBlockRenderer::new);
        BuiltinItemRendererRegistry.INSTANCE.register(this.getBlockItem(), new TardisRoundelItemRenderer());
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateHelper.createSimpleBlockStateWithModel(blockStateModelGenerator, this, ModelHelper.BLOCK_INVISIBLE);
    }

    @Override
    public void registerItemModel(ItemModelGenerator itemModelGenerator) {
        ModelHelper.createItemModel(itemModelGenerator, this.getBlockItem(), "item/block/tardis/misc/" + this.getName());
    }

    @Override
    public void registerTags() {
        this.tags.add(BlockTags.AXE_MINEABLE);
    }
}

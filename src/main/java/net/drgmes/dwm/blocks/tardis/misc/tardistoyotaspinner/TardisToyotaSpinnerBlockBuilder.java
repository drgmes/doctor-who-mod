package net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner;

import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.models.TardisToyotaSpinnerModel;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

public class TardisToyotaSpinnerBlockBuilder extends BlockBuilder {
    public TardisToyotaSpinnerBlockBuilder(String name) {
        super(name, new TardisToyotaSpinnerBlock(getBlockSettings()));
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return BlockBuilder.getBlockSettings().nonOpaque();
    }

    @Override
    public void registerCustomRender() {
        EntityModelLayerRegistry.registerModelLayer(TardisToyotaSpinnerModel.LAYER_LOCATION, TardisToyotaSpinnerModel::getTexturedModelData);
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_TOYOTA_SPINNER, TardisToyotaSpinnerBlockRenderer::new);
        BuiltinItemRendererRegistry.INSTANCE.register(this.getBlockItem(), new TardisToyotaSpinnerItemRenderer());
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        ModelHelper.createBlockStateWithModel(blockStateModelGenerator, this.getBlock(), ModelHelper.BLOCK_INVISIBLE);
    }

    @Override
    public void registerItemModel(ItemModelGenerator itemModelGenerator) {
        ModelHelper.createItemModel(itemModelGenerator, this.getBlockItem(), "item/tardis/misc/" + this.getName());
    }
}

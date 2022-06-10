package net.drgmes.dwm.blocks.tardis.others.tardistoyotaspinner;

import net.drgmes.dwm.blocks.tardis.others.tardistoyotaspinner.models.TardisToyotaSpinnerModel;
import net.drgmes.dwm.data.client.ModBlockStateProvider;
import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public class TardisToyotaSpinnerBlockBuilder extends BlockBuilder {
    public TardisToyotaSpinnerBlockBuilder(String name) {
        super(name, () -> new TardisToyotaSpinnerBlock(getBlockBehaviour()));
    }

    public static BlockBehaviour.Properties getBlockBehaviour() {
        return BlockBuilder.getBlockBehaviour().noOcclusion();
    }

    @Override
    public void registerBlockStateAndModel(ModBlockStateProvider provider) {
        ModelFile entityModel = provider.models().getExistingFile(provider.modLoc("block/invisible"));
        provider.simpleBlock(this.get(), entityModel);
    }

    @Override
    public void registerItemModel(ModItemModelProvider provider) {
        ItemModelBuilder builder = provider.getBuilder(this.name);
        ModelHelper.applyExternalOBJModel(builder, "item/tardis/others/toyotaspinner/" + this.getResourceName(), true);
        ModelHelper.rotateToBlockStyle(builder, 0.4F);

        builder.transforms().transform(TransformType.GUI).translation(-3F, -2.5F, 0);
    }

    @Override
    public void registerCustomRender() {
        ItemBlockRenderTypes.setRenderLayer(this.get(), (layer) -> layer == RenderType.translucent());
    }

    @Override
    public void registerCustomEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TARDIS_TOYOTA_SPINNER.get(), TardisToyotaSpinnerBlockRenderer::new);
    }

    @Override
    public void registerCustomLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TardisToyotaSpinnerModel.LAYER_LOCATION, TardisToyotaSpinnerModel::createBodyLayer);
    }
}

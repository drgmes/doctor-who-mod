package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox;

import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.models.TardisExteriorPoliceBoxModel;
import net.drgmes.dwm.data.client.ModBlockStateProvider;
import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public class TardisExteriorPoliceBoxBlockBuilder extends BlockBuilder {
    public TardisExteriorPoliceBoxBlockBuilder(String name) {
        super(name, () -> new TardisExteriorPoliceBoxBlock(getBlockBehaviour()));
        this.disableNativeDrop();
    }

    public static BlockBehaviour.Properties getBlockBehaviour() {
        return BlockBehaviour.Properties.copy(Blocks.BEDROCK).noOcclusion().lightLevel((blockState) -> {
            if (blockState.getValue(TardisExteriorPoliceBoxBlock.HALF) == DoubleBlockHalf.UPPER) {
                return blockState.getValue(TardisExteriorPoliceBoxBlock.LIT) ? 16 : 0;
            }

            return 0;
        });
    }

    @Override
    public void registerBlockStateAndModel(ModBlockStateProvider provider) {
        ModelFile entityModel = provider.models().getExistingFile(provider.modLoc("block/invisible"));
        provider.simpleBlock(this.get(), entityModel);
    }

    @Override
    public void registerItemModel(ModItemModelProvider provider) {
        ItemModelBuilder builder = provider.getBuilder(this.name);
        ModelHelper.applyExternalOBJModel(builder, "item/tardis/exteriors/" + this.getResourceName(), true);
        ModelHelper.rotateToBlockStyle(builder, 0.7F);

        builder.transforms().transform(ModelBuilder.Perspective.GUI).translation(-5F, -2.25F, 0);
    }

    @Override
    public void registerCustomRender() {
        ItemBlockRenderTypes.setRenderLayer(this.get(), (layer) -> layer == RenderType.translucent());
    }

    @Override
    public void registerCustomEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TARDIS_EXTERIOR.get(), TardisExteriorPoliceBoxBlockRenderer::new);
    }

    @Override
    public void registerCustomLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TardisExteriorPoliceBoxModel.LAYER_LOCATION, TardisExteriorPoliceBoxModel::createBodyLayer);
    }
}

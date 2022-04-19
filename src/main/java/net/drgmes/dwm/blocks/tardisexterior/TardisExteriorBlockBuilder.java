package net.drgmes.dwm.blocks.tardisexterior;

import net.drgmes.dwm.data.client.ModBlockStateProvider;
import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.model.generators.ModelFile;

public class TardisExteriorBlockBuilder extends BlockBuilder {
    public TardisExteriorBlockBuilder(String name) {
        super(name, () -> new TardisExteriorBlock(getBlockBehaviour()));
        this.disableNativeDrop();
    }

    public static BlockBehaviour.Properties getBlockBehaviour() {
        return BlockBehaviour.Properties.copy(Blocks.BEDROCK).noOcclusion();
    }

    @Override
    public void registerBlockStateAndModel(ModBlockStateProvider provider) {
        ModelFile entityModel = provider.models().getExistingFile(provider.modLoc("block/invisible"));
        provider.simpleBlock(this.get(), entityModel);
    }

    @Override
    public void registerItemModel(ModItemModelProvider provider) {
        // super.registerItemModel(provider);
        // ModelHelper.rotateToBlockStyle(provider.getBuilder(this.name));
    }

    @Override
    public void registerCustomRender() {
        ItemBlockRenderTypes.setRenderLayer(this.get(), (layer) -> layer == RenderType.translucent());
    }

    @Override
    public void registerCustomRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TARDIS_EXTERIOR.get(), TardisExteriorBlockRenderer::new);
    }
}

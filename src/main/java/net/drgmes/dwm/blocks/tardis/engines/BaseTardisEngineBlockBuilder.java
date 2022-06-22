package net.drgmes.dwm.blocks.tardis.engines;

import net.drgmes.dwm.data.client.ModBlockStateProvider;
import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.Supplier;

public abstract class BaseTardisEngineBlockBuilder extends BlockBuilder {
    public BaseTardisEngineBlockBuilder(String name, Supplier<? extends BaseTardisEngineBlock> factory) {
        super(name, factory);
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
        ItemModelBuilder builder = provider.getBuilder(this.name);
        ModelHelper.applyExternalOBJModel(builder, "item/tardis/engines/" + this.getResourceName(), true);
        ModelHelper.rotateToBlockStyle(builder, 0.15F);

        builder.transforms().transform(TransformType.GUI).translation(-1.25F, -3.5F, 0);
    }

    @Override
    public void registerCustomRender() {
        ItemBlockRenderTypes.setRenderLayer(this.get(), (layer) -> layer == RenderType.translucent());
    }
}

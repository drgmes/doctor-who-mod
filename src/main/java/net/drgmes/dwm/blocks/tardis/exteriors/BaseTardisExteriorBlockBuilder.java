package net.drgmes.dwm.blocks.tardis.exteriors;

import java.util.function.Supplier;

import net.drgmes.dwm.data.client.ModBlockStateProvider;
import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public abstract class BaseTardisExteriorBlockBuilder extends BlockBuilder {
    public BaseTardisExteriorBlockBuilder(String name, Supplier<? extends BaseTardisExteriorBlock<?>> factory) {
        super(name, factory);
        this.disableNativeDrop();
    }

    public static BlockBehaviour.Properties getBlockBehaviour() {
        return BlockBehaviour.Properties.copy(Blocks.BEDROCK).noOcclusion().lightLevel((blockState) -> {
            if (blockState.getValue(BaseTardisExteriorBlock.HALF) == DoubleBlockHalf.UPPER) {
                return blockState.getValue(BaseTardisExteriorBlock.LIT) ? 16 : 0;
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
}

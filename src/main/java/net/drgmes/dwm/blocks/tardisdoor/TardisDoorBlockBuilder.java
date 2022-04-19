package net.drgmes.dwm.blocks.tardisdoor;

import net.drgmes.dwm.data.client.ModBlockStateProvider;
import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.drgmes.dwm.data.common.ModLootTableProvider;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

public class TardisDoorBlockBuilder extends BlockBuilder {
    public TardisDoorBlockBuilder(String name) {
        super(name, () -> new TardisDoorBlock(getBlockBehaviour()));
    }

    public static BlockBehaviour.Properties getBlockBehaviour() {
        return BlockBuilder.getBlockBehaviour().strength(3.0F).noOcclusion();
    }

    @Override
    public void registerBlockStateAndModel(ModBlockStateProvider provider) {
        ResourceLocation texture = provider.modLoc("block/" + this.getResourceName());
        ModelFile bottom = provider.models().doorBottomLeft(this.getResourceName() + "_bottom", texture, texture);
        ModelFile top = provider.models().doorTopLeft(this.getResourceName() + "_top", texture, texture);

        provider.getVariantBuilder(this.get()).forAllStatesExcept(state -> {
            int yRot = ((int) state.getValue(DoorBlock.FACING).toYRot()) + 90;
            return ConfiguredModel.builder().modelFile(state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? bottom : top).rotationY(yRot % 360).build();
        }, TardisDoorBlock.WATERLOGGED, TardisDoorBlock.OPEN);
    }

    @Override
    public void registerItemModel(ModItemModelProvider provider) {
        ModelFile itemGenerated = provider.getExistingFile(provider.mcLoc("item/generated"));
        provider.getBuilder(this.name).parent(itemGenerated).texture("layer0", "item/" + this.name);
    }

    @Override
    public void registerLootTable(ModLootTableProvider.ModBlockLootTable provider) {
        provider.addDoorDrop(this.get());
    }

    @Override
    public void registerCustomRender() {
        ItemBlockRenderTypes.setRenderLayer(this.get(), (layer) -> layer == RenderType.translucent());
    }

    @Override
    public void addTags() {
        super.addTags();
        this.tags.add(BlockTags.DOORS);
        this.tags.add(BlockTags.IMPERMEABLE);
    }
}

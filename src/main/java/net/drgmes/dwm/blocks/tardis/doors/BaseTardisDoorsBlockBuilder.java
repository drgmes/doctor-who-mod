package net.drgmes.dwm.blocks.tardis.doors;

import java.util.function.Supplier;

import net.drgmes.dwm.data.client.ModBlockStateProvider;
import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.drgmes.dwm.data.common.ModLootTableProvider;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public abstract class BaseTardisDoorsBlockBuilder extends BlockBuilder {
    public BaseTardisDoorsBlockBuilder(String name, Supplier<? extends BaseTardisDoorsBlock> factory) {
        super(name, factory);
    }

    public static BlockBehaviour.Properties getBlockBehaviour() {
        return BlockBuilder.getBlockBehaviour().strength(3.0F).noOcclusion();
    }

    @Override
    public void registerBlockStateAndModel(ModBlockStateProvider provider) {
        ModelFile entityModel = provider.models().getExistingFile(provider.modLoc("block/invisible"));
        provider.simpleBlock(this.get(), entityModel);
    }

    @Override
    public void registerItemModel(ModItemModelProvider provider) {
        ItemModelBuilder builder = provider.getBuilder(this.name);
        ModelHelper.applyExternalOBJModel(builder, "item/tardis/doors/" + this.getResourceName(), true);
        ModelHelper.rotateToBlockStyle(builder, 0.8F);
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

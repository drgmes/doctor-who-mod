package net.drgmes.dwm.blocks.tardis.others.tardisroomdestroyer;

import net.drgmes.dwm.data.client.ModBlockStateProvider;
import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class TardisRoomDestroyerBlockBuilder extends BlockBuilder {
    public TardisRoomDestroyerBlockBuilder(String name) {
        super(name, () -> new TardisRoomDestroyerBlock(getBlockBehaviour()));
    }

    public static BlockBehaviour.Properties getBlockBehaviour() {
        return BlockBehaviour.Properties.copy(Blocks.BEDROCK);
    }

    public void registerBlockStateAndModel(ModBlockStateProvider provider) {
        provider.simpleBlock(this.get(), provider.models().getExistingFile(provider.modLoc("block/" + this.getResourceName())));
    }

    public void registerItemModel(ModItemModelProvider provider) {
        provider.getBuilder(this.name).parent(provider.getExistingFile(provider.modLoc("block/" + this.getResourceName())));
    }
}

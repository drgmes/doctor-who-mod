package net.drgmes.dwm.blocks.tardis.others.tardisroomcreator;

import net.drgmes.dwm.data.client.ModBlockStateProvider;
import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class TardisRoomCreatorBlockBuilder extends BlockBuilder {
    public TardisRoomCreatorBlockBuilder(String name) {
        super(name, () -> new TardisRoomCreatorBlock(getBlockBehaviour()));
    }

    public static BlockBehaviour.Properties getBlockBehaviour() {
        return BlockBuilder.getBlockBehaviour();
    }

    public void registerBlockStateAndModel(ModBlockStateProvider provider) {
        provider.simpleBlock(this.get(), provider.models().getExistingFile(provider.modLoc("block/" + this.getResourceName())));
    }

    public void registerItemModel(ModItemModelProvider provider) {
        provider.getBuilder(this.name).parent(provider.getExistingFile(provider.modLoc("block/" + this.getResourceName())));
    }
}

package net.drgmes.dwm.blocks.tardisexterior;

import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class TardisExteriorBlockBuilder extends BlockBuilder {
    public TardisExteriorBlockBuilder(String name) {
        super(name, () -> new TardisExteriorBlock(getBlockBehaviour()));
        this.disableNativeDrop();
    }

    public static BlockBehaviour.Properties getBlockBehaviour() {
        return BlockBehaviour.Properties.copy(Blocks.BEDROCK);
    }

    @Override
    public void registerCustomRender() {
        ItemBlockRenderTypes.setRenderLayer((Block) this.blockObject.get(), (layer) -> layer == RenderType.cutout());
    }
}

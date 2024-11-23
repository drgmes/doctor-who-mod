package net.drgmes.dwm.blocks.tardis.engines;

import net.drgmes.dwm.utils.helpers.WorldHelper;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.util.math.Box;

public abstract class BaseTardisEngineBlockRenderer<C extends BaseTardisEngineBlockEntity> implements BlockEntityRenderer<C> {
    protected final BlockEntityRendererFactory.Context ctx;

    public BaseTardisEngineBlockRenderer(BlockEntityRendererFactory.Context context) {
        this.ctx = context;
    }

    // @Override
    public Box getRenderBoundingBox(C blockEntity) {
        return WorldHelper.getRenderBoundingBox(blockEntity);
    }
}

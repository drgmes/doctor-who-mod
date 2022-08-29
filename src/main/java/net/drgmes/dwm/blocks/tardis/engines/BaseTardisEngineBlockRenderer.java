package net.drgmes.dwm.blocks.tardis.engines;

import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public abstract class BaseTardisEngineBlockRenderer<C extends BaseTardisEngineBlockEntity> implements BlockEntityRenderer<C> {
    protected final BlockEntityRendererFactory.Context ctx;

    public BaseTardisEngineBlockRenderer(BlockEntityRendererFactory.Context context) {
        this.ctx = context;
    }
}

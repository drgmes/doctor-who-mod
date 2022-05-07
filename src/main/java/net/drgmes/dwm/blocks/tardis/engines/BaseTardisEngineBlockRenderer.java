package net.drgmes.dwm.blocks.tardis.engines;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class BaseTardisEngineBlockRenderer<C extends BaseTardisEngineBlockEntity> implements BlockEntityRenderer<C> {
    protected BlockEntityRendererProvider.Context ctx;

    public BaseTardisEngineBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.ctx = context;
    }
}

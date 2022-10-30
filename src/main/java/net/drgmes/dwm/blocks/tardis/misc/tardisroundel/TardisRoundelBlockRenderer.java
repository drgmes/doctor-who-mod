package net.drgmes.dwm.blocks.tardis.misc.tardisroundel;

import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.models.TardisRoundelModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class TardisRoundelBlockRenderer implements BlockEntityRenderer<TardisRoundelBlockEntity> {
    protected final BlockEntityRendererFactory.Context ctx;

    public TardisRoundelBlockRenderer(BlockEntityRendererFactory.Context context) {
        this.ctx = context;
    }

    @Override
    public void render(TardisRoundelBlockEntity tile, float delta, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        boolean isLit = tile.getCachedState().get(TardisRoundelBlock.LIT);
        EntityModelLayer modelLayer = tile.lightMode ? TardisRoundelModel.LAYER_LOCATION_LIGHT : TardisRoundelModel.LAYER_LOCATION_DARK;
        TardisRoundelModel model = new TardisRoundelModel(this.ctx.getLayerRenderDispatcher().getModelPart(modelLayer));
        VertexConsumer vertexConsumer = buffer.getBuffer(isLit ? RenderLayer.getEntityAlpha(modelLayer.getId()) : model.getLayer(modelLayer.getId()));
        model.setupAnim(tile);

        matrixStack.push();
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(180));
        matrixStack.translate(0, -1, 0);
        model.render(matrixStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();

        if (tile.blockTemplate != null && !Objects.equals(tile.blockTemplate.getPath(), "") && tile.getWorld() != null) {
            BlockState blockState = Registry.BLOCK.get(tile.blockTemplate).getDefaultState();
            if (blockState.contains(Properties.AXIS)) blockState = blockState.with(Properties.AXIS, tile.getCachedState().get(TardisRoundelBlock.AXIS));
            if (blockState.contains(Properties.FACING)) blockState = blockState.with(Properties.FACING, tile.getCachedState().get(TardisRoundelBlock.FACING));

            matrixStack.push();
            ctx.getRenderManager().renderBlock(blockState, tile.getPos(), tile.getWorld(), matrixStack, buffer.getBuffer(RenderLayer.getTranslucent()), true, tile.getWorld().random);
            matrixStack.pop();
        }
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }
}

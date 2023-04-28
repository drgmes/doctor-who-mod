package net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota;

import net.drgmes.dwm.blocks.tardis.engines.BaseTardisEngineBlockRenderer;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.models.TardisEngineToyotaModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class TardisEngineToyotaBlockRenderer extends BaseTardisEngineBlockRenderer<TardisEngineToyotaBlockEntity> {
    public TardisEngineToyotaBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(TardisEngineToyotaBlockEntity tile, float delta, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        ModelPart modelRoot = this.ctx.getLayerModelPart(TardisEngineToyotaModel.LAYER_LOCATION);
        TardisEngineToyotaModel model = new TardisEngineToyotaModel(modelRoot);
        VertexConsumer vertexConsumer = buffer.getBuffer(model.getLayer(TardisEngineToyotaModel.LAYER_LOCATION.getId()));
        model.setupAnim(tile);

        float rotateDegrees = tile.getCachedState().get(TardisEngineToyotaBlock.FACING).asRotation();
        float scale = 0.4F;

        matrixStack.push();
        matrixStack.translate(0.5, 0, 0.5);
        matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotateDegrees));
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0, -1.5F, 0);
        model.render(matrixStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
    }
}

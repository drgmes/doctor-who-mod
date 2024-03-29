package net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner;

import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.models.TardisToyotaSpinnerModel;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class TardisToyotaSpinnerBlockRenderer implements BlockEntityRenderer<TardisToyotaSpinnerBlockEntity> {
    protected final BlockEntityRendererFactory.Context ctx;

    public TardisToyotaSpinnerBlockRenderer(BlockEntityRendererFactory.Context context) {
        this.ctx = context;
    }

    @Override
    public void render(TardisToyotaSpinnerBlockEntity tile, float delta, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        TardisToyotaSpinnerModel model = new TardisToyotaSpinnerModel(this.ctx.getLayerModelPart(TardisToyotaSpinnerModel.LAYER_LOCATION));
        VertexConsumer vertexConsumer = buffer.getBuffer(model.getLayer(TardisToyotaSpinnerModel.LAYER_LOCATION.getId()));
        model.setupAnim(tile);

        float rotateDegrees = tile.getCachedState().get(TardisToyotaSpinnerBlock.FACING).asRotation();
        float scale = 1.5F;

        matrixStack.push();
        matrixStack.translate(0.5, 0, 0.5);
        matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotateDegrees));
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0, -1.075F, 0);
        model.render(matrixStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }
}

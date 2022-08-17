package net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner;

import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.models.TardisToyotaSpinnerModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class TardisToyotaSpinnerBlockRenderer implements BlockEntityRenderer<TardisToyotaSpinnerBlockEntity> {
    protected final BlockEntityRendererFactory.Context ctx;

    public TardisToyotaSpinnerBlockRenderer(BlockEntityRendererFactory.Context context) {
        this.ctx = context;
    }

    @Override
    public void render(TardisToyotaSpinnerBlockEntity tile, float delta, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        float rotateDegrees = tile.getCachedState().get(TardisToyotaSpinnerBlock.FACING).asRotation();
        EntityModelLayer modelLayer = TardisToyotaSpinnerModel.LAYER_LOCATION;
        TardisToyotaSpinnerModel model = new TardisToyotaSpinnerModel(this.ctx.getLayerRenderDispatcher().getModelPart(modelLayer));
        VertexConsumer vertexConsumer = buffer.getBuffer(model.getLayer(modelLayer.getId()));
        model.setupAnim(tile);

        float scale = 1.5F;
        matrixStack.push();
        matrixStack.translate(0.5, 0, 0.5);
        matrixStack.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(180));
        matrixStack.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(180));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotateDegrees));
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

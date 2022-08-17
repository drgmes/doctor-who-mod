package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox;

import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.models.TardisExteriorPoliceBoxModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class TardisExteriorPoliceBoxBlockRenderer implements BlockEntityRenderer<TardisExteriorPoliceBoxBlockEntity> {
    protected final BlockEntityRendererFactory.Context ctx;

    public TardisExteriorPoliceBoxBlockRenderer(BlockEntityRendererFactory.Context context) {
        this.ctx = context;
    }

    @Override
    public void render(TardisExteriorPoliceBoxBlockEntity tile, float delta, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        DoubleBlockHalf half = tile.getCachedState().get(TardisExteriorPoliceBoxBlock.HALF);
        if (half != DoubleBlockHalf.LOWER) return;

        EntityModelLayer modelLayer = TardisExteriorPoliceBoxModel.LAYER_LOCATION;
        TardisExteriorPoliceBoxModel model = new TardisExteriorPoliceBoxModel(this.ctx.getLayerRenderDispatcher().getModelPart(modelLayer));
        VertexConsumer vertexConsumer = buffer.getBuffer(model.getLayer(modelLayer.getId()));
        model.setupAnim(tile);

        float speed = 0.3F;
        float intense = 0.4F;
        float percent = tile.getMaterializedPercent();
        float alpha = (float) Math.cos(percent * speed) * intense + (1.0F / 100) * percent;
        float alphaClamped = percent < 10 ? 0 : Math.max(0, Math.min(1.0F, alpha));

        float rotateDegrees = tile.getCachedState().get(TardisExteriorPoliceBoxBlock.FACING).asRotation();
        float scale = 1.2F;
        matrixStack.push();
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(180));
        matrixStack.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(180));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotateDegrees));
        matrixStack.translate(0, -1.265 * scale, 0);
        matrixStack.scale(scale, scale + 0.15F, scale);
        model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, alphaClamped);
        matrixStack.pop();
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }
}

package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox;

import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.models.TardisDoorsPoliceBoxModel;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.TardisExteriorPoliceBoxBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

public class TardisDoorsPoliceBoxBlockRenderer implements BlockEntityRenderer<TardisDoorsPoliceBoxBlockEntity> {
    protected final BlockEntityRendererFactory.Context ctx;

    public TardisDoorsPoliceBoxBlockRenderer(BlockEntityRendererFactory.Context context) {
        this.ctx = context;
    }

    @Override
    public void render(TardisDoorsPoliceBoxBlockEntity tile, float delta, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        Direction face = tile.getCachedState().get(TardisExteriorPoliceBoxBlock.FACING);
        DoubleBlockHalf half = tile.getCachedState().get(TardisExteriorPoliceBoxBlock.HALF);
        if (half != DoubleBlockHalf.LOWER) return;

        EntityModelLayer modelLayer = TardisDoorsPoliceBoxModel.LAYER_LOCATION;
        TardisDoorsPoliceBoxModel model = new TardisDoorsPoliceBoxModel(this.ctx.getLayerRenderDispatcher().getModelPart(modelLayer));
        VertexConsumer vertexConsumer = buffer.getBuffer(model.getLayer(modelLayer.getId()));
        model.setupAnim(tile);

        matrixStack.push();
        this.setupModelView(matrixStack, face);
        model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
        model.renderDoors(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
        matrixStack.pop();
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

    public void setupModelView(MatrixStack matrixStack, Direction face) {
        float scale = 1.4F;
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(180));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(face.asRotation()));
        matrixStack.translate(0, -1.1 * scale, -0.325F);
        matrixStack.scale(scale, scale, scale);
    }
}

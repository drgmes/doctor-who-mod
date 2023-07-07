package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.models.TardisDoorsPoliceBoxModel;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class TardisDoorsPoliceBoxBlockRenderer implements BlockEntityRenderer<TardisDoorsPoliceBoxBlockEntity> {
    protected final BlockEntityRendererFactory.Context ctx;

    public TardisDoorsPoliceBoxBlockRenderer(BlockEntityRendererFactory.Context context) {
        this.ctx = context;
    }

    @Override
    public void render(TardisDoorsPoliceBoxBlockEntity tile, float delta, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        Direction face = tile.getCachedState().get(BaseTardisDoorsBlock.FACING);
        DoubleBlockHalf half = tile.getCachedState().get(BaseTardisDoorsBlock.HALF);
        if (half != DoubleBlockHalf.LOWER) return;

        TardisDoorsPoliceBoxModel model = new TardisDoorsPoliceBoxModel(this.ctx.getLayerModelPart(TardisDoorsPoliceBoxModel.LAYER_LOCATION));
        RenderLayer modelLayer = model.getLayer(TardisDoorsPoliceBoxModel.LAYER_LOCATION.getId());
        model.setupAnim(tile);

        matrixStack.push();
        this.setupModelView(matrixStack, face);
        model.render(matrixStack, buffer.getBuffer(modelLayer), light, overlay, 1, 1, 1, 1);
        this.drawForeground(tile, matrixStack, buffer);
        model.renderDoors(matrixStack, buffer.getBuffer(modelLayer), light, overlay, 1, 1, 1, 1);
        matrixStack.pop();
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

    public void setupModelView(MatrixStack matrixStack, Direction face) {
        float scale = 1.4F;
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(face.asRotation()));
        matrixStack.translate(0, -1.1 * scale, -0.325F);
        matrixStack.scale(scale, scale, scale);
    }

    private void drawForeground(TardisDoorsPoliceBoxBlockEntity tile, MatrixStack matrixStack, VertexConsumerProvider buffer) {
        if (ModCompats.immersivePortals()) return;

        matrixStack.push();
        matrixStack.scale(1, 0.85F, 1);
        matrixStack.translate(-0.5, 0, -0.125);
        RenderHelper.drawRectangle(matrixStack, buffer.getBuffer(RenderLayer.getGui()), 0, 0, 1, 2, 0XFF01102A);
        ((VertexConsumerProvider.Immediate) buffer).draw(RenderLayer.getGui());
        matrixStack.pop();

        if (tile.getCachedState().get(TardisDoorsPoliceBoxBlock.OPEN)) {
            matrixStack.push();
            matrixStack.scale(1, 0.85F, 1);
            matrixStack.translate(0.5, 0, -0.05);
            matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180));
            RenderHelper.drawRectangle(matrixStack, buffer.getBuffer(RenderLayer.getEndPortal()), 0, 0, 1, 2, 0xFF000000);
            ((VertexConsumerProvider.Immediate) buffer).draw(RenderLayer.getEndPortal());
            matrixStack.pop();
        }
    }
}

package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.models.TardisDoorsPoliceBoxModel;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class TardisDoorsPoliceBoxBlockRenderer implements BlockEntityRenderer<TardisDoorsPoliceBoxBlockEntity> {
    protected final BlockEntityRendererFactory.Context ctx;

    public TardisDoorsPoliceBoxBlockRenderer(BlockEntityRendererFactory.Context context) {
        this.ctx = context;
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

    @Override
    public void render(TardisDoorsPoliceBoxBlockEntity tile, float delta, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        DoubleBlockHalf half = tile.getCachedState().get(BaseTardisDoorsBlock.HALF);
        if (half != DoubleBlockHalf.LOWER) return;

        float rotateDegrees = tile.getCachedState().get(BaseTardisDoorsBlock.FACING).asRotation();
        TardisDoorsPoliceBoxModel model = new TardisDoorsPoliceBoxModel(this.ctx.getLayerModelPart(TardisDoorsPoliceBoxModel.LAYER_LOCATION));
        RenderLayer modelLayer = model.getLayer(TardisDoorsPoliceBoxModel.LAYER_LOCATION.getId());
        model.setupAnim(tile);

        float scale = 0.465F;

        matrixStack.push();
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotateDegrees));
        matrixStack.translate(0, scale * -0.5F, -0.415F);
        matrixStack.scale(scale, scale + 0.025F, scale);

        VertexConsumer vertexConsumer = buffer.getBuffer(modelLayer);
        model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
        model.renderDoors(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
        this.drawForeground(tile, matrixStack, buffer);

        matrixStack.pop();
    }

    private void drawForeground(TardisDoorsPoliceBoxBlockEntity tile, MatrixStack matrixStack, VertexConsumerProvider buffer) {
        if (ModCompats.immersivePortals()) return;
        if (!tile.getCachedState().get(TardisDoorsPoliceBoxBlock.OPEN)) return;

        float scale = 2F;

        matrixStack.push();
        matrixStack.scale(scale, scale + 0.025F, scale);
        matrixStack.translate(-0.5F, -1.25F, -0.05F);

        RenderHelper.drawRectangle(matrixStack, buffer.getBuffer(RenderLayer.getGui()), 0, 0, 1, 2, 0XFF001D38);

        matrixStack.push();
        matrixStack.translate(1F, 0, 0.05F);
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180));
        RenderHelper.drawRectangle(matrixStack, buffer.getBuffer(RenderLayer.getEndPortal()), 0, 0, 1, 2, 0xFF000000);
        matrixStack.pop();

        matrixStack.pop();
    }
}

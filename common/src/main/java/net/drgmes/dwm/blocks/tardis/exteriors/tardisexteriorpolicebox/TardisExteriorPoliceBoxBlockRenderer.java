package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox;

import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.models.TardisExteriorPoliceBoxModel;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class TardisExteriorPoliceBoxBlockRenderer implements BlockEntityRenderer<TardisExteriorPoliceBoxBlockEntity> {
    protected final BlockEntityRendererFactory.Context ctx;

    public TardisExteriorPoliceBoxBlockRenderer(BlockEntityRendererFactory.Context context) {
        this.ctx = context;
    }

    @Override
    public void render(TardisExteriorPoliceBoxBlockEntity tile, float delta, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        DoubleBlockHalf half = tile.getCachedState().get(TardisExteriorPoliceBoxBlock.HALF);
        if (half != DoubleBlockHalf.LOWER) return;

        TardisExteriorPoliceBoxModel model = new TardisExteriorPoliceBoxModel(this.ctx.getLayerModelPart(TardisExteriorPoliceBoxModel.LAYER_LOCATION));
        RenderLayer modelLayer = model.getLayer(TardisExteriorPoliceBoxModel.LAYER_LOCATION.getId());
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
        matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotateDegrees));
        matrixStack.translate(0, -1.265 * scale, 0);
        matrixStack.scale(scale, scale + 0.15F, scale);
        model.render(matrixStack, buffer.getBuffer(modelLayer), light, overlay, 1, 1, 1, alphaClamped);
        this.drawForeground(tile, matrixStack, buffer);
        model.renderDoors(matrixStack, buffer.getBuffer(modelLayer), light, overlay, 1, 1, 1, alphaClamped);
        matrixStack.pop();
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

    private void drawForeground(TardisExteriorPoliceBoxBlockEntity tile, MatrixStack matrixStack, VertexConsumerProvider buffer) {
        if (ModCompats.immersivePortals()) return;
        if (!tile.getCachedState().get(TardisExteriorPoliceBoxBlock.OPEN)) return;

        matrixStack.push();
        matrixStack.scale(0.95F, 0.75F, 1);
        matrixStack.translate(-0.5, 0, -0.405);
        RenderHelper.drawRectangle(matrixStack, buffer.getBuffer(RenderLayer.getEndPortal()), 0, 0, 1, 2, 0xFF000000);
        matrixStack.pop();
    }
}

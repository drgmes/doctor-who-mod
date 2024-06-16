package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox;

import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.models.TardisExteriorPoliceBoxModel;
import net.drgmes.dwm.setup.ModCompats;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class TardisExteriorPoliceBoxBlockRenderer implements BlockEntityRenderer<TardisExteriorPoliceBoxBlockEntity> {
    protected final BlockEntityRendererFactory.Context ctx;

    public TardisExteriorPoliceBoxBlockRenderer(BlockEntityRendererFactory.Context context) {
        this.ctx = context;
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

    @Override
    public void render(TardisExteriorPoliceBoxBlockEntity tile, float delta, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        DoubleBlockHalf half = tile.getCachedState().get(TardisExteriorPoliceBoxBlock.HALF);
        if (half != DoubleBlockHalf.LOWER) return;

        boolean isLit = tile.getCachedState().get(TardisExteriorPoliceBoxBlock.LIT);
        boolean isOpen = tile.getCachedState().get(TardisExteriorPoliceBoxBlock.OPEN);
        float rotateDegrees = tile.getCachedState().get(TardisExteriorPoliceBoxBlock.FACING).asRotation();
        float materializedPercent = tile.getMaterializedPercent();

        TardisExteriorPoliceBoxModel model = new TardisExteriorPoliceBoxModel(this.ctx.getLayerModelPart(TardisExteriorPoliceBoxModel.LAYER_LOCATION));
        Identifier texture = TardisExteriorPoliceBoxModel.LAYER_LOCATION.getId();
        RenderLayer modelLayer = model.getLayer(texture);
        model.setupAnim(tile);

        float scale = 0.465F;
        float dematSpeed = 0.3F;
        float dematIntense = 0.4F;

        float alpha = (float) Math.cos(materializedPercent * dematSpeed) * dematIntense + (1.0F / 100) * materializedPercent;
        float alphaClamped = materializedPercent < 10 ? 0 : Math.max(0, Math.min(1.0F, alpha));

        matrixStack.push();
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotateDegrees));
        matrixStack.translate(0,  scale * -0.5, 0);
        matrixStack.scale(scale, scale + 0.025F, scale);

        VertexConsumer vertexConsumer = buffer.getBuffer(modelLayer);
        model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, alphaClamped);
        model.renderDoors(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, alphaClamped);
        model.renderLamp(matrixStack, isLit ? buffer.getBuffer(RenderLayer.getEntityAlpha(texture)) : vertexConsumer, light, overlay, 1, 1, 1, alphaClamped);
        if (isOpen && !ModCompats.immersivePortals()) model.renderBoti(matrixStack, buffer.getBuffer(RenderLayer.getEndPortal()), light, overlay, 1, 1, 1, alphaClamped);

        matrixStack.pop();
    }
}

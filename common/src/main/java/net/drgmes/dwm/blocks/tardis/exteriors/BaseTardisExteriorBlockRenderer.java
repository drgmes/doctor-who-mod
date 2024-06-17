package net.drgmes.dwm.blocks.tardis.exteriors;

import net.drgmes.dwm.setup.ModCompats;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.function.Function;

public abstract class BaseTardisExteriorBlockRenderer<C extends BaseTardisExteriorBlockEntity> implements BlockEntityRenderer<C> {
    protected final BlockEntityRendererFactory.Context ctx;
    protected final EntityModelLayer modelLayer;
    protected final Function<ModelPart, BaseTardisExteriorModel> modelFactory;

    protected final float modelScale;
    protected final float modelYScale;
    protected final float modelYOffset;

    private static final float DEMAT_SPEED = 0.3F;
    private static final float DEMAT_INTENSE = 0.4F;

    public BaseTardisExteriorBlockRenderer(BlockEntityRendererFactory.Context context, EntityModelLayer modelLayer, Function<ModelPart, BaseTardisExteriorModel> modelFactory, float modelScale, float modelYScale, float modelYOffset) {
        this.ctx = context;
        this.modelLayer = modelLayer;
        this.modelFactory = modelFactory;
        this.modelScale = modelScale;
        this.modelYScale = modelYScale;
        this.modelYOffset = modelYOffset;
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

    @Override
    public void render(C tile, float delta, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        DoubleBlockHalf half = tile.getCachedState().get(BaseTardisExteriorBlock.HALF);
        if (half != DoubleBlockHalf.LOWER) return;

        boolean hasImmersivePortals = ModCompats.immersivePortals();
        boolean isLit = tile.getCachedState().get(BaseTardisExteriorBlock.LIT);
        boolean isOpen = tile.getCachedState().get(BaseTardisExteriorBlock.OPEN);
        float rotateDegrees = tile.getCachedState().get(BaseTardisExteriorBlock.FACING).asRotation();
        float materializedPercent = tile.getMaterializedPercent();

        BaseTardisExteriorModel model = this.modelFactory.apply(this.ctx.getLayerModelPart(this.modelLayer));
        RenderLayer renderLayer = model.getLayer(this.modelLayer.getId());
        model.setupAnim(tile);

        float alpha = (float) Math.cos(materializedPercent * DEMAT_SPEED) * DEMAT_INTENSE + (1.0F / 100) * materializedPercent;
        float alphaClamped = materializedPercent < 10 ? 0 : Math.max(0, Math.min(1.0F, alpha));

        matrixStack.push();
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotateDegrees));
        matrixStack.translate(0, this.modelScale * this.modelYOffset, 0);
        matrixStack.scale(this.modelScale, this.modelScale + this.modelYScale, this.modelScale);

        VertexConsumer vertexConsumer = buffer.getBuffer(renderLayer);
        model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, alphaClamped);
        if (!hasImmersivePortals || !isOpen) model.renderDoors(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, alphaClamped);

        model.renderLamp(matrixStack, isLit ? buffer.getBuffer(RenderLayer.getEntityAlpha(this.modelLayer.getId())) : vertexConsumer, light, overlay, 1, 1, 1, alphaClamped);
        if (isOpen && !hasImmersivePortals) model.renderBoti(matrixStack, buffer.getBuffer(RenderLayer.getEndPortal()), light, overlay, 1, 1, 1, alphaClamped);

        matrixStack.pop();
    }
}

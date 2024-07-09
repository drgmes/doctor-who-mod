package net.drgmes.dwm.blocks.tardis.exteriors;

import net.drgmes.dwm.enums.TardisExteriorState;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

import java.util.function.Function;

public abstract class BaseTardisExteriorBlockRenderer<C extends BaseTardisExteriorBlockEntity> implements BlockEntityRenderer<C> {
    protected final BlockEntityRendererFactory.Context ctx;
    protected final EntityModelLayer modelLayer;
    protected final Function<ModelPart, BaseTardisExteriorModel> modelFactory;

    protected final float modelScale;
    protected final float modelYScale;
    protected final float modelYOffset;

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
        float alpha = this.getAlpha(tile);

        BaseTardisExteriorModel model = this.modelFactory.apply(this.ctx.getLayerModelPart(this.modelLayer));
        RenderLayer renderLayer = model.getLayer(this.modelLayer.getId());
        model.setupAnim(tile);

        matrixStack.push();
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotateDegrees));
        matrixStack.translate(0, this.modelScale * this.modelYOffset, 0);
        matrixStack.scale(this.modelScale, this.modelScale + this.modelYScale, this.modelScale);

        VertexConsumer vertexConsumer = buffer.getBuffer(renderLayer);
        model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, alpha);
        if (!hasImmersivePortals || !isOpen) model.renderDoors(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, alpha);

        model.renderLamp(matrixStack, isLit ? buffer.getBuffer(RenderLayer.getEntityAlpha(this.modelLayer.getId())) : vertexConsumer, light, overlay, 1, 1, 1, alpha);
        if (isOpen && !hasImmersivePortals) model.renderBoti(matrixStack, buffer.getBuffer(RenderLayer.getEndPortal()), light, overlay, 1, 1, 1, alpha);

        matrixStack.pop();
    }

    private float getAlpha(C tile) {
        TardisExteriorState exteriorState = tile.getExteriorState();
        float value = tile.getMaterializedStateValue();

        float alpha = switch (exteriorState) {
            case PROCESS_PULSE -> (float) Math.cos(value * 5) * 0.4F + 0.75F;
            default -> value < 0.1F ? 0 : (float) Math.cos(value * 30) * 0.4F + value;
        };

        return CommonHelper.clamp(alpha, 0, 1);
    }
}

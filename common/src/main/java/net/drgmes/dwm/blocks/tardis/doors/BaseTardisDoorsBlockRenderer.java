package net.drgmes.dwm.blocks.tardis.doors;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.utils.helpers.RenderHelper;
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

public abstract class BaseTardisDoorsBlockRenderer<C extends BaseTardisDoorsBlockEntity> implements BlockEntityRenderer<C> {
    protected final BlockEntityRendererFactory.Context ctx;
    protected final EntityModelLayer modelLayer;
    protected final Function<ModelPart, BaseTardisDoorsModel> modelFactory;

    protected final float modelScale;
    protected final float modelYScale;
    protected final float modelYOffset;
    protected final float modelZOffset;
    protected final float foregroundScale;
    protected final int color;

    public BaseTardisDoorsBlockRenderer(BlockEntityRendererFactory.Context context, EntityModelLayer modelLayer, Function<ModelPart, BaseTardisDoorsModel> modelFactory, float modelScale, float modelYScale, float modelYOffset, float modelZOffset, float foregroundScale, int color) {
        this.ctx = context;
        this.modelLayer = modelLayer;
        this.modelFactory = modelFactory;
        this.modelScale = modelScale;
        this.modelYScale = modelYScale;
        this.modelYOffset = modelYOffset;
        this.modelZOffset = modelZOffset;
        this.foregroundScale = foregroundScale;
        this.color = color;
    }

    public BaseTardisDoorsBlockRenderer(BlockEntityRendererFactory.Context context, EntityModelLayer modelLayer, Function<ModelPart, BaseTardisDoorsModel> modelFactory, float modelScale, float modelYScale, float modelYOffset, float modelZOffset, int color) {
        this(context, modelLayer, modelFactory, modelScale, modelYScale, modelYOffset, modelZOffset, 2.0F, color);
    }

    @Override
    public int getRenderDistance() {
        return 256;
    }

    @Override
    public void render(C tile, float delta, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        DoubleBlockHalf half = tile.getCachedState().get(BaseTardisDoorsBlock.HALF);
        if (half != DoubleBlockHalf.LOWER) return;

        boolean hasImmersivePortals = ModCompats.immersivePortals();
        boolean isOpen = tile.getCachedState().get(BaseTardisExteriorBlock.OPEN);
        float rotateDegrees = tile.getCachedState().get(BaseTardisDoorsBlock.FACING).asRotation();

        BaseTardisDoorsModel model = this.modelFactory.apply(this.ctx.getLayerModelPart(this.modelLayer));
        RenderLayer modelLayer = model.getLayer(this.modelLayer.getId());
        model.setupAnim(tile);

        matrixStack.push();
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotateDegrees));
        matrixStack.translate(0, this.modelScale * this.modelYOffset, this.modelZOffset);
        matrixStack.scale(this.modelScale, this.modelScale + this.modelYScale, this.modelScale);

        VertexConsumer vertexConsumer = buffer.getBuffer(modelLayer);
        model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
        model.renderDoors(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
        if (isOpen && !hasImmersivePortals) this.drawForeground(tile, matrixStack, buffer);

        matrixStack.pop();
    }

    private void drawForeground(C tile, MatrixStack matrixStack, VertexConsumerProvider buffer) {
        matrixStack.push();
        matrixStack.scale(this.foregroundScale, this.foregroundScale + this.modelYScale, this.foregroundScale);
        matrixStack.translate(-0.5F, -1.25F, -0.05F);

        RenderHelper.drawRectangle(matrixStack, buffer.getBuffer(RenderLayer.getGui()), 0, 0, 1, 2, this.color);

        matrixStack.push();
        matrixStack.translate(1F, 0, 0.05F);
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180));
        RenderHelper.drawRectangle(matrixStack, buffer.getBuffer(RenderLayer.getEndPortal()), 0, 0, 1, 2, 0xFF000000);
        matrixStack.pop();

        matrixStack.pop();
    }
}

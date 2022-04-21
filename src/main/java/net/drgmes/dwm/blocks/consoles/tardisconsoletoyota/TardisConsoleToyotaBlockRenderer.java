package net.drgmes.dwm.blocks.consoles.tardisconsoletoyota;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.drgmes.dwm.blocks.consoles.tardisconsoletoyota.models.TardisConsoleToyotaModel;
import net.drgmes.dwm.entities.tardis.consoles.renderers.BaseTardisConsoleBlockRenderer;
import net.drgmes.dwm.utils.base.blockentities.BaseTardisConsoleBlockEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TardisConsoleToyotaBlockRenderer extends BaseTardisConsoleBlockRenderer {
    public TardisConsoleToyotaBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BaseTardisConsoleBlockEntity tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        BlockState blockState = tile.getBlockState();
        ModelPart modelRoot = this.ctx.bakeLayer(TardisConsoleToyotaModel.LAYER_LOCATION);
        TardisConsoleToyotaModel model = new TardisConsoleToyotaModel(modelRoot);
        VertexConsumer vertexConsumer = buffer.getBuffer(model.renderType(TardisConsoleToyotaModel.LAYER_LOCATION.getModel()));

        poseStack.pushPose();
        poseStack.translate(0.5, 0.7, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()));
        poseStack.translate(0, -0.8F, 0);

        this.animate(tile, modelRoot);
        model.renderToBuffer(poseStack, vertexConsumer, combinedOverlay, packedLight, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    protected void activateButton(ModelPart model) {
        model.y += 0.25F;
    }

    protected void animateButton(ModelPart model, int phase) {
        if (phase > 0) model.y += 0.25F;
    }

    protected void activateLever(ModelPart model) {
        model.xRot -= 1.5F;
    }

    protected void animateLever(ModelPart model, int phase) {
        model.xRot -= 1.5F;
    }

    protected void activateRotator(ModelPart model, int phase) {
        model.yRot += 1.57F * phase;
    }

    protected void animateRotator(ModelPart model, int phase) {
        model.yRot += 1.57F;
    }

    protected void activateHandbrake(ModelPart model) {
        model.xRot -= 0.6F;
        model.yRot += 1.4F;
        model.zRot -= 0.9F;
    }

    protected void activateStarter(ModelPart model) {
        model.xRot -= 2.2F;
    }
}

package net.drgmes.dwm.blocks.tardis.others.tardistoyotaspinner;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.drgmes.dwm.blocks.tardis.others.tardistoyotaspinner.models.TardisToyotaSpinnerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TardisToyotaSpinnerBlockRenderer implements BlockEntityRenderer<TardisToyotaSpinnerBlockEntity> {
    protected BlockEntityRendererProvider.Context ctx;

    public TardisToyotaSpinnerBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.ctx = context;
    }

    @Override
    public void render(TardisToyotaSpinnerBlockEntity tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int combinedOverlay) {
        float rotateDegrees = tile.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot();
        ModelPart modelRoot = this.ctx.bakeLayer(TardisToyotaSpinnerModel.LAYER_LOCATION);
        TardisToyotaSpinnerModel model = new TardisToyotaSpinnerModel(modelRoot);
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(TardisToyotaSpinnerModel.LAYER_LOCATION.getModel()));
        model.setupAnim(tile);

        float scale = 1.5F;
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotateDegrees));
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0, -1.075F, 0);
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}

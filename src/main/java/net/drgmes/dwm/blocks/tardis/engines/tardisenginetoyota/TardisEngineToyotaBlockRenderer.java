package net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.drgmes.dwm.blocks.tardis.engines.BaseTardisEngineBlockRenderer;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.models.TardisEngineToyotaModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TardisEngineToyotaBlockRenderer extends BaseTardisEngineBlockRenderer<TardisEngineToyotaBlockEntity> {
    public TardisEngineToyotaBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TardisEngineToyotaBlockEntity tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int combinedOverlay) {
        float rotateDegrees = tile.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot();
        ModelPart modelRoot = this.ctx.bakeLayer(TardisEngineToyotaModel.LAYER_LOCATION);
        TardisEngineToyotaModel model = new TardisEngineToyotaModel(modelRoot);
        VertexConsumer vertexConsumer = buffer.getBuffer(model.renderType(TardisEngineToyotaModel.LAYER_LOCATION.getModel()));
        model.setupAnim(tile);

        float scale = 0.4F;
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotateDegrees));
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0, -1.5F, 0);
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}

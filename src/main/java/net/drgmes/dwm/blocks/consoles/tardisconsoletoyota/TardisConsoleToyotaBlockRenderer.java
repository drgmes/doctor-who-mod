package net.drgmes.dwm.blocks.consoles.tardisconsoletoyota;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.drgmes.dwm.blocks.consoles.tardisconsoletoyota.models.TardisConsoleToyotaModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TardisConsoleToyotaBlockRenderer implements BlockEntityRenderer<TardisConsoleToyotaBlockEntity> {
    private BlockEntityRendererProvider.Context ctx;

    public TardisConsoleToyotaBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.ctx = context;
    }

    @Override
    public void render(TardisConsoleToyotaBlockEntity tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        BlockState blockState = tile.getBlockState();
        if (blockState == null) return;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.7, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()));
        poseStack.translate(0, -0.8F, 0);

        TardisConsoleToyotaModel model = new TardisConsoleToyotaModel(ctx.bakeLayer(TardisConsoleToyotaModel.LAYER_LOCATION));
        VertexConsumer vertexConsumer = buffer.getBuffer(model.renderType(TardisConsoleToyotaModel.LAYER_LOCATION.getModel()));
        model.renderToBuffer(poseStack, vertexConsumer, combinedOverlay, packedLight, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}

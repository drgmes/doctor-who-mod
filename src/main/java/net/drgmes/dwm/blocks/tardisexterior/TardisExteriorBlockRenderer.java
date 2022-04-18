package net.drgmes.dwm.blocks.tardisexterior;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.drgmes.dwm.blocks.tardisexterior.models.TardisPoliceBoxExteriorModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TardisExteriorBlockRenderer implements BlockEntityRenderer<TardisExteriorBlockEntity> {
    private BlockEntityRendererProvider.Context ctx;

    public TardisExteriorBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.ctx = context;
    }

    @Override
    public void render(TardisExteriorBlockEntity tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        BlockState blockState = tile.getBlockState();
        if (blockState == null) return;

        Direction face = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        DoubleBlockHalf half = blockState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
        if (half != DoubleBlockHalf.LOWER) return;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.7, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(face.toYRot()));

        this.renderExterior(tile, partialTicks, poseStack, buffer, combinedOverlay, packedLight, 1.0F);
        poseStack.popPose();
    }

    public void renderExterior(TardisExteriorBlockEntity tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedOverlay, int packedLight, float alpha) {
        TardisPoliceBoxExteriorModel model = new TardisPoliceBoxExteriorModel(ctx.bakeLayer(TardisPoliceBoxExteriorModel.LAYER_LOCATION));
        VertexConsumer vertexConsumer = buffer.getBuffer(model.renderType(TardisPoliceBoxExteriorModel.LAYER_LOCATION.getModel()));
        float scale = 1.25F;

        poseStack.pushPose();
        poseStack.scale(scale, scale + 0.15F, scale);
        poseStack.translate(0, -1, 0);

        model.setupAnim(tile);
        model.renderToBuffer(poseStack, vertexConsumer, combinedOverlay, packedLight, 1.0F, 1.0F, 1.0F, alpha);

        poseStack.popPose();
    }
}

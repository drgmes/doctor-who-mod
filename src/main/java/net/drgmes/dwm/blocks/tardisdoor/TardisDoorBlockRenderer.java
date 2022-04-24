package net.drgmes.dwm.blocks.tardisdoor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.drgmes.dwm.blocks.tardisdoor.models.TardisPoliceBoxDoorModel;
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
public class TardisDoorBlockRenderer implements BlockEntityRenderer<TardisDoorBlockEntity> {
    private BlockEntityRendererProvider.Context ctx;

    public TardisDoorBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.ctx = context;
    }

    @Override
    public void render(TardisDoorBlockEntity tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedOverlay, int packedLight) {
        BlockState blockState = tile.getBlockState();
        if (blockState == null) return;

        Direction face = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        DoubleBlockHalf half = blockState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
        if (half != DoubleBlockHalf.LOWER) return;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.7, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(face.toYRot()));

        this.renderDoor(tile, partialTicks, poseStack, buffer, combinedOverlay, packedLight, 1.0F);
        poseStack.popPose();
    }

    public void renderDoor(TardisDoorBlockEntity tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedOverlay, int packedLight, float alpha) {
        TardisPoliceBoxDoorModel model = new TardisPoliceBoxDoorModel(ctx.bakeLayer(TardisPoliceBoxDoorModel.LAYER_LOCATION));
        VertexConsumer vertexConsumer = buffer.getBuffer(model.renderType(TardisPoliceBoxDoorModel.LAYER_LOCATION.getModel()));
        float scale = 1.5F;

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0, -1, -0.2075F);

        model.setupAnim(tile.getBlockState());
        model.renderToBuffer(poseStack, vertexConsumer, combinedOverlay, packedLight, 1.0F, 1.0F, 1.0F, alpha);

        poseStack.popPose();
    }
}

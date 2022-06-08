package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.models.TardisDoorsPoliceBoxModel;
import net.drgmes.dwm.common.tardis.boti.renderer.BotiEntraceData;
import net.drgmes.dwm.common.tardis.boti.renderer.BotiRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TardisDoorsPoliceBoxBlockRenderer implements BlockEntityRenderer<TardisDoorsPoliceBoxBlockEntity> {
    private BlockEntityRendererProvider.Context ctx;

    public TardisDoorsPoliceBoxBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.ctx = context;
    }

    @Override
    public void render(TardisDoorsPoliceBoxBlockEntity tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        BlockState blockState = tile.getBlockState();
        if (blockState == null) return;

        Direction face = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        DoubleBlockHalf half = blockState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
        if (half != DoubleBlockHalf.LOWER) return;

        ResourceLocation modelResource = TardisDoorsPoliceBoxModel.LAYER_LOCATION.getModel();
        TardisDoorsPoliceBoxModel model = new TardisDoorsPoliceBoxModel(ctx.bakeLayer(TardisDoorsPoliceBoxModel.LAYER_LOCATION));
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutout(modelResource));
        model.setupAnim(tile);

        poseStack.pushPose();
        this.setupModelView(poseStack, face);
        model.renderToBuffer(poseStack, buffer, packedLight, combinedOverlay, 1, 1, 1, 1);
        model.renderDoorsToBuffer(poseStack, buffer, packedLight, combinedOverlay, 1, 1, 1, 1);
        poseStack.popPose();

        BotiEntraceData entraceData = new BotiEntraceData(tile.getBlockPos(), tile.tardisLevelUUID + "-exterior");
        entraceData.setBotiStorage(tile.getBotiStorage());

        entraceData.setBotiRenderer((innerPoseStack, innerBufferSource) -> {
            innerPoseStack.pushPose();
            this.setupModelView(innerPoseStack, face);
            model.renderBotiToBuffer(innerPoseStack, innerBufferSource.getBuffer(RenderType.entityCutout(modelResource)), packedLight, combinedOverlay, 1, 1, 1, 1);
            innerPoseStack.popPose();
        });

        entraceData.setBotiTransformer((innerPoseStack) -> {
            innerPoseStack.translate(0.5, 0, 0.5);
            innerPoseStack.mulPose(Vector3f.YP.rotationDegrees(180));
            innerPoseStack.mulPose(Vector3f.YN.rotationDegrees(face.toYRot()));
            innerPoseStack.mulPose(Vector3f.YP.rotationDegrees(entraceData.getBotiStorage().getDirection().toYRot()));
            innerPoseStack.translate(-0.5, 0, -0.5);
        });

        if (blockState.getValue(BlockStateProperties.OPEN)) {
            BotiRenderer.addEntraceData(entraceData);
        }
    }

    public void setupModelView(PoseStack poseStack, Direction face) {
        float scale = 1.5F;
        poseStack.translate(0.5, 2.25, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(face.toYRot()));
        poseStack.translate(0, 0, -0.31F);
        poseStack.scale(scale, scale, scale);
    }
}

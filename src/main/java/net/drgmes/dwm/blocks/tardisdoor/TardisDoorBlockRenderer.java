package net.drgmes.dwm.blocks.tardisdoor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.drgmes.dwm.blocks.tardisdoor.models.TardisPoliceBoxDoorModel;
import net.drgmes.dwm.common.boti.BotiEntraceData;
import net.drgmes.dwm.common.boti.BotiManager;
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
public class TardisDoorBlockRenderer implements BlockEntityRenderer<TardisDoorBlockEntity> {
    private BlockEntityRendererProvider.Context ctx;

    public TardisDoorBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.ctx = context;
    }

    @Override
    public void render(TardisDoorBlockEntity tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedOverlay, int packedLight) {
        BlockState blockState = tile.getBlockState();
        if (blockState == null) return;

        Direction face = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        DoubleBlockHalf half = blockState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
        if (half != DoubleBlockHalf.LOWER) return;

        ResourceLocation modelResource = TardisPoliceBoxDoorModel.LAYER_LOCATION.getModel();
        TardisPoliceBoxDoorModel model = new TardisPoliceBoxDoorModel(ctx.bakeLayer(TardisPoliceBoxDoorModel.LAYER_LOCATION));
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutout(modelResource));
        model.setupAnim(tile.getBlockState());

        poseStack.pushPose();
        this.setupModelView(poseStack, face, true);
        model.renderToBuffer(poseStack, buffer, combinedOverlay, packedLight, 1, 1, 1, 1);
        model.renderDoorsToBuffer(poseStack, buffer, combinedOverlay, packedLight, 1, 1, 1, 1);
        poseStack.popPose();

        BotiEntraceData entraceData = new BotiEntraceData(tile.getBlockPos(), tile.tardisLevelUUID + "-exterior");

        entraceData.setBotiRenderer((innerPoseStack, innerBufferSource) -> {
            innerPoseStack.pushPose();
            this.setupModelView(innerPoseStack, face, true);
            model.renderBotiToBuffer(innerPoseStack, innerBufferSource.getBuffer(RenderType.entityCutout(modelResource)), combinedOverlay, packedLight, 1, 1, 1, 1);
            innerPoseStack.popPose();
        });

        entraceData.setBotiTransformer((innerPoseStack) -> {
            this.setupModelView(innerPoseStack, face, false);
            innerPoseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
            innerPoseStack.mulPose(Vector3f.YP.rotationDegrees(180));
            innerPoseStack.translate(-0.5, 0, 0);
        });

        if (blockState.getValue(BlockStateProperties.OPEN)) {
            BotiManager.addEntraceData(entraceData);
        }
    }

    public void setupModelView(PoseStack poseStack, Direction face, boolean scaling) {
        poseStack.translate(0.5, 2.25, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(face.toYRot()));
        poseStack.translate(0, 0, -0.31F);

        if (scaling) {
            float scale = 1.5F;
            poseStack.scale(scale, scale, scale);
        }
    }
}

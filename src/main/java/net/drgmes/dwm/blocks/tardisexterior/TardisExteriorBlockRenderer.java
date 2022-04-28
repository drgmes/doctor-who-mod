package net.drgmes.dwm.blocks.tardisexterior;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.drgmes.dwm.blocks.tardisexterior.models.TardisPoliceBoxExteriorModel;
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

        ResourceLocation modelResource = TardisPoliceBoxExteriorModel.LAYER_LOCATION.getModel();
        TardisPoliceBoxExteriorModel model = new TardisPoliceBoxExteriorModel(ctx.bakeLayer(TardisPoliceBoxExteriorModel.LAYER_LOCATION));
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(modelResource));
        model.setupAnim(tile.getBlockState());

        float speed = 0.3F;
        float intense = 0.4F;
        float percent = tile.getMaterializedPercent();
        float alpha = (float) Math.cos(percent * speed) * intense + (1.0F / 100) * percent;
        float alphaClamped = percent < 10 ? 0 : Math.max(0, Math.min(1.0F, alpha));

        poseStack.pushPose();
        this.setupModelView(poseStack, face);
        model.renderToBuffer(poseStack, vertexConsumer, combinedOverlay, packedLight, 1, 1, 1, alphaClamped);
        poseStack.popPose();

        BotiEntraceData entraceData = new BotiEntraceData(tile.getBlockPos(), tile.tardisLevelUUID + "-exterior");

        entraceData.setDoorsRenderer((innerPoseStack, innerBufferSource) -> {
            innerPoseStack.pushPose();
            this.setupModelView(innerPoseStack, face);
            model.renderDoorsToBuffer(innerPoseStack, innerBufferSource.getBuffer(RenderType.entityTranslucent(modelResource)), combinedOverlay, packedLight, 1, 1, 1, alphaClamped);
            innerPoseStack.popPose();
        });

        entraceData.setBotiRenderer((innerPoseStack, innerBufferSource) -> {
            innerPoseStack.pushPose();
            this.setupModelView(innerPoseStack, face);
            model.renderBotiToBuffer(innerPoseStack, innerBufferSource.getBuffer(RenderType.entityTranslucent(modelResource)), combinedOverlay, packedLight, 1, 1, 1, alphaClamped);
            innerPoseStack.popPose();
        });

        entraceData.setBotiTransformer((innerPoseStack) -> {
            this.setupModelView(innerPoseStack, face);
            innerPoseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
            innerPoseStack.translate(0, -0.5, -0.375);
        });

        if (blockState.getValue(BlockStateProperties.OPEN)) {
            BotiManager.addEntraceData(entraceData);
        }
        else {
            entraceData.renderDoors(poseStack, buffer);
        }
    }

    public void setupModelView(PoseStack poseStack, Direction face) {
        float scale = 1.25F;
        poseStack.translate(0.5, 0.7, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(face.toYRot()));
        poseStack.scale(scale, scale + 0.15F, scale);
        poseStack.translate(0, -1, 0);
    }
}

package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.models.TardisExteriorPoliceBoxModel;
import net.drgmes.dwm.common.tardis.boti.renderer.BotiEntranceData;
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
public class TardisExteriorPoliceBoxBlockRenderer implements BlockEntityRenderer<TardisExteriorPoliceBoxBlockEntity> {
    private final BlockEntityRendererProvider.Context ctx;

    public TardisExteriorPoliceBoxBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.ctx = context;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public void render(TardisExteriorPoliceBoxBlockEntity tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int combinedOverlay) {
        BlockState blockState = tile.getBlockState();
        if (blockState == null) return;

        Direction face = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        DoubleBlockHalf half = blockState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
        if (half != DoubleBlockHalf.LOWER) return;

        ResourceLocation modelResource = TardisExteriorPoliceBoxModel.LAYER_LOCATION.getModel();
        TardisExteriorPoliceBoxModel model = new TardisExteriorPoliceBoxModel(ctx.bakeLayer(TardisExteriorPoliceBoxModel.LAYER_LOCATION));
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(modelResource));
        model.setupAnim(tile);

        float speed = 0.3F;
        float intense = 0.4F;
        float percent = tile.getMaterializedPercent();
        float alpha = (float) Math.cos(percent * speed) * intense + (1.0F / 100) * percent;
        float alphaClamped = percent < 10 ? 0 : Math.max(0, Math.min(1.0F, alpha));

        poseStack.pushPose();
        this.setupModelView(poseStack, face);
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, combinedOverlay, 1, 1, 1, alphaClamped);
        poseStack.popPose();

        BotiEntranceData entranceData = new BotiEntranceData(tile.getBlockPos(), tile.tardisLevelUUID);
        entranceData.setBotiStorage(tile.getBotiStorage());

        entranceData.setDoorsRenderer((innerPoseStack, innerBufferSource) -> {
            innerPoseStack.pushPose();
            this.setupModelView(innerPoseStack, face);
            model.renderDoorsToBuffer(innerPoseStack, innerBufferSource.getBuffer(RenderType.entityTranslucent(modelResource)), packedLight, combinedOverlay, 1, 1, 1, alphaClamped);
            innerPoseStack.popPose();
        });

        entranceData.setBotiRenderer((innerPoseStack, innerBufferSource) -> {
            innerPoseStack.pushPose();
            this.setupModelView(innerPoseStack, face);
            model.renderBotiToBuffer(innerPoseStack, innerBufferSource.getBuffer(RenderType.entityTranslucent(modelResource)), packedLight, combinedOverlay, 1, 1, 1, alphaClamped);
            innerPoseStack.popPose();
        });

        entranceData.setBotiTransformer((innerPoseStack) -> {
            innerPoseStack.translate(0.5, 0, 0.5);
            innerPoseStack.mulPose(Vector3f.YP.rotationDegrees(180));
            innerPoseStack.mulPose(Vector3f.YN.rotationDegrees(face.toYRot()));
            innerPoseStack.mulPose(Vector3f.YP.rotationDegrees(entranceData.getBotiStorage().getDirection().toYRot()));
            innerPoseStack.translate(-0.5, 0, -0.5);
        });

        if (blockState.getValue(BlockStateProperties.OPEN)) {
            BotiRenderer.addEntranceData(entranceData);
        } else {
            entranceData.renderDoors(poseStack, buffer);
        }
    }

    public void setupModelView(PoseStack poseStack, Direction face) {
        float scale = 1.25F;
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(face.toYRot()));
        poseStack.translate(0, -1.6, 0);
        poseStack.scale(scale, scale + 0.15F, scale);
    }
}

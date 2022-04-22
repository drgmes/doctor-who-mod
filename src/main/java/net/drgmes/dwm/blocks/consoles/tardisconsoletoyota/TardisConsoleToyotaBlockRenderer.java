package net.drgmes.dwm.blocks.consoles.tardisconsoletoyota;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.drgmes.dwm.blocks.consoles.tardisconsoletoyota.models.TardisConsoleToyotaModel;
import net.drgmes.dwm.entities.tardis.consoles.renderers.BaseTardisConsoleBlockRenderer;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.drgmes.dwm.utils.base.blockentities.BaseTardisConsoleBlockEntity;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
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
        float rotateDegrees = tile.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot();
        ModelPart modelRoot = this.ctx.bakeLayer(TardisConsoleToyotaModel.LAYER_LOCATION);
        TardisConsoleToyotaModel model = new TardisConsoleToyotaModel(modelRoot);
        VertexConsumer vertexConsumer = buffer.getBuffer(model.renderType(TardisConsoleToyotaModel.LAYER_LOCATION.getModel()));

        // Render Model

        poseStack.pushPose();
        poseStack.translate(0.5, 0.7, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotateDegrees));
        poseStack.translate(0, -0.8F, 0);

        this.animate(tile, modelRoot, partialTicks);
        model.renderToBuffer(poseStack, vertexConsumer, combinedOverlay, packedLight, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

        // Render Screen texts

        tile.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
            try {
                if (!provider.isValid() || !tile.getLevel().dimensionTypeRegistration().is(ModDimensionTypes.TARDIS)) return;

                float scaling = 0.0027F;
                BlockPos prevExteriorPosition = provider.getPreviousExteriorPosition();
                BlockPos currExteriorPosition = provider.getCurrentExteriorPosition();
                BlockPos destExteriorPosition = provider.getDestinationExteriorPosition();
                String posPrevName = prevExteriorPosition.getX() + " " + prevExteriorPosition.getY() + " " + prevExteriorPosition.getZ();
                String posCurrName = currExteriorPosition.getX() + " " + currExteriorPosition.getY() + " " + currExteriorPosition.getZ();
                String posDestName = destExteriorPosition.getX() + " " + destExteriorPosition.getY() + " " + destExteriorPosition.getZ();
                String facingPrevName = provider.getPreviousExteriorFacing().name().toUpperCase();
                String facingCurrName = provider.getCurrentExteriorFacing().name().toUpperCase();
                String facingDestName = provider.getDestinationExteriorFacing().name().toUpperCase();
                String dimPrevName = provider.getPreviousExteriorDimension().location().getPath().toUpperCase();
                String dimCurrName = provider.getCurrentExteriorDimension().location().getPath().toUpperCase();
                String dimDestName = provider.getDestinationExteriorDimension().location().getPath().toUpperCase();

                String[] lines = new String[] {
                    this.buildScreenParamText("Prev Position", posPrevName),
                    this.buildScreenParamText("Curr Position", posCurrName),
                    this.buildScreenParamText("Dest Position", posDestName),
                    "",
                    this.buildScreenParamText("Prev Facing", facingPrevName),
                    this.buildScreenParamText("Curr Facing", facingCurrName),
                    this.buildScreenParamText("Dest Facing", facingDestName),
                    "",
                    this.buildScreenParamText("Prev Dimension", dimPrevName),
                    this.buildScreenParamText("Curr Dimension", dimCurrName),
                    this.buildScreenParamText("Dest Dimension", dimDestName)
                };

                poseStack.pushPose();
                poseStack.translate(0.5D, 1.085D, 0.5D);
                poseStack.mulPose(Vector3f.YN.rotationDegrees(rotateDegrees - 90));
                poseStack.translate(-0.255D, 0, 0.53D);
                poseStack.scale(scaling, -scaling, scaling);
                poseStack.mulPose(Vector3f.XN.rotation(-1.3F));

                for (int i = 0; i < lines.length; i++) {
                    this.ctx.getFont().drawInBatch(lines[i], 0, i * this.ctx.getFont().lineHeight, 0xFFFFFF, true, poseStack.last().pose(), buffer, false, 0, LightTexture.FULL_BRIGHT);
                }

                poseStack.popPose();
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }

    protected void activateButton(ModelPart model) {
        model.y += 0.25F;
    }

    protected void animateButton(ModelPart model, float partialTicks, int phase) {
        if (phase != 0) model.y += 0.25F;
    }

    protected void activateLever(ModelPart model) {
        model.xRot -= 1.5F;
    }

    protected void animateLever(ModelPart model, float partialTicks, int phase) {
        if (phase != 0) model.xRot -= 1.5F;
    }

    protected void activateRotator(ModelPart model, int phase) {
        model.yRot += 1.57F * phase;
    }

    protected void animateRotator(ModelPart model, float partialTicks, int phase) {
        float step = 1.57F;
        if (phase != 0) model.yRot += (step / 2) * phase;
    }

    protected void activateHandbrake(ModelPart model) {
        model.xRot -= 0.6F;
        model.yRot += 1.4F;
        model.zRot -= 0.9F;
    }

    protected void activateStarter(ModelPart model) {
        model.xRot -= 2.2F;
    }

    private String buildScreenParamText(String title, String append) {
        int lineWidth = 188;
        Font font = this.ctx.getFont();
        String str = title + ": " + append;

        return title + ": " + " ".repeat((lineWidth - font.width(str)) / font.width(" ")) + append;
    }
}

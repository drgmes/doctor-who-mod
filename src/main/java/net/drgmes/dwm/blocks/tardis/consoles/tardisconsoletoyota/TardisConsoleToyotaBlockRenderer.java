package net.drgmes.dwm.blocks.tardis.consoles.tardisconsoletoyota;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockRenderer;
import net.drgmes.dwm.blocks.tardis.consoles.tardisconsoletoyota.models.TardisConsoleToyotaModel;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoleTypes;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.setup.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
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
    public void render(BaseTardisConsoleBlockEntity tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int combinedOverlay) {
        float rotateDegrees = tile.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot();
        ModelPart modelRoot = this.ctx.bakeLayer(TardisConsoleToyotaModel.LAYER_LOCATION);
        TardisConsoleToyotaModel model = new TardisConsoleToyotaModel(modelRoot);
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(TardisConsoleToyotaModel.LAYER_LOCATION.getModel()));
        model.setupAnim(tile);

        float scale = 0.4F;
        poseStack.pushPose();
        poseStack.translate(0.5, 0.7, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotateDegrees));
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0, 0.25F, 0);
        this.animate(tile, modelRoot, partialTicks);
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

        this.renderScreen(tile, poseStack, buffer, packedLight, combinedOverlay, rotateDegrees);
        this.renderScrewdriver(tile, poseStack, buffer, packedLight, combinedOverlay, rotateDegrees);
    }

    @Override
    protected void activateLever(ModelPart model, boolean value, TardisConsoleControlRoles controlRole, float partialTicks) {
        if (value) model.xRot -= 1.25F;
    }

    @Override
    protected void activateLever(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks) {
        if (controlRole.type == TardisConsoleControlRoleTypes.BOOLEAN || controlRole.type == TardisConsoleControlRoleTypes.BOOLEAN_DIRECT) {
            this.activateLever(model, value != 0, controlRole, partialTicks);
            return;
        }

        model.xRot -= (1F / (controlRole.maxIntValue - 1)) * value;
    }

    @Override
    protected void animateLever(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks) {
        this.activateLever(model, value, controlRole, partialTicks);
    }

    @Override
    protected void activateButton(ModelPart model, boolean value, TardisConsoleControlRoles controlRole, float partialTicks) {
        if (value) model.y += 0.5F;
    }

    @Override
    protected void activateButton(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks) {
        this.activateButton(model, value != 0, controlRole, partialTicks);
    }

    @Override
    protected void animateButton(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks) {
        this.activateButton(model, value, controlRole, partialTicks);
    }

    @Override
    protected void activateSlider(ModelPart model, boolean value, TardisConsoleControlRoles controlRole, float partialTicks) {
        if (value) model.z += 4F;
    }

    @Override
    protected void activateSlider(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks) {
        model.z += value * (4F / (controlRole.maxIntValue > 0 ? controlRole.maxIntValue : 1));
    }

    @Override
    protected void animateSlider(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks) {
        this.activateSlider(model, value, controlRole, partialTicks);
    }

    @Override
    protected void activateRotator(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks) {
        model.yRot += 1.57F * value;
    }

    @Override
    protected void animateRotator(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks) {
        if (value != 0) this.activateRotator(model, value / 2, controlRole, partialTicks);
    }

    @Override
    protected void activateHandbrake(ModelPart model, TardisConsoleControlRoles controlRole) {
        model.yRot -= 1.55F;
    }

    @Override
    protected void activateStarter(ModelPart model, TardisConsoleControlRoles controlRole) {
        model.xRot += 2F;
    }

    private void renderScreen(BaseTardisConsoleBlockEntity tile, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int combinedOverlay, float rotateDegrees) {
        tile.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
            if (!provider.isValid()) return;

            poseStack.pushPose();
            poseStack.translate(0.5, 1F, 0.5);
            poseStack.mulPose(Vector3f.YN.rotationDegrees(rotateDegrees + 60));

            if (tile.monitorPage == 1) this.renderScreenPage2(tile, poseStack, buffer, packedLight, combinedOverlay, provider);
            else this.renderScreenPage1(tile, poseStack, buffer, packedLight, combinedOverlay, provider);

            poseStack.popPose();
        });
    }

    private void renderScreenPage1(BaseTardisConsoleBlockEntity tile, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int combinedOverlay, ITardisLevelData provider) {
        String flight = "NO";
        if (provider.getSystem(TardisSystemFlight.class) instanceof TardisSystemFlight flightSystem) {
            if (flightSystem.inProgress()) flight = flightSystem.getProgressPercent() + "%";
        }

        String materialized = "YES";
        if (provider.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization materializationSystem) {
            if (materializationSystem.inProgress()) materialized = materializationSystem.getProgressPercent() + "%";
            else if (!materializationSystem.isMaterialized()) materialized = "NO";
        }

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

        this.printStringsToScreen(poseStack, buffer, packedLight, 0.002F, new String[] {
            this.buildScreenParamText("Flight", flight),
            this.buildScreenParamText("Materialized", materialized),
            "",
            this.buildScreenParamText("Prev Position", posPrevName),
            this.buildScreenParamText("Curr Position", posCurrName),
            this.buildScreenParamText("Dest Position", posDestName),
            "",
            this.buildScreenParamText("Prev Facing", facingPrevName),
            this.buildScreenParamText("Curr Facing", facingCurrName),
            this.buildScreenParamText("Dest Facing", facingDestName),
            "",
            this.buildScreenParamText("Prev Dimension", dimPrevName.replace("_", " ")),
            this.buildScreenParamText("Curr Dimension", dimCurrName.replace("_", " ")),
            this.buildScreenParamText("Dest Dimension", dimDestName.replace("_", " "))
        });
    }

    private void renderScreenPage2(BaseTardisConsoleBlockEntity tile, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int combinedOverlay, ITardisLevelData provider) {
        String shieldsState = provider.isShieldsEnabled() ? "ON" : "OFF";
        String artronEnergyHarvestingState = provider.isEnergyArtronHarvesting() ? "ON" : "OFF";
        String forgeEnergyHarvestingState = provider.isEnergyForgeHarvesting() ? "ON" : "OFF";

        this.printStringsToScreen(poseStack, buffer, packedLight, 0.002F, new String[] {
            this.buildScreenParamText("Shields", shieldsState),
            this.buildScreenParamText("Artron Energy Harvesting", artronEnergyHarvestingState),
            this.buildScreenParamText("Forge Energy Harvesting", forgeEnergyHarvestingState),
            "",
            this.buildScreenParamText("Artron Energy", provider.getEnergyArtron() + " AE"),
            this.buildScreenParamText("Forge Energy", provider.getEnergyForge() + " FE"),
        });
    }

    private void renderScrewdriver(BaseTardisConsoleBlockEntity tile, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int combinedOverlay, float rotateDegrees) {
        if (tile.screwdriverItemStack != null && !tile.screwdriverItemStack.isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
            float scale = 0.25F;

            poseStack.pushPose();
            poseStack.translate(0.5, 1.25F, 0.5);
            poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
            poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
            poseStack.mulPose(Vector3f.YP.rotationDegrees(rotateDegrees));
            poseStack.translate(0.5545F, 0.065F, 0.545F);
            poseStack.mulPose(Vector3f.YN.rotationDegrees(30));
            poseStack.mulPose(Vector3f.ZN.rotationDegrees(-15));
            poseStack.mulPose(Vector3f.YN.rotationDegrees(-90));
            poseStack.scale(scale, scale, scale);
            mc.getItemRenderer().renderStatic(mc.player, tile.screwdriverItemStack, TransformType.NONE, false, poseStack, buffer, mc.level, packedLight, combinedOverlay, 0);
            poseStack.popPose();
        }
    }

    private String buildScreenParamText(String title, String appendInput) {
        int substring = 16;
        String append = appendInput.substring(0, Math.min(substring, appendInput.length()));
        append += appendInput.length() > append.length() ? "..." : "";

        int lineWidth = 239;
        Font font = this.ctx.getFont();
        String str = title + ": " + append;

        return title + ": " + " ".repeat((lineWidth - font.width(str)) / font.width(" ")) + append;
    }

    private void printStringsToScreen(PoseStack poseStack, MultiBufferSource buffer, int packedLight, float scaling, String[] lines) {
        Font font = this.ctx.getFont();

        poseStack.pushPose();
        poseStack.translate(-0.255D, 0.095F, 0.765D);
        poseStack.scale(scaling, -scaling, scaling);
        poseStack.mulPose(Vector3f.XN.rotation(-1.31F));

        for (int i = 0; i < lines.length; i++) {
            font.drawInBatch(
                lines[i],
                0,
                i * font.lineHeight,
                0xFFFFFF,
                true,
                poseStack.last().pose(),
                buffer,
                false,
                0,
                packedLight
            );
        }

        poseStack.popPose();
    }
}

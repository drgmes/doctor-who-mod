package net.drgmes.dwm.blocks.consoles.tardisconsoletoyota;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.drgmes.dwm.blocks.consoles.tardisconsoletoyota.models.TardisConsoleToyotaModel;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoleTypes;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.entities.tardis.consoles.renderers.BaseTardisConsoleBlockRenderer;
import net.drgmes.dwm.setup.ModCapabilities;
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

        poseStack.pushPose();
        poseStack.translate(0.5, 0.7, 0.5);
        poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotateDegrees));
        poseStack.translate(0, -0.8F, 0);
        this.animate(tile, modelRoot, partialTicks);
        model.renderToBuffer(poseStack, vertexConsumer, combinedOverlay, packedLight, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.5D, 1.085D, 0.5D);
        poseStack.mulPose(Vector3f.YN.rotationDegrees(rotateDegrees - 90));
        this.renderScreen(tile, poseStack, buffer);
        poseStack.popPose();
    }

    @Override
    protected void activateButton(ModelPart model, boolean value, TardisConsoleControlRoles controlRole, float partialTicks) {
        if (value) model.y += 0.25F;
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
    protected void activateLever(ModelPart model, boolean value, TardisConsoleControlRoles controlRole, float partialTicks) {
        if (value) model.xRot -= 1.5F;
    }

    @Override
    protected void activateLever(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks) {
        if (controlRole.type == TardisConsoleControlRoleTypes.NUMBER || controlRole.type == TardisConsoleControlRoleTypes.NUMBER_DIRECT || controlRole.type == TardisConsoleControlRoleTypes.NUMBER_CYCLE) {
            model.xRot -= (1.5F / (controlRole.maxIntValue - 1)) * Math.abs(value);
            return;
        }

        this.activateLever(model, value != 0, controlRole, partialTicks);
    }

    @Override
    protected void animateLever(ModelPart model, int value, TardisConsoleControlRoles controlRole, float partialTicks) {
        this.activateLever(model, value, controlRole, partialTicks);
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
        model.xRot -= 0.6F;
        model.yRot += 1.4F;
        model.zRot -= 0.9F;
    }

    @Override
    protected void activateStarter(ModelPart model, TardisConsoleControlRoles controlRole) {
        model.xRot -= 2.2F;
    }

    private void renderScreen(BaseTardisConsoleBlockEntity tile, PoseStack poseStack, MultiBufferSource buffer) {
        tile.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
            if (!provider.isValid()) return;

            if (tile.monitorPage == 1) this.renderScreenPage2(tile, poseStack, buffer, provider);
            else this.renderScreenPage1(tile, poseStack, buffer, provider);
        });
    }

    private void renderScreenPage1(BaseTardisConsoleBlockEntity tile, PoseStack poseStack, MultiBufferSource buffer, ITardisLevelData provider) {
        String flight = "NO";
        if (provider.getSystem(TardisSystemFlight.class) instanceof TardisSystemFlight flightSystem) {
            if (flightSystem.tickInFlight > 0) flight = flightSystem.getFlightPercent() + "%";
        }

        String isMaterialized = "YES";
        if (provider.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization materializationSystem) {
            if (!materializationSystem.isMaterialized) isMaterialized = "NO";
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

        this.printStringsToScreen(poseStack, buffer, 0.002175F, new String[] {
            this.buildScreenParamText("Flight", flight),
            this.buildScreenParamText("Materialized", isMaterialized),
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

    private void renderScreenPage2(BaseTardisConsoleBlockEntity tile, PoseStack poseStack, MultiBufferSource buffer, ITardisLevelData provider) {
        String shieldsState = provider.isShieldsEnabled() ? "ON" : "OFF";
        String artronEnergyHarvestingState = provider.isEnergyArtronHarvesting() ? "ON" : "OFF";
        String forgeEnergyHarvestingState = provider.isEnergyForgeHarvesting() ? "ON" : "OFF";

        this.printStringsToScreen(poseStack, buffer, 0.002175F, new String[] {
            this.buildScreenParamText("Shields", shieldsState),
            this.buildScreenParamText("Artron Energy Harvesting", artronEnergyHarvestingState),
            this.buildScreenParamText("Forge Energy Harvesting", forgeEnergyHarvestingState),
            "",
            this.buildScreenParamText("Artron Energy", provider.getEnergyArtron() + " AE"),
            this.buildScreenParamText("Forge Energy", provider.getEnergyForge() + " FE"),
        });
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

    private void printStringsToScreen(PoseStack poseStack, MultiBufferSource buffer, float scaling, String[] lines) {
        Font font = this.ctx.getFont();

        poseStack.pushPose();
        poseStack.translate(-0.255D, 0, 0.53D);
        poseStack.scale(scaling, -scaling, scaling);
        poseStack.mulPose(Vector3f.XN.rotation(-1.3F));

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
                LightTexture.FULL_BRIGHT
            );
        }

        poseStack.popPose();
    }
}

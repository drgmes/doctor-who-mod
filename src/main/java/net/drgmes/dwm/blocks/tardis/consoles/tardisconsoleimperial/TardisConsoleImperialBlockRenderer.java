package net.drgmes.dwm.blocks.tardis.consoles.tardisconsoleimperial;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlock;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockRenderer;
import net.drgmes.dwm.blocks.tardis.consoles.tardisconsoleimperial.models.TardisConsoleImperialModel;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.consoles.controls.ETardisConsoleControlRole;
import net.drgmes.dwm.common.tardis.consoles.controls.ETardisConsoleControlRoleType;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class TardisConsoleImperialBlockRenderer extends BaseTardisConsoleBlockRenderer<TardisConsoleImperialBlockEntity> {
    public TardisConsoleImperialBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(TardisConsoleImperialBlockEntity tile, float delta, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        float rotateDegrees = tile.getCachedState().get(BaseTardisConsoleBlock.FACING).asRotation();
        EntityModelLayer modelLayer = TardisConsoleImperialModel.LAYER_LOCATION;
        ModelPart modelRoot = this.ctx.getLayerRenderDispatcher().getModelPart(modelLayer);
        TardisConsoleImperialModel model = new TardisConsoleImperialModel(modelRoot);
        VertexConsumer vertexConsumer = buffer.getBuffer(model.getLayer(modelLayer.getId()));
        model.setupAnim(tile);

        float scale = 0.4F;
        matrixStack.push();
        matrixStack.translate(0.5, 0.7, 0.5);
        matrixStack.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(180));
        matrixStack.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(180));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotateDegrees));
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0, 0.25F, 0);
        this.animate(tile, modelRoot, delta);
        model.render(matrixStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();

        this.renderScreen(tile, matrixStack, buffer, light, overlay, rotateDegrees);
        this.renderScrewdriver(tile, matrixStack, buffer, light, overlay, rotateDegrees);
    }

    @Override
    protected void activateLever(ModelPart model, boolean value, ETardisConsoleControlRole controlRole, float delta) {
        if (value) model.pitch -= 1.25F;
    }

    @Override
    protected void activateLever(ModelPart model, int value, ETardisConsoleControlRole controlRole, float delta) {
        if (controlRole.type == ETardisConsoleControlRoleType.BOOLEAN || controlRole.type == ETardisConsoleControlRoleType.BOOLEAN_DIRECT) {
            this.activateLever(model, value != 0, controlRole, delta);
            return;
        }

        model.pitch -= (1F / (controlRole.maxIntValue - 1)) * value;
    }

    @Override
    protected void animateLever(ModelPart model, int value, ETardisConsoleControlRole controlRole, float delta) {
        this.activateLever(model, value, controlRole, delta);
    }

    @Override
    protected void activateButton(ModelPart model, boolean value, ETardisConsoleControlRole controlRole, float delta) {
        if (value) model.pivotY += 0.5F;
    }

    @Override
    protected void activateButton(ModelPart model, int value, ETardisConsoleControlRole controlRole, float delta) {
        this.activateButton(model, value != 0, controlRole, delta);
    }

    @Override
    protected void animateButton(ModelPart model, int value, ETardisConsoleControlRole controlRole, float delta) {
        this.activateButton(model, value, controlRole, delta);
    }

    @Override
    protected void activateSlider(ModelPart model, boolean value, ETardisConsoleControlRole controlRole, float delta) {
        if (value) model.pivotZ -= 4F;
    }

    @Override
    protected void activateSlider(ModelPart model, int value, ETardisConsoleControlRole controlRole, float delta) {
        model.pivotZ -= value * (4F / (controlRole.maxIntValue > 0 ? controlRole.maxIntValue : 1));
    }

    @Override
    protected void animateSlider(ModelPart model, int value, ETardisConsoleControlRole controlRole, float delta) {
        this.activateSlider(model, value, controlRole, delta);
    }

    @Override
    protected void activateRotator(ModelPart model, int value, ETardisConsoleControlRole controlRole, float delta) {
        model.yaw += 1.57F * value;
    }

    @Override
    protected void animateRotator(ModelPart model, int value, ETardisConsoleControlRole controlRole, float delta) {
        if (value != 0) this.activateRotator(model, value / 2, controlRole, delta);
    }

    @Override
    protected void activateHandbrake(ModelPart model, ETardisConsoleControlRole controlRole) {
        model.yaw -= 1.55F;
    }

    @Override
    protected void activateStarter(ModelPart model, ETardisConsoleControlRole controlRole) {
        model.pitch += 2F;
    }

    private void renderScreen(TardisConsoleImperialBlockEntity tile, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay, float rotateDegrees) {
        if (!tile.tardisStateManager.isValid()) return;

        matrixStack.push();
        matrixStack.translate(0.5, 1F, 0.5);
        matrixStack.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(rotateDegrees + 60));

        if (tile.monitorPage == 1) this.renderScreenPage2(tile, matrixStack, buffer, light, overlay, tile.tardisStateManager);
        else this.renderScreenPage1(tile, matrixStack, buffer, light, overlay, tile.tardisStateManager);

        matrixStack.pop();
    }

    private void renderScreenPage1(TardisConsoleImperialBlockEntity tile, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay, TardisStateManager tardis) {
        String NONE = "-";

        String flight = "NO";
        TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);
        if (flightSystem.inProgress()) flight = flightSystem.getProgressPercent() + "%";

        String materialized = "YES";
        TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);
        if (materializationSystem.inProgress()) materialized = materializationSystem.getProgressPercent() + "%";
        else if (!materializationSystem.isMaterialized()) materialized = "NO";

        BlockPos prevExteriorPosition = tardis.getPreviousExteriorPosition();
        BlockPos currExteriorPosition = tardis.getCurrentExteriorPosition();
        BlockPos destExteriorPosition = tardis.getDestinationExteriorPosition();
        String posPrevName = prevExteriorPosition.getX() + " " + prevExteriorPosition.getY() + " " + prevExteriorPosition.getZ();
        String posCurrName = currExteriorPosition.getX() + " " + currExteriorPosition.getY() + " " + currExteriorPosition.getZ();
        String posDestName = destExteriorPosition.getX() + " " + destExteriorPosition.getY() + " " + destExteriorPosition.getZ();
        String facingPrevName = tardis.getPreviousExteriorFacing().name().toUpperCase();
        String facingCurrName = tardis.getCurrentExteriorFacing().name().toUpperCase();
        String facingDestName = tardis.getDestinationExteriorFacing().name().toUpperCase();
        String dimPrevName = tardis.getPreviousExteriorDimension().getValue().getPath().toUpperCase();
        String dimCurrName = tardis.getCurrentExteriorDimension().getValue().getPath().toUpperCase();
        String dimDestName = tardis.getDestinationExteriorDimension().getValue().getPath().toUpperCase();

        this.printStringsToScreen(matrixStack, buffer, light, 0.002F, new String[]{
            this.buildScreenParamText("Flight", !flightSystem.isEnabled() ? NONE : flight),
            this.buildScreenParamText("Materialized", !materializationSystem.isEnabled() ? NONE : materialized),
            "",
            this.buildScreenParamText("Prev Position", !flightSystem.isEnabled() ? NONE : posPrevName),
            this.buildScreenParamText("Curr Position", !flightSystem.isEnabled() ? NONE : posCurrName),
            this.buildScreenParamText("Dest Position", !flightSystem.isEnabled() ? NONE : posDestName),
            "",
            this.buildScreenParamText("Prev Facing", !flightSystem.isEnabled() ? NONE : facingPrevName),
            this.buildScreenParamText("Curr Facing", !flightSystem.isEnabled() ? NONE : facingCurrName),
            this.buildScreenParamText("Dest Facing", !flightSystem.isEnabled() ? NONE : facingDestName),
            "",
            this.buildScreenParamText("Prev Dimension", !flightSystem.isEnabled() ? NONE : dimPrevName.replace("_", " ")),
            this.buildScreenParamText("Curr Dimension", !flightSystem.isEnabled() ? NONE : dimCurrName.replace("_", " ")),
            this.buildScreenParamText("Dest Dimension", !flightSystem.isEnabled() ? NONE : dimDestName.replace("_", " "))
        });
    }

    private void renderScreenPage2(TardisConsoleImperialBlockEntity tile, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay, TardisStateManager tardis) {
        String shieldsState = tardis.isShieldsEnabled() ? "ON" : "OFF";
        String artronEnergyHarvestingState = tardis.isEnergyArtronHarvesting() ? "ON" : "OFF";
        String forgeEnergyHarvestingState = tardis.isEnergyForgeHarvesting() ? "ON" : "OFF";

        this.printStringsToScreen(matrixStack, buffer, light, 0.002F, new String[]{
            this.buildScreenParamText("Shields", shieldsState),
            this.buildScreenParamText("Artron Energy Harvesting", artronEnergyHarvestingState),
            this.buildScreenParamText("Forge Energy Harvesting", forgeEnergyHarvestingState),
            "",
            this.buildScreenParamText("Artron Energy", tardis.getEnergyArtron() + " AE"),
            this.buildScreenParamText("Forge Energy", tardis.getEnergyForge() + " FE"),
        });
    }

    private void renderScrewdriver(TardisConsoleImperialBlockEntity tile, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay, float rotateDegrees) {
        if (tile.screwdriverItemStack != null && !tile.screwdriverItemStack.isEmpty()) {
            MinecraftClient mc = MinecraftClient.getInstance();
            float scale = 0.25F;

            matrixStack.push();
            matrixStack.translate(0.5, 1.25F, 0.5);
            matrixStack.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(180));
            matrixStack.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(180));
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotateDegrees));
            matrixStack.translate(0.5545F, 0.065F, 0.545F);
            matrixStack.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(30));
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(15));
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
            matrixStack.scale(scale, scale, scale);
            mc.getItemRenderer().renderItem(mc.player, tile.screwdriverItemStack, ModelTransformation.Mode.NONE, false, matrixStack, buffer, mc.world, light, overlay, 0);
            matrixStack.pop();
        }
    }

    private String buildScreenParamText(String title, String appendInput) {
        int substring = 16;
        String append = appendInput.substring(0, Math.min(substring, appendInput.length()));
        append += appendInput.length() > append.length() ? "..." : "";

        int lineWidth = 239;
        TextRenderer textRenderer = this.ctx.getTextRenderer();
        String str = title + ": " + append;

        return title + ": " + " ".repeat((lineWidth - textRenderer.getWidth(str)) / textRenderer.getWidth(" ")) + append;
    }

    private void printStringsToScreen(MatrixStack matrixStack, VertexConsumerProvider buffer, int light, float scaling, String[] lines) {
        TextRenderer textRenderer = this.ctx.getTextRenderer();

        matrixStack.push();
        matrixStack.translate(-0.255D, 0.095F, 0.765D);
        matrixStack.scale(scaling, -scaling, scaling);
        matrixStack.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(1.31F));

        for (int i = 0; i < lines.length; i++) {
            textRenderer.draw(
                lines[i],
                0,
                i * textRenderer.fontHeight,
                0xFFFFFF,
                true,
                matrixStack.peek().getPositionMatrix(),
                buffer,
                false,
                0,
                240
            );
        }

        matrixStack.pop();
    }
}

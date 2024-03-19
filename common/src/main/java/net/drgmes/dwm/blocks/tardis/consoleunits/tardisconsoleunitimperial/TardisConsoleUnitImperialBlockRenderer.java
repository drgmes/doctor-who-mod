package net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlock;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockRenderer;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial.models.TardisConsoleUnitImperialModel;
import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlRole;
import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlRoleType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class TardisConsoleUnitImperialBlockRenderer extends BaseTardisConsoleUnitBlockRenderer<TardisConsoleUnitImperialBlockEntity> {
    public TardisConsoleUnitImperialBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(TardisConsoleUnitImperialBlockEntity tile, float delta, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        ModelPart modelRoot = this.ctx.getLayerModelPart(TardisConsoleUnitImperialModel.LAYER_LOCATION);
        TardisConsoleUnitImperialModel model = new TardisConsoleUnitImperialModel(modelRoot);
        VertexConsumer vertexConsumer = buffer.getBuffer(model.getLayer(TardisConsoleUnitImperialModel.LAYER_LOCATION.getId()));
        model.setupAnim(tile);

        float rotateDegrees = tile.getCachedState().get(BaseTardisConsoleUnitBlock.FACING).asRotation();
        float scale = 0.4F;

        matrixStack.push();
        matrixStack.translate(0.5, 0.7, 0.5);
        matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotateDegrees));
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0, 0.25F, 0);
        this.animate(tile, modelRoot, delta);
        model.render(matrixStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();

        this.renderScreen(tile, matrixStack, buffer, rotateDegrees);
        this.renderSonicScrewdriver(tile, matrixStack, buffer, light, overlay, rotateDegrees);
    }

    @Override
    protected void activateLever(ModelPart model, boolean value, ETardisConsoleUnitControlRole controlRole, float delta) {
        if (value) model.pitch -= 1.25F;
    }

    @Override
    protected void activateLever(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta) {
        if (controlRole.type == ETardisConsoleUnitControlRoleType.BOOLEAN || controlRole.type == ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT) {
            this.activateLever(model, value != 0, controlRole, delta);
            return;
        }

        model.pitch -= (1F / (controlRole.maxIntValue - 1)) * value;
    }

    @Override
    protected void animateLever(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta) {
        this.activateLever(model, value, controlRole, delta);
    }

    @Override
    protected void activateButton(ModelPart model, boolean value, ETardisConsoleUnitControlRole controlRole, float delta) {
        if (value) model.pivotY += 0.5F;
    }

    @Override
    protected void activateButton(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta) {
        this.activateButton(model, value != 0, controlRole, delta);
    }

    @Override
    protected void animateButton(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta) {
        this.activateButton(model, value, controlRole, delta);
    }

    @Override
    protected void activateSlider(ModelPart model, boolean value, ETardisConsoleUnitControlRole controlRole, float delta) {
        if (value) model.pivotZ -= 4F;
    }

    @Override
    protected void activateSlider(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta) {
        model.pivotZ -= value * (4F / (controlRole.maxIntValue > 0 ? controlRole.maxIntValue : 1));
    }

    @Override
    protected void animateSlider(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta) {
        this.activateSlider(model, value, controlRole, delta);
    }

    @Override
    protected void activateRotator(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta) {
        model.yaw += 1.57F * value;
    }

    @Override
    protected void animateRotator(ModelPart model, int value, ETardisConsoleUnitControlRole controlRole, float delta) {
        if (value != 0) this.activateRotator(model, value / 2, controlRole, delta);
    }

    @Override
    protected void activateHandbrake(ModelPart model, ETardisConsoleUnitControlRole controlRole) {
        model.yaw -= 1.55F;
    }

    @Override
    protected void activateStarter(ModelPart model, ETardisConsoleUnitControlRole controlRole) {
        model.pitch += 2F;
    }

    @Override
    protected void animateFuelIndicator(TardisConsoleUnitImperialBlockEntity tile, ModelPart modelRoot, float delta) {
        if (tile.tardis.getFuelAmount() <= 0) return;
        ModelPart model = this.getModelPart(modelRoot, "controls/control_indicators/control_indicator_1$_arrow_r1");
        model.yaw += tile.tardis.getFuelAmount() / (float) tile.tardis.getFuelCapacity() * 2.075F;
    }

    @Override
    protected void animateEnergyIndicator(TardisConsoleUnitImperialBlockEntity tile, ModelPart modelRoot, float delta) {
        if (tile.tardis.getEnergyAmount() <= 0) return;
        ModelPart model = this.getModelPart(modelRoot, "controls/control_indicators/control_indicator_2$_arrow_r1");
        model.yaw += tile.tardis.getEnergyAmount() / (float) tile.tardis.getEnergyCapacity() * 2.075F;
    }

    @Override
    protected void printStringsToScreen(MatrixStack matrixStack, VertexConsumerProvider buffer, String[] lines) {
        float scaling = 0.002F;
        matrixStack.push();
        matrixStack.translate(-0.255D, 0.095F, 0.765D);
        matrixStack.scale(scaling, -scaling, scaling);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(1.31F));
        super.printStringsToScreen(matrixStack, buffer, lines);
        matrixStack.pop();
    }

    private void renderScreen(TardisConsoleUnitImperialBlockEntity tile, MatrixStack matrixStack, VertexConsumerProvider buffer, float rotateDegrees) {
        matrixStack.push();
        matrixStack.translate(0.5, 1.015F, 0.5);
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(rotateDegrees + 60));
        super.renderScreen(tile, matrixStack, buffer);
        matrixStack.pop();
    }

    private void renderSonicScrewdriver(TardisConsoleUnitImperialBlockEntity tile, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay, float rotateDegrees) {
        if (tile.sonicScrewdriverItemStack != null && !tile.sonicScrewdriverItemStack.isEmpty()) {
            MinecraftClient mc = MinecraftClient.getInstance();
            float scale = 0.25F;

            matrixStack.push();
            matrixStack.translate(0.5, 1.25F, 0.5);
            matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180));
            matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotateDegrees));
            matrixStack.translate(0.5545F, 0.065F, 0.545F);
            matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(30));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(15));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
            matrixStack.scale(scale, scale, scale);
            mc.getItemRenderer().renderItem(mc.player, tile.sonicScrewdriverItemStack, ModelTransformationMode.NONE, false, matrixStack, buffer, mc.world, light, overlay, 0);
            matrixStack.pop();
        }
    }
}

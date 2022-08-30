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
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;

public class TardisConsoleUnitImperialBlockRenderer extends BaseTardisConsoleUnitBlockRenderer<TardisConsoleUnitImperialBlockEntity> {
    public TardisConsoleUnitImperialBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(TardisConsoleUnitImperialBlockEntity tile, float delta, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        float rotateDegrees = tile.getCachedState().get(BaseTardisConsoleUnitBlock.FACING).asRotation();
        EntityModelLayer modelLayer = TardisConsoleUnitImperialModel.LAYER_LOCATION;
        ModelPart modelRoot = this.ctx.getLayerRenderDispatcher().getModelPart(modelLayer);
        TardisConsoleUnitImperialModel model = new TardisConsoleUnitImperialModel(modelRoot);
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

        this.renderScreen(tile, matrixStack, buffer, rotateDegrees);
        this.renderScrewdriver(tile, matrixStack, buffer, light, overlay, rotateDegrees);
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
    protected void printStringsToScreen(MatrixStack matrixStack, VertexConsumerProvider buffer, String[] lines) {
        float scaling = 0.002F;
        matrixStack.push();
        matrixStack.translate(-0.255D, 0.095F, 0.765D);
        matrixStack.scale(scaling, -scaling, scaling);
        matrixStack.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(1.31F));
        super.printStringsToScreen(matrixStack, buffer, lines);
        matrixStack.pop();
    }

    private void renderScreen(TardisConsoleUnitImperialBlockEntity tile, MatrixStack matrixStack, VertexConsumerProvider buffer, float rotateDegrees) {
        matrixStack.push();
        matrixStack.translate(0.5, 1F, 0.5);
        matrixStack.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(rotateDegrees + 60));
        super.renderScreen(tile, matrixStack, buffer);
        matrixStack.pop();
    }

    private void renderScrewdriver(TardisConsoleUnitImperialBlockEntity tile, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay, float rotateDegrees) {
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
}

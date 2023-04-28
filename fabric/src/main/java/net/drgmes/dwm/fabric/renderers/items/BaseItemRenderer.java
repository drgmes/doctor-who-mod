package net.drgmes.dwm.fabric.renderers.items;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

import java.util.function.Function;

public abstract class BaseItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    public final Function<ModelPart, Model> modelBuilder;
    public final EntityModelLayer entityModelLayer;

    public BaseItemRenderer(Function<ModelPart, Model> modelBuilder, EntityModelLayer entityModelLayer) {
        this.modelBuilder = modelBuilder;
        this.entityModelLayer = entityModelLayer;
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        Model model = this.modelBuilder.apply(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(this.entityModelLayer));
        VertexConsumer vertexConsumer = buffer.getBuffer(model.getLayer(this.entityModelLayer.getId()));

        matrixStack.push();
        matrixStack.translate(0, 1.5, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
        model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
        this.customRender(matrixStack, vertexConsumer, model, light, overlay);
        matrixStack.pop();
    }

    public void customRender(MatrixStack matrixStack, VertexConsumer vertexConsumer, Model model, int light, int overlay) {
    }
}

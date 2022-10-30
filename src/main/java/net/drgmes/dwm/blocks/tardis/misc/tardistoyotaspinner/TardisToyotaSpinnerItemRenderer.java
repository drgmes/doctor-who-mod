package net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner;

import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.models.TardisToyotaSpinnerModel;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;

public class TardisToyotaSpinnerItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    @Override
    public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        EntityModelLayer modelLayer = TardisToyotaSpinnerModel.LAYER_LOCATION;
        TardisToyotaSpinnerModel model = new TardisToyotaSpinnerModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(modelLayer));
        VertexConsumer vertexConsumer = buffer.getBuffer(model.getLayer(modelLayer.getId()));

        float scale = 3F;
        matrixStack.push();
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0, 1.85F, 0);
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
        model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
        matrixStack.pop();
    }
}

package net.drgmes.dwm.blocks.tardis.consoles.tardisconsoleimperial;

import net.drgmes.dwm.blocks.tardis.consoles.tardisconsoleimperial.models.TardisConsoleImperialModel;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;

public class TardisConsoleImperialItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    @Override
    public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrixStack, VertexConsumerProvider buffer, int light, int overlay) {
        EntityModelLayer modelLayer = TardisConsoleImperialModel.LAYER_LOCATION;
        TardisConsoleImperialModel model = new TardisConsoleImperialModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(modelLayer));
        VertexConsumer vertexConsumer = buffer.getBuffer(model.getLayer(modelLayer.getId()));

        matrixStack.push();
        matrixStack.translate(0, 1.5, 0);
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
        model.render(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
        matrixStack.pop();
    }
}
package net.drgmes.dwm.entities.tardis.consoleunit.controls;

import net.drgmes.dwm.enums.TardisConsoleUnitControlRole;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class TardisConsoleControlEntityRenderer extends EntityRenderer<TardisConsoleControlEntity> {
    public TardisConsoleControlEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(TardisConsoleControlEntity entity) {
        return null;
    }

    @Override
    protected void renderLabelIfPresent(TardisConsoleControlEntity entity, Text text, MatrixStack matrixStack, VertexConsumerProvider buffer, int light) {
        if (this.dispatcher.getSquaredDistanceToCamera(entity) > 6) return;

        TardisConsoleUnitControlRole controlRole = entity.getTardisControlRole();
        if (controlRole.name == null) return;

        float scale = 0.0055F;
        float backgroundOpacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
        int backgroundColor = (int) (backgroundOpacity * 255.0F) << 24;

        matrixStack.push();
        matrixStack.translate(0, entity.getNameLabelHeight() * 0.425F, 0);
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.scale(-scale, -scale, scale);

        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        TextRenderer textRenderer = this.getTextRenderer();
        float textWidth = textRenderer.getWidth(text);
        float x = textWidth / -2F;

        textRenderer.draw(text, x, 0, 553648127, false, matrix4f, buffer, TextRenderer.TextLayerType.SEE_THROUGH, backgroundColor, light);
        textRenderer.draw(text, x, 0, -1, false, matrix4f, buffer, TextRenderer.TextLayerType.NORMAL, 0, light);

        matrixStack.pop();
    }
}

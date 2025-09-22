package net.drgmes.dwm.utils.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import org.joml.Matrix4f;
import org.joml.Vector2i;

import java.util.List;

public class RenderHelper {
    public static ButtonWidget getButtonWidget(int x, int y, int width, int height, Text message, ButtonWidget.PressAction onPress) {
        return ButtonWidget.builder(message, onPress).size(width, height).position(x, y).build();
    }

    public static boolean checkMouseInboundPosition(double mouseX, double mouseY, Vector2i pos, Vector2i size) {
        int x = pos.x + size.x;
        int y = pos.y + size.y;
        return (mouseX >= pos.x && mouseX <= x) && (mouseY >= pos.y && mouseY <= y);
    }

    public static void drawTextClipped(Text text, TextRenderer textRenderer, DrawContext context, Vector2i pos, int maxTextLength, int color) {
        List<OrderedText> lines = Language.getInstance().reorder(textRenderer.getTextHandler().wrapLines(text, maxTextLength, Style.EMPTY));
        context.drawText(textRenderer, lines.get(0), pos.x, pos.y, color, true);
    }

    public static Vector2i drawTextMultiline(Text text, TextRenderer textRenderer, DrawContext context, Vector2i pos, int lineHeight, int maxTextLength, int color) {
        List<OrderedText> lines = Language.getInstance().reorder(textRenderer.getTextHandler().wrapLines(text, maxTextLength, Style.EMPTY));

        int offsetY = 0;
        for (OrderedText line : lines) {
            context.drawText(textRenderer, line, pos.x, pos.y + offsetY, color, true);
            offsetY += lineHeight;
        }

        return new Vector2i(0, offsetY);
    }

    public static void drawRectangle(MatrixStack matrixStack, VertexConsumer vertexConsumer, float x1, float y1, float x2, float y2, int color) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        vertexConsumer.vertex(matrix, x1, y1, 0).color(color);
        vertexConsumer.vertex(matrix, x1, y2, 0).color(color);
        vertexConsumer.vertex(matrix, x2, y2, 0).color(color);
        vertexConsumer.vertex(matrix, x2, y1, 0).color(color);
    }

    public static void drawTessellatorRectangle(MatrixStack matrixStack, float x1, float y1, float x2, float y2, int color) {
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        builder.vertex(matrix, x1, y1, 0f).color(color);
        builder.vertex(matrix, x1, y2, 0f).color(color);
        builder.vertex(matrix, x2, y2, 0f).color(color);
        builder.vertex(matrix, x2, y1, 0f).color(color);

        BufferRenderer.drawWithGlobalProgram(builder.end());
        RenderSystem.disableBlend();
    }
}

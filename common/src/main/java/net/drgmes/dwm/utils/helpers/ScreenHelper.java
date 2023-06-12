package net.drgmes.dwm.utils.helpers;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import net.minecraft.util.math.Vec2f;

import java.util.List;

public class ScreenHelper {
    public static ButtonWidget getButtonWidget(int x, int y, int width, int height, Text message, ButtonWidget.PressAction onPress) {
        return ButtonWidget.builder(message, onPress).size(width, height).position(x, y).build();
    }

    public static boolean checkMouseInboundPosition(double mouseX, double mouseY, Vec2f pos, Vec2f size) {
        final float x = pos.x + size.x;
        final float y = pos.y + size.y;

        return (mouseX >= pos.x && mouseX <= x) && (mouseY >= pos.y && mouseY <= y);
    }

    public static void drawText(OrderedText text, TextRenderer textRenderer, DrawContext context, float x, float y, int color, boolean shadow) {
        textRenderer.draw(text, x, y, color, shadow, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers(), TextRenderer.TextLayerType.SEE_THROUGH, 0, 240);
    }

    public static void drawText(Text text, TextRenderer textRenderer, DrawContext context, float x, float y, int color, boolean shadow) {
        drawText(text.asOrderedText(), textRenderer, context, x, y, color, shadow);
    }

    public static void drawTextClipped(Text text, TextRenderer textRenderer, DrawContext context, Vec2f pos, int maxTextLength, int color) {
        List<OrderedText> lines = Language.getInstance().reorder(textRenderer.getTextHandler().wrapLines(text, maxTextLength, Style.EMPTY));
        drawText(lines.get(0), textRenderer, context, pos.x, pos.y, color, true);
    }

    public static Vec2f drawTextMultiline(Text text, TextRenderer textRenderer, DrawContext context, Vec2f pos, int lineHeight, int maxTextLength, int color) {
        List<OrderedText> lines = Language.getInstance().reorder(textRenderer.getTextHandler().wrapLines(text, maxTextLength, Style.EMPTY));

        float offsetY = 0;
        for (OrderedText line : lines) {
            drawText(line, textRenderer, context, pos.x, pos.y + offsetY, color, true);
            offsetY += lineHeight;
        }

        return new Vec2f(0, offsetY);
    }
}

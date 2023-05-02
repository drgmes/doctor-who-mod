package net.drgmes.dwm.utils.helpers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
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

    public static void drawClipped(MinecraftClient mc, MatrixStack matrixStack, Text text, Vec2f pos, int lineHeight, int maxTextLength, int color) {
        List<OrderedText> lines = Language.getInstance().reorder(mc.textRenderer.getTextHandler().wrapLines(text, maxTextLength, Style.EMPTY));
        OrderedText clippedText = lines.get(0);
        mc.textRenderer.drawWithShadow(matrixStack, clippedText, pos.x, pos.y, color);
    }

    public static Vec2f drawMultiline(MinecraftClient mc, MatrixStack matrixStack, Text text, Vec2f pos, int lineHeight, int maxTextLength, int color) {
        List<OrderedText> lines = Language.getInstance().reorder(mc.textRenderer.getTextHandler().wrapLines(text, maxTextLength, Style.EMPTY));

        float offsetY = 0;
        for (OrderedText line : lines) {
            mc.textRenderer.drawWithShadow(matrixStack, line, pos.x, pos.y + offsetY, color);
            offsetY += lineHeight;
        }

        return new Vec2f(0, offsetY);
    }
}

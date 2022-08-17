package net.drgmes.dwm.utils.helpers;

import net.minecraft.util.math.Vec2f;

public class ScreenHelper {
    public static boolean checkMouseInboundPosition(double mouseX, double mouseY, Vec2f pos, Vec2f size) {
        final float x = pos.x + size.x;
        final float y = pos.y + size.y;

        return (mouseX >= pos.x && mouseX <= x) && (mouseY >= pos.y && mouseY <= y);
    }
}

package net.drgmes.dwm.utils.helpers;

import net.minecraft.world.phys.Vec2;

public class ScreenHelper {
    public static boolean checkMouseInboundPosition(double mouseX, double mouseY, Vec2 pos, Vec2 size) {
        final float x = pos.x + size.x;
        final float y = pos.y + size.y;

        return (mouseX >= pos.x && mouseX <= x) && (mouseY >= pos.y && mouseY <= y);
    }
}

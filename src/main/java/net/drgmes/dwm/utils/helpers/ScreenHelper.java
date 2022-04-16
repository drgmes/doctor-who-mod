package net.drgmes.dwm.utils.helpers;

public class ScreenHelper {
    public static boolean checkMouseInboundPosition(double mouseX, double mouseY, int[] pos, int[] sizes) {
        final int x2 = pos[0] + sizes[0];
        final int y2 = pos[1] + sizes[1];

        return (mouseX >= pos[0] && mouseX <= x2) && (mouseY >= pos[1] && mouseY <= y2);
    }
}

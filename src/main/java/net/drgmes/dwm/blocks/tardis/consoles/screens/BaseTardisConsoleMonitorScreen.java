package net.drgmes.dwm.blocks.tardis.consoles.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

public abstract class BaseTardisConsoleMonitorScreen extends BaseTardisConsoleScreen {
    public BaseTardisConsoleMonitorScreen(Text title, BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
        super(title, tardisConsoleBlockEntity);
    }

    @Override
    public Identifier getBackground() {
        return DWM.TEXTURES.GUI.TARDIS.CONSOLE.MONITOR;
    }

    @Override
    public Vec2f getBackgroundSize() {
        return DWM.TEXTURES.GUI.TARDIS.CONSOLE.MONITOR_SIZE.multiply(0.795F);
    }
}

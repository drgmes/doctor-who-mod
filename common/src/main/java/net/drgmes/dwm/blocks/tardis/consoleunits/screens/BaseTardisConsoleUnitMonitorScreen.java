package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

public abstract class BaseTardisConsoleUnitMonitorScreen extends BaseTardisConsoleUnitScreen {
    public BaseTardisConsoleUnitMonitorScreen(Text title, BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
        super(title, tardisConsoleUnitBlockEntity);
    }

    @Override
    public Identifier getBackground() {
        return DWM.TEXTURES.GUI.TARDIS.CONSOLE.MONITOR;
    }

    @Override
    public Vector2i getBackgroundSize() {
        return DWM.TEXTURES.GUI.TARDIS.CONSOLE.MONITOR_SIZE.div(1 / 0.795F, new Vector2i());
    }
}

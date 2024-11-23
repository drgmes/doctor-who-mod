package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.utils.base.screens.BaseScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public abstract class BaseTardisConsoleUnitScreen extends BaseScreen {
    protected final BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity;

    public BaseTardisConsoleUnitScreen(Text title, BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
        super(title);
        this.tardisConsoleUnitBlockEntity = tardisConsoleUnitBlockEntity;
    }

    @Override
    public Vector2i getTitleRenderPos() {
        return this.getRenderPos(23, 8);
    }

    protected void apply() {
        this.close();
    }
}

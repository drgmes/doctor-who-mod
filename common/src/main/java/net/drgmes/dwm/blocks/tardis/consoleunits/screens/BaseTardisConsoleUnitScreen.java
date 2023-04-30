package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.utils.base.screens.BaseScreen;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;

public abstract class BaseTardisConsoleUnitScreen extends BaseScreen {
    protected final BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity;

    public BaseTardisConsoleUnitScreen(Text title, BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
        super(title);
        this.tardisConsoleUnitBlockEntity = tardisConsoleUnitBlockEntity;
    }

    @Override
    public Vec2f getTitleRenderPos() {
        return this.getRenderPos(23, 8);
    }

    @Override
    public void close() {
        this.tardisConsoleUnitBlockEntity.markDirty();
        super.close();
    }

    protected void apply() {
        this.close();
    }
}
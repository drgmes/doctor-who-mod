package net.drgmes.dwm.blocks.tardis.consoles.screens;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.utils.base.screens.BaseScreen;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;

public abstract class BaseTardisConsoleScreen extends BaseScreen {
    protected final BaseTardisConsoleBlockEntity tardisConsoleBlockEntity;

    public BaseTardisConsoleScreen(Text title, BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
        super(title);
        this.tardisConsoleBlockEntity = tardisConsoleBlockEntity;
    }

    @Override
    public Vec2f getTitleRenderPos() {
        return this.getRenderPos(23, 8);
    }

    @Override
    public void close() {
        this.tardisConsoleBlockEntity.markDirty();
        super.close();
    }

    protected void apply() {
        this.close();
    }
}

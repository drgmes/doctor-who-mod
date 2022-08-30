package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

public abstract class BaseTardisConsoleUnitTelepathicInterfaceScreen extends BaseTardisConsoleUnitScreen {
    protected static final int LINE_PADDING = 3;

    protected ButtonWidget acceptButton;
    protected ButtonWidget cancelButton;

    public BaseTardisConsoleUnitTelepathicInterfaceScreen(Text title, BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
        super(title, tardisConsoleUnitBlockEntity);
    }

    @Override
    public Identifier getBackground() {
        return DWM.TEXTURES.GUI.TARDIS.CONSOLE.TELEPATHIC_INTERFACE;
    }

    @Override
    public Vec2f getBackgroundSize() {
        return DWM.TEXTURES.GUI.TARDIS.CONSOLE.TELEPATHIC_INTERFACE_SIZE.multiply(0.795F);
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = (int) (this.getBackgroundSize().x - BACKGROUND_BORDERS * 2) / 2 - 1;
        int buttonOffset = (int) (this.getBackgroundSize().y - BACKGROUND_BORDERS - BUTTON_HEIGHT - 1);

        Vec2f cancelButtonPos = this.getRenderPos(BACKGROUND_BORDERS + 1, buttonOffset);
        this.cancelButton = new ButtonWidget((int) cancelButtonPos.x, (int) cancelButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.TELEPATHIC_INTERFACE_BTN_CANCEL, (b) -> this.close());

        Vec2f acceptButtonPos = this.getRenderPos(BACKGROUND_BORDERS + buttonWidth + 2, buttonOffset);
        this.acceptButton = new ButtonWidget((int) acceptButtonPos.x, (int) acceptButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.TELEPATHIC_INTERFACE_BTN_ACCEPT, (b) -> this.apply());

        this.addDrawableChild(this.cancelButton);
        this.addDrawableChild(this.acceptButton);
    }
}

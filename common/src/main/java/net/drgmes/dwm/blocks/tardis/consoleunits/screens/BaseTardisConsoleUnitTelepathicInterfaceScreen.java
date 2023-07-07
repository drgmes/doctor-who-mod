package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.utils.helpers.RenderHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

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
    public Vector2i getBackgroundSize() {
        return DWM.TEXTURES.GUI.TARDIS.CONSOLE.TELEPATHIC_INTERFACE_SIZE.div(1 / 0.795F, new Vector2i());
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = this.getBackgroundSize().x / 2 - this.getBackgroundBorderSize().x - 1;
        int buttonOffset = this.getBackgroundSize().y - this.getBackgroundBorderSize().y - BUTTON_HEIGHT - 1;

        Vector2i cancelButtonPos = this.getRenderPos(this.getBackgroundBorderSize().x + 1, buttonOffset);
        this.cancelButton = RenderHelper.getButtonWidget(cancelButtonPos.x, cancelButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.TELEPATHIC_INTERFACE_BTN_CANCEL, (b) -> this.close());

        Vector2i acceptButtonPos = this.getRenderPos(this.getBackgroundBorderSize().x + buttonWidth + 2, buttonOffset);
        this.acceptButton = RenderHelper.getButtonWidget(acceptButtonPos.x, acceptButtonPos.y, buttonWidth, BUTTON_HEIGHT, DWM.TEXTS.TELEPATHIC_INTERFACE_BTN_ACCEPT, (b) -> this.apply());

        this.addDrawableChild(this.cancelButton);
        this.addDrawableChild(this.acceptButton);
    }
}

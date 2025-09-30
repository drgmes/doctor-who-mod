package net.drgmes.dwm.blocks.tardis.consoleunits.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.utils.base.screens.elements.BaseButton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public abstract class BaseTardisConsoleUnitTelepathicInterfaceScreen extends BaseTardisConsoleUnitScreen {
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
    public Vector2i getBackgroundOriginSize() {
        return DWM.TEXTURES.GUI.TARDIS.CONSOLE.TELEPATHIC_INTERFACE_SIZE;
    }

    @Override
    protected void init() {
        super.init();

        Vector2i acceptButtonPos = this.getRightBottomRenderPos(BUTTON_SIZE + SCREEN_MARGIN, BUTTON_SIZE + SCREEN_MARGIN);
        this.acceptButton = new BaseButton(acceptButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.TELEPATHIC_INTERFACE_BTN_ACCEPT, DWM.TEXTURES.GUI.COMMON.ELEMENTS.ACCEPT, (b) -> {
            this.apply();
        });

        Vector2i cancelButtonPos = this.getLeftBottomRenderPos(SCREEN_MARGIN, BUTTON_SIZE + SCREEN_MARGIN);
        this.cancelButton = new BaseButton(cancelButtonPos, BUTTON_SIZE, BUTTON_PADDING, DWM.TEXTS.TELEPATHIC_INTERFACE_BTN_CANCEL, DWM.TEXTURES.GUI.COMMON.ELEMENTS.CANCEL, (b) -> {
            this.back();
        });

        this.addDrawableChild(this.acceptButton);
        this.addDrawableChild(this.cancelButton);
    }
}

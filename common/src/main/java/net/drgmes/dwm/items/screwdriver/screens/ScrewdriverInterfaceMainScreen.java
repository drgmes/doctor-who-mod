package net.drgmes.dwm.items.screwdriver.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.network.server.ScrewdriverUpdatePacket;
import net.drgmes.dwm.utils.helpers.ScreenHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;

public class ScrewdriverInterfaceMainScreen extends BaseScrewdriverInterfaceScreen {
    protected ButtonWidget modeButton;

    public ScrewdriverInterfaceMainScreen(ItemStack screwdriverItemStack, boolean isMainHand) {
        super(screwdriverItemStack, isMainHand);
    }

    @Override
    protected void init() {
        int buttonWidth = (int) (this.getBackgroundSize().x - BACKGROUND_BORDERS * 2 - PADDING * 2);
        int buttonOffset = BACKGROUND_BORDERS + PADDING;

        Vec2f modeButtonPos = this.getRenderPos(buttonOffset, buttonOffset);
        this.modeButton = ScreenHelper.getButtonWidget((int) modeButtonPos.x, (int) modeButtonPos.y, buttonWidth, BUTTON_HEIGHT, this.getModeButtonTitle(), (btn) -> {
            this.mode = this.mode.next();
            Screwdriver.setInteractionMode(this.screwdriverItemStack, this.mode);
            btn.setMessage(this.getModeButtonTitle());
            this.apply();
        });

        this.addDrawableChild(this.modeButton);
        super.init();
    }

    private Text getModeButtonTitle() {
        return DWM.TEXTS.SCREWDRIVER_INTERFACE_BTN_MODE.apply(this.mode);
    }

    private void apply() {
        new ScrewdriverUpdatePacket(this.screwdriverItemStack, this.isMainHand).sendToServer();
    }
}

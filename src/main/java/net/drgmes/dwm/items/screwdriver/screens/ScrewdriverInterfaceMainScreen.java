package net.drgmes.dwm.items.screwdriver.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.network.ServerboundScrewdriverUpdatePacket;
import net.drgmes.dwm.setup.ModPackets;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;

public class ScrewdriverInterfaceMainScreen extends BaseScrewdriverInterfaceScreen {
    protected Button modeButton;

    public ScrewdriverInterfaceMainScreen(ItemStack screwdriverItemStack, boolean isMainHand) {
        super(screwdriverItemStack, isMainHand);
    }

    @Override
    protected void init() {
        int buttonWidth = (int) (this.getBackgroundSize().x - BACKGROUND_BORDERS * 2 - PADDING * 2);
        int buttonOffset = (int) (BACKGROUND_BORDERS + PADDING);

        Vec2 modeButtonPos = this.getRenderPos(BACKGROUND_BORDERS + PADDING, buttonOffset);
        this.modeButton = new Button((int) modeButtonPos.x, (int) modeButtonPos.y, buttonWidth, BUTTON_HEIGHT, this.getModeButtonTitle(), (btn) -> {
            this.mode = this.mode.next();
            Screwdriver.setInteractionMode(this.screwdriverItemStack, this.mode);
            btn.setMessage(this.getModeButtonTitle());
            this.apply();
        });

        this.addRenderableWidget(this.modeButton);
        super.init();
    }

    private Component getModeButtonTitle() {
        return DWM.TEXTS.SCREWDRIVER_INTERFACE_BTN_MODE.apply(this.mode);
    }

    private void apply() {
        ModPackets.INSTANCE.sendToServer(new ServerboundScrewdriverUpdatePacket(this.screwdriverItemStack, this.isMainHand));
    }
}

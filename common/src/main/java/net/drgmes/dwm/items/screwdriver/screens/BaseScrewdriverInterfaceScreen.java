package net.drgmes.dwm.items.screwdriver.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.common.screwdriver.Screwdriver.EScrewdriverMode;
import net.drgmes.dwm.utils.base.screens.BaseScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

public abstract class BaseScrewdriverInterfaceScreen extends BaseScreen {
    protected static final int LINE_PADDING = 3;

    protected final ItemStack screwdriverItemStack;
    protected final boolean isMainHand;

    protected EScrewdriverMode mode;

    public BaseScrewdriverInterfaceScreen(Text title, ItemStack screwdriverItemStack, boolean isMainHand) {
        super(title);

        this.isMainHand = isMainHand;
        this.screwdriverItemStack = screwdriverItemStack;
        this.mode = Screwdriver.getInteractionMode(this.screwdriverItemStack);
    }

    @Override
    public Identifier getBackground() {
        return DWM.TEXTURES.GUI.SCREWDRIVER.INTERFACE_MAIN;
    }

    @Override
    public Vec2f getBackgroundSize() {
        return DWM.TEXTURES.GUI.SCREWDRIVER.INTERFACE_MAIN_SIZE.multiply(0.795F);
    }

    @Override
    public Vec2f getTitleRenderPos() {
        return this.getRenderPos(23, 8);
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return true;
    }
}

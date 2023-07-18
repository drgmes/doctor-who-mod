package net.drgmes.dwm.items.sonicscrewdriver.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicscrewdriver.SonicScrewdriver;
import net.drgmes.dwm.common.sonicscrewdriver.SonicScrewdriver.EMode;
import net.drgmes.dwm.utils.base.screens.BaseScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

public abstract class BaseSonicScrewdriverInterfaceScreen extends BaseScreen {
    protected static final int LINE_PADDING = 3;

    protected final ItemStack sonicScrewdriverItemStack;
    protected final boolean isMainHand;

    protected EMode mode;

    public BaseSonicScrewdriverInterfaceScreen(Text title, ItemStack sonicScrewdriverItemStack, boolean isMainHand) {
        super(title);

        this.isMainHand = isMainHand;
        this.sonicScrewdriverItemStack = sonicScrewdriverItemStack;
        this.mode = SonicScrewdriver.getInteractionMode(this.sonicScrewdriverItemStack);
    }

    @Override
    public Identifier getBackground() {
        return DWM.TEXTURES.GUI.SONIC_SCREWDRIVER.INTERFACE_MAIN;
    }

    @Override
    public Vector2i getBackgroundSize() {
        return DWM.TEXTURES.GUI.SONIC_SCREWDRIVER.INTERFACE_MAIN_SIZE.div(1 / 0.795F, new Vector2i());
    }

    @Override
    public Vector2i getTitleRenderPos() {
        return this.getRenderPos(23, 8);
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return true;
    }
}

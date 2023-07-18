package net.drgmes.dwm.items.sonicdevices.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.common.sonicdevice.SonicDevice.EMode;
import net.drgmes.dwm.utils.base.screens.BaseScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

public abstract class BaseSonicDeviceInterfaceScreen extends BaseScreen {
    protected static final int LINE_PADDING = 3;

    protected final ItemStack sonicDeviceItemStack;
    protected final String slot;

    protected EMode mode;

    public BaseSonicDeviceInterfaceScreen(Text title, ItemStack sonicDeviceItemStack, String slot) {
        super(title);

        this.mode = SonicDevice.getInteractionMode(sonicDeviceItemStack);
        this.sonicDeviceItemStack = sonicDeviceItemStack;
        this.slot = slot;
    }

    @Override
    public Identifier getBackground() {
        return DWM.TEXTURES.GUI.SONIC_DEVICE.INTERFACE_MAIN;
    }

    @Override
    public Vector2i getBackgroundSize() {
        return DWM.TEXTURES.GUI.SONIC_DEVICE.INTERFACE_MAIN_SIZE.div(1 / 0.795F, new Vector2i());
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

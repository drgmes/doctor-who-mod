package net.drgmes.dwm.items.sonicdevices.screens;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.enums.SonicDeviceMode;
import net.drgmes.dwm.utils.base.screens.BaseScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;

@Environment(EnvType.CLIENT)
public abstract class BaseSonicDeviceInterfaceScreen extends BaseScreen {
    protected static final int LINE_PADDING = 3;

    protected final ItemStack sonicDeviceItemStack;
    protected final String slot;

    protected SonicDeviceMode mode;

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
    public Vector2i getBackgroundOriginSize() {
        return DWM.TEXTURES.GUI.SONIC_DEVICE.INTERFACE_MAIN_SIZE;
    }

    @Override
    public boolean shouldCloseOnInventoryKey() {
        return true;
    }
}

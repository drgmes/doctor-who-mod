package net.drgmes.dwm.enums;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicdevice.modes.BaseSonicDeviceMode;
import net.drgmes.dwm.common.sonicdevice.modes.scan.SonicDeviceScanMode;
import net.drgmes.dwm.common.sonicdevice.modes.setting.SonicDeviceSettingMode;
import net.drgmes.dwm.common.sonicdevice.modes.tardis.SonicDeviceTardisMode;
import net.minecraft.text.Text;

public enum SonicDeviceMode {
    SCAN(SonicDeviceScanMode.INSTANCE, DWM.TEXTS.SONIC_DEVICE_MODE_SCAN, DWM.TEXTS.SONIC_DEVICE_MODE_SCAN_DESCRIPTION),
    SETTING(SonicDeviceSettingMode.INSTANCE, DWM.TEXTS.SONIC_DEVICE_MODE_SETTING, DWM.TEXTS.SONIC_DEVICE_MODE_SETTING_DESCRIPTION),
    TARDIS_RELOCATION(SonicDeviceTardisMode.INSTANCE, DWM.TEXTS.SONIC_DEVICE_MODE_TARDIS_RELOCATION, DWM.TEXTS.SONIC_DEVICE_MODE_TARDIS_RELOCATION_DESCRIPTION);

    private final BaseSonicDeviceMode mode;
    private final Text title;
    private final Text description;

    SonicDeviceMode(BaseSonicDeviceMode mode, Text title, Text description) {
        this.mode = mode;
        this.title = title;
        this.description = description;
    }

    public BaseSonicDeviceMode getInstance() {
        return this.mode;
    }

    public Text getTitle() {
        return this.title;
    }

    public Text getDescription() {
        return this.description;
    }
}

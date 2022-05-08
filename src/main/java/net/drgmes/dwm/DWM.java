package net.drgmes.dwm;

import net.drgmes.dwm.setup.Registration;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod(DWM.MODID)
public class DWM {
    public static final String MODID = "dwm";
    public static final int CHUNKS_UPDATE_RADIUS = 2;

    public static class TIMINGS {
        public static final float DEMAT = 240;
        public static final float REMAT = 180;
        public static final float FLIGHT_LOOP = 32;
        public static final float ERROR_SOUND = 120;
    }

    public static class TEXTS {
        public static final Component TARDIS_DOORS_LOCKED = new TranslatableComponent("message.tardis.control.role.doors.locked");
        public static final Component TARDIS_DOORS_UNLOCKED = new TranslatableComponent("message.tardis.control.role.doors.unlocked");
    }

    public static class LOCS {
        public static final ResourceLocation TARDIS = new ResourceLocation(DWM.MODID, "tardis");
    }

    public static class TEXTURES {
        public static final ResourceLocation GUI_TEST = new ResourceLocation(DWM.MODID, "textures/gui/test.png");
        public static final int[] GUI_TEST_SIZE = new int[]{224, 176};
    }

    public DWM() {
        Registration.init();
    }
}

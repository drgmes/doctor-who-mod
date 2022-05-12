package net.drgmes.dwm;

import net.drgmes.dwm.setup.Registration;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
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
        public static final Component TARDIS_DOORS_LOCKED = new TranslatableComponent("message." + DWM.MODID + ".tardis.control.role.doors.locked");
        public static final Component TARDIS_DOORS_UNLOCKED = new TranslatableComponent("message." + DWM.MODID + ".tardis.control.role.doors.unlocked");

        public static final Component TELEPATHIC_INTERFACE_NAME = new TranslatableComponent("screen." + DWM.MODID + ".telepathic_interface");
        public static final Component TELEPATHIC_INTERFACE_NAME_LOCATIONS = new TranslatableComponent("screen." + DWM.MODID + ".telepathic_interface.locations");
        public static final Component TELEPATHIC_INTERFACE_NAME_BANNERS = new TranslatableComponent("screen." + DWM.MODID + ".telepathic_interface.banners");
        public static final Component TELEPATHIC_INTERFACE_FLD_SEARCH = new TranslatableComponent("screen." + DWM.MODID + ".telepathic_interface.search");
        public static final Component TELEPATHIC_INTERFACE_BTN_CANCEL = new TranslatableComponent("screen." + DWM.MODID + ".telepathic_interface.cancel");
        public static final Component TELEPATHIC_INTERFACE_BTN_ACCEPT = new TranslatableComponent("screen." + DWM.MODID + ".telepathic_interface.accept");
    }

    public static class LOCS {
        public static final ResourceLocation TARDIS = new ResourceLocation(DWM.MODID, "tardis");
    }

    public static class TEXTURES {
        public static class GUI {
            public static class TARDIS {
                public static class CONSOLE {
                    public static final ResourceLocation TELEPATHIC_INTERFACE = new ResourceLocation(DWM.MODID, "textures/gui/tardis/console/telepathic_interface.png");
                    public static final Vec2 TELEPATHIC_INTERFACE_SIZE = new Vec2(403, 303);
                }
            }
        }
    }

    public DWM() {
        Registration.init();
    }
}

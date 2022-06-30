package net.drgmes.dwm;

import com.mojang.logging.LogUtils;
import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.setup.Registration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.function.Function;

@Mod(DWM.MODID)
public class DWM {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final String MODID = "dwm";
    public static final int CHUNKS_UPDATE_RADIUS = 2;

    public DWM() {
        Registration.init();
    }

    public static class TIMINGS {
        public static final float DEMAT = 240;
        public static final float REMAT = 180;
        public static final float FLIGHT_LOOP = 32;
    }

    public static class TEXTS {
        public static final Component TARDIS_DOORS_LOCKED = Component.translatable("message." + DWM.MODID + ".tardis.control.role.doors.locked");
        public static final Component TARDIS_DOORS_UNLOCKED = Component.translatable("message." + DWM.MODID + ".tardis.control.role.doors.unlocked");

        public static final Component ARS_INTERFACE_NAME = Component.translatable("screen." + DWM.MODID + ".ars_interface.title");
        public static final Component ARS_INTERFACE_FLD_SEARCH = Component.translatable("screen." + DWM.MODID + ".ars_interface.field.search");
        public static final Component ARS_INTERFACE_BTN_CANCEL = Component.translatable("screen." + DWM.MODID + ".ars_interface.button.cancel");
        public static final Component ARS_INTERFACE_BTN_GENERATE = Component.translatable("screen." + DWM.MODID + ".ars_interface.button.generate");
        public static final Component ARS_INTERFACE_BTN_DESTROY = Component.translatable("screen." + DWM.MODID + ".ars_interface.button.destroy");

        public static final Component TELEPATHIC_INTERFACE_NAME = Component.translatable("screen." + DWM.MODID + ".telepathic_interface.title");
        public static final Component TELEPATHIC_INTERFACE_NAME_LOCATIONS = Component.translatable("screen." + DWM.MODID + ".telepathic_interface.title.locations");
        public static final Component TELEPATHIC_INTERFACE_NAME_BANNERS = Component.translatable("screen." + DWM.MODID + ".telepathic_interface.title.banners");
        public static final Component TELEPATHIC_INTERFACE_FLD_SEARCH = Component.translatable("screen." + DWM.MODID + ".telepathic_interface.field.search");
        public static final Component TELEPATHIC_INTERFACE_BTN_CANCEL = Component.translatable("screen." + DWM.MODID + ".telepathic_interface.button.cancel");
        public static final Component TELEPATHIC_INTERFACE_BTN_ACCEPT = Component.translatable("screen." + DWM.MODID + ".telepathic_interface.button.accept");

        public static final Component SCREWDRIVER_MODE_SCAN = Component.translatable("title." + DWM.MODID + ".screwdriver.mode.scan");
        public static final Component SCREWDRIVER_MODE_SETTING = Component.translatable("title." + DWM.MODID + ".screwdriver.mode.setting");
        public static final Component SCREWDRIVER_MODE_TARDIS_RELOCATION = Component.translatable("title." + DWM.MODID + ".screwdriver.mode.tardis_relocation");

        public static final Component SCREWDRIVER_INTERFACE_NAME = Component.translatable("screen." + DWM.MODID + ".screwdriver_interface.title");
        public static final Function<Screwdriver.ScrewdriverMode, Component> SCREWDRIVER_INTERFACE_BTN_MODE = (mode) -> Component.translatable("screen." + DWM.MODID + ".screwdriver_interface.button.mode", mode.getTitle());
    }

    public static class LOCS {
        public static final ResourceLocation TARDIS = new ResourceLocation(DWM.MODID, "tardis");
    }

    public static class TEXTURES {
        public static class GUI {
            public static class TARDIS {
                public static final ResourceLocation ARS_INTERFACE = new ResourceLocation(DWM.MODID, "textures/gui/tardis/ars/interface.png");
                public static final Vec2 ARS_INTERFACE_SIZE = new Vec2(403, 303);

                public static class CONSOLE {
                    public static final ResourceLocation TELEPATHIC_INTERFACE = new ResourceLocation(DWM.MODID, "textures/gui/tardis/console/telepathic_interface.png");
                    public static final Vec2 TELEPATHIC_INTERFACE_SIZE = new Vec2(403, 303);
                }

                public static class ENGINE {
                    public static final ResourceLocation SYSTEMS_INTERFACE = new ResourceLocation(DWM.MODID, "textures/gui/tardis/engine/systems_interface.png");
                    public static final Vec2 SYSTEMS_INTERFACE_SIZE = new Vec2(176, 166);
                }
            }

            public static class SCREWDRIVER {
                public static final ResourceLocation INTERFACE_MAIN = new ResourceLocation(DWM.MODID, "textures/gui/screwdriver/interface/main.png");
                public static final Vec2 INTERFACE_MAIN_SIZE = new Vec2(403, 303);
            }
        }
    }
}

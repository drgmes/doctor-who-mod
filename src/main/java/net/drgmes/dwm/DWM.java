package net.drgmes.dwm;

import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class DWM {
    public static final String MODID = "dwm";
    public static final Logger LOGGER = LoggerFactory.getLogger(DWM.MODID);
    public static final int CHUNKS_UPDATE_RADIUS = 2;

    public static Identifier getIdentifier(String path) {
        return Identifier.of(DWM.MODID, path);
    }

    public static class TIMINGS {
        public static final float DEMAT = 240;
        public static final float REMAT = 180;
        public static final float FLIGHT_LOOP = 32;
        public static final float ERROR_SOUND = 120;

        public static final int SCREWDRIVER_TIMEOUT = 4;
    }

    public static class TEXTS {
        public static final Text DEMATERIALIZATION_CIRCUIT_NOT_INSTALLED = Text.translatable("message." + DWM.MODID + ".tardis.system.dematerialization_circuit.not_installed");
        public static final Text DIRECTIONAL_UNIT_NOT_INSTALLED = Text.translatable("message." + DWM.MODID + ".tardis.system.directional_unit.not_installed");
        public static final Text SHIELDS_GENERATOR_NOT_INSTALLED = Text.translatable("message." + DWM.MODID + ".tardis.system.shields_generator.not_installed");

        public static final Text TARDIS_BROKEN = Text.translatable("message." + DWM.MODID + ".tardis.broken");
        public static final Text TARDIS_REPAIRED = Text.translatable("message." + DWM.MODID + ".tardis.repaired");
        public static final Text TARDIS_NOT_ENOUGH_FUEL = Text.translatable("message." + DWM.MODID + ".tardis.fuel.not_enough");
        public static final Text TARDIS_DOORS_LOCKED = Text.translatable("message." + DWM.MODID + ".tardis.control.role.doors.locked");
        public static final Text TARDIS_DOORS_UNLOCKED = Text.translatable("message." + DWM.MODID + ".tardis.control.role.doors.unlocked");

        public static final Text ARS_INTERFACE_NAME = Text.translatable("screen." + DWM.MODID + ".ars_interface.title");
        public static final Text ARS_INTERFACE_FLD_SEARCH = Text.translatable("screen." + DWM.MODID + ".ars_interface.field.search");
        public static final Text ARS_INTERFACE_BTN_CANCEL = Text.translatable("screen." + DWM.MODID + ".ars_interface.button.cancel");
        public static final Text ARS_INTERFACE_BTN_GENERATE = Text.translatable("screen." + DWM.MODID + ".ars_interface.button.generate");
        public static final Text ARS_INTERFACE_BTN_DESTROY = Text.translatable("screen." + DWM.MODID + ".ars_interface.button.destroy");

        public static final Text MONITOR_NAME_CONSOLE_ROOMS = Text.translatable("screen." + DWM.MODID + ".monitor.title.console_rooms");
        public static final Text MONITOR_BTN_CONSOLE_ROOMS_CANCEL = Text.translatable("screen." + DWM.MODID + ".monitor.button.console_rooms.cancel");
        public static final Text MONITOR_BTN_CONSOLE_ROOMS_ACCEPT = Text.translatable("screen." + DWM.MODID + ".monitor.button.console_rooms.accept");
        public static final Text MONITOR_BTN_CONSOLE_ROOMS_PREV = Text.translatable("screen." + DWM.MODID + ".monitor.button.console_rooms.prev");
        public static final Text MONITOR_BTN_CONSOLE_ROOMS_NEXT = Text.translatable("screen." + DWM.MODID + ".monitor.button.console_rooms.next");

        public static final Text MONITOR_STATE_ON = Text.translatable("title." + DWM.MODID + ".monitor.state.value.on");
        public static final Text MONITOR_STATE_OFF = Text.translatable("title." + DWM.MODID + ".monitor.state.value.off");
        public static final Text MONITOR_STATE_YES = Text.translatable("title." + DWM.MODID + ".monitor.state.value.yes");
        public static final Text MONITOR_STATE_NO = Text.translatable("title." + DWM.MODID + ".monitor.state.value.no");

        public static final Text TELEPATHIC_INTERFACE_NAME_LOCATIONS = Text.translatable("screen." + DWM.MODID + ".telepathic_interface.title.locations");
        public static final Text TELEPATHIC_INTERFACE_NAME_BANNERS = Text.translatable("screen." + DWM.MODID + ".telepathic_interface.title.banners");
        public static final Text TELEPATHIC_INTERFACE_FLD_SEARCH = Text.translatable("screen." + DWM.MODID + ".telepathic_interface.field.search");
        public static final Text TELEPATHIC_INTERFACE_BTN_CANCEL = Text.translatable("screen." + DWM.MODID + ".telepathic_interface.button.cancel");
        public static final Text TELEPATHIC_INTERFACE_BTN_ACCEPT = Text.translatable("screen." + DWM.MODID + ".telepathic_interface.button.accept");

        public static final Text SCREWDRIVER_MODE_SCAN = Text.translatable("title." + DWM.MODID + ".screwdriver.mode.scan");
        public static final Text SCREWDRIVER_MODE_SETTING = Text.translatable("title." + DWM.MODID + ".screwdriver.mode.setting");
        public static final Text SCREWDRIVER_MODE_TARDIS_RELOCATION = Text.translatable("title." + DWM.MODID + ".screwdriver.mode.tardis_relocation");

        public static final Text SCREWDRIVER_INTERFACE_NAME = Text.translatable("screen." + DWM.MODID + ".screwdriver_interface.title");
        public static final Function<Screwdriver.EScrewdriverMode, Text> SCREWDRIVER_INTERFACE_BTN_MODE = (mode) -> Text.translatable("screen." + DWM.MODID + ".screwdriver_interface.button.mode", mode.getTitle());
    }

    public static class LOCS {
        public static final Identifier TARDIS = DWM.getIdentifier("tardis");
    }

    public static class TEXTURES {
        public static class GUI {
            public static class TARDIS {
                public static final Identifier ARS_INTERFACE = DWM.getIdentifier("textures/gui/tardis/ars/interface.png");
                public static final Vec2f ARS_INTERFACE_SIZE = new Vec2f(403, 303);

                public static class CONSOLE {
                    public static final Identifier MONITOR = DWM.getIdentifier("textures/gui/tardis/console_unit/monitor.png");
                    public static final Vec2f MONITOR_SIZE = new Vec2f(403, 303);

                    public static final Identifier TELEPATHIC_INTERFACE = DWM.getIdentifier("textures/gui/tardis/console_unit/telepathic_interface.png");
                    public static final Vec2f TELEPATHIC_INTERFACE_SIZE = new Vec2f(403, 303);
                }

                public static class ENGINE {
                    public static final Identifier SYSTEMS_INTERFACE = DWM.getIdentifier("textures/gui/tardis/engine/systems_interface.png");
                    public static final Vec2f SYSTEMS_INTERFACE_SIZE = new Vec2f(176, 166);
                }
            }

            public static class SCREWDRIVER {
                public static final Identifier INTERFACE_MAIN = DWM.getIdentifier("textures/gui/screwdriver/interface/main.png");
                public static final Vec2f INTERFACE_MAIN_SIZE = new Vec2f(403, 303);
            }
        }
    }
}

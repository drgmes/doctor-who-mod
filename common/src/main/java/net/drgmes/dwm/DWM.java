package net.drgmes.dwm;

import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiFunction;
import java.util.function.Function;

public class DWM {
    public static final String MODID = "dwm";
    public static final Logger LOGGER = LoggerFactory.getLogger(DWM.MODID);

    public static class COMMON {
        public static final int TARDIS_ROOMS_OFFSET = 1000;
    }

    public static class TIMINGS {
        public static final float DEMAT = 240;
        public static final float REMAT = 180;
        public static final float FLIGHT_LOOP = 32;

        public static final int SONIC_DEVICE_TIMEOUT = 4;
    }

    public static class TEXTS {
        public static final BiFunction<String, Formatting, Text> TARDIS_ID = (id, color) -> Text.translatable("title." + DWM.MODID + ".tardis_id", Text.literal(id).formatted(color));
        public static final BiFunction<String, Formatting, Text> TARDIS_POS = (pos, color) -> Text.translatable("title." + DWM.MODID + ".tardis_pos", Text.literal(pos).formatted(color));
        public static final BiFunction<String, Formatting, Text> TARDIS_LAST_POS = (pos, color) -> Text.translatable("title." + DWM.MODID + ".tardis_last_pos", Text.literal(pos).formatted(color));

        public static final Function<String, Text> TARDIS_REMOVED = (id) -> Text.translatable("message." + DWM.MODID + ".tardis.removed", Text.literal(id).formatted(Formatting.AQUA));
        public static final Function<Float, Text> TARDIS_ARRIVE_IN = (time) -> Text.translatable("message." + DWM.MODID + ".tardis.arrive", Text.literal(String.valueOf(time)).formatted(Formatting.AQUA));
        public static final Function<String, Text> TARDIS_ARRIVE_FAILED = (pos) -> Text.translatable("message." + DWM.MODID + ".tardis.arrive.failed", Text.literal(pos).formatted(Formatting.GOLD));
        public static final Text TARDIS_ARRIVE_NO_FUEL = Text.translatable("message." + DWM.MODID + ".tardis.arrive.no_fuel");
        public static final Text TARDIS_LOCKED = Text.translatable("message." + DWM.MODID + ".tardis.locked");
        public static final Text TARDIS_BROKEN = Text.translatable("message." + DWM.MODID + ".tardis.broken");
        public static final Text TARDIS_REPAIRED = Text.translatable("message." + DWM.MODID + ".tardis.repaired");
        public static final Text TARDIS_NOT_ALLOWED = Text.translatable("message." + DWM.MODID + ".tardis.not_allowed");
        public static final Text TARDIS_MUST_BE_MATERIALIZED = Text.translatable("message." + DWM.MODID + ".tardis.must_be_materialized");
        public static final Text TARDIS_MUST_BE_LANDED = Text.translatable("message." + DWM.MODID + ".tardis.must_be_landed");
        public static final Text TARDIS_ALREADY_MATERIALIZED = Text.translatable("message." + DWM.MODID + ".tardis.already_materialized");
        public static final Text TARDIS_ALREADY_DEMATERIALIZED = Text.translatable("message." + DWM.MODID + ".tardis.already_dematerialized");
        public static final Text TARDIS_ALREADY_IN_FLIGHT = Text.translatable("message." + DWM.MODID + ".tardis.already_in_flight");
        public static final Text TARDIS_ALREADY_LANDED = Text.translatable("message." + DWM.MODID + ".tardis.already_landed");
        public static final Text TARDIS_NOT_ENOUGH_FUEL = Text.translatable("message." + DWM.MODID + ".tardis.fuel.not_enough");
        public static final Text TARDIS_HANDBRAKE_ACTIVATED = Text.translatable("message." + DWM.MODID + ".tardis.handbrake.activated");
        public static final Text TARDIS_DOORS_LOCKED = Text.translatable("message." + DWM.MODID + ".tardis.control.role.doors.locked");
        public static final Text TARDIS_DOORS_UNLOCKED = Text.translatable("message." + DWM.MODID + ".tardis.control.role.doors.unlocked");

        public static final Text DEMATERIALIZATION_CIRCUIT_NOT_INSTALLED = Text.translatable("message." + DWM.MODID + ".tardis.system.dematerialization_circuit.not_installed");
        public static final Text DIRECTIONAL_UNIT_NOT_INSTALLED = Text.translatable("message." + DWM.MODID + ".tardis.system.directional_unit.not_installed");
        public static final Text SHIELDS_GENERATOR_NOT_INSTALLED = Text.translatable("message." + DWM.MODID + ".tardis.system.shields_generator.not_installed");
        public static final Text SHIELDS_GENERATOR_NOT_ACTIVE = Text.translatable("message." + DWM.MODID + ".tardis.system.shields_generator.not_active");

        public static final Text ARS_INTERFACE_NAME = Text.translatable("screen." + DWM.MODID + ".ars_interface.title");
        public static final Text ARS_INTERFACE_FLD_SEARCH = Text.translatable("screen." + DWM.MODID + ".ars_interface.field.search");
        public static final Text ARS_INTERFACE_BTN_CANCEL = Text.translatable("screen." + DWM.MODID + ".ars_interface.button.cancel");
        public static final Text ARS_INTERFACE_BTN_GENERATE = Text.translatable("screen." + DWM.MODID + ".ars_interface.button.generate");
        public static final Text ARS_INTERFACE_BTN_DESTROY = Text.translatable("screen." + DWM.MODID + ".ars_interface.button.destroy");

        public static final Text MONITOR_TITLE = Text.translatable("screen." + DWM.MODID + ".monitor.title");
        public static final Text MONITOR_DATA_ID = Text.translatable("screen." + DWM.MODID + ".monitor.data.id");
        public static final Text MONITOR_DATA_OWNER = Text.translatable("screen." + DWM.MODID + ".monitor.data.owner");
        public static final Text MONITOR_ACTION_SETTINGS = Text.translatable("screen." + DWM.MODID + ".monitor.action.settings");
        public static final Text MONITOR_ACTION_WAYPOINTS = Text.translatable("screen." + DWM.MODID + ".monitor.action.waypoints");
        public static final Text MONITOR_ACTION_RESEARCHER = Text.translatable("screen." + DWM.MODID + ".monitor.action.researcher");
        public static final Text MONITOR_ACTION_CONSOLE_ROOMS = Text.translatable("screen." + DWM.MODID + ".monitor.action.console_rooms");
        public static final Text MONITOR_ACTION_EXTERNAL_MONITOR = Text.translatable("screen." + DWM.MODID + ".monitor.action.external_monitor");

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

        public static final Text SONIC_DEVICE_MODE_SCAN = Text.translatable("title." + DWM.MODID + ".sonic_device.mode.scan");
        public static final Text SONIC_DEVICE_MODE_SCAN_DESCRIPTION = Text.translatable("title." + DWM.MODID + ".sonic_device.mode.scan.description");
        public static final Text SONIC_DEVICE_MODE_SETTING = Text.translatable("title." + DWM.MODID + ".sonic_device.mode.setting");
        public static final Text SONIC_DEVICE_MODE_SETTING_DESCRIPTION = Text.translatable("title." + DWM.MODID + ".sonic_device.mode.setting.description");
        public static final Text SONIC_DEVICE_MODE_TARDIS_RELOCATION = Text.translatable("title." + DWM.MODID + ".sonic_device.mode.tardis_relocation");
        public static final Text SONIC_DEVICE_MODE_TARDIS_RELOCATION_DESCRIPTION = Text.translatable("title." + DWM.MODID + ".sonic_device.mode.tardis_relocation.description");

        public static final Text SONIC_DEVICE_INTERFACE_NAME = Text.translatable("screen." + DWM.MODID + ".sonic_device_interface.title");
        public static final Function<SonicDevice.EMode, Text> SONIC_DEVICE_INTERFACE_BTN_MODE = (mode) -> Text.translatable("screen." + DWM.MODID + ".sonic_device_interface.button.mode", mode.getTitle().copy().formatted(Formatting.AQUA));
    }

    public static class LOCS {
        public static final Identifier TARDIS = DWM.getIdentifier("tardis");
        public static final Identifier ENTITY_BLOCK_ITEM = DWM.getIdentifier("item/templates/entity_block_item");
    }

    public static class MODELS {
        public static final Identifier BUILTIN_ENTITY = new Identifier("builtin/entity");
        public static final Identifier ITEM_GENERATED = new Identifier("item/generated");
        public static final Identifier BLOCK_CUBE_ALL = new Identifier("block/cube_all");
        public static final Identifier BLOCK_CUBE_BOTTOM_TOP = new Identifier("block/cube_bottom_top");
        public static final Identifier BLOCK_ORIENTABLE = new Identifier("block/orientable");
        public static final Identifier BLOCK_SLAB = new Identifier("block/slab");
        public static final Identifier BLOCK_SLAB_TOP = new Identifier("block/slab_top");
        public static final Identifier BLOCK_STAIRS = new Identifier("block/stairs");
        public static final Identifier BLOCK_STAIRS_INNER = new Identifier("block/inner_stairs");
        public static final Identifier BLOCK_STAIRS_OUTER = new Identifier("block/outer_stairs");
        public static final Identifier BLOCK_WALL_POST = new Identifier("block/template_wall_post");
        public static final Identifier BLOCK_WALL_SIDE = new Identifier("block/template_wall_side");
        public static final Identifier BLOCK_WALL_SIDE_TALL = new Identifier("block/template_wall_side_tall");
        public static final Identifier BLOCK_WALL_INVENTORY = new Identifier("block/wall_inventory");

        public static final Identifier BLOCK_INVISIBLE = DWM.getIdentifier("block/templates/invisible");
        public static final Identifier BLOCK_WALL_XY_POST = DWM.getIdentifier("block/templates/template_wall_xy_post");
        public static final Identifier BLOCK_WALL_XY_SIDE = DWM.getIdentifier("block/templates/template_wall_xy_side");
        public static final Identifier BLOCK_WALL_XY_SIDE_TALL = DWM.getIdentifier("block/templates/template_wall_xy_side_tall");
        public static final Identifier BLOCK_WALL_XY_INVENTORY = DWM.getIdentifier("block/templates/wall_xy_inventory");
    }

    public static class TEXTURES {
        public static class GUI {
            public static class TARDIS {
                public static class ARS {
                    public static final Identifier CREATOR_INTERFACE = DWM.getIdentifier("textures/gui/tardis/ars/creator_interface.png");
                    public static final Vector2i CREATOR_INTERFACE_SIZE = new Vector2i(371, 297);

                    public static final Identifier DESTROYER_INTERFACE = DWM.getIdentifier("textures/gui/tardis/ars/destroyer_interface.png");
                    public static final Vector2i DESTROYER_INTERFACE_SIZE = new Vector2i(403, 303);
                }

                public static class CONSOLE {
                    public static final Identifier MONITOR = DWM.getIdentifier("textures/gui/tardis/console_unit/monitor.png");
                    public static final Vector2i MONITOR_SIZE = new Vector2i(403, 303);

                    public static final Identifier TELEPATHIC_INTERFACE = DWM.getIdentifier("textures/gui/tardis/console_unit/telepathic_interface.png");
                    public static final Vector2i TELEPATHIC_INTERFACE_SIZE = new Vector2i(403, 303);
                }

                public static class ENGINE {
                    public static final Identifier SYSTEMS_INTERFACE = DWM.getIdentifier("textures/gui/tardis/engine/systems_interface.png");
                    public static final Vector2i SYSTEMS_INTERFACE_SIZE = new Vector2i(176, 166);
                }
            }

            public static class SONIC_DEVICE {
                public static final Identifier INTERFACE_MAIN = DWM.getIdentifier("textures/gui/sonic_device/interface/main.png");
                public static final Vector2i INTERFACE_MAIN_SIZE = new Vector2i(403, 303);
            }
        }
    }

    public static Identifier getIdentifier(String path) {
        return new Identifier(DWM.MODID, path);
    }
}

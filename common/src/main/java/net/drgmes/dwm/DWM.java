package net.drgmes.dwm;

import net.drgmes.dwm.enums.SonicDeviceMode;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.joml.Vector2i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DWM {
    public static final String MODID = "dwm";
    public static final Logger LOGGER = LoggerFactory.getLogger(DWM.MODID);

    public static class COMMON {
        public static final int TARDIS_ROOMS_OFFSET = 1000;
    }

    public static class TIMINGS {
        public static final int DEMAT_DURATION = 240;
        public static final int REMAT_DURATION = 170;
        public static final int PULSE_LOOP = 48;
        public static final int FLIGHT_LOOP = 32;
        public static final int RECONSTRUCTION_LOOP = 48;
        public static final int RECONSTRUCTION_DURATION = 200;
        public static final int RECONSTRUCTION_NOTIFICATION = 40;

        public static final int SONIC_DEVICE_TIMEOUT = 4;
    }

    public static class TEXTS {
        public static final Function<Text, Text> SONIC_DEVICE_MODE_TITLE = (mode) -> Text.translatable("title.dwm.sonic_device.mode", mode);
        public static final Text SONIC_DEVICE_MODE_SCAN = Text.translatable("title.dwm.sonic_device.mode.scan");
        public static final Text SONIC_DEVICE_MODE_SCAN_DESCRIPTION = Text.translatable("title.dwm.sonic_device.mode.scan.description");
        public static final Text SONIC_DEVICE_MODE_SETTING = Text.translatable("title.dwm.sonic_device.mode.setting");
        public static final Text SONIC_DEVICE_MODE_SETTING_DESCRIPTION = Text.translatable("title.dwm.sonic_device.mode.setting.description");
        public static final Text SONIC_DEVICE_MODE_TARDIS_RELOCATION = Text.translatable("title.dwm.sonic_device.mode.tardis_relocation");
        public static final Text SONIC_DEVICE_MODE_TARDIS_RELOCATION_DESCRIPTION = Text.translatable("title.dwm.sonic_device.mode.tardis_relocation.description");

        public static final BiFunction<String, Formatting, Text> TARDIS_ID = (id, color) -> Text.translatable("title.dwm.tardis_id", Text.literal(id).formatted(color));
        public static final BiFunction<String, Formatting, Text> TARDIS_POS = (pos, color) -> Text.translatable("title.dwm.tardis_pos", Text.literal(pos).formatted(color));
        public static final BiFunction<String, Formatting, Text> TARDIS_LAST_POS = (pos, color) -> Text.translatable("title.dwm.tardis_last_pos", Text.literal(pos).formatted(color));

        public static final Text MONITOR_STATE_ON = Text.translatable("title.dwm.monitor.state.value.on");
        public static final Text MONITOR_STATE_OFF = Text.translatable("title.dwm.monitor.state.value.off");
        public static final Text MONITOR_STATE_YES = Text.translatable("title.dwm.monitor.state.value.yes");
        public static final Text MONITOR_STATE_NO = Text.translatable("title.dwm.monitor.state.value.no");

        public static final Text ARS_CATEGORIES_BACK = Text.translatable("title.dwm.ars.categories.back").formatted(Formatting.YELLOW);

        public static final Function<Float, Text> TARDIS_ARRIVE_TIMER = (time) -> Text.translatable("message.dwm.tardis.arrive.timer", Text.literal(String.valueOf(time)).formatted(Formatting.AQUA));
        public static final Function<String, Text> TARDIS_ARRIVE_FAILED = (pos) -> Text.translatable("message.dwm.tardis.arrive.failed", Text.literal(pos).formatted(Formatting.GOLD));

        public static final Text TARDIS_NO_FUEL = Text.translatable("message.dwm.tardis.no_fuel");
        public static final Text TARDIS_LOCKED = Text.translatable("message.dwm.tardis.locked");
        public static final Text TARDIS_BROKEN = Text.translatable("message.dwm.tardis.broken");
        public static final Text TARDIS_REPAIRED = Text.translatable("message.dwm.tardis.repaired");
        public static final Text TARDIS_NOT_ALLOWED = Text.translatable("message.dwm.tardis.not_allowed");
        public static final Text TARDIS_MUST_BE_MATERIALIZED = Text.translatable("message.dwm.tardis.must_be_materialized");
        public static final Text TARDIS_MUST_BE_LANDED = Text.translatable("message.dwm.tardis.must_be_landed");
        public static final Text TARDIS_ALREADY_MATERIALIZED = Text.translatable("message.dwm.tardis.already_materialized");
        public static final Text TARDIS_ALREADY_DEMATERIALIZED = Text.translatable("message.dwm.tardis.already_dematerialized");
        public static final Text TARDIS_ALREADY_IN_FLIGHT = Text.translatable("message.dwm.tardis.already_in_flight");
        public static final Text TARDIS_ALREADY_LANDED = Text.translatable("message.dwm.tardis.already_landed");
        public static final Text TARDIS_NOT_ENOUGH_FUEL = Text.translatable("message.dwm.tardis.fuel.not_enough");
        public static final Text TARDIS_HANDBRAKE_ACTIVATED = Text.translatable("message.dwm.tardis.handbrake.activated");
        public static final Text TARDIS_DOORS_LOCKED = Text.translatable("message.dwm.tardis.control.role.doors.locked");
        public static final Text TARDIS_DOORS_UNLOCKED = Text.translatable("message.dwm.tardis.control.role.doors.unlocked");
        public static final Function<String, Text> TARDIS_REMOVED = (id) -> Text.translatable("message.dwm.tardis.removed", Text.literal(id).formatted(Formatting.AQUA));

        public static final Text MATERIALIZATION_SYSTEM_NOT_INSTALLED = Text.translatable("message.dwm.tardis.system.materialization.not_installed");
        public static final Text MATERIALIZATION_SYSTEM_SAFE_POSITION_NOT_FOUND = Text.translatable("message.dwm.tardis.system.materialization.safe_position_not_found");
        public static final Text FLIGHT_SYSTEM_NOT_INSTALLED = Text.translatable("message.dwm.tardis.system.flight.not_installed");
        public static final Text SHIELDS_SYSTEM_NOT_INSTALLED = Text.translatable("message.dwm.tardis.system.shields.not_installed");
        public static final Text SHIELDS_SYSTEM_NOT_ACTIVE = Text.translatable("message.dwm.tardis.system.shields.not_active");

        public static final Text ARS_CONSOLE_ROOM_REBUILD_LOCKED = Text.translatable("message.dwm.tardis.ars.console_room.rebuild.locked");
        public static final Text ARS_CONSOLE_ROOM_REBUILD_IN_PROGRESS = Text.translatable("message.dwm.tardis.ars.console_room.rebuild.in_progress");
        public static final Text ARS_CONSOLE_ROOM_REBUILD_PREPARE = Text.translatable("message.dwm.tardis.ars.console_room.rebuild.prepare");
        public static final Text ARS_CONSOLE_ROOM_REBUILD_STARTED = Text.translatable("message.dwm.tardis.ars.console_room.rebuild.started");
        public static final Text ARS_CONSOLE_ROOM_REBUILD_FINISHED = Text.translatable("message.dwm.tardis.ars.console_room.rebuild.finished");
        public static final Text ARS_CONSOLE_ROOM_REBUILD_FAILED = Text.translatable("message.dwm.tardis.ars.console_room.rebuild.failed");
        public static final Function<Float, Text> ARS_CONSOLE_ROOM_REBUILD_TIMER = (time) -> Text.translatable("message.dwm.tardis.ars.console_room.rebuild.timer", Text.literal(String.valueOf(time)).formatted(Formatting.AQUA));

        public static final Text ARS_SECONDARY_ROOM_BUILD_SUCCESS = Text.translatable("message.dwm.tardis.ars.secondary_room.build.success");
        public static final Text ARS_SECONDARY_ROOM_BUILD_FAILED = Text.translatable("message.dwm.tardis.ars.secondary_room.build.failed");
        public static final Function<String, Text> ARS_SECONDARY_ROOM_BUILD_FAILED_DETAILS = (pos) -> Text.translatable("message.dwm.tardis.ars.secondary_room.build.failed.details", Text.literal(pos).formatted(Formatting.YELLOW));

        public static final Text ARS_SECONDARY_ROOM_DESTROY_SUCCESS = Text.translatable("message.dwm.tardis.ars.secondary_room.destroy.success");
        public static final Text ARS_SECONDARY_ROOM_DESTROY_FAILED = Text.translatable("message.dwm.tardis.ars.secondary_room.destroy.failed");

        public static final Text TELEPATHIC_INTERFACE_MAP_COORDS_LOADED = Text.translatable("message.dwm.tardis.telepathic_interface.map.loaded");
        public static final Function<String, Text> TELEPATHIC_INTERFACE_MAP_BANNER_LOADED = (color) -> Text.translatable("message.dwm.tardis.telepathic_interface.map.loaded.banner", Text.literal(color).formatted(Formatting.GOLD));

        public static final Text ARS_DESTROYER_MUST_BE_SNEAKING = Text.translatable("message.dwm.ars_destroyer.must_be_sneaking");

        public static final Function<List<Text>, Text> SONIC_DEVICE_TARDIS_RELOCATED = (coords) -> Text.translatable("message.dwm.sonic_device.tardis_relocated", coords.get(0), coords.get(1), coords.get(2));

        public static final Text ARS_INTERFACE_NAME = Text.translatable("screen.dwm.ars_interface.title");
        public static final Text ARS_INTERFACE_SEARCH = Text.translatable("screen.dwm.ars_interface.search");
        public static final Text ARS_INTERFACE_MESSAGE = Text.translatable("screen.dwm.ars_interface.message");
        public static final Text ARS_INTERFACE_BTN_CANCEL = Text.translatable("screen.dwm.ars_interface.button.cancel");
        public static final Text ARS_INTERFACE_BTN_GENERATE = Text.translatable("screen.dwm.ars_interface.button.generate");
        public static final Text ARS_INTERFACE_BTN_DESTROY = Text.translatable("screen.dwm.ars_interface.button.destroy");

        public static final Text MONITOR_TITLE = Text.translatable("screen.dwm.monitor.title");
        public static final Text MONITOR_DATA_ID = Text.translatable("screen.dwm.monitor.data.id");
        public static final Text MONITOR_DATA_OWNER = Text.translatable("screen.dwm.monitor.data.owner");
        public static final Text MONITOR_DATA_EXTERIOR = Text.translatable("screen.dwm.monitor.data.exterior");
        public static final Text MONITOR_ACTION_EXTERIOR = Text.translatable("screen.dwm.monitor.action.exterior");
        public static final Text MONITOR_ACTION_WAYPOINTS = Text.translatable("screen.dwm.monitor.action.waypoints");
        public static final Text MONITOR_ACTION_RESEARCHER = Text.translatable("screen.dwm.monitor.action.researcher");
        public static final Text MONITOR_ACTION_CONSOLE_ROOMS = Text.translatable("screen.dwm.monitor.action.console_rooms");
        public static final Text MONITOR_ACTION_EXTERNAL_MONITOR = Text.translatable("screen.dwm.monitor.action.external_monitor");

        public static final Text MONITOR_EXTERNAL_SHELLS_CANCEL = Text.translatable("screen.dwm.monitor.external_shells.cancel");
        public static final Text MONITOR_EXTERNAL_SHELLS_ACCEPT = Text.translatable("screen.dwm.monitor.external_shells.accept");

        public static final Text MONITOR_CONSOLE_ROOMS_CANCEL = Text.translatable("screen.dwm.monitor.console_rooms.cancel");
        public static final Text MONITOR_CONSOLE_ROOMS_ACCEPT = Text.translatable("screen.dwm.monitor.console_rooms.accept");
        public static final Text MONITOR_CONSOLE_ROOMS_PREV = Text.translatable("screen.dwm.monitor.console_rooms.prev");
        public static final Text MONITOR_CONSOLE_ROOMS_NEXT = Text.translatable("screen.dwm.monitor.console_rooms.next");

        public static final Text TELEPATHIC_INTERFACE_NAME_LOCATIONS = Text.translatable("screen.dwm.telepathic_interface.title.locations");
        public static final Text TELEPATHIC_INTERFACE_NAME_BANNERS = Text.translatable("screen.dwm.telepathic_interface.title.banners");
        public static final Text TELEPATHIC_INTERFACE_SEARCH = Text.translatable("screen.dwm.telepathic_interface.search");
        public static final Text TELEPATHIC_INTERFACE_BTN_CANCEL = Text.translatable("screen.dwm.telepathic_interface.button.cancel");
        public static final Text TELEPATHIC_INTERFACE_BTN_ACCEPT = Text.translatable("screen.dwm.telepathic_interface.button.accept");

        public static final Text SONIC_DEVICE_INTERFACE_NAME = Text.translatable("screen.dwm.sonic_device_interface.title");
        public static final Function<SonicDeviceMode, Text> SONIC_DEVICE_INTERFACE_BTN_MODE = (mode) -> Text.translatable("screen.dwm.sonic_device_interface.button.mode", mode.getTitle().copy().formatted(Formatting.AQUA));

        public static final Text ARGUMENT_PLAYER_PRESENT = Text.translatable("argument.dwm.tardis.player_present");
        public static final Text ARGUMENT_INVALID_TARDIS = Text.translatable("argument.dwm.tardis.invalid");
        public static final Function<String, Text> ARGUMENT_INVALID_DIMENSION = (id) -> Text.translatable("argument.dimension.invalid", id);
    }

    public static class LOCS {
        public static final Identifier TARDIS = DWM.getIdentifier("tardis");
        public static final Identifier ENTITY_BLOCK_ITEM = DWM.getIdentifier("item/templates/entity_block_item");
    }

    public static class MODELS {
        public static final Identifier BUILTIN_ENTITY = Identifier.ofVanilla("builtin/entity");
        public static final Identifier ITEM_GENERATED = Identifier.ofVanilla("item/generated");
        public static final Identifier BLOCK_CUBE_ALL = Identifier.ofVanilla("block/cube_all");
        public static final Identifier BLOCK_CUBE_BOTTOM_TOP = Identifier.ofVanilla("block/cube_bottom_top");
        public static final Identifier BLOCK_ORIENTABLE = Identifier.ofVanilla("block/orientable");
        public static final Identifier BLOCK_SLAB = Identifier.ofVanilla("block/slab");
        public static final Identifier BLOCK_SLAB_TOP = Identifier.ofVanilla("block/slab_top");
        public static final Identifier BLOCK_STAIRS = Identifier.ofVanilla("block/stairs");
        public static final Identifier BLOCK_STAIRS_INNER = Identifier.ofVanilla("block/inner_stairs");
        public static final Identifier BLOCK_STAIRS_OUTER = Identifier.ofVanilla("block/outer_stairs");
        public static final Identifier BLOCK_WALL_POST = Identifier.ofVanilla("block/template_wall_post");
        public static final Identifier BLOCK_WALL_SIDE = Identifier.ofVanilla("block/template_wall_side");
        public static final Identifier BLOCK_WALL_SIDE_TALL = Identifier.ofVanilla("block/template_wall_side_tall");
        public static final Identifier BLOCK_WALL_INVENTORY = Identifier.ofVanilla("block/wall_inventory");

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
        return Identifier.of(DWM.MODID, path);
    }
}

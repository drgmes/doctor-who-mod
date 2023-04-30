package net.drgmes.dwm.setup;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.network.client.*;
import net.drgmes.dwm.network.server.*;

public class ModNetwork {
    public static SimpleNetworkManager NETWORK_MANAGER = SimpleNetworkManager.create(DWM.MODID);

    public static MessageType DIMENSION_ADD = Registration.registerS2CMessageType("dimension_add", DimensionAddPacket::create);
    public static MessageType TARDIS_CONSOLE_UNIT_CONTROLS_STATES_UPDATE = Registration.registerS2CMessageType("tardis_console_unit_controls_states_update", TardisConsoleUnitControlsStatesUpdatePacket::create);
    public static MessageType TARDIS_CONSOLE_UNIT_MONITOR_OPEN = Registration.registerS2CMessageType("tardis_console_unit_monitor_open", TardisConsoleUnitMonitorOpenPacket::create);
    public static MessageType TARDIS_CONSOLE_UNIT_MONITOR_PAGE_UPDATE = Registration.registerS2CMessageType("tardis_console_unit_monitor_page_update", TardisConsoleUnitMonitorPageUpdatePacket::create);
    public static MessageType TARDIS_CONSOLE_UNIT_SCREWDRIVER_SLOT_UPDATE = Registration.registerS2CMessageType("tardis_console_unit_screwdriver_slot_update", TardisConsoleUnitScrewdriverSlotUpdatePacket::create);
    public static MessageType TARDIS_CONSOLE_UNIT_TELEPATHIC_INTERFACE_MAP_BANNERS_OPEN = Registration.registerS2CMessageType("tardis_console_unit_telepathic_interface_map_banners_open", TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket::create);
    public static MessageType TARDIS_CONSOLE_UNIT_TELEPATHIC_INTERFACE_LOCATIONS_OPEN = Registration.registerS2CMessageType("tardis_console_unit_telepathic_interface_locations_open", TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket::create);
    public static MessageType TARDIS_CONSOLE_UNIT_UPDATE = Registration.registerS2CMessageType("tardis_console_unit_update", TardisConsoleUnitUpdatePacket::create);
    public static MessageType TARDIS_EXTERIOR_UPDATE = Registration.registerS2CMessageType("tardis_exterior_update", TardisExteriorUpdatePacket::create);
    public static MessageType TARDIS_ROUNDEL_BLOCK_TEMPLATE_CLEAR = Registration.registerS2CMessageType("tardis_roundel_block_template_clear", TardisRoundelBlockTemplateClearPacket::create);
    public static MessageType TARDIS_ROUNDEL_BLOCK_TEMPLATE_UPDATE = Registration.registerS2CMessageType("tardis_roundel_block_template_update", TardisRoundelBlockTemplateUpdatePacket::create);
    public static MessageType TARDIS_ROUNDEL_UPDATE = Registration.registerS2CMessageType("tardis_roundel_update", TardisRoundelUpdatePacket::create);
    public static MessageType TARDIS_TOYOTA_SPINNER_UPDATE = Registration.registerS2CMessageType("tardis_toyota_spinner_update", TardisToyotaSpinnerUpdatePacket::create);

    public static MessageType ARS_CREATOR_APPLY = Registration.registerC2SMessageType("ars_creator_apply", ArsCreatorApplyPacket::create);
    public static MessageType ARS_DESTROYER_APPLY = Registration.registerC2SMessageType("ars_destroyer_apply", ArsDestroyerApplyPacket::create);
    public static MessageType SCREWDRIVER_UPDATE = Registration.registerC2SMessageType("screwdriver_update", ScrewdriverUpdatePacket::create);
    public static MessageType SCREWDRIVER_USE = Registration.registerC2SMessageType("screwdriver_use", ScrewdriverUsePacket::create);
    public static MessageType TARDIS_CONSOLE_UNIT_INIT = Registration.registerC2SMessageType("tardis_console_unit_init", TardisConsoleUnitInitPacket::create);
    public static MessageType TARDIS_CONSOLE_UNIT_SOUND = Registration.registerC2SMessageType("tardis_console_unit_sound", TardisConsoleUnitSoundPacket::create);
    public static MessageType TARDIS_CONSOLE_UNIT_MONITOR_CONSOLE_ROOM_APPLY = Registration.registerC2SMessageType("tardis_console_unit_monitor_console_room_apply", TardisConsoleUnitMonitorConsoleRoomApplyPacket::create);
    public static MessageType TARDIS_CONSOLE_UNIT_TELEPATHIC_INTERFACE_LOCATION_APPLY = Registration.registerC2SMessageType("tardis_console_unit_telepathic_interface_location_apply", TardisConsoleUnitTelepathicInterfaceLocationApplyPacket::create);
    public static MessageType TARDIS_CONSOLE_UNIT_TELEPATHIC_INTERFACE_MAP_BANNER_APPLY = Registration.registerC2SMessageType("tardis_console_unit_telepathic_interface_map_banner_apply", TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket::create);
    public static MessageType TARDIS_INTERIOR_DOORS_INIT = Registration.registerC2SMessageType("tardis_interior_doors_init", TardisInteriorDoorsInitPacket::create);

    public static void init() {
    }
}

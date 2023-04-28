package net.drgmes.dwm.setup;

import net.drgmes.dwm.blocks.common.engineertable.EngineerTableBlockBuilder;
import net.drgmes.dwm.blocks.common.titaniumore.TitaniumOreBlockBuilder;
import net.drgmes.dwm.blocks.common.titaniumore.TitaniumOreDeepslateBlockBuilder;
import net.drgmes.dwm.blocks.common.titaniumraw.TitaniumRawBlockBuilder;
import net.drgmes.dwm.blocks.decorative.carbonfiber.CarbonFiberBlockBuilder;
import net.drgmes.dwm.blocks.decorative.carbonfiber.CarbonFiberSlabBlockBuilder;
import net.drgmes.dwm.blocks.decorative.carbonfiber.CarbonFiberStairsBlockBuilder;
import net.drgmes.dwm.blocks.decorative.carbonfiber.CarbonFiberWallBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumbeams.TitaniumBeamsBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumblock.TitaniumBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumblock.TitaniumSlabBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumblock.TitaniumStairsBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumblock.TitaniumWallBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumdoor.TitaniumDoorBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumdoor.TitaniumDoorColoredBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumglass.TitaniumGlassBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniummatrix.TitaniumMatrixBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniummatrix.TitaniumMatrixSlabBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniummatrix.TitaniumMatrixStairsBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniummatrix.TitaniumMatrixWallBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumpanel.TitaniumPanelBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumpanel.TitaniumPanelSlabBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumpanel.TitaniumPanelStairsBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumpanel.TitaniumPanelWallBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumpanellamp.TitaniumPanelLampBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumpanellamp.TitaniumPanelLampColoredBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumskin.TitaniumSkinBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumskin.TitaniumSkinSlabBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumskin.TitaniumSkinStairsBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumskin.TitaniumSkinWallBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumskincoil.TitaniumSkinCoilBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumskincoil.TitaniumSkinCoilSlabBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumskinlamp.TitaniumSkinLampBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumskinlamp.TitaniumSkinLampColoredBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumskinstripes.TitaniumSkinStripesBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumskinstripes.TitaniumSkinStripesSlabBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumskinvent.TitaniumSkinVentBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumskinvent.TitaniumSkinVentSlabBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumskinventdark.TitaniumSkinVentDarkBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumskinventdark.TitaniumSkinVentDarkSlabBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumsmooth.TitaniumSmoothBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumsmooth.TitaniumSmoothSlabBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumsmooth.TitaniumSmoothStairsBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumsmooth.TitaniumSmoothWallBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumstripedpanel.TitaniumStripedPanelColoredBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumstripedpanel.TitaniumStripedPanelSlabColoredBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumstripedpanel.TitaniumStripedPanelStairsColoredBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumstripedpanel.TitaniumStripedPanelWallColoredBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumstripedskin.TitaniumStripedSkinColoredBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumstripedskin.TitaniumStripedSkinSlabColoredBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumstripedskin.TitaniumStripedSkinStairsColoredBlockBuilder;
import net.drgmes.dwm.blocks.decorative.titaniumstripedskin.TitaniumStripedSkinWallColoredBlockBuilder;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial.TardisConsoleUnitImperialBlockBuilder;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota.TardisConsoleUnitToyotaBlockBuilder;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.TardisDoorsPoliceBoxBlockBuilder;
import net.drgmes.dwm.blocks.tardis.engines.tardisengineimperial.TardisEngineImperialBlockBuilder;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.TardisEngineToyotaBlockBuilder;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.TardisExteriorPoliceBoxBlockBuilder;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlockBuilder;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockBuilder;
import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.TardisRoundelBlockBuilder;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.TardisToyotaSpinnerBlockBuilder;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.builders.ColoredBlockBuilders;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
    public static final List<BlockBuilder> BLOCK_BUILDERS = new ArrayList<>();

    // Common
    public static final BlockBuilder TITANIUM_ORE = new TitaniumOreBlockBuilder("titanium_ore");
    public static final BlockBuilder TITANIUM_ORE_DEEPSLATE = new TitaniumOreDeepslateBlockBuilder("titanium_ore_deepslate");
    public static final BlockBuilder TITANIUM_RAW = new TitaniumRawBlockBuilder("titanium_raw");
    public static final BlockBuilder ENGINEER_TABLE = new EngineerTableBlockBuilder("engineer_table");

    // Tardis Exteriors
    public static final BlockBuilder TARDIS_EXTERIOR_POLICE_BOX = new TardisExteriorPoliceBoxBlockBuilder("tardis_exterior_police_box");

    // Tardis Doors
    public static final BlockBuilder TARDIS_DOORS_POLICE_BOX = new TardisDoorsPoliceBoxBlockBuilder("tardis_doors_police_box");

    // Tardis Consoles
    public static final BlockBuilder TARDIS_CONSOLE_UNIT_IMPERIAL = new TardisConsoleUnitImperialBlockBuilder("tardis_console_unit_imperial");
    public static final BlockBuilder TARDIS_CONSOLE_UNIT_TOYOTA = new TardisConsoleUnitToyotaBlockBuilder("tardis_console_unit_toyota");

    // Tardis Engines
    public static final BlockBuilder TARDIS_ENGINE_IMPERIAL = new TardisEngineImperialBlockBuilder("tardis_engine_imperial");
    public static final BlockBuilder TARDIS_ENGINE_TOYOTA = new TardisEngineToyotaBlockBuilder("tardis_engine_toyota");

    // Tardis Misc
    public static final BlockBuilder TARDIS_ARS_CREATOR = new TardisArsCreatorBlockBuilder("tardis_ars_creator");
    public static final BlockBuilder TARDIS_ARS_DESTROYER = new TardisArsDestroyerBlockBuilder("tardis_ars_destroyer");
    public static final BlockBuilder TARDIS_ROUNDEL = new TardisRoundelBlockBuilder("tardis_roundel");

    // Tardis Decorations
    public static final BlockBuilder TARDIS_TOYOTA_SPINNER = new TardisToyotaSpinnerBlockBuilder("tardis_toyota_spinner");
//    public static final BlockBuilder TARDIS_ROUNDEL_ATTACHMENT_DARK = new TardisRoundelAttachmentBlockBuilder("tardis_roundel_attachment_dark");
//    public static final BlockBuilder TARDIS_ROUNDEL_ATTACHMENT_LIGHT = new TardisRoundelAttachmentBlockBuilder("tardis_roundel_attachment_light");
//    public static final BlockBuilder TARDIS_ROUNDEL_ATTACHMENT_DARK_LAMP = new TardisRoundelAttachmentBlockBuilder("tardis_roundel_attachment_dark_lamp", true);
//    public static final BlockBuilder TARDIS_ROUNDEL_ATTACHMENT_LIGHT_LAMP = new TardisRoundelAttachmentBlockBuilder("tardis_roundel_attachment_light_lamp", true);

    // Simple Decorative blocks
    public static final BlockBuilder TITANIUM_BLOCK = new TitaniumBlockBuilder("titanium_block");
    public static final BlockBuilder TITANIUM_SMOOTH = new TitaniumSmoothBlockBuilder("titanium_smooth");
    public static final BlockBuilder TITANIUM_SKIN_COIL = new TitaniumSkinCoilBlockBuilder("titanium_skin_coil");
    public static final BlockBuilder TITANIUM_SKIN_STRIPES = new TitaniumSkinStripesBlockBuilder("titanium_skin_stripes");
    public static final BlockBuilder TITANIUM_SKIN_VENT = new TitaniumSkinVentBlockBuilder("titanium_skin_vent");
    public static final BlockBuilder TITANIUM_SKIN_VENT_DARK = new TitaniumSkinVentDarkBlockBuilder("titanium_skin_vent_dark");
    public static final BlockBuilder TITANIUM_BEAMS = new TitaniumBeamsBlockBuilder("titanium_beams");
    public static final BlockBuilder TITANIUM_MATRIX = new TitaniumMatrixBlockBuilder("titanium_matrix");
    public static final BlockBuilder TITANIUM_GLASS = new TitaniumGlassBlockBuilder("titanium_glass");
    public static final BlockBuilder CARBON_FIBER = new CarbonFiberBlockBuilder("carbon_fiber");

    // Decorative Plates
    public static final BlockBuilder TITANIUM_PANEL = new TitaniumPanelBlockBuilder("titanium_panel");
    public static final ColoredBlockBuilders TITANIUM_STRIPED_PANELS = new ColoredBlockBuilders("titanium_striped_panel", TitaniumStripedPanelColoredBlockBuilder::new);

    // Decorative Skins
    public static final BlockBuilder TITANIUM_SKIN = new TitaniumSkinBlockBuilder("titanium_skin");
    public static final ColoredBlockBuilders TITANIUM_STRIPED_SKINS = new ColoredBlockBuilders("titanium_striped_skin", TitaniumStripedSkinColoredBlockBuilder::new);

    // Decorative Plate Lamps
    public static final BlockBuilder TITANIUM_PANEL_LAMP_BASE = new TitaniumPanelLampBlockBuilder("titanium_panel_lamp_base");
    public static final ColoredBlockBuilders TITANIUM_PANEL_LAMPS = new ColoredBlockBuilders("titanium_panel_lamp", TitaniumPanelLampColoredBlockBuilder::new);

    // Decorative Skin Lamps
    public static final BlockBuilder TITANIUM_SKIN_LAMP_BASE = new TitaniumSkinLampBlockBuilder("titanium_skin_lamp_base");
    public static final ColoredBlockBuilders TITANIUM_SKIN_LAMPS = new ColoredBlockBuilders("titanium_skin_lamp", TitaniumSkinLampColoredBlockBuilder::new);

    // Simple Decorative stairs
    public static final BlockBuilder TITANIUM_STAIRS = new TitaniumStairsBlockBuilder("titanium_stairs");
    public static final BlockBuilder TITANIUM_SMOOTH_STAIRS = new TitaniumSmoothStairsBlockBuilder("titanium_smooth_stairs");
    public static final BlockBuilder TITANIUM_MATRIX_STAIRS = new TitaniumMatrixStairsBlockBuilder("titanium_matrix_stairs");
    public static final BlockBuilder CARBON_FIBER_STAIRS = new CarbonFiberStairsBlockBuilder("carbon_fiber_stairs");

    // Decorative Plate Stairs
    public static final BlockBuilder TITANIUM_PANEL_STAIRS = new TitaniumPanelStairsBlockBuilder("titanium_panel_stairs");
    public static final ColoredBlockBuilders TITANIUM_STRIPED_PANEL_STAIRS = new ColoredBlockBuilders("titanium_striped_panel_stairs", TitaniumStripedPanelStairsColoredBlockBuilder::new);

    // Decorative Skin Stairs
    public static final BlockBuilder TITANIUM_SKIN_STAIRS = new TitaniumSkinStairsBlockBuilder("titanium_skin_stairs");
    public static final ColoredBlockBuilders TITANIUM_STRIPED_SKIN_STAIRS = new ColoredBlockBuilders("titanium_striped_skin_stairs", TitaniumStripedSkinStairsColoredBlockBuilder::new);

    // Simple Decorative slabs
    public static final BlockBuilder TITANIUM_SLAB = new TitaniumSlabBlockBuilder("titanium_slab");
    public static final BlockBuilder TITANIUM_SMOOTH_SLAB = new TitaniumSmoothSlabBlockBuilder("titanium_smooth_slab");
    public static final BlockBuilder TITANIUM_SKIN_COIL_SLAB = new TitaniumSkinCoilSlabBlockBuilder("titanium_skin_coil_slab");
    public static final BlockBuilder TITANIUM_SKIN_STRIPES_SLAB = new TitaniumSkinStripesSlabBlockBuilder("titanium_skin_stripes_slab");
    public static final BlockBuilder TITANIUM_SKIN_VENT_SLAB = new TitaniumSkinVentSlabBlockBuilder("titanium_skin_vent_slab");
    public static final BlockBuilder TITANIUM_SKIN_VENT_DARK_SLAB = new TitaniumSkinVentDarkSlabBlockBuilder("titanium_skin_vent_dark_slab");
    public static final BlockBuilder TITANIUM_MATRIX_SLAB = new TitaniumMatrixSlabBlockBuilder("titanium_matrix_slab");
    public static final BlockBuilder CARBON_FIBER_SLAB = new CarbonFiberSlabBlockBuilder("carbon_fiber_slab");

    // Decorative Plate slabs
    public static final BlockBuilder TITANIUM_PANEL_SLAB = new TitaniumPanelSlabBlockBuilder("titanium_panel_slab");
    public static final ColoredBlockBuilders TITANIUM_STRIPED_PANEL_SLABS = new ColoredBlockBuilders("titanium_striped_panel_slab", TitaniumStripedPanelSlabColoredBlockBuilder::new);

    // Decorative Skin slabs
    public static final BlockBuilder TITANIUM_SKIN_SLAB = new TitaniumSkinSlabBlockBuilder("titanium_skin_slab");
    public static final ColoredBlockBuilders TITANIUM_STRIPED_SKIN_SLABS = new ColoredBlockBuilders("titanium_striped_skin_slab", TitaniumStripedSkinSlabColoredBlockBuilder::new);

    // Simple Decorative walls
    public static final BlockBuilder TITANIUM_WALL = new TitaniumWallBlockBuilder("titanium_wall");
    public static final BlockBuilder TITANIUM_SMOOTH_WALL = new TitaniumSmoothWallBlockBuilder("titanium_smooth_wall");
    public static final BlockBuilder TITANIUM_MATRIX_WALL = new TitaniumMatrixWallBlockBuilder("titanium_matrix_wall");
    public static final BlockBuilder CARBON_FIBER_WALL = new CarbonFiberWallBlockBuilder("carbon_fiber_wall");

    // Decorative Plate walls
    public static final BlockBuilder TITANIUM_PANEL_WALL = new TitaniumPanelWallBlockBuilder("titanium_panel_wall");
    public static final ColoredBlockBuilders TITANIUM_STRIPED_PANEL_WALLS = new ColoredBlockBuilders("titanium_striped_panel_wall", TitaniumStripedPanelWallColoredBlockBuilder::new);

    // Decorative Skin walls
    public static final BlockBuilder TITANIUM_SKIN_WALL = new TitaniumSkinWallBlockBuilder("titanium_skin_wall");
    public static final ColoredBlockBuilders TITANIUM_STRIPED_SKIN_WALLS = new ColoredBlockBuilders("titanium_striped_skin_wall", TitaniumStripedSkinWallColoredBlockBuilder::new);

    // Decorative Misc
    public static final BlockBuilder TITANIUM_DOOR_BASE = new TitaniumDoorBlockBuilder("titanium_door_base");
    public static final ColoredBlockBuilders TITANIUM_DOORS = new ColoredBlockBuilders("titanium_door", TitaniumDoorColoredBlockBuilder::new);

    public static void init() {
    }
}

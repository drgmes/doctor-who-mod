package net.drgmes.dwm.setup;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial.TardisConsoleUnitImperialBlockRenderer;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial.models.TardisConsoleUnitImperialModel;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota.TardisConsoleUnitToyotaBlockRenderer;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota.models.TardisConsoleUnitToyotaModel;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.TardisDoorsPoliceBoxBlockRenderer;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.models.TardisDoorsPoliceBoxModel;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.TardisEngineToyotaBlockRenderer;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.models.TardisEngineToyotaModel;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.TardisExteriorPoliceBoxBlockRenderer;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.models.TardisExteriorPoliceBoxModel;
import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.TardisRoundelBlockRenderer;
import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.models.TardisRoundelModel;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.TardisToyotaSpinnerBlockRenderer;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.models.TardisToyotaSpinnerModel;
import net.drgmes.dwm.entities.tardis.consoleunit.controls.TardisConsoleControlEntityRenderer;
import net.drgmes.dwm.items.tardis.systems.dematerializationcircuit.models.TardisSystemDematerializationCircuitModel;
import net.drgmes.dwm.utils.builders.BlockBuilder;

public class ModRenderers {
    public static void setup() {
        setupRenderTypes();
//        setupEntityModelLayers();
//        setupEntityRenderers();
        setupBlockEntityRenderers();
    }

    public static void setupRenderTypes() {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            if (blockBuilder.getRenderLayer() != null) {
                RenderTypeRegistry.register(blockBuilder.getRenderLayer(), blockBuilder.getBlock());
            }
        }
    }

    public static void setupEntityModelLayers() {
        EntityModelLayerRegistry.register(TardisExteriorPoliceBoxModel.LAYER_LOCATION, TardisExteriorPoliceBoxModel::getTexturedModelData);
        EntityModelLayerRegistry.register(TardisDoorsPoliceBoxModel.LAYER_LOCATION, TardisDoorsPoliceBoxModel::getTexturedModelData);
        EntityModelLayerRegistry.register(TardisConsoleUnitImperialModel.LAYER_LOCATION, TardisConsoleUnitImperialModel::getTexturedModelData);
        EntityModelLayerRegistry.register(TardisConsoleUnitToyotaModel.LAYER_LOCATION, TardisConsoleUnitToyotaModel::getTexturedModelData);
        EntityModelLayerRegistry.register(TardisEngineToyotaModel.LAYER_LOCATION, TardisEngineToyotaModel::getTexturedModelData);
        EntityModelLayerRegistry.register(TardisToyotaSpinnerModel.LAYER_LOCATION, TardisToyotaSpinnerModel::getTexturedModelData);
        EntityModelLayerRegistry.register(TardisRoundelModel.LAYER_LOCATION_DARK, TardisRoundelModel::getTexturedModelData);
        EntityModelLayerRegistry.register(TardisRoundelModel.LAYER_LOCATION_LIGHT, TardisRoundelModel::getTexturedModelData);
        EntityModelLayerRegistry.register(TardisSystemDematerializationCircuitModel.LAYER_LOCATION, TardisSystemDematerializationCircuitModel::getTexturedModelData);
    }

    public static void setupEntityRenderers() {
        EntityRendererRegistry.register(ModEntities.TARDIS_CONSOLE_UNIT_CONTROL.entityType, TardisConsoleControlEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL.entityType, TardisConsoleControlEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_MEDIUM.entityType, TardisConsoleControlEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_LARGE.entityType, TardisConsoleControlEntityRenderer::new);
    }

    public static void setupBlockEntityRenderers() {
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_EXTERIOR_POLICE_BOX.getBlockEntityType(), TardisExteriorPoliceBoxBlockRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_DOORS_POLICE_BOX.getBlockEntityType(), TardisDoorsPoliceBoxBlockRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_CONSOLE_UNIT_IMPERIAL.getBlockEntityType(), TardisConsoleUnitImperialBlockRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_CONSOLE_UNIT_TOYOTA.getBlockEntityType(), TardisConsoleUnitToyotaBlockRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_ENGINE_TOYOTA.getBlockEntityType(), TardisEngineToyotaBlockRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_TOYOTA_SPINNER.getBlockEntityType(), TardisToyotaSpinnerBlockRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_ROUNDEL.getBlockEntityType(), TardisRoundelBlockRenderer::new);
    }
}

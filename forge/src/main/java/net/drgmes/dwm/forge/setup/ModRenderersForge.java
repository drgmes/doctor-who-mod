package net.drgmes.dwm.forge.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial.models.TardisConsoleUnitImperialModel;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota.models.TardisConsoleUnitToyotaModel;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.models.TardisDoorsPoliceBoxModel;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.models.TardisEngineToyotaModel;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.models.TardisExteriorPoliceBoxModel;
import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.models.TardisRoundelModel;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.models.TardisToyotaSpinnerModel;
import net.drgmes.dwm.entities.tardis.consoleunit.controls.TardisConsoleControlEntityRenderer;
import net.drgmes.dwm.items.tardis.systems.dematerializationcircuit.models.TardisSystemDematerializationCircuitModel;
import net.drgmes.dwm.setup.ModEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DWM.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRenderersForge {
    public static void setup() {
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TardisExteriorPoliceBoxModel.LAYER_LOCATION, TardisExteriorPoliceBoxModel::getTexturedModelData);
        event.registerLayerDefinition(TardisDoorsPoliceBoxModel.LAYER_LOCATION, TardisDoorsPoliceBoxModel::getTexturedModelData);
        event.registerLayerDefinition(TardisConsoleUnitImperialModel.LAYER_LOCATION, TardisConsoleUnitImperialModel::getTexturedModelData);
        event.registerLayerDefinition(TardisConsoleUnitToyotaModel.LAYER_LOCATION, TardisConsoleUnitToyotaModel::getTexturedModelData);
        event.registerLayerDefinition(TardisEngineToyotaModel.LAYER_LOCATION, TardisEngineToyotaModel::getTexturedModelData);
        event.registerLayerDefinition(TardisToyotaSpinnerModel.LAYER_LOCATION, TardisToyotaSpinnerModel::getTexturedModelData);
        event.registerLayerDefinition(TardisRoundelModel.LAYER_LOCATION_DARK, TardisRoundelModel::getTexturedModelData);
        event.registerLayerDefinition(TardisRoundelModel.LAYER_LOCATION_LIGHT, TardisRoundelModel::getTexturedModelData);
        event.registerLayerDefinition(TardisSystemDematerializationCircuitModel.LAYER_LOCATION, TardisSystemDematerializationCircuitModel::getTexturedModelData);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.TARDIS_CONSOLE_UNIT_CONTROL.getEntityType(), TardisConsoleControlEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL.getEntityType(), TardisConsoleControlEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_MEDIUM.getEntityType(), TardisConsoleControlEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_LARGE.getEntityType(), TardisConsoleControlEntityRenderer::new);
    }
}

package net.drgmes.dwm.neoforge.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial.models.TardisConsoleUnitImperialModel;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota.models.TardisConsoleUnitToyotaModel;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorscapsule.tardisdoorsphonebox.models.TardisDoorsCapsuleModel;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorsphonebox.models.TardisDoorsPhoneBoxModel;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.models.TardisDoorsPoliceBoxModel;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.models.TardisEngineToyotaModel;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorcapsule.models.TardisExteriorCapsuleModel;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorphonebox.models.TardisExteriorPhoneBoxModel;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.models.TardisExteriorPoliceBoxModel;
import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.models.TardisRoundelModel;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.models.TardisToyotaSpinnerModel;
import net.drgmes.dwm.entities.tardis.consoleunit.controls.TardisConsoleControlEntityRenderer;
import net.drgmes.dwm.items.tardis.systems.dematerializationcircuit.models.TardisSystemDematerializationCircuitModel;
import net.drgmes.dwm.setup.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = DWM.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModRenderers {
    public static void setup() {
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TardisExteriorCapsuleModel.LAYER_LOCATION, TardisExteriorCapsuleModel::getTexturedModelData);
        event.registerLayerDefinition(TardisExteriorPoliceBoxModel.LAYER_LOCATION, TardisExteriorPoliceBoxModel::getTexturedModelData);
        event.registerLayerDefinition(TardisExteriorPhoneBoxModel.LAYER_LOCATION, TardisExteriorPhoneBoxModel::getTexturedModelData);

        event.registerLayerDefinition(TardisDoorsCapsuleModel.LAYER_LOCATION, TardisDoorsCapsuleModel::getTexturedModelData);
        event.registerLayerDefinition(TardisDoorsPoliceBoxModel.LAYER_LOCATION, TardisDoorsPoliceBoxModel::getTexturedModelData);
        event.registerLayerDefinition(TardisDoorsPhoneBoxModel.LAYER_LOCATION, TardisDoorsPhoneBoxModel::getTexturedModelData);

        event.registerLayerDefinition(TardisConsoleUnitImperialModel.LAYER_LOCATION, TardisConsoleUnitImperialModel::getTexturedModelData);
        event.registerLayerDefinition(TardisConsoleUnitToyotaModel.LAYER_LOCATION, TardisConsoleUnitToyotaModel::getTexturedModelData);

        event.registerLayerDefinition(TardisEngineToyotaModel.LAYER_LOCATION, TardisEngineToyotaModel::getTexturedModelData);

        event.registerLayerDefinition(TardisToyotaSpinnerModel.LAYER_LOCATION, TardisToyotaSpinnerModel::getTexturedModelData);
        event.registerLayerDefinition(TardisRoundelModel.LAYER_LOCATION_DARK, TardisRoundelModel::getTexturedModelData);
        event.registerLayerDefinition(TardisRoundelModel.LAYER_LOCATION_LIGHT, TardisRoundelModel::getTexturedModelData);
        event.registerLayerDefinition(TardisSystemDematerializationCircuitModel.LAYER_LOCATION, TardisSystemDematerializationCircuitModel::getTexturedModelData);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.TARDIS_CONSOLE_UNIT_CONTROL.getEntityType(), TardisConsoleControlEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL.getEntityType(), TardisConsoleControlEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_MEDIUM.getEntityType(), TardisConsoleControlEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_LARGE.getEntityType(), TardisConsoleControlEntityRenderer::new);
    }
}

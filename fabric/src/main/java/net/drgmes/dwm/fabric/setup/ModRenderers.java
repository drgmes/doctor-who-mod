package net.drgmes.dwm.fabric.setup;

import net.drgmes.dwm.fabric.renderers.items.*;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModItems;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;

public class ModRenderers {
    public static void setup() {
        net.drgmes.dwm.setup.ModRenderers.setupEntityModelLayers();
        net.drgmes.dwm.setup.ModRenderers.setupEntityRenderers();

        BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.TARDIS_EXTERIOR_CAPSULE.getBlockItem(), new TardisExteriorCapsuleItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.TARDIS_EXTERIOR_POLICE_BOX.getBlockItem(), new TardisExteriorPoliceBoxItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.TARDIS_EXTERIOR_PHONE_BOX.getBlockItem(), new TardisExteriorPhoneBoxItemRenderer());

        BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.TARDIS_DOORS_POLICE_BOX.getBlockItem(), new TardisDoorsPoliceBoxItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.TARDIS_DOORS_PHONE_BOX.getBlockItem(), new TardisDoorsPhoneBoxItemRenderer());

        BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.TARDIS_CONSOLE_UNIT_IMPERIAL.getBlockItem(), new TardisConsoleUnitImperialItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.TARDIS_CONSOLE_UNIT_TOYOTA.getBlockItem(), new TardisConsoleUnitToyotaItemRenderer());

        BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.TARDIS_ENGINE_TOYOTA.getBlockItem(), new TardisEngineToyotaItemRenderer());

        BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.TARDIS_TOYOTA_SPINNER.getBlockItem(), new TardisToyotaSpinnerItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.TARDIS_ROUNDEL.getBlockItem(), new TardisRoundelItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(ModItems.TARDIS_SYSTEM_DEMATERIALIZATION_CIRCUIT.getItem(), new TardisSystemDematerializationCircuitItemRenderer());
    }
}

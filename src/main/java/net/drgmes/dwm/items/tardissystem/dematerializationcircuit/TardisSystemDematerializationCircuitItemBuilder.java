package net.drgmes.dwm.items.tardissystem.dematerializationcircuit;

import net.drgmes.dwm.common.tardis.systems.ITardisSystem;
import net.drgmes.dwm.items.tardissystem.TardisSystemItemBuilder;
import net.drgmes.dwm.items.tardissystem.dematerializationcircuit.models.TardisSystemDematerializationCircuitModel;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

public class TardisSystemDematerializationCircuitItemBuilder extends TardisSystemItemBuilder {
    public TardisSystemDematerializationCircuitItemBuilder(String name, Class<? extends ITardisSystem> systemType) {
        super(name, systemType);
    }

    @Override
    public void registerCustomRender() {
        EntityModelLayerRegistry.registerModelLayer(TardisSystemDematerializationCircuitModel.LAYER_LOCATION, TardisSystemDematerializationCircuitModel::getTexturedModelData);
        BuiltinItemRendererRegistry.INSTANCE.register(this.getItem(), new TardisSystemDematerializationCircuitItemRenderer());
    }
}

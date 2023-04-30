package net.drgmes.dwm.fabric.renderers.items;

import net.drgmes.dwm.items.tardis.systems.dematerializationcircuit.models.TardisSystemDematerializationCircuitModel;

public class TardisSystemDematerializationCircuitItemRenderer extends BaseItemRenderer {
    public TardisSystemDematerializationCircuitItemRenderer() {
        super(TardisSystemDematerializationCircuitModel::new, TardisSystemDematerializationCircuitModel.LAYER_LOCATION);
    }
}

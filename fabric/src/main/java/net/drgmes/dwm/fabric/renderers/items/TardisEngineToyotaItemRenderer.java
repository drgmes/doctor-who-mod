package net.drgmes.dwm.fabric.renderers.items;

import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.models.TardisEngineToyotaModel;

public class TardisEngineToyotaItemRenderer extends BaseItemRenderer {
    public TardisEngineToyotaItemRenderer() {
        super(TardisEngineToyotaModel::new, TardisEngineToyotaModel.LAYER_LOCATION);
    }
}

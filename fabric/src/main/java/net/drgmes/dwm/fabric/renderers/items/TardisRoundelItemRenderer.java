package net.drgmes.dwm.fabric.renderers.items;

import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.models.TardisRoundelModel;

public class TardisRoundelItemRenderer extends BaseItemRenderer {
    public TardisRoundelItemRenderer() {
        super(TardisRoundelModel::new, TardisRoundelModel.LAYER_LOCATION_DARK);
    }
}

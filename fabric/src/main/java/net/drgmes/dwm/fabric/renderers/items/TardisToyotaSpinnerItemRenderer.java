package net.drgmes.dwm.fabric.renderers.items;

import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.models.TardisToyotaSpinnerModel;

public class TardisToyotaSpinnerItemRenderer extends BaseItemRenderer {
    public TardisToyotaSpinnerItemRenderer() {
        super(TardisToyotaSpinnerModel::new, TardisToyotaSpinnerModel.LAYER_LOCATION);
    }
}

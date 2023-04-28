package net.drgmes.dwm.fabric.renderers.items;

import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota.models.TardisConsoleUnitToyotaModel;

public class TardisConsoleUnitToyotaItemRenderer extends BaseItemRenderer {
    public TardisConsoleUnitToyotaItemRenderer() {
        super(TardisConsoleUnitToyotaModel::new, TardisConsoleUnitToyotaModel.LAYER_LOCATION);
    }
}

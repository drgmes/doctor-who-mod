package net.drgmes.dwm.fabric.renderers.items;

import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial.models.TardisConsoleUnitImperialModel;

public class TardisConsoleUnitImperialItemRenderer extends BaseItemRenderer {
    public TardisConsoleUnitImperialItemRenderer() {
        super(TardisConsoleUnitImperialModel::new, TardisConsoleUnitImperialModel.LAYER_LOCATION);
    }
}

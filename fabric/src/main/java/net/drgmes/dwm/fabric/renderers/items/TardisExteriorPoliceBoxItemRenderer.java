package net.drgmes.dwm.fabric.renderers.items;

import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.models.TardisExteriorPoliceBoxModel;

public class TardisExteriorPoliceBoxItemRenderer extends BaseItemRenderer {
    public TardisExteriorPoliceBoxItemRenderer() {
        super(TardisExteriorPoliceBoxModel::new, TardisExteriorPoliceBoxModel.LAYER_LOCATION);
    }
}

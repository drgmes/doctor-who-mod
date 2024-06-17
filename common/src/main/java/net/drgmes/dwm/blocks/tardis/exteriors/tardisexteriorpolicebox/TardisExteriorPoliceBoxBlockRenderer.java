package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockRenderer;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.models.TardisExteriorPoliceBoxModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class TardisExteriorPoliceBoxBlockRenderer extends BaseTardisExteriorBlockRenderer<TardisExteriorPoliceBoxBlockEntity> {
    public TardisExteriorPoliceBoxBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(
            context,
            TardisExteriorPoliceBoxModel.LAYER_LOCATION,
            TardisExteriorPoliceBoxModel::new,
            0.465F,
            0.025F,
            -0.5F
        );
    }
}

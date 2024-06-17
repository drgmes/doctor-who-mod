package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorcapsule;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockRenderer;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorcapsule.models.TardisExteriorCapsuleModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class TardisExteriorCapsuleBlockRenderer extends BaseTardisExteriorBlockRenderer<TardisExteriorCapsuleBlockEntity> {
    public TardisExteriorCapsuleBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(
            context,
            TardisExteriorCapsuleModel.LAYER_LOCATION,
            TardisExteriorCapsuleModel::new,
            0.465F,
            0.01F,
            -0.45F
        );
    }
}

package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorphonebox;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockRenderer;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorphonebox.models.TardisExteriorPhoneBoxModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class TardisExteriorPhoneBoxBlockRenderer extends BaseTardisExteriorBlockRenderer<TardisExteriorPhoneBoxBlockEntity> {
    public TardisExteriorPhoneBoxBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(context, TardisExteriorPhoneBoxModel.LAYER_LOCATION, TardisExteriorPhoneBoxModel::new, 0.465F, 0.025F, -0.5F);
    }
}

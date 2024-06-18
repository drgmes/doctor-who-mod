package net.drgmes.dwm.blocks.tardis.doors.tardisdoorscapsule.tardisdoorsphonebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockRenderer;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorscapsule.tardisdoorsphonebox.models.TardisDoorsCapsuleModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class TardisDoorsCapsuleBlockRenderer extends BaseTardisDoorsBlockRenderer<TardisDoorsCapsuleBlockEntity> {
    public TardisDoorsCapsuleBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(
            context,
            TardisDoorsCapsuleModel.LAYER_LOCATION,
            TardisDoorsCapsuleModel::new,
            0.465F,
            0.01F,
            -0.45F,
            -0.415F,
            2.25F,
            0xFF202020
        );
    }
}

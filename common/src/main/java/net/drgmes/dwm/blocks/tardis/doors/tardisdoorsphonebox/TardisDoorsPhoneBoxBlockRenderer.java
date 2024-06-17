package net.drgmes.dwm.blocks.tardis.doors.tardisdoorsphonebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockRenderer;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorsphonebox.models.TardisDoorsPhoneBoxModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class TardisDoorsPhoneBoxBlockRenderer extends BaseTardisDoorsBlockRenderer<TardisDoorsPhoneBoxBlockEntity> {
    public TardisDoorsPhoneBoxBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(
            context,
            TardisDoorsPhoneBoxModel.LAYER_LOCATION,
            TardisDoorsPhoneBoxModel::new,
            0.465F,
            0.025F,
            -0.5F,
            -0.415F,
            0XFF31070A
        );
    }
}

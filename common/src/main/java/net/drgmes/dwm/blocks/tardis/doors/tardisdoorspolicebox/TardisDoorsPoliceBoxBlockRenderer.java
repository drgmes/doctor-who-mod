package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockRenderer;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.models.TardisDoorsPoliceBoxModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class TardisDoorsPoliceBoxBlockRenderer extends BaseTardisDoorsBlockRenderer<TardisDoorsPoliceBoxBlockEntity> {
    public TardisDoorsPoliceBoxBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(
            context,
            TardisDoorsPoliceBoxModel.LAYER_LOCATION,
            TardisDoorsPoliceBoxModel::new,
            0.465F,
            0.025F,
            -0.5F,
            -0.415F,
            0XFF001427
        );
    }
}

package net.drgmes.dwm.fabric.renderers.items;

import net.drgmes.dwm.blocks.tardis.doors.tardisdoorsphonebox.models.TardisDoorsPhoneBoxModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class TardisDoorsPhoneBoxItemRenderer extends BaseItemRenderer {
    public TardisDoorsPhoneBoxItemRenderer() {
        super(TardisDoorsPhoneBoxModel::new, TardisDoorsPhoneBoxModel.LAYER_LOCATION);
    }

    @Override
    public void customRender(MatrixStack matrixStack, VertexConsumer vertexConsumer, Model model, int light, int overlay) {
        ((TardisDoorsPhoneBoxModel) model).renderDoors(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
    }
}

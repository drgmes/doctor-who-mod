package net.drgmes.dwm.fabric.renderers.items;

import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.models.TardisDoorsPoliceBoxModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class TardisDoorsPoliceBoxItemRenderer extends BaseItemRenderer {
    public TardisDoorsPoliceBoxItemRenderer() {
        super(TardisDoorsPoliceBoxModel::new, TardisDoorsPoliceBoxModel.LAYER_LOCATION);
    }

    @Override
    public void customRender(MatrixStack matrixStack, VertexConsumer vertexConsumer, Model model, int light, int overlay) {
        ((TardisDoorsPoliceBoxModel) model).renderDoors(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
    }
}

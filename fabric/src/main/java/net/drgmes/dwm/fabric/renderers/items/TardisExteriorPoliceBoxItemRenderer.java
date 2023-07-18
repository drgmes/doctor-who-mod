package net.drgmes.dwm.fabric.renderers.items;

import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.models.TardisExteriorPoliceBoxModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class TardisExteriorPoliceBoxItemRenderer extends BaseItemRenderer {
    public TardisExteriorPoliceBoxItemRenderer() {
        super(TardisExteriorPoliceBoxModel::new, TardisExteriorPoliceBoxModel.LAYER_LOCATION);
    }

    @Override
    public void customRender(MatrixStack matrixStack, VertexConsumer vertexConsumer, Model model, int light, int overlay) {
        ((TardisExteriorPoliceBoxModel) model).renderDoors(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
    }
}

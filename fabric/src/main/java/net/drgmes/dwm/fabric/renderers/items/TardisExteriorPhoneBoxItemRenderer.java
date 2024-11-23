package net.drgmes.dwm.fabric.renderers.items;

import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorphonebox.models.TardisExteriorPhoneBoxModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class TardisExteriorPhoneBoxItemRenderer extends BaseItemRenderer {
    public TardisExteriorPhoneBoxItemRenderer() {
        super(TardisExteriorPhoneBoxModel::new, TardisExteriorPhoneBoxModel.LAYER_LOCATION);
    }

    @Override
    public void customRender(MatrixStack matrixStack, VertexConsumer vertexConsumer, Model model, int light, int overlay) {
        ((TardisExteriorPhoneBoxModel) model).renderDoors(matrixStack, vertexConsumer, light, overlay, 0xFFFFFFFF);
        ((TardisExteriorPhoneBoxModel) model).renderLamp(matrixStack, vertexConsumer, light, overlay, 0xFFFFFFFF);
    }
}

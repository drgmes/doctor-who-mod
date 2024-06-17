package net.drgmes.dwm.fabric.renderers.items;

import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorcapsule.models.TardisExteriorCapsuleModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class TardisExteriorCapsuleItemRenderer extends BaseItemRenderer {
    public TardisExteriorCapsuleItemRenderer() {
        super(TardisExteriorCapsuleModel::new, TardisExteriorCapsuleModel.LAYER_LOCATION);
    }

    @Override
    public void customRender(MatrixStack matrixStack, VertexConsumer vertexConsumer, Model model, int light, int overlay) {
        ((TardisExteriorCapsuleModel) model).renderDoors(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
        ((TardisExteriorCapsuleModel) model).renderLamp(matrixStack, vertexConsumer, light, overlay, 1, 1, 1, 1);
    }
}

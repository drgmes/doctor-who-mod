package net.drgmes.dwm.fabric.renderers.items;

import net.drgmes.dwm.blocks.tardis.doors.tardisdoorscapsule.tardisdoorsphonebox.models.TardisDoorsCapsuleModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class TardisDoorsCapsuleItemRenderer extends BaseItemRenderer {
    public TardisDoorsCapsuleItemRenderer() {
        super(TardisDoorsCapsuleModel::new, TardisDoorsCapsuleModel.LAYER_LOCATION);
    }

    @Override
    public void customRender(MatrixStack matrixStack, VertexConsumer vertexConsumer, Model model, int light, int overlay) {
        ((TardisDoorsCapsuleModel) model).renderDoors(matrixStack, vertexConsumer, light, overlay, 0xFFFFFFFF);
    }
}

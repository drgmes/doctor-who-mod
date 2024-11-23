package net.drgmes.dwm.blocks.tardis.doors;

import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.TardisDoorsPoliceBoxBlock;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public abstract class BaseTardisDoorsModel extends Model {
    private final ModelPart base;
    private final ModelPart door_left;
    private final ModelPart door_right;

    private final float doorAngle;

    public BaseTardisDoorsModel(ModelPart root, float doorAngle) {
        super(RenderLayer::getEntityTranslucentCull);

        this.doorAngle = doorAngle;

        this.base = root.getChild("base");
        this.door_left = root.getChild("door_left");
        this.door_right = root.getChild("door_right");
    }

    public BaseTardisDoorsModel(ModelPart root) {
        this(root, 1.75F);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        base.render(matrixStack, vertexConsumer, light, overlay, color);
    }

    public void renderDoors(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        door_left.render(matrixStack, vertexConsumer, light, overlay, color);
        door_right.render(matrixStack, vertexConsumer, light, overlay, color);
    }

    public void setupAnim(BaseTardisDoorsBlockEntity tile) {
        if (tile.getCachedState().get(TardisDoorsPoliceBoxBlock.OPEN)) {
            this.door_left.yaw = -this.doorAngle;
            this.door_right.yaw = this.doorAngle;
        }
    }
}

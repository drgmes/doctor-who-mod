package net.drgmes.dwm.blocks.tardis.exteriors;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public abstract class BaseTardisExteriorModel extends Model {
    protected final ModelPart base;
    protected final ModelPart door_left;
    protected final ModelPart door_right;
    protected final ModelPart lamp;
    protected final ModelPart boti;

    private final float doorAngle;

    public BaseTardisExteriorModel(ModelPart root, float doorAngle) {
        super(RenderLayer::getEntityTranslucentCull);

        this.doorAngle = doorAngle;

        this.base = root.getChild("base");
        this.door_left = root.getChild("door_left");
        this.door_right = root.getChild("door_right");
        this.lamp = root.getChild("lamp");
        this.boti = root.getChild("boti");
    }

    public BaseTardisExteriorModel(ModelPart root) {
        this(root, 1.46F);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        base.render(matrixStack, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    public void renderDoors(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        door_left.render(matrixStack, vertexConsumer, light, overlay, red, green, blue, alpha);
        door_right.render(matrixStack, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    public void renderLamp(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        lamp.render(matrixStack, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    public void renderBoti(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        boti.render(matrixStack, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    public void setupAnim(BaseTardisExteriorBlockEntity tile) {
        if (tile.getCachedState().get(BaseTardisExteriorBlock.OPEN)) {
            this.door_left.yaw = -this.doorAngle;
            this.door_right.yaw = this.doorAngle;
        }
    }
}

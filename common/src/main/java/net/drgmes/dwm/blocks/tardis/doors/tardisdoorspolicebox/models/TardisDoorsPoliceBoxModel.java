package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.models;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.TardisDoorsPoliceBoxBlock;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.TardisDoorsPoliceBoxBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;

public class TardisDoorsPoliceBoxModel extends Model {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(DWM.getIdentifier("textures/block/tardis/doors/tardis_doors_police_box.png"), "main");

    private final ModelPart base;
    private final ModelPart door_left;
    private final ModelPart door_right;

    public TardisDoorsPoliceBoxModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucentCull);

        this.base = root.getChild("base");
        this.door_left = root.getChild("door_left");
        this.door_right = root.getChild("door_right");
    }

    @SuppressWarnings("unused")
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(22, 20).cuboid(-8.0F, -1.5F, -7.95F, 16.0F, 1.0F, 4.0F), ModelTransform.pivot(0.0F, 24.5F, 6.0F));
        ModelPartData platform_beams = base.addChild("platform_beams", ModelPartBuilder.create().uv(12, 24).cuboid(-8.0F, -27.525F, -8.0F, 2.0F, 27.0F, 4.0F).uv(0, 24).cuboid(6.0F, -27.525F, -8.0F, 2.0F, 27.0F, 4.0F), ModelTransform.pivot(0.0F, 0.5F, 0.0F));
        ModelPartData door_corners = base.addChild("door_corners", ModelPartBuilder.create().uv(55, 54).cuboid(-6.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F).uv(47, 55).cuboid(5.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F).uv(24, 25).cuboid(-7.5F, -27.0F, -7.3F, 15.0F, 3.0F, 3.0F).uv(24, 25).cuboid(-7.5F, -27.025F, -8.3F, 15.0F, 3.0F, 3.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData door_left = modelPartData.addChild("door_left", ModelPartBuilder.create().uv(26, 0).cuboid(0.55F, -3.625F, -0.1F, 4.0F, 14.0F, 1.0F).uv(28, 31).cuboid(4.85F, -2.125F, -0.8F, 1.0F, 1.0F, 1.0F).uv(8, 24).cuboid(1.05F, -3.125F, -0.2F, 3.0F, 3.0F, 1.0F).uv(26, 15).cuboid(1.05F, -3.125F, 0.8F, 3.0F, 3.0F, 2.0F), ModelTransform.pivot(-5.55F, 12.125F, -0.4F));
        ModelPartData door_left_separators = door_left.addChild("door_left_separators", ModelPartBuilder.create().uv(56, 39).cuboid(-5.0F, -2.0F, -7.0F, 4.0F, 1.0F, 1.0F).uv(53, 16).cuboid(-5.0F, -2.0F, -6.0F, 4.0F, 1.0F, 1.0F).uv(56, 37).cuboid(-5.0F, -7.0F, -7.0F, 4.0F, 1.0F, 1.0F).uv(53, 14).cuboid(-5.0F, -7.0F, -6.0F, 4.0F, 1.0F, 1.0F).uv(56, 35).cuboid(-5.0F, -12.0F, -7.0F, 4.0F, 1.0F, 1.0F).uv(34, 15).cuboid(-5.0F, -12.0F, -6.0F, 4.0F, 1.0F, 1.0F).uv(34, 56).cuboid(-5.0F, -17.0F, -7.0F, 4.0F, 1.0F, 1.0F).uv(45, 18).cuboid(-5.0F, -17.0F, -6.0F, 4.0F, 1.0F, 1.0F).uv(56, 33).cuboid(-5.0F, -24.0F, -7.0F, 4.0F, 1.0F, 1.0F).uv(36, 17).cuboid(-5.0F, -24.0F, -6.0F, 4.0F, 1.0F, 1.0F), ModelTransform.pivot(5.55F, 12.375F, 6.4F));
        ModelPartData door_left_corners = door_left.addChild("door_left_corners", ModelPartBuilder.create().uv(12, 55).cuboid(-6.025F, -24.0F, -6.975F, 1.0F, 23.0F, 1.0F).uv(4, 55).cuboid(-6.025F, -24.0F, -5.975F, 1.0F, 23.0F, 1.0F).uv(8, 55).cuboid(-1.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F).uv(0, 55).cuboid(-1.0F, -24.0F, -6.0F, 1.0F, 23.0F, 1.0F), ModelTransform.pivot(5.55F, 12.375F, 6.4F));
        ModelPartData door_left_window = door_left.addChild("door_left_window", ModelPartBuilder.create().uv(46, 0).cuboid(-5.0F, -23.2F, -6.4F, 4.0F, 6.0F, 1.0F), ModelTransform.pivot(5.55F, 12.375F, 6.4F));
        ModelPartData door_left_window_borders_a = door_left_window.addChild("door_left_window_borders_a", ModelPartBuilder.create().uv(59, 54).cuboid(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F).uv(36, 58).cuboid(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F).uv(56, 31).cuboid(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F).uv(24, 56).cuboid(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData door_left_window_border_a_middle = door_left_window_borders_a.addChild("door_left_window_border_a_middle", ModelPartBuilder.create().uv(57, 20).cuboid(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F).uv(56, 27).cuboid(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F), ModelTransform.pivot(0.0F, -18.9F, 0.0F));
        ModelPartData door_left_window_border_a_y_a = door_left_window_borders_a.addChild("door_left_window_border_a_y_a", ModelPartBuilder.create().uv(65, 13).cuboid(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F).uv(38, 64).cuboid(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F), ModelTransform.pivot(0.1F, -18.9F, 0.0F));
        ModelPartData door_left_window_border_a_y_b = door_left_window_borders_a.addChild("door_left_window_border_a_y_b", ModelPartBuilder.create().uv(36, 64).cuboid(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F).uv(34, 64).cuboid(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F), ModelTransform.pivot(1.4F, -18.9F, 0.0F));
        ModelPartData door_left_window_borders_b = door_left_window.addChild("door_left_window_borders_b", ModelPartBuilder.create().uv(32, 58).cuboid(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F).uv(28, 58).cuboid(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F).uv(56, 12).cuboid(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F).uv(56, 10).cuboid(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F), ModelTransform.pivot(0.0F, 0.0F, 0.25F));
        ModelPartData door_left_window_border_b_middle = door_left_window_borders_b.addChild("door_left_window_border_b_middle", ModelPartBuilder.create().uv(55, 53).cuboid(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F).uv(45, 14).cuboid(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F), ModelTransform.pivot(0.0F, -18.9F, 0.0F));
        ModelPartData door_left_window_border_b_y_a = door_left_window_borders_b.addChild("door_left_window_border_b_y_a", ModelPartBuilder.create().uv(32, 64).cuboid(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F).uv(30, 64).cuboid(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F), ModelTransform.pivot(0.1F, -18.9F, 0.0F));
        ModelPartData door_left_window_border_b_y_b = door_left_window_borders_b.addChild("door_left_window_border_b_y_b", ModelPartBuilder.create().uv(28, 64).cuboid(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F).uv(26, 64).cuboid(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F), ModelTransform.pivot(1.4F, -18.9F, 0.0F));
        ModelPartData door_left_beams = door_left.addChild("door_left_beams", ModelPartBuilder.create().uv(36, 31).cuboid(5.05F, -11.625F, -1.1F, 1.0F, 23.0F, 2.0F).uv(30, 31).cuboid(5.025F, -11.625F, -0.1F, 1.0F, 23.0F, 2.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData door_right = modelPartData.addChild("door_right", ModelPartBuilder.create().uv(36, 0).cuboid(-4.55F, -3.625F, -0.1F, 4.0F, 14.0F, 1.0F).uv(20, 25).cuboid(-3.55F, -2.625F, -0.11F, 2.0F, 2.0F, 1.0F).uv(0, 24).cuboid(-5.85F, -2.625F, -0.8F, 1.0F, 2.0F, 1.0F), ModelTransform.pivot(5.55F, 12.125F, -0.4F));
        ModelPartData door_right_separators = door_right.addChild("door_right_separators", ModelPartBuilder.create().uv(57, 25).cuboid(-5.0F, -2.0F, -7.0F, 4.0F, 1.0F, 1.0F).uv(55, 6).cuboid(-5.0F, -2.0F, -6.0F, 4.0F, 1.0F, 1.0F).uv(56, 51).cuboid(-5.0F, -7.0F, -7.0F, 4.0F, 1.0F, 1.0F).uv(55, 18).cuboid(-5.0F, -7.0F, -6.0F, 4.0F, 1.0F, 1.0F).uv(56, 49).cuboid(-5.0F, -12.0F, -7.0F, 4.0F, 1.0F, 1.0F).uv(56, 0).cuboid(-5.0F, -12.0F, -6.0F, 4.0F, 1.0F, 1.0F).uv(56, 47).cuboid(-5.0F, -17.0F, -7.0F, 4.0F, 1.0F, 1.0F).uv(56, 2).cuboid(-5.0F, -17.0F, -6.0F, 4.0F, 1.0F, 1.0F).uv(56, 45).cuboid(-5.0F, -24.0F, -7.0F, 4.0F, 1.0F, 1.0F).uv(44, 15).cuboid(-5.0F, -24.0F, -6.0F, 4.0F, 1.0F, 1.0F), ModelTransform.pivot(0.45F, 12.375F, 6.4F));
        ModelPartData door_right_corners = door_right.addChild("door_right_corners", ModelPartBuilder.create().uv(20, 55).cuboid(-6.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F).uv(52, 31).cuboid(-6.0F, -24.0F, -6.0F, 1.0F, 23.0F, 1.0F).uv(16, 55).cuboid(-0.975F, -24.0F, -6.975F, 1.0F, 23.0F, 1.0F).uv(48, 31).cuboid(-0.975F, -24.0F, -5.975F, 1.0F, 23.0F, 1.0F), ModelTransform.pivot(0.45F, 12.375F, 6.4F));
        ModelPartData door_right_window = door_right.addChild("door_right_window", ModelPartBuilder.create().uv(46, 7).cuboid(-5.0F, -23.2F, -6.4F, 4.0F, 6.0F, 1.0F), ModelTransform.pivot(0.45F, 12.375F, 6.4F));
        ModelPartData door_right_window_borders_a = door_right_window.addChild("door_right_window_borders_a", ModelPartBuilder.create().uv(40, 63).cuboid(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F).uv(59, 61).cuboid(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F).uv(56, 43).cuboid(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F).uv(56, 41).cuboid(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData door_right_window_border_a_middle = door_right_window_borders_a.addChild("door_right_window_border_a_middle", ModelPartBuilder.create().uv(57, 22).cuboid(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F).uv(57, 21).cuboid(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F), ModelTransform.pivot(0.0F, -18.9F, 0.0F));
        ModelPartData door_right_window_border_a_y_a = door_right_window_borders_a.addChild("door_right_window_border_a_y_a", ModelPartBuilder.create().uv(65, 64).cuboid(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F).uv(63, 65).cuboid(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F), ModelTransform.pivot(0.1F, -18.9F, 0.0F));
        ModelPartData door_right_window_border_a_y_b = door_right_window_borders_a.addChild("door_right_window_border_a_y_b", ModelPartBuilder.create().uv(65, 58).cuboid(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F).uv(65, 52).cuboid(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F), ModelTransform.pivot(1.4F, -18.9F, 0.0F));
        ModelPartData door_right_window_borders_b = door_right_window.addChild("door_right_window_borders_b", ModelPartBuilder.create().uv(24, 58).cuboid(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F).uv(43, 57).cuboid(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F).uv(56, 8).cuboid(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F).uv(56, 4).cuboid(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F), ModelTransform.pivot(0.0F, 0.0F, 0.25F));
        ModelPartData door_right_window_border_b_middle = door_right_window_borders_b.addChild("door_right_window_border_b_middle", ModelPartBuilder.create().uv(44, 17).cuboid(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F).uv(35, 19).cuboid(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F), ModelTransform.pivot(0.0F, -18.9F, 0.0F));
        ModelPartData door_right_window_border_b_y_a = door_right_window_borders_b.addChild("door_right_window_border_b_y_a", ModelPartBuilder.create().uv(24, 64).cuboid(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F).uv(63, 59).cuboid(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F), ModelTransform.pivot(0.1F, -18.9F, 0.0F));
        ModelPartData door_right_window_border_b_y_b = door_right_window_borders_b.addChild("door_right_window_border_b_y_b", ModelPartBuilder.create().uv(63, 53).cuboid(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F).uv(44, 63).cuboid(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F), ModelTransform.pivot(1.4F, -18.9F, 0.0F));
        ModelPartData door_right_beams = door_right.addChild("door_right_beams", ModelPartBuilder.create().uv(42, 31).cuboid(-6.5F, -24.0F, -7.525F, 1.0F, 23.0F, 2.0F).uv(24, 31).cuboid(-6.475F, -24.0F, -6.475F, 1.0F, 23.0F, 2.0F), ModelTransform.pivot(0.45F, 12.375F, 6.4F));

        return TexturedModelData.of(modelData, 128, 128);
    }

    public void setupAnim(TardisDoorsPoliceBoxBlockEntity tile) {
        float doorAngle = 1.75F;

        if (tile.getCachedState().get(TardisDoorsPoliceBoxBlock.OPEN)) {
            this.door_left.yaw = -doorAngle;
            this.door_right.yaw = doorAngle;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        base.render(matrixStack, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    public void renderDoors(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        door_left.render(matrixStack, vertexConsumer, light, overlay, red, green, blue, alpha);
        door_right.render(matrixStack, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}

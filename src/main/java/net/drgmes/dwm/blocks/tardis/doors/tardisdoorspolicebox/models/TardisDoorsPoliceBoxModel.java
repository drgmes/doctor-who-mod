package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.drgmes.dwm.DWM;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class TardisDoorsPoliceBoxModel extends EntityModel<Entity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DWM.MODID, "textures/entity/tardis/doors/tardis_doors_police_box.png"), "main");
    private final ModelPart base;
    private final ModelPart boti;
    private final ModelPart door_left;
    private final ModelPart door_right;

    public TardisDoorsPoliceBoxModel(ModelPart root) {
        this.base = root.getChild("base");
        this.boti = root.getChild("boti");
        this.door_left = root.getChild("door_left");
        this.door_right = root.getChild("door_right");
    }

    public void setupAnim(BlockState blockState) {
        float doorAngle = 1.75F;

        if (blockState.getValue(BlockStateProperties.OPEN)) {
            this.door_left.yRot = -doorAngle;
            this.door_right.yRot = doorAngle;
        }
    }

    @Override
    public void setupAnim(Entity entity, float f1, float f2, float f3, float f4, float f5) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        base.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void renderDoorsToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        door_left.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        door_right.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void renderBotiToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        boti.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(22, 20).addBox(-8.0F, -1.5F, -7.95F, 16.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.5F, 6.0F));
        PartDefinition platform_beams = base.addOrReplaceChild("platform_beams", CubeListBuilder.create().texOffs(12, 24).addBox(-8.0F, -27.525F, -8.0F, 2.0F, 27.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(0, 24).addBox(6.0F, -27.525F, -8.0F, 2.0F, 27.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.0F));
        PartDefinition door_corners = base.addOrReplaceChild("door_corners", CubeListBuilder.create().texOffs(55, 54).addBox(-6.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(47, 55).addBox(5.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 25).addBox(-7.5F, -27.0F, -7.3F, 15.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(24, 25).addBox(-7.5F, -27.025F, -8.3F, 15.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition door_left = partdefinition.addOrReplaceChild("door_left", CubeListBuilder.create().texOffs(26, 0).addBox(0.55F, -3.625F, -0.1F, 4.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(28, 31).addBox(4.85F, -2.125F, -0.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 24).addBox(1.05F, -3.125F, -0.2F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(26, 15).addBox(1.05F, -3.125F, 0.8F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.55F, 12.125F, -0.4F));
        PartDefinition door_left_separators = door_left.addOrReplaceChild("door_left_separators", CubeListBuilder.create().texOffs(56, 39).addBox(-5.0F, -2.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(53, 16).addBox(-5.0F, -2.0F, -6.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 37).addBox(-5.0F, -7.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(53, 14).addBox(-5.0F, -7.0F, -6.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 35).addBox(-5.0F, -12.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(34, 15).addBox(-5.0F, -12.0F, -6.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(34, 56).addBox(-5.0F, -17.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 18).addBox(-5.0F, -17.0F, -6.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 33).addBox(-5.0F, -24.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 17).addBox(-5.0F, -24.0F, -6.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.55F, 12.375F, 6.4F));
        PartDefinition door_left_corners = door_left.addOrReplaceChild("door_left_corners", CubeListBuilder.create().texOffs(12, 55).addBox(-6.025F, -24.0F, -6.975F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 55).addBox(-6.025F, -24.0F, -5.975F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 55).addBox(-1.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 55).addBox(-1.0F, -24.0F, -6.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.55F, 12.375F, 6.4F));
        PartDefinition door_left_window = door_left.addOrReplaceChild("door_left_window", CubeListBuilder.create().texOffs(46, 0).addBox(-5.0F, -23.2F, -6.4F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.55F, 12.375F, 6.4F));
        PartDefinition door_left_window_borders_a = door_left_window.addOrReplaceChild("door_left_window_borders_a", CubeListBuilder.create().texOffs(59, 54).addBox(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 58).addBox(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 31).addBox(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 56).addBox(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition door_left_window_border_a_middle = door_left_window_borders_a.addOrReplaceChild("door_left_window_border_a_middle", CubeListBuilder.create().texOffs(57, 20).addBox(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 27).addBox(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.9F, 0.0F));
        PartDefinition door_left_window_border_a_y_a = door_left_window_borders_a.addOrReplaceChild("door_left_window_border_a_y_a", CubeListBuilder.create().texOffs(65, 13).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(38, 64).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, -18.9F, 0.0F));
        PartDefinition door_left_window_border_a_y_b = door_left_window_borders_a.addOrReplaceChild("door_left_window_border_a_y_b", CubeListBuilder.create().texOffs(36, 64).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(34, 64).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.4F, -18.9F, 0.0F));
        PartDefinition door_left_window_borders_b = door_left_window.addOrReplaceChild("door_left_window_borders_b", CubeListBuilder.create().texOffs(32, 58).addBox(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(28, 58).addBox(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 12).addBox(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 10).addBox(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.25F));
        PartDefinition door_left_window_border_b_middle = door_left_window_borders_b.addOrReplaceChild("door_left_window_border_b_middle", CubeListBuilder.create().texOffs(55, 53).addBox(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(45, 14).addBox(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.9F, 0.0F));
        PartDefinition door_left_window_border_b_y_a = door_left_window_borders_b.addOrReplaceChild("door_left_window_border_b_y_a", CubeListBuilder.create().texOffs(32, 64).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 64).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, -18.9F, 0.0F));
        PartDefinition door_left_window_border_b_y_b = door_left_window_borders_b.addOrReplaceChild("door_left_window_border_b_y_b", CubeListBuilder.create().texOffs(28, 64).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(26, 64).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.4F, -18.9F, 0.0F));
        PartDefinition door_left_beams = door_left.addOrReplaceChild("door_left_beams", CubeListBuilder.create().texOffs(36, 31).addBox(5.05F, -11.625F, -1.1F, 1.0F, 23.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(30, 31).addBox(5.025F, -11.625F, -0.1F, 1.0F, 23.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition door_right = partdefinition.addOrReplaceChild("door_right", CubeListBuilder.create().texOffs(36, 0).addBox(-4.55F, -3.625F, -0.1F, 4.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 25).addBox(-3.55F, -2.625F, -0.11F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 24).addBox(-5.85F, -2.625F, -0.8F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.55F, 12.125F, -0.4F));
        PartDefinition door_right_separators = door_right.addOrReplaceChild("door_right_separators", CubeListBuilder.create().texOffs(57, 25).addBox(-5.0F, -2.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(55, 6).addBox(-5.0F, -2.0F, -6.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 51).addBox(-5.0F, -7.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(55, 18).addBox(-5.0F, -7.0F, -6.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 49).addBox(-5.0F, -12.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 0).addBox(-5.0F, -12.0F, -6.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 47).addBox(-5.0F, -17.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 2).addBox(-5.0F, -17.0F, -6.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 45).addBox(-5.0F, -24.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(44, 15).addBox(-5.0F, -24.0F, -6.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.45F, 12.375F, 6.4F));
        PartDefinition door_right_corners = door_right.addOrReplaceChild("door_right_corners", CubeListBuilder.create().texOffs(20, 55).addBox(-6.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(52, 31).addBox(-6.0F, -24.0F, -6.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(16, 55).addBox(-0.975F, -24.0F, -6.975F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 31).addBox(-0.975F, -24.0F, -5.975F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.45F, 12.375F, 6.4F));
        PartDefinition door_right_window = door_right.addOrReplaceChild("door_right_window", CubeListBuilder.create().texOffs(46, 7).addBox(-5.0F, -23.2F, -6.4F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.45F, 12.375F, 6.4F));
        PartDefinition door_right_window_borders_a = door_right_window.addOrReplaceChild("door_right_window_borders_a", CubeListBuilder.create().texOffs(40, 63).addBox(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(59, 61).addBox(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 43).addBox(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 41).addBox(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition door_right_window_border_a_middle = door_right_window_borders_a.addOrReplaceChild("door_right_window_border_a_middle", CubeListBuilder.create().texOffs(57, 22).addBox(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(57, 21).addBox(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.9F, 0.0F));
        PartDefinition door_right_window_border_a_y_a = door_right_window_borders_a.addOrReplaceChild("door_right_window_border_a_y_a", CubeListBuilder.create().texOffs(65, 64).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(63, 65).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, -18.9F, 0.0F));
        PartDefinition door_right_window_border_a_y_b = door_right_window_borders_a.addOrReplaceChild("door_right_window_border_a_y_b", CubeListBuilder.create().texOffs(65, 58).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(65, 52).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.4F, -18.9F, 0.0F));
        PartDefinition door_right_window_borders_b = door_right_window.addOrReplaceChild("door_right_window_borders_b", CubeListBuilder.create().texOffs(24, 58).addBox(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(43, 57).addBox(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 8).addBox(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(56, 4).addBox(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.25F));
        PartDefinition door_right_window_border_b_middle = door_right_window_borders_b.addOrReplaceChild("door_right_window_border_b_middle", CubeListBuilder.create().texOffs(44, 17).addBox(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(35, 19).addBox(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.9F, 0.0F));
        PartDefinition door_right_window_border_b_y_a = door_right_window_borders_b.addOrReplaceChild("door_right_window_border_b_y_a", CubeListBuilder.create().texOffs(24, 64).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(63, 59).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, -18.9F, 0.0F));
        PartDefinition door_right_window_border_b_y_b = door_right_window_borders_b.addOrReplaceChild("door_right_window_border_b_y_b", CubeListBuilder.create().texOffs(63, 53).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(44, 63).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.4F, -18.9F, 0.0F));
        PartDefinition door_right_beams = door_right.addOrReplaceChild("door_right_beams", CubeListBuilder.create().texOffs(42, 31).addBox(-6.5F, -24.0F, -7.525F, 1.0F, 23.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(24, 31).addBox(-6.475F, -24.0F, -6.475F, 1.0F, 23.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.45F, 12.375F, 6.4F));
        PartDefinition boti = partdefinition.addOrReplaceChild("boti", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -24.0F, -3.0F, 12.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}

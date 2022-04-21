package net.drgmes.dwm.blocks.tardisexterior.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlockEntity;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TardisPoliceBoxExteriorModel extends EntityModel<Entity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DWM.MODID, "textures/entity/exteriors/tardis_police_box.png"), "main");
    private final ModelPart base;
    private final ModelPart door_left;
    private final ModelPart door_right;

    public TardisPoliceBoxExteriorModel(ModelPart root) {
        this.base = root.getChild("base");
        this.door_left = root.getChild("door_left");
        this.door_right = root.getChild("door_right");
    }

    public void setupAnim(TardisExteriorBlockEntity tile) {
        BlockState blockState = tile.getBlockState();
        float doorAngle = 1.46F;

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
        base.render(poseStack, buffer, packedLight, packedOverlay);
        door_left.render(poseStack, buffer, packedLight, packedOverlay);
        door_right.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -1.0F, -9.0F, 18.0F, 1.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition platform_beams = base.addOrReplaceChild("platform_beams", CubeListBuilder.create().texOffs(22, 62).addBox(-8.0F, -27.0F, 6.0F, 2.0F, 26.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(22, 62).addBox(6.0F, -27.0F, 6.0F, 2.0F, 26.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(22, 62).addBox(-8.0F, -27.0F, -8.0F, 2.0F, 26.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(22, 62).addBox(6.0F, -27.0F, -8.0F, 2.0F, 26.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(30, 62).addBox(-7.3F, -27.5F, 6.3F, 1.0F, 26.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 62).addBox(-7.3F, -27.5F, -7.3F, 1.0F, 26.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 62).addBox(6.3F, -27.5F, -7.3F, 1.0F, 26.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 62).addBox(6.3F, -27.5F, 6.3F, 1.0F, 26.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.0F));
        PartDefinition labels = base.addOrReplaceChild("labels", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));
        PartDefinition label_front = labels.addOrReplaceChild("label_front", CubeListBuilder.create().texOffs(54, 10).addBox(-7.0F, -25.0F, -8.4F, 14.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_front_corners_left = label_front.addOrReplaceChild("label_front_corners_left", CubeListBuilder.create().texOffs(0, 13).addBox(-7.0F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 13).addBox(-6.9F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_front_corners_right = label_front.addOrReplaceChild("label_front_corners_right", CubeListBuilder.create().texOffs(0, 13).addBox(7.0F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 13).addBox(6.9F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_front_corners_top = label_front.addOrReplaceChild("label_front_corners_top", CubeListBuilder.create().texOffs(37, 35).addBox(-7.0F, -25.0F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(37, 35).addBox(-7.0F, -24.9F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_front_corners_bottom = label_front.addOrReplaceChild("label_front_corners_bottom", CubeListBuilder.create().texOffs(37, 35).addBox(-7.0F, -23.0001F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(37, 35).addBox(-7.0F, -23.1F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_back = labels.addOrReplaceChild("label_back", CubeListBuilder.create().texOffs(54, 10).addBox(-7.0F, -25.0F, -8.4F, 14.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
        PartDefinition label_back_corners_left = label_back.addOrReplaceChild("label_back_corners_left", CubeListBuilder.create().texOffs(0, 13).addBox(-7.0F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 13).addBox(-6.9F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_back_corners_right = label_back.addOrReplaceChild("label_back_corners_right", CubeListBuilder.create().texOffs(0, 13).addBox(7.0F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 13).addBox(6.9F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_back_corners_top = label_back.addOrReplaceChild("label_back_corners_top", CubeListBuilder.create().texOffs(37, 35).addBox(-7.0F, -25.0F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(37, 35).addBox(-7.0F, -24.9F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_back_corners_bottom = label_back.addOrReplaceChild("label_back_corners_bottom", CubeListBuilder.create().texOffs(37, 35).addBox(-7.0F, -23.0001F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(37, 35).addBox(-7.0F, -23.1F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_left = labels.addOrReplaceChild("label_left", CubeListBuilder.create().texOffs(54, 10).addBox(-7.0F, -25.0F, -8.4F, 14.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
        PartDefinition label_left_corners_left = label_left.addOrReplaceChild("label_left_corners_left", CubeListBuilder.create().texOffs(0, 13).addBox(-7.0F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 13).addBox(-6.9F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_left_corners_right = label_left.addOrReplaceChild("label_left_corners_right", CubeListBuilder.create().texOffs(0, 13).addBox(7.0F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 13).addBox(6.9F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_left_corners_top = label_left.addOrReplaceChild("label_left_corners_top", CubeListBuilder.create().texOffs(37, 35).addBox(-7.0F, -25.0F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(37, 35).addBox(-7.0F, -24.9F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_left_corners_bottom = label_left.addOrReplaceChild("label_left_corners_bottom", CubeListBuilder.create().texOffs(37, 35).addBox(-7.0F, -23.0001F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(37, 35).addBox(-7.0F, -23.1F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_right = labels.addOrReplaceChild("label_right", CubeListBuilder.create().texOffs(54, 10).addBox(-7.0F, -25.0F, -8.4F, 14.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition label_right_corners_left = label_right.addOrReplaceChild("label_right_corners_left", CubeListBuilder.create().texOffs(0, 13).addBox(-7.0F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 13).addBox(-6.9F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_right_corners_right = label_right.addOrReplaceChild("label_right_corners_right", CubeListBuilder.create().texOffs(0, 13).addBox(7.0F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 13).addBox(6.9F, -25.0F, -8.6F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_right_corners_top = label_right.addOrReplaceChild("label_right_corners_top", CubeListBuilder.create().texOffs(37, 35).addBox(-7.0F, -25.0F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(37, 35).addBox(-7.0F, -24.9F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition label_right_corners_bottom = label_right.addOrReplaceChild("label_right_corners_bottom", CubeListBuilder.create().texOffs(37, 35).addBox(-7.0F, -23.0001F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(37, 35).addBox(-7.0F, -23.1F, -8.6F, 14.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition roof = base.addOrReplaceChild("roof", CubeListBuilder.create().texOffs(0, 19).addBox(-7.1F, -27.0F, -6.9F, 14.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(0, 35).addBox(-6.6F, -27.2F, -6.4F, 13.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)).texOffs(40, 38).addBox(-6.1F, -27.4F, -5.9F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(42, 19).addBox(-5.6F, -27.6F, -5.4F, 11.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(0, 50).addBox(-5.1F, -27.8F, -4.9F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(40, 52).addBox(-4.6F, -28.0F, -4.4F, 9.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(54, 0).addBox(-4.1F, -28.2F, -3.9F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, -1.0F, -0.1F));
        PartDefinition lantern = roof.addOrReplaceChild("lantern", CubeListBuilder.create().texOffs(9, 5).addBox(-1.0F, -31.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 7).addBox(-1.5F, -28.3F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.1F, 0.0F, 0.1F));
        PartDefinition lantern_plate_1 = lantern.addOrReplaceChild("lantern_plate_1", CubeListBuilder.create().texOffs(7, 0).addBox(-1.5F, -30.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(7, 0).addBox(-1.5F, -30.1F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.8F, 0.0F));
        PartDefinition lantern_plate_2 = lantern.addOrReplaceChild("lantern_plate_2", CubeListBuilder.create().texOffs(7, 0).addBox(-1.5F, -30.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(7, 0).addBox(-1.5F, -30.1F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.4F, 0.0F));
        PartDefinition lantern_plate_3 = lantern.addOrReplaceChild("lantern_plate_3", CubeListBuilder.create().texOffs(7, 0).addBox(-1.5F, -30.0F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(7, 0).addBox(-1.5F, -30.1F, -1.5F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition walls = base.addOrReplaceChild("walls", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_left = walls.addOrReplaceChild("wall_left", CubeListBuilder.create().texOffs(0, 62).addBox(-5.0F, -16.0F, -6.5F, 10.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(34, 62).addBox(-1.0F, -24.0F, -7.1F, 2.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 63).addBox(-0.5F, -24.0F, -7.3F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
        PartDefinition wall_left_windows = wall_left.addOrReplaceChild("wall_left_windows", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_left_window_left = wall_left_windows.addOrReplaceChild("wall_left_window_left", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -23.2F, -6.4F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_left_window_left_borders = wall_left_window_left.addOrReplaceChild("wall_left_window_left_borders", CubeListBuilder.create().texOffs(0, 19).addBox(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 19).addBox(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_left_window_left_border_middle = wall_left_window_left_borders.addOrReplaceChild("wall_left_window_left_border_middle", CubeListBuilder.create().texOffs(9, 3).addBox(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 3).addBox(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.9F, 0.0F));
        PartDefinition wall_left_window_left_border_y_a = wall_left_window_left_borders.addOrReplaceChild("wall_left_window_left_border_y_a", CubeListBuilder.create().texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, -18.9F, 0.0F));
        PartDefinition wall_left_window_left_border_y_b = wall_left_window_left_borders.addOrReplaceChild("wall_left_window_left_border_y_b", CubeListBuilder.create().texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.4F, -18.9F, 0.0F));
        PartDefinition wall_left_window_right = wall_left_windows.addOrReplaceChild("wall_left_window_right", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -23.2F, -6.4F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 0.0F, 0.0F));
        PartDefinition wall_left_window_right_borders = wall_left_window_right.addOrReplaceChild("wall_left_window_right_borders", CubeListBuilder.create().texOffs(0, 19).addBox(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 19).addBox(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_left_window_right_border_middle = wall_left_window_right_borders.addOrReplaceChild("wall_left_window_right_border_middle", CubeListBuilder.create().texOffs(9, 3).addBox(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 3).addBox(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.9F, 0.0F));
        PartDefinition wall_left_window_right_border_y_a = wall_left_window_right_borders.addOrReplaceChild("wall_left_window_right_border_y_a", CubeListBuilder.create().texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, -18.9F, 0.0F));
        PartDefinition wall_left_window_right_border_y_b = wall_left_window_right_borders.addOrReplaceChild("wall_left_window_right_border_y_b", CubeListBuilder.create().texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.4F, -18.9F, 0.0F));
        PartDefinition wall_left_corners = wall_left.addOrReplaceChild("wall_left_corners", CubeListBuilder.create().texOffs(54, 14).addBox(-6.0F, -24.7F, -7.3F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 63).addBox(-6.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 63).addBox(5.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 62).addBox(-6.7F, -27.0F, -7.3F, 1.0F, 26.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 62).addBox(5.7F, -27.0F, -7.3F, 1.0F, 26.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_left_separators = wall_left.addOrReplaceChild("wall_left_separators", CubeListBuilder.create().texOffs(54, 16).addBox(-5.0F, -2.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -7.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -12.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -17.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -24.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_right = walls.addOrReplaceChild("wall_right", CubeListBuilder.create().texOffs(0, 62).addBox(-5.0F, -16.0F, -6.5F, 10.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(34, 62).addBox(-1.0F, -24.0F, -7.1F, 2.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 63).addBox(-0.5F, -24.0F, -7.3F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition wall_right_windows = wall_right.addOrReplaceChild("wall_right_windows", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_right_window_left = wall_right_windows.addOrReplaceChild("wall_right_window_left", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -23.2F, -6.4F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_right_window_left_borders = wall_right_window_left.addOrReplaceChild("wall_right_window_left_borders", CubeListBuilder.create().texOffs(0, 19).addBox(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 19).addBox(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_right_window_left_border_middle = wall_right_window_left_borders.addOrReplaceChild("wall_right_window_left_border_middle", CubeListBuilder.create().texOffs(9, 3).addBox(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 3).addBox(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.9F, 0.0F));
        PartDefinition wall_right_window_left_border_y_a = wall_right_window_left_borders.addOrReplaceChild("wall_right_window_left_border_y_a", CubeListBuilder.create().texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, -18.9F, 0.0F));
        PartDefinition wall_right_window_left_border_y_b = wall_right_window_left_borders.addOrReplaceChild("wall_right_window_left_border_y_b", CubeListBuilder.create().texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.4F, -18.9F, 0.0F));
        PartDefinition wall_right_window_right = wall_right_windows.addOrReplaceChild("wall_right_window_right", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -23.2F, -6.4F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 0.0F, 0.0F));
        PartDefinition wall_right_window_right_borders = wall_right_window_right.addOrReplaceChild("wall_right_window_right_borders", CubeListBuilder.create().texOffs(0, 19).addBox(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 19).addBox(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_right_window_right_border_middle = wall_right_window_right_borders.addOrReplaceChild("wall_right_window_right_border_middle", CubeListBuilder.create().texOffs(9, 3).addBox(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 3).addBox(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.9F, 0.0F));
        PartDefinition wall_right_window_right_border_y_a = wall_right_window_right_borders.addOrReplaceChild("wall_right_window_right_border_y_a", CubeListBuilder.create().texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, -18.9F, 0.0F));
        PartDefinition wall_right_window_right_border_y_b = wall_right_window_right_borders.addOrReplaceChild("wall_right_window_right_border_y_b", CubeListBuilder.create().texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.4F, -18.9F, 0.0F));
        PartDefinition wall_right_corners = wall_right.addOrReplaceChild("wall_right_corners", CubeListBuilder.create().texOffs(54, 14).addBox(-6.0F, -24.7F, -7.3F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 63).addBox(-6.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 63).addBox(5.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 62).addBox(-6.7F, -27.0F, -7.3F, 1.0F, 26.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 62).addBox(5.7F, -27.0F, -7.3F, 1.0F, 26.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_right_separators = wall_right.addOrReplaceChild("wall_right_separators", CubeListBuilder.create().texOffs(54, 16).addBox(-5.0F, -2.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -7.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -12.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -17.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -24.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_back = walls.addOrReplaceChild("wall_back", CubeListBuilder.create().texOffs(0, 62).addBox(-5.0F, -16.0F, -6.5F, 10.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(34, 62).addBox(-1.0F, -24.0F, -7.1F, 2.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 63).addBox(-0.5F, -24.0F, -7.3F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
        PartDefinition wall_back_windows = wall_back.addOrReplaceChild("wall_back_windows", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_back_window_left = wall_back_windows.addOrReplaceChild("wall_back_window_left", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -23.2F, -6.4F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_back_window_left_borders = wall_back_window_left.addOrReplaceChild("wall_back_window_left_borders", CubeListBuilder.create().texOffs(0, 19).addBox(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 19).addBox(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_back_window_left_border_middle = wall_back_window_left_borders.addOrReplaceChild("wall_back_window_left_border_middle", CubeListBuilder.create().texOffs(9, 3).addBox(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 3).addBox(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.9F, 0.0F));
        PartDefinition wall_back_window_left_border_y_a = wall_back_window_left_borders.addOrReplaceChild("wall_back_window_left_border_y_a", CubeListBuilder.create().texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, -18.9F, 0.0F));
        PartDefinition wall_back_window_left_border_y_b = wall_back_window_left_borders.addOrReplaceChild("wall_back_window_left_border_y_b", CubeListBuilder.create().texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.4F, -18.9F, 0.0F));
        PartDefinition wall_back_window_right = wall_back_windows.addOrReplaceChild("wall_back_window_right", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -23.2F, -6.4F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 0.0F, 0.0F));
        PartDefinition wall_back_window_right_borders = wall_back_window_right.addOrReplaceChild("wall_back_window_right_borders", CubeListBuilder.create().texOffs(0, 19).addBox(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 19).addBox(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_back_window_right_border_middle = wall_back_window_right_borders.addOrReplaceChild("wall_back_window_right_border_middle", CubeListBuilder.create().texOffs(9, 3).addBox(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 3).addBox(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.9F, 0.0F));
        PartDefinition wall_back_window_right_border_y_a = wall_back_window_right_borders.addOrReplaceChild("wall_back_window_right_border_y_a", CubeListBuilder.create().texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, -18.9F, 0.0F));
        PartDefinition wall_back_window_right_border_y_b = wall_back_window_right_borders.addOrReplaceChild("wall_back_window_right_border_y_b", CubeListBuilder.create().texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.4F, -18.9F, 0.0F));
        PartDefinition wall_back_corners = wall_back.addOrReplaceChild("wall_back_corners", CubeListBuilder.create().texOffs(54, 14).addBox(-6.0F, -24.7F, -7.3F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 63).addBox(-6.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 63).addBox(5.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 62).addBox(-6.7F, -27.0F, -7.3F, 1.0F, 26.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 62).addBox(5.7F, -27.0F, -7.3F, 1.0F, 26.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition wall_back_separators = wall_back.addOrReplaceChild("wall_back_separators", CubeListBuilder.create().texOffs(54, 16).addBox(-5.0F, -2.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -7.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -12.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -17.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -24.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition door_corners = base.addOrReplaceChild("door_corners", CubeListBuilder.create().texOffs(30, 62).addBox(-6.7F, -27.0F, -7.3F, 1.0F, 26.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(30, 62).addBox(5.7F, -27.0F, -7.3F, 1.0F, 26.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 14).addBox(-6.0F, -24.7F, -7.3F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 63).addBox(5.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition door_left = partdefinition.addOrReplaceChild("door_left", CubeListBuilder.create().texOffs(40, 63).addBox(5.05F, -11.625F, -0.9F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 62).addBox(0.55F, -3.625F, -0.1F, 4.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 11).addBox(1.05F, -3.125F, -0.2F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 15).addBox(4.85F, -2.125F, -0.8F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.55F, 11.625F, -6.4F));
        PartDefinition door_left_separators = door_left.addOrReplaceChild("door_left_separators", CubeListBuilder.create().texOffs(54, 16).addBox(-5.0F, -2.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -7.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -12.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -17.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -24.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.55F, 12.375F, 6.4F));
        PartDefinition door_left_corners = door_left.addOrReplaceChild("door_left_corners", CubeListBuilder.create().texOffs(40, 63).addBox(-6.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 63).addBox(-1.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.55F, 12.375F, 6.4F));
        PartDefinition door_left_window = door_left.addOrReplaceChild("door_left_window", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -23.2F, -6.4F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.55F, 12.375F, 6.4F));
        PartDefinition door_left_window_borders = door_left_window.addOrReplaceChild("door_left_window_borders", CubeListBuilder.create().texOffs(0, 19).addBox(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 19).addBox(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition door_left_window_border_middle = door_left_window_borders.addOrReplaceChild("door_left_window_border_middle", CubeListBuilder.create().texOffs(9, 3).addBox(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 3).addBox(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.9F, 0.0F));
        PartDefinition door_left_window_border_y_a = door_left_window_borders.addOrReplaceChild("door_left_window_border_y_a", CubeListBuilder.create().texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, -18.9F, 0.0F));
        PartDefinition door_left_window_border_y_b = door_left_window_borders.addOrReplaceChild("door_left_window_border_y_b", CubeListBuilder.create().texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.4F, -18.9F, 0.0F));
        PartDefinition door_right = partdefinition.addOrReplaceChild("door_right", CubeListBuilder.create().texOffs(40, 63).addBox(-6.05F, -11.625F, -0.9F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 62).addBox(-4.55F, -3.625F, -0.1F, 4.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 13).addBox(-3.55F, -2.625F, -0.11F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(14, 13).addBox(-5.85F, -2.625F, -0.8F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.55F, 11.625F, -6.4F));
        PartDefinition door_right_separators = door_right.addOrReplaceChild("door_right_separators", CubeListBuilder.create().texOffs(54, 16).addBox(-5.0F, -2.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -7.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -12.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -17.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(54, 16).addBox(-5.0F, -24.0F, -7.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.45F, 12.375F, 6.4F));
        PartDefinition door_right_corners = door_right.addOrReplaceChild("door_right_corners", CubeListBuilder.create().texOffs(40, 63).addBox(-6.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 63).addBox(-1.0F, -24.0F, -7.0F, 1.0F, 23.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.45F, 12.375F, 6.4F));
        PartDefinition door_right_window = door_right.addOrReplaceChild("door_right_window", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -23.2F, -6.4F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.45F, 12.375F, 6.4F));
        PartDefinition door_right_window_borders = door_right_window.addOrReplaceChild("door_right_window_borders", CubeListBuilder.create().texOffs(0, 19).addBox(-5.8F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 19).addBox(-1.2F, -23.0F, -6.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -23.8F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(8, 11).addBox(-5.2F, -17.2F, -6.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition door_right_window_border_middle = door_right_window_borders.addOrReplaceChild("door_right_window_border_middle", CubeListBuilder.create().texOffs(9, 3).addBox(-5.2F, -1.0F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 3).addBox(-5.2F, -1.1F, -6.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.9F, 0.0F));
        PartDefinition door_right_window_border_y_a = door_right_window_borders.addOrReplaceChild("door_right_window_border_y_a", CubeListBuilder.create().texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, -18.9F, 0.0F));
        PartDefinition door_right_window_border_y_b = door_right_window_borders.addOrReplaceChild("door_right_window_border_y_b", CubeListBuilder.create().texOffs(4, 18).addBox(-3.7F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(4, 18).addBox(-3.8F, -4.1F, -6.5F, 0.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.4F, -18.9F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
}
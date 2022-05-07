package net.drgmes.dwm.blocks.tardis.others.tardistoyotaspinner.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.others.tardistoyotaspinner.TardisToyotaSpinnerBlockEntity;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TardisToyotaSpinnerModel extends EntityModel<Entity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DWM.MODID, "textures/entity/tardis/others/tardis_toyota_spinner.png"), "main");
    private final ModelPart platform;
    private final ModelPart rotor_1;
    private final ModelPart rotor_2;
    private final ModelPart rotor_3;

    public TardisToyotaSpinnerModel(ModelPart root) {
        this.platform = root.getChild("platform");
        this.rotor_1 = root.getChild("rotor_1");
        this.rotor_2 = root.getChild("rotor_2");
        this.rotor_3 = root.getChild("rotor_3");
    }

    public void setupAnim(TardisToyotaSpinnerBlockEntity tile) {
        float angle = 0.035F;
        this.platform.yRot -= tile.tickInProgress * angle;
        this.rotor_1.yRot += tile.tickInProgress * angle;
        this.rotor_2.yRot -= tile.tickInProgress * angle;
        this.rotor_3.yRot += tile.tickInProgress * angle;
    }

    @Override
    public void setupAnim(Entity entity, float f1, float f2, float f3, float f4, float f5) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        platform.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rotor_1.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rotor_2.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rotor_3.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition platform = partdefinition.addOrReplaceChild("platform", CubeListBuilder.create(), PartPose.offset(0.0F, 13.3F, 0.0F));
        PartDefinition platform_15_r1 = platform.addOrReplaceChild("platform_15_r1", CubeListBuilder.create().texOffs(0, 50).addBox(-10.0547F, -5.0F, -2.0F, 1.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2849F, -0.274F, -0.8249F));
        PartDefinition platform_14_r1 = platform.addOrReplaceChild("platform_14_r1", CubeListBuilder.create().texOffs(20, 80).addBox(-10.0547F, -5.0F, -2.0F, 1.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
        PartDefinition platform_13_r1 = platform.addOrReplaceChild("platform_13_r1", CubeListBuilder.create().texOffs(197, 27).addBox(-10.0547F, -5.0F, -2.0F, 1.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2849F, 0.274F, -0.8249F));
        PartDefinition platform_12_r1 = platform.addOrReplaceChild("platform_12_r1", CubeListBuilder.create().texOffs(32, 205).addBox(9.0547F, -5.0F, -2.0F, 1.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2849F, -0.274F, 0.8249F));
        PartDefinition platform_11_r1 = platform.addOrReplaceChild("platform_11_r1", CubeListBuilder.create().texOffs(42, 205).addBox(9.0547F, -5.0F, -2.0F, 1.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
        PartDefinition platform_10_r1 = platform.addOrReplaceChild("platform_10_r1", CubeListBuilder.create().texOffs(207, 81).addBox(9.0547F, -5.0F, -2.0F, 1.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2849F, 0.274F, 0.8249F));
        PartDefinition platform_9_r1 = platform.addOrReplaceChild("platform_9_r1", CubeListBuilder.create().texOffs(162, 211).addBox(-2.0F, -5.0F, 9.0547F, 4.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, -0.7854F, 0.0F));
        PartDefinition platform_8_r1 = platform.addOrReplaceChild("platform_8_r1", CubeListBuilder.create().texOffs(16, 212).addBox(-2.0F, -5.0F, 9.0547F, 4.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, -0.3927F, 0.0F));
        PartDefinition platform_7_r1 = platform.addOrReplaceChild("platform_7_r1", CubeListBuilder.create().texOffs(212, 146).addBox(-2.0F, -5.0F, 9.0547F, 4.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));
        PartDefinition platform_6_r1 = platform.addOrReplaceChild("platform_6_r1", CubeListBuilder.create().texOffs(91, 214).addBox(-2.0F, -5.0F, 9.0547F, 4.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.3927F, 0.0F));
        PartDefinition platform_5_r1 = platform.addOrReplaceChild("platform_5_r1", CubeListBuilder.create().texOffs(117, 214).addBox(-2.0F, -5.0F, 9.0547F, 4.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.7854F, 0.0F));
        PartDefinition platform_4_r1 = platform.addOrReplaceChild("platform_4_r1", CubeListBuilder.create().texOffs(214, 123).addBox(-2.0F, -5.0F, -10.0547F, 4.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, -0.7854F, 0.0F));
        PartDefinition platform_3_r1 = platform.addOrReplaceChild("platform_3_r1", CubeListBuilder.create().texOffs(214, 212).addBox(-2.0F, -5.0F, -10.0547F, 4.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, -0.3927F, 0.0F));
        PartDefinition platform_2_r1 = platform.addOrReplaceChild("platform_2_r1", CubeListBuilder.create().texOffs(101, 216).addBox(-2.0F, -5.0F, -10.0547F, 4.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));
        PartDefinition platform_1_r1 = platform.addOrReplaceChild("platform_1_r1", CubeListBuilder.create().texOffs(217, 75).addBox(-2.0F, -5.0F, -10.0547F, 4.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.3927F, 0.0F));
        PartDefinition platform_0_r1 = platform.addOrReplaceChild("platform_0_r1", CubeListBuilder.create().texOffs(189, 217).addBox(-2.0F, -5.0F, -10.0547F, 4.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.7854F, 0.0F));
        PartDefinition rotor_1 = partdefinition.addOrReplaceChild("rotor_1", CubeListBuilder.create(), PartPose.offset(0.0F, 25.8F, 0.0F));
        PartDefinition rotor_1_body = rotor_1.addOrReplaceChild("rotor_1_body", CubeListBuilder.create().texOffs(0, 186).addBox(-3.0F, -13.5F, -15.082F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(114, 183).addBox(-3.0F, -13.5F, 9.082F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(182, 0).addBox(9.082F, -13.5F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(24, 175).addBox(-15.082F, -13.5F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition rotor_1_body_15_r1 = rotor_1_body.addOrReplaceChild("rotor_1_body_15_r1", CubeListBuilder.create().texOffs(0, 175).addBox(-15.082F, -3.5F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(180, 128).addBox(9.082F, -3.5F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(90, 183).addBox(-3.0F, -3.5F, 9.082F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(138, 185).addBox(-3.0F, -3.5F, -15.082F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
        PartDefinition rotor_1_body_13_r1 = rotor_1_body.addOrReplaceChild("rotor_1_body_13_r1", CubeListBuilder.create().texOffs(72, 178).addBox(-15.082F, -3.5F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(183, 16).addBox(9.082F, -3.5F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(184, 109).addBox(-3.0F, -3.5F, 9.082F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(187, 74).addBox(-3.0F, -3.5F, -15.082F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, 0.3927F, 0.0F));
        PartDefinition rotor_1_body_9_r1 = rotor_1_body.addOrReplaceChild("rotor_1_body_9_r1", CubeListBuilder.create().texOffs(42, 183).addBox(-3.0F, -3.5F, 9.082F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(184, 182).addBox(-3.0F, -3.5F, -15.082F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition rotor_1_body_5_r1 = rotor_1_body.addOrReplaceChild("rotor_1_body_5_r1", CubeListBuilder.create().texOffs(184, 167).addBox(-3.0F, -3.5F, 9.082F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(162, 187).addBox(-3.0F, -3.5F, -15.082F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        PartDefinition rotor_1_corner = rotor_1.addOrReplaceChild("rotor_1_corner", CubeListBuilder.create().texOffs(202, 102).addBox(-0.75F, -3.5F, -15.5957F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(16, 199).addBox(-0.25F, -3.5F, 8.5957F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(92, 42).addBox(8.5957F, -3.5F, 0.25F, 7.0F, 6.0F, -1.0F, new CubeDeformation(0.0F)).texOffs(84, 207).addBox(-15.5957F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, 0.0F, -0.2182F, 0.0F));
        PartDefinition rotor_1_corner_15_r1 = rotor_1_corner.addOrReplaceChild("rotor_1_corner_15_r1", CubeListBuilder.create().texOffs(207, 17).addBox(-15.5957F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(211, 102).addBox(8.5957F, -3.5F, -0.75F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(158, 198).addBox(-0.25F, -3.5F, 8.5957F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(201, 188).addBox(-0.75F, -3.5F, -15.5957F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
        PartDefinition rotor_1_corner_13_r1 = rotor_1_corner.addOrReplaceChild("rotor_1_corner_13_r1", CubeListBuilder.create().texOffs(117, 207).addBox(-15.5957F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(146, 211).addBox(8.5957F, -3.5F, -0.75F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(188, 199).addBox(-0.25F, -3.5F, 8.5957F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(64, 40).addBox(0.25F, -3.5F, -15.5957F, -1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));
        PartDefinition rotor_1_corner_9_r1 = rotor_1_corner.addOrReplaceChild("rotor_1_corner_9_r1", CubeListBuilder.create().texOffs(198, 120).addBox(-0.25F, -3.5F, 8.5957F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(201, 4).addBox(-0.75F, -3.5F, -15.5957F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition rotor_1_corner_5_r1 = rotor_1_corner.addOrReplaceChild("rotor_1_corner_5_r1", CubeListBuilder.create().texOffs(66, 200).addBox(-0.25F, -3.5F, 8.5957F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(203, 133).addBox(-0.75F, -3.5F, -15.5957F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        PartDefinition rotor_2 = partdefinition.addOrReplaceChild("rotor_2", CubeListBuilder.create(), PartPose.offset(0.0F, 25.8F, 0.0F));
        PartDefinition rotor_2_body = rotor_2.addOrReplaceChild("rotor_2_body", CubeListBuilder.create().texOffs(134, 174).addBox(-4.0F, -10.5F, -20.1094F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(84, 167).addBox(-4.0F, -10.5F, 14.1094F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(164, 115).addBox(14.1094F, -10.5F, -4.0F, 6.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(0, 162).addBox(-20.1094F, -10.5F, -4.0F, 6.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));
        PartDefinition rotor_2_body_15_r1 = rotor_2_body.addOrReplaceChild("rotor_2_body_15_r1", CubeListBuilder.create().texOffs(154, 161).addBox(-20.1094F, -3.5F, -4.0F, 6.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(162, 102).addBox(14.1094F, -3.5F, -4.0F, 6.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(166, 141).addBox(-4.0F, -3.5F, 14.1094F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(106, 172).addBox(-4.0F, -3.5F, -20.1094F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
        PartDefinition rotor_2_body_13_r1 = rotor_2_body.addOrReplaceChild("rotor_2_body_13_r1", CubeListBuilder.create().texOffs(28, 162).addBox(-20.1094F, -3.5F, -4.0F, 6.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(165, 29).addBox(14.1094F, -3.5F, -4.0F, 6.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(171, 63).addBox(-4.0F, -3.5F, 14.1094F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(174, 155).addBox(-4.0F, -3.5F, -20.1094F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, 0.3927F, 0.0F));
        PartDefinition rotor_2_body_9_r1 = rotor_2_body.addOrReplaceChild("rotor_2_body_9_r1", CubeListBuilder.create().texOffs(166, 45).addBox(-4.0F, -3.5F, 14.1094F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(50, 172).addBox(-4.0F, -3.5F, -20.1094F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition rotor_2_body_5_r1 = rotor_2_body.addOrReplaceChild("rotor_2_body_5_r1", CubeListBuilder.create().texOffs(171, 91).addBox(-4.0F, -3.5F, 14.1094F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(162, 174).addBox(-4.0F, -3.5F, -20.1094F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        PartDefinition rotor_2_corner = rotor_2.addOrReplaceChild("rotor_2_corner", CubeListBuilder.create().texOffs(142, 196).addBox(-0.75F, -3.5F, -20.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(91, 194).addBox(-0.25F, -3.5F, 13.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(48, 91).addBox(13.8457F, -3.5F, 0.25F, 7.0F, 6.0F, -1.0F, new CubeDeformation(0.0F)).texOffs(166, 56).addBox(-20.8457F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -15.5F, 0.0F, 0.0F, -0.2182F, 0.0F));
        PartDefinition rotor_2_corner_15_r1 = rotor_2_corner.addOrReplaceChild("rotor_2_corner_15_r1", CubeListBuilder.create().texOffs(164, 22).addBox(-20.8457F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(196, 152).addBox(13.8457F, -3.5F, -0.75F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(49, 194).addBox(-0.25F, -3.5F, 13.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(196, 55).addBox(-0.75F, -3.5F, -20.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
        PartDefinition rotor_2_corner_13_r1 = rotor_2_corner.addOrReplaceChild("rotor_2_corner_13_r1", CubeListBuilder.create().texOffs(100, 194).addBox(-20.8457F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(205, 68).addBox(13.8457F, -3.5F, -0.75F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(110, 194).addBox(-0.25F, -3.5F, 13.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(62, 34).addBox(0.25F, -3.5F, -20.8457F, -1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));
        PartDefinition rotor_2_corner_9_r1 = rotor_2_corner.addOrReplaceChild("rotor_2_corner_9_r1", CubeListBuilder.create().texOffs(179, 193).addBox(-0.25F, -3.5F, 13.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(194, 139).addBox(-0.75F, -3.5F, -20.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition rotor_2_corner_5_r1 = rotor_2_corner.addOrReplaceChild("rotor_2_corner_5_r1", CubeListBuilder.create().texOffs(126, 194).addBox(-0.25F, -3.5F, 13.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 197).addBox(-0.75F, -3.5F, -20.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        PartDefinition rotor_3 = partdefinition.addOrReplaceChild("rotor_3", CubeListBuilder.create(), PartPose.offset(0.0F, 25.8F, 0.0F));
        PartDefinition rotor_3_body = rotor_3.addOrReplaceChild("rotor_3_body", CubeListBuilder.create().texOffs(58, 161).addBox(-5.0F, -13.5F, -25.1367F, 10.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(148, 130).addBox(-5.0F, -13.5F, 19.1367F, 10.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(0, 147).addBox(19.1367F, -13.5F, -5.0F, 6.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(140, 107).addBox(-25.1367F, -13.5F, -5.0F, 6.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));
        PartDefinition rotor_3_body_15_r1 = rotor_3_body.addOrReplaceChild("rotor_3_body_15_r1", CubeListBuilder.create().texOffs(139, 87).addBox(-25.1367F, -3.5F, -5.0F, 6.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(144, 146).addBox(19.1367F, -3.5F, -5.0F, 6.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(145, 69).addBox(-5.0F, -3.5F, 19.1367F, 10.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(157, 11).addBox(-5.0F, -3.5F, -25.1367F, 10.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
        PartDefinition rotor_3_body_13_r1 = rotor_3_body.addOrReplaceChild("rotor_3_body_13_r1", CubeListBuilder.create().texOffs(144, 54).addBox(-25.1367F, -3.5F, -5.0F, 6.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(32, 147).addBox(19.1367F, -3.5F, -5.0F, 6.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(150, 0).addBox(-5.0F, -3.5F, 19.1367F, 10.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(161, 80).addBox(-5.0F, -3.5F, -25.1367F, 10.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, 0.3927F, 0.0F));
        PartDefinition rotor_3_body_9_r1 = rotor_3_body.addOrReplaceChild("rotor_3_body_9_r1", CubeListBuilder.create().texOffs(139, 40).addBox(-5.0F, -3.5F, 19.1367F, 10.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(96, 156).addBox(-5.0F, -3.5F, -25.1367F, 10.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition rotor_3_body_5_r1 = rotor_3_body.addOrReplaceChild("rotor_3_body_5_r1", CubeListBuilder.create().texOffs(70, 150).addBox(-5.0F, -3.5F, 19.1367F, 10.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).texOffs(122, 161).addBox(-5.0F, -3.5F, -25.1367F, 10.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        PartDefinition rotor_3_corner = rotor_3.addOrReplaceChild("rotor_3_corner", CubeListBuilder.create().texOffs(33, 192).addBox(-0.75F, -3.5F, -25.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(24, 186).addBox(-0.25F, -3.5F, 18.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(78, 31).addBox(18.8457F, -3.5F, 0.25F, 7.0F, 6.0F, -1.0F, new CubeDeformation(0.0F)).texOffs(76, 42).addBox(-25.8457F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -20.5F, 0.0F, 0.0F, -0.2182F, 0.0F));
        PartDefinition rotor_3_corner_15_r1 = rotor_3_corner.addOrReplaceChild("rotor_3_corner_15_r1", CubeListBuilder.create().texOffs(62, 0).addBox(-25.8457F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(147, 122).addBox(18.8457F, -3.5F, -0.75F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(87, 62).addBox(-0.25F, -3.5F, 18.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(75, 189).addBox(-0.75F, -3.5F, -25.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
        PartDefinition rotor_3_corner_13_r1 = rotor_3_corner.addOrReplaceChild("rotor_3_corner_13_r1", CubeListBuilder.create().texOffs(22, 147).addBox(-25.8457F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(20, 162).addBox(18.8457F, -3.5F, -0.75F, 7.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(187, 49).addBox(-0.25F, -3.5F, 18.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(22, 43).addBox(0.25F, -3.5F, -25.8457F, -1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));
        PartDefinition rotor_3_corner_9_r1 = rotor_3_corner.addOrReplaceChild("rotor_3_corner_9_r1", CubeListBuilder.create().texOffs(64, 69).addBox(-0.25F, -3.5F, 18.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(188, 35).addBox(-0.75F, -3.5F, -25.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition rotor_3_corner_5_r1 = rotor_3_corner.addOrReplaceChild("rotor_3_corner_5_r1", CubeListBuilder.create().texOffs(59, 187).addBox(-0.25F, -3.5F, 18.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(192, 95).addBox(-0.75F, -3.5F, -25.8457F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }
}

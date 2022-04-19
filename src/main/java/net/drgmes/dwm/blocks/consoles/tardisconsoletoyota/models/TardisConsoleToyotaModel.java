package net.drgmes.dwm.blocks.consoles.tardisconsoletoyota.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.consoles.tardisconsoletoyota.TardisConsoleToyotaBlockEntity;
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
public class TardisConsoleToyotaModel extends EntityModel<Entity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(DWM.MODID, "textures/entity/consoles/tardis_console_toyota.png"), "main");
    private final ModelPart base;
    private final ModelPart screwdriver_slot;
    private final ModelPart controls;

    public TardisConsoleToyotaModel(ModelPart root) {
        this.base = root.getChild("base");
        this.screwdriver_slot = root.getChild("screwdriver_slot");
        this.controls = root.getChild("controls");
    }

    @Override
    public void setupAnim(Entity entity, float f1, float f2, float f3, float f4, float f5) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        base.render(poseStack, buffer, packedLight, packedOverlay);
        screwdriver_slot.render(poseStack, buffer, packedLight, packedOverlay);
        controls.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition platform_a = base.addOrReplaceChild("platform_a", CubeListBuilder.create().texOffs(87, 87).addBox(-3.6863F, -1.0F, -8.0F, 7.0F, 1.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(90, 68).addBox(-4.6863F, -1.25F, -7.0F, 9.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)).texOffs(0, 101).addBox(-5.6863F, -1.5F, -6.0F, 11.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(117, 83).addBox(-6.6863F, -1.5001F, -5.0F, 13.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(117, 94).addBox(-7.6863F, -1.25F, -4.0F, 15.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(90, 119).addBox(-8.6863F, -1.0F, -3.0F, 17.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition platform_b = base.addOrReplaceChild("platform_b", CubeListBuilder.create().texOffs(128, 117).addBox(-2.5147F, -3.0F, -6.0F, 5.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(148, 103).addBox(-6.0F, -3.0F, -2.4853F, 12.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(23, 148).addBox(-6.0F, -6.0F, -2.4853F, 12.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(122, 68).addBox(-2.5147F, -6.0F, -6.0F, 5.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition platform_b_8_r1 = platform_b.addOrReplaceChild("platform_b_8_r1", CubeListBuilder.create().texOffs(126, 103).addBox(-2.5147F, -6.0F, -6.0F, 5.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(57, 148).addBox(-6.0F, -6.0F, -2.4853F, 12.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(150, 117).addBox(-6.0F, -3.0F, -2.4853F, 12.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(129, 15).addBox(-2.5147F, -3.0F, -6.0F, 5.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition platform_c = base.addOrReplaceChild("platform_c", CubeListBuilder.create().texOffs(44, 117).addBox(-2.0711F, -23.0F, -5.0F, 4.0F, 20.0F, 10.0F, new CubeDeformation(0.0F)).texOffs(100, 126).addBox(-5.0F, -23.0F, -2.0711F, 10.0F, 20.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition platform_c_3_r1 = platform_c.addOrReplaceChild("platform_c_3_r1", CubeListBuilder.create().texOffs(0, 128).addBox(-5.0F, -23.0F, -2.0711F, 10.0F, 20.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(72, 118).addBox(-2.0711F, -23.0F, -5.0F, 4.0F, 20.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition platform_c_beams = platform_c.addOrReplaceChild("platform_c_beams", CubeListBuilder.create(), PartPose.offset(0.0F, -1.5F, 0.0F));
        PartDefinition platform_c_beam_8_r1 = platform_c_beams.addOrReplaceChild("platform_c_beam_8_r1", CubeListBuilder.create().texOffs(20, 168).addBox(-6.0F, -24.0F, 0.0F, 1.0F, 21.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(28, 168).addBox(5.0F, -24.0F, -0.7353F, 1.0F, 21.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(36, 168).addBox(-0.2647F, -24.0F, 5.0F, 1.0F, 21.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(44, 168).addBox(-0.2647F, -24.0F, -6.0F, 1.0F, 21.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, 0.0F, 0.3927F, 0.0F));
        PartDefinition platform_c_beam_7_r1 = platform_c_beams.addOrReplaceChild("platform_c_beam_7_r1", CubeListBuilder.create().texOffs(24, 168).addBox(-6.0F, -24.0F, -0.7353F, 1.0F, 21.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(32, 168).addBox(5.0F, -24.0F, -0.7353F, 1.0F, 21.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(40, 168).addBox(-0.2647F, -24.0F, 5.0F, 1.0F, 21.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(48, 168).addBox(-0.2647F, -24.0F, -6.0F, 1.0F, 21.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, 0.0F, -0.3927F, 0.0F));
        PartDefinition platform_rotor_a = base.addOrReplaceChild("platform_rotor_a", CubeListBuilder.create().texOffs(129, 0).addBox(-2.5147F, -0.5002F, -6.0F, 5.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(142, 29).addBox(-6.0F, -0.5004F, -2.4853F, 12.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -22.5F, 0.0F));
        PartDefinition platform_rotor_a_3_r1 = platform_rotor_a.addOrReplaceChild("platform_rotor_a_3_r1", CubeListBuilder.create().texOffs(86, 150).addBox(-6.0F, -23.0003F, -2.4853F, 12.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(129, 35).addBox(-2.5147F, -23.0001F, -6.0F, 5.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 22.5F, 0.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition platform_rotor_b = base.addOrReplaceChild("platform_rotor_b", CubeListBuilder.create().texOffs(128, 131).addBox(-2.5147F, -0.5F, -6.0F, 5.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(150, 131).addBox(-6.0F, -0.5002F, -2.4853F, 12.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));
        PartDefinition platform_rotor_b_3_r1 = platform_rotor_b.addOrReplaceChild("platform_rotor_b_3_r1", CubeListBuilder.create().texOffs(150, 131).addBox(-6.0F, -0.5001F, -2.4853F, 12.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(128, 131).addBox(-2.5147F, -0.5003F, -6.0F, 5.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition platform_desks = base.addOrReplaceChild("platform_desks", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition platform_desk_bottom = platform_desks.addOrReplaceChild("platform_desk_bottom", CubeListBuilder.create(), PartPose.offset(0.0F, -11.0F, 0.0F));
        PartDefinition platform_desk_bottom_8_r1 = platform_desk_bottom.addOrReplaceChild("platform_desk_bottom_8_r1", CubeListBuilder.create().texOffs(42, 38).addBox(-6.6274F, -1.0F, -16.0F, 13.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, -0.7854F, 0.0F));
        PartDefinition platform_desk_bottom_7_r1 = platform_desk_bottom.addOrReplaceChild("platform_desk_bottom_7_r1", CubeListBuilder.create().texOffs(45, 88).addBox(0.0F, -1.0F, -6.6274F, 16.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));
        PartDefinition platform_desk_bottom_6_r1 = platform_desk_bottom.addOrReplaceChild("platform_desk_bottom_6_r1", CubeListBuilder.create().texOffs(90, 53).addBox(0.0F, -1.0F, -6.6274F, 16.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2132F, -0.762F, -0.3038F));
        PartDefinition platform_desk_bottom_5_r1 = platform_desk_bottom.addOrReplaceChild("platform_desk_bottom_5_r1", CubeListBuilder.create().texOffs(84, 20).addBox(0.0F, -1.0F, -6.6274F, 16.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, -1.3526F, -1.5708F));
        PartDefinition platform_desk_bottom_4_r1 = platform_desk_bottom.addOrReplaceChild("platform_desk_bottom_4_r1", CubeListBuilder.create().texOffs(42, 20).addBox(-6.6274F, -1.0F, -16.0F, 13.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.9234F, 0.7854F, 3.1416F));
        PartDefinition platform_desk_bottom_3_r1 = platform_desk_bottom.addOrReplaceChild("platform_desk_bottom_3_r1", CubeListBuilder.create().texOffs(42, 2).addBox(-6.6274F, -1.0F, -16.0F, 13.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.2182F));
        PartDefinition platform_desk_bottom_2_r1 = platform_desk_bottom.addOrReplaceChild("platform_desk_bottom_2_r1", CubeListBuilder.create().texOffs(0, 86).addBox(0.0F, -1.0F, -6.6274F, 16.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.9284F, 0.762F, -2.8378F));
        PartDefinition platform_desk_bottom_1_r1 = platform_desk_bottom.addOrReplaceChild("platform_desk_bottom_1_r1", CubeListBuilder.create().texOffs(84, 38).addBox(0.0F, -1.0F, -6.6274F, 16.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 1.3526F, -1.5708F));
        PartDefinition platform_desk_top = platform_desks.addOrReplaceChild("platform_desk_top", CubeListBuilder.create().texOffs(0, 56).addBox(0.0F, -15.0F, -6.6274F, 16.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));
        PartDefinition platform_desk_top_8_r1 = platform_desk_top.addOrReplaceChild("platform_desk_top_8_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-6.6274F, -15.0F, -16.0F, 13.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(45, 58).addBox(0.0F, -15.0F, -6.6274F, 16.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition platform_desk_top_5_r1 = platform_desk_top.addOrReplaceChild("platform_desk_top_5_r1", CubeListBuilder.create().texOffs(0, 71).addBox(0.0F, -15.0F, -6.6274F, 16.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition platform_desk_top_4_r1 = platform_desk_top.addOrReplaceChild("platform_desk_top_4_r1", CubeListBuilder.create().texOffs(0, 18).addBox(-6.6274F, -15.0F, -16.0F, 13.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(45, 73).addBox(0.0F, -15.0F, -6.6274F, 16.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.3562F, 0.0F));
        PartDefinition platform_desk_top_3_r1 = platform_desk_top.addOrReplaceChild("platform_desk_top_3_r1", CubeListBuilder.create().texOffs(0, 36).addBox(-6.6274F, -15.0F, -16.0F, 13.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(84, 0).addBox(0.0F, -15.0F, -6.6274F, 16.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
        PartDefinition desk_panel = base.addOrReplaceChild("desk_panel", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition desk_panel_separators = desk_panel.addOrReplaceChild("desk_panel_separators", CubeListBuilder.create(), PartPose.offset(0.0F, 0.75F, 0.0F));
        PartDefinition desk_panel_separator_4_r1 = desk_panel_separators.addOrReplaceChild("desk_panel_separator_4_r1", CubeListBuilder.create().texOffs(135, 48).addBox(4.4576F, -1.5F, 0.1344F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.6362F, -15.9482F, 15.3578F, -2.8798F, 0.7854F, 3.1416F));
        PartDefinition desk_panel_separator_3_r1 = desk_panel_separators.addOrReplaceChild("desk_panel_separator_3_r1", CubeListBuilder.create().texOffs(144, 62).addBox(4.4576F, -1.5F, 0.1344F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.3578F, -15.9482F, 7.6362F, -2.8798F, -0.7854F, 3.1416F));
        PartDefinition desk_panel_separator_2_r1 = desk_panel_separators.addOrReplaceChild("desk_panel_separator_2_r1", CubeListBuilder.create().texOffs(116, 144).addBox(4.1977F, -1.5F, 0.1443F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.8267F, -15.9456F, -15.1808F, 0.2618F, -0.7854F, 0.0F));
        PartDefinition desk_panel_separator_1_r1 = desk_panel_separators.addOrReplaceChild("desk_panel_separator_1_r1", CubeListBuilder.create().texOffs(144, 144).addBox(4.1348F, -1.5F, 0.1392F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.1328F, -15.9469F, -7.8677F, 0.2618F, 0.7854F, 0.0F));
        PartDefinition desk_panel_sections = desk_panel.addOrReplaceChild("desk_panel_sections", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition desk_panel_section_a = desk_panel_sections.addOrReplaceChild("desk_panel_section_a", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition desk_panel_section_a_13_r1 = desk_panel_section_a.addOrReplaceChild("desk_panel_section_a_13_r1", CubeListBuilder.create().texOffs(111, 15).addBox(9.5F, 0.0F, 3.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(64, 112).addBox(-10.5F, 0.0F, 3.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(84, 38).addBox(8.5F, 0.0F, 2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(84, 44).addBox(-9.5F, 0.0F, 2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(42, 8).addBox(7.5F, 0.0F, 1.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(42, 20).addBox(-8.5F, 0.0F, 1.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(165, 14).addBox(6.5F, 0.0F, 0.5F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(165, 35).addBox(-7.5F, 0.0F, 0.5F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(153, 158).addBox(5.5F, 0.0F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(160, 59).addBox(-6.5F, 0.0F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(66, 155).addBox(4.5F, 0.0F, -0.5F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(155, 89).addBox(-5.5F, 0.0F, -0.5F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(0, 114).addBox(-4.5F, 0.0F, -0.5F, 9.0F, 1.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -15.5F, -15.5F, 0.2618F, 0.0F, 0.0F));
        PartDefinition desk_panel_section_b = desk_panel_sections.addOrReplaceChild("desk_panel_section_b", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));
        PartDefinition desk_panel_section_b_13_r1 = desk_panel_section_b.addOrReplaceChild("desk_panel_section_b_13_r1", CubeListBuilder.create().texOffs(69, 109).addBox(9.5F, 0.0F, 3.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(100, 109).addBox(-10.5F, 0.0F, 3.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(84, 20).addBox(8.5F, 0.0F, 2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(84, 26).addBox(-9.5F, 0.0F, 2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 36).addBox(7.5F, 0.0F, 1.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(42, 0).addBox(-8.5F, 0.0F, 1.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 165).addBox(6.5F, 0.0F, 0.5F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(165, 0).addBox(-7.5F, 0.0F, 0.5F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(105, 158).addBox(5.5F, 0.0F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(129, 158).addBox(-6.5F, 0.0F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(14, 155).addBox(4.5F, 0.0F, -0.5F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(40, 155).addBox(-5.5F, 0.0F, -0.5F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(95, 105).addBox(-4.5F, 0.0F, -0.5F, 9.0F, 1.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -15.5F, -15.5F, 0.2618F, 0.0F, 0.0F));
        PartDefinition desk_panel_section_c = desk_panel_sections.addOrReplaceChild("desk_panel_section_c", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));
        PartDefinition desk_panel_section_c_13_r1 = desk_panel_section_c.addOrReplaceChild("desk_panel_section_c_13_r1", CubeListBuilder.create().texOffs(64, 108).addBox(9.5F, 0.0F, 3.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(95, 108).addBox(-10.5F, 0.0F, 3.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(84, 0).addBox(8.5F, 0.0F, 2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(84, 6).addBox(-9.5F, 0.0F, 2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 18).addBox(7.5F, 0.0F, 1.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 26).addBox(-8.5F, 0.0F, 1.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(118, 158).addBox(6.5F, 0.0F, 0.5F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(142, 158).addBox(-7.5F, 0.0F, 0.5F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(54, 155).addBox(5.5F, 0.0F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(92, 156).addBox(-6.5F, 0.0F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(0, 152).addBox(4.5F, 0.0F, -0.5F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(153, 76).addBox(-5.5F, 0.0F, -0.5F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(64, 104).addBox(-4.5F, 0.0F, -0.5F, 9.0F, 1.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -15.5F, -15.5F, 0.2618F, 0.0F, 0.0F));
        PartDefinition desk_panel_section_d = desk_panel_sections.addOrReplaceChild("desk_panel_section_d", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition desk_panel_section_d_13_r1 = desk_panel_section_d.addOrReplaceChild("desk_panel_section_d_13_r1", CubeListBuilder.create().texOffs(0, 106).addBox(9.5F, 0.0F, 3.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(34, 106).addBox(-10.5F, 0.0F, 3.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(45, 73).addBox(8.5F, 0.0F, 2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 77).addBox(-9.5F, 0.0F, 2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(7.5F, 0.0F, 1.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(0, 8).addBox(-8.5F, 0.0F, 1.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(31, 117).addBox(6.5F, 0.0F, 0.5F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(80, 156).addBox(-7.5F, 0.0F, 0.5F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(132, 144).addBox(5.5F, 0.0F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(28, 155).addBox(-6.5F, 0.0F, -0.5F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F)).texOffs(151, 14).addBox(4.5F, 0.0F, -0.5F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(151, 36).addBox(-5.5F, 0.0F, -0.5F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)).texOffs(33, 103).addBox(-4.5F, 0.0F, -0.5F, 9.0F, 1.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -15.5F, -15.5F, 0.2618F, 0.0F, 0.0F));
        PartDefinition desk_panel_borders = desk_panel.addOrReplaceChild("desk_panel_borders", CubeListBuilder.create(), PartPose.offset(0.0F, -0.725F, 0.5F));
        PartDefinition desk_panel_border_12_r1 = desk_panel_borders.addOrReplaceChild("desk_panel_border_12_r1", CubeListBuilder.create().texOffs(42, 38).addBox(-3.5F, -0.75F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.5484F, -14.7767F, 12.9981F, -0.4246F, 0.7234F, -0.237F));
        PartDefinition desk_panel_border_11_r1 = desk_panel_borders.addOrReplaceChild("desk_panel_border_11_r1", CubeListBuilder.create().texOffs(0, 44).addBox(-3.5F, -0.75F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.4981F, -14.7767F, 8.0484F, -0.0747F, 0.8163F, 0.2599F));
        PartDefinition desk_panel_border_10_r1 = desk_panel_borders.addOrReplaceChild("desk_panel_border_10_r1", CubeListBuilder.create().texOffs(160, 113).addBox(-7.0F, 0.75F, -0.5F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.848F, -15.7426F, -0.5F, -0.2618F, 1.5708F, 0.0F));
        PartDefinition desk_panel_border_9_r1 = desk_panel_borders.addOrReplaceChild("desk_panel_border_9_r1", CubeListBuilder.create().texOffs(42, 41).addBox(-3.5F, -0.75F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.4981F, -14.7767F, -9.0484F, 3.0669F, 0.8163F, -2.8817F));
        PartDefinition desk_panel_border_8_r1 = desk_panel_borders.addOrReplaceChild("desk_panel_border_8_r1", CubeListBuilder.create().texOffs(42, 44).addBox(-3.5F, -0.75F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.5484F, -14.7767F, -13.9981F, 2.717F, 0.7234F, 2.9045F));
        PartDefinition desk_panel_border_7_r1 = desk_panel_borders.addOrReplaceChild("desk_panel_border_7_r1", CubeListBuilder.create().texOffs(160, 144).addBox(-7.0F, 0.75F, -0.5F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -15.7426F, -16.348F, -0.2618F, 3.1416F, 0.0F));
        PartDefinition desk_panel_border_6_r1 = desk_panel_borders.addOrReplaceChild("desk_panel_border_6_r1", CubeListBuilder.create().texOffs(42, 28).addBox(-3.5F, -0.75F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.5484F, -14.7767F, -13.9981F, 2.717F, -0.7234F, -2.9045F));
        PartDefinition desk_panel_border_5_r1 = desk_panel_borders.addOrReplaceChild("desk_panel_border_5_r1", CubeListBuilder.create().texOffs(0, 47).addBox(-3.5F, -0.75F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.4981F, -14.7767F, -9.0484F, 3.0669F, -0.8163F, 2.8817F));
        PartDefinition desk_panel_border_4_r1 = desk_panel_borders.addOrReplaceChild("desk_panel_border_4_r1", CubeListBuilder.create().texOffs(160, 147).addBox(-7.0F, 0.75F, -0.5F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.848F, -15.7426F, -0.5F, -0.2618F, -1.5708F, 0.0F));
        PartDefinition desk_panel_border_3_r1 = desk_panel_borders.addOrReplaceChild("desk_panel_border_3_r1", CubeListBuilder.create().texOffs(42, 31).addBox(-3.5F, -0.75F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.4981F, -14.7767F, 8.0484F, -0.0747F, -0.8163F, -0.2599F));
        PartDefinition desk_panel_border_2_r1 = desk_panel_borders.addOrReplaceChild("desk_panel_border_2_r1", CubeListBuilder.create().texOffs(42, 47).addBox(-3.5F, -0.75F, -0.5F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.5484F, -14.7767F, 12.9981F, -0.4246F, -0.7234F, 0.237F));
        PartDefinition desk_panel_border_1_r1 = desk_panel_borders.addOrReplaceChild("desk_panel_border_1_r1", CubeListBuilder.create().texOffs(160, 150).addBox(-7.0F, 0.75F, -0.5F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -15.7426F, 15.348F, -0.2618F, 0.0F, 0.0F));
        PartDefinition desk_panel_outlines = desk_panel.addOrReplaceChild("desk_panel_outlines", CubeListBuilder.create(), PartPose.offset(0.0F, -16.4676F, -15.848F));
        PartDefinition desk_panel_outline_8_r1 = desk_panel_outlines.addOrReplaceChild("desk_panel_outline_8_r1", CubeListBuilder.create().texOffs(84, 15).addBox(-7.0F, -15.6176F, 15.777F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.4676F, 15.848F, 0.0F, -2.3562F, 0.0F));
        PartDefinition desk_panel_outline_7_r1 = desk_panel_outlines.addOrReplaceChild("desk_panel_outline_7_r1", CubeListBuilder.create().texOffs(90, 83).addBox(-7.0F, -15.6176F, 15.777F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.4676F, 15.848F, 0.0F, -1.5708F, 0.0F));
        PartDefinition desk_panel_outline_6_r1 = desk_panel_outlines.addOrReplaceChild("desk_panel_outline_6_r1", CubeListBuilder.create().texOffs(100, 35).addBox(-7.0F, -15.6176F, 15.777F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.4676F, 15.848F, 0.0F, -0.7854F, 0.0F));
        PartDefinition desk_panel_outline_5_r1 = desk_panel_outlines.addOrReplaceChild("desk_panel_outline_5_r1", CubeListBuilder.create().texOffs(148, 110).addBox(-7.0F, -15.6176F, 15.777F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.4676F, 15.848F, 0.0F, 0.0F, 0.0F));
        PartDefinition desk_panel_outline_4_r1 = desk_panel_outlines.addOrReplaceChild("desk_panel_outline_4_r1", CubeListBuilder.create().texOffs(150, 124).addBox(-7.0F, -15.6176F, 15.777F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.4676F, 15.848F, 0.0F, 0.7854F, 0.0F));
        PartDefinition desk_panel_outline_3_r1 = desk_panel_outlines.addOrReplaceChild("desk_panel_outline_3_r1", CubeListBuilder.create().texOffs(150, 137).addBox(-7.0F, -15.6176F, 15.777F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.4676F, 15.848F, 0.0F, 1.5708F, 0.0F));
        PartDefinition desk_panel_outline_2_r1 = desk_panel_outlines.addOrReplaceChild("desk_panel_outline_2_r1", CubeListBuilder.create().texOffs(150, 140).addBox(-7.0F, -15.6176F, 15.777F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.4676F, 15.848F, 0.0F, 2.3562F, 0.0F));
        PartDefinition desk_panel_outline_1_r1 = desk_panel_outlines.addOrReplaceChild("desk_panel_outline_1_r1", CubeListBuilder.create().texOffs(160, 71).addBox(-7.0F, -15.6176F, 15.777F, 14.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.4676F, 15.848F, 0.0F, 3.1416F, 0.0F));
        PartDefinition screwdriver_slot = partdefinition.addOrReplaceChild("screwdriver_slot", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition screwdriver_slot_base_r1 = screwdriver_slot.addOrReplaceChild("screwdriver_slot_base_r1", CubeListBuilder.create().texOffs(95, 78).addBox(0.75F, -19.25F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));
        PartDefinition controls = partdefinition.addOrReplaceChild("controls", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition control_indicator_3_r1 = controls.addOrReplaceChild("control_indicator_3_r1", CubeListBuilder.create().texOffs(90, 98).addBox(4.5F, -19.0F, -5.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 110).addBox(-6.5F, -19.0F, -5.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(34, 110).mirror().addBox(-1.0F, -19.0F, -2.75F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_telepatic_interface = controls.addOrReplaceChild("control_telepatic_interface", CubeListBuilder.create(), PartPose.offset(-0.25F, 0.0F, 0.0F));
        PartDefinition control_telepatic_interface_a = control_telepatic_interface.addOrReplaceChild("control_telepatic_interface_a", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_telepatic_interface_a_3_r1 = control_telepatic_interface_a.addOrReplaceChild("control_telepatic_interface_a_3_r1", CubeListBuilder.create().texOffs(0, 56).addBox(-0.25F, -0.4998F, -7.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(45, 56).addBox(-0.25F, -0.4998F, 2.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(28, 133).addBox(-0.5F, -0.5F, -7.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.6914F, -15.7558F, 0.0F, 0.0F, 0.0F, -0.2618F));
        PartDefinition control_telepatic_interface_b = control_telepatic_interface.addOrReplaceChild("control_telepatic_interface_b", CubeListBuilder.create(), PartPose.offset(-12.6914F, -15.7558F, 0.0F));
        PartDefinition control_telepatic_interface_b_3_r1 = control_telepatic_interface_b.addOrReplaceChild("control_telepatic_interface_b_3_r1", CubeListBuilder.create().texOffs(100, 105).addBox(1.75F, -0.4998F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 62).addBox(1.25F, -0.4998F, -2.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(151, 1).addBox(1.5F, -0.5F, -6.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2618F));
        PartDefinition control_telepatic_interface_c = control_telepatic_interface.addOrReplaceChild("control_telepatic_interface_c", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_telepatic_interface_c_4_r1 = control_telepatic_interface_c.addOrReplaceChild("control_telepatic_interface_c_4_r1", CubeListBuilder.create().texOffs(95, 104).addBox(3.25F, -0.4998F, -5.25F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(96, 73).addBox(3.25F, -0.4998F, 2.25F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(45, 62).addBox(3.5F, -0.5F, -5.25F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 71).addBox(3.5F, -0.5F, 0.25F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.6914F, -15.7558F, 0.0F, 0.0F, 0.0F, -0.2618F));
        PartDefinition control_telepatic_interface_d = control_telepatic_interface.addOrReplaceChild("control_telepatic_interface_d", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_telepatic_interface_d_4_r1 = control_telepatic_interface_d.addOrReplaceChild("control_telepatic_interface_d_4_r1", CubeListBuilder.create().texOffs(90, 78).addBox(5.75F, -0.4998F, -4.25F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(0, 95).addBox(5.75F, -0.4998F, 1.25F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(34, 101).addBox(5.5F, -0.5F, -4.25F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(64, 103).addBox(5.5F, -0.5F, 0.25F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.6914F, -15.7558F, 0.0F, 0.0F, 0.0F, -0.2618F));
        PartDefinition control_monitor = controls.addOrReplaceChild("control_monitor", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -0.5F));
        PartDefinition control_monitor_main_r1 = control_monitor.addOrReplaceChild("control_monitor_main_r1", CubeListBuilder.create().texOffs(151, 49).addBox(-2.5F, -0.5F, -4.5F, 5.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.4241F, -16.3483F, 0.5F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_monitor_borders = control_monitor.addOrReplaceChild("control_monitor_borders", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_monitor_border_4_r1 = control_monitor_borders.addOrReplaceChild("control_monitor_border_4_r1", CubeListBuilder.create().texOffs(0, 45).addBox(2.5F, -0.75F, -4.5F, 0.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(18, 45).addBox(-2.5F, -0.75F, -4.5F, 0.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).texOffs(51, 0).addBox(-2.5F, -0.75F, 4.5F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).texOffs(52, 56).addBox(-2.5F, -0.75F, -4.5F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.4241F, -16.3483F, 0.5F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_handbrake = controls.addOrReplaceChild("control_handbrake", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_handbrake_base_2_r1 = control_handbrake.addOrReplaceChild("control_handbrake_base_2_r1", CubeListBuilder.create().texOffs(0, 0).addBox(6.5F, -19.25F, 6.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 86).addBox(4.0F, -19.0F, 6.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));
        PartDefinition control_handbrake_body = control_handbrake.addOrReplaceChild("control_handbrake_body", CubeListBuilder.create(), PartPose.offset(6.77F, -17.3545F, 12.5572F));
        PartDefinition control_handbrake_body_r1 = control_handbrake_body.addOrReplaceChild("control_handbrake_body_r1", CubeListBuilder.create().texOffs(42, 50).addBox(-2.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.25F, 0.0F, -1.5F, -0.3776F, -0.7926F, 0.2753F));
        PartDefinition control_starter = controls.addOrReplaceChild("control_starter", CubeListBuilder.create(), PartPose.offset(-11.0F, 0.0F, 0.0F));
        PartDefinition control_starter_base_r1 = control_starter.addOrReplaceChild("control_starter_base_r1", CubeListBuilder.create().texOffs(45, 79).addBox(4.0F, -19.0F, 6.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));
        PartDefinition control_starter_body = control_starter.addOrReplaceChild("control_starter_body", CubeListBuilder.create(), PartPose.offsetAndRotation(5.5F, -16.2907F, 12.1297F, 1.1345F, 0.0F, 0.0F));
        PartDefinition control_starter_body_main_r1 = control_starter_body.addOrReplaceChild("control_starter_body_main_r1", CubeListBuilder.create().texOffs(0, 50).addBox(-2.0F, -3.0F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));
        PartDefinition control_starter_rod_left = control_starter_body.addOrReplaceChild("control_starter_rod_left", CubeListBuilder.create(), PartPose.offset(-5.5F, 16.2907F, -12.1297F));
        PartDefinition control_starter_rod_left_2_r1 = control_starter_rod_left.addOrReplaceChild("control_starter_rod_left_2_r1", CubeListBuilder.create().texOffs(11, 17).addBox(6.5F, -21.0F, 7.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(13, 17).addBox(6.75F, -21.0F, 7.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));
        PartDefinition control_starter_rod_right = control_starter_body.addOrReplaceChild("control_starter_rod_right", CubeListBuilder.create(), PartPose.offset(-7.75F, 16.2907F, -12.1297F));
        PartDefinition control_starter_rod_right_2_r1 = control_starter_rod_right.addOrReplaceChild("control_starter_rod_right_2_r1", CubeListBuilder.create().texOffs(4, 17).addBox(6.5F, -21.0F, 7.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(9, 17).addBox(6.75F, -21.0F, 7.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_1 = controls.addOrReplaceChild("control_lever_1", CubeListBuilder.create(), PartPose.offset(-0.5F, -0.15F, 0.0F));
        PartDefinition control_lever_1_base_r1 = control_lever_1.addOrReplaceChild("control_lever_1_base_r1", CubeListBuilder.create().texOffs(9, 10).addBox(-1.0F, -0.0066F, -0.5248F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -16.687F, -11.5727F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_1_body = control_lever_1.addOrReplaceChild("control_lever_1_body", CubeListBuilder.create(), PartPose.offset(-5.0F, -16.5405F, -11.6118F));
        PartDefinition control_lever_1_body_r1 = control_lever_1_body.addOrReplaceChild("control_lever_1_body_r1", CubeListBuilder.create().texOffs(0, 101).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_2 = controls.addOrReplaceChild("control_lever_2", CubeListBuilder.create(), PartPose.offset(10.5F, -0.15F, 0.0F));
        PartDefinition control_lever_2_base_r1 = control_lever_2.addOrReplaceChild("control_lever_2_base_r1", CubeListBuilder.create().texOffs(0, 10).addBox(-1.0F, -0.0066F, -0.5248F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -16.687F, -11.5727F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_2_body = control_lever_2.addOrReplaceChild("control_lever_2_body", CubeListBuilder.create(), PartPose.offset(-5.0F, -16.5405F, -11.6118F));
        PartDefinition control_lever_2_body_r1 = control_lever_2_body.addOrReplaceChild("control_lever_2_body_r1", CubeListBuilder.create().texOffs(90, 93).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_3 = controls.addOrReplaceChild("control_lever_3", CubeListBuilder.create(), PartPose.offset(2.0F, -0.95F, 4.0F));
        PartDefinition control_lever_3_base_r1 = control_lever_3.addOrReplaceChild("control_lever_3_base_r1", CubeListBuilder.create().texOffs(9, 8).addBox(-1.0F, -0.0066F, -0.5248F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -16.687F, -12.5727F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_3_body = control_lever_3.addOrReplaceChild("control_lever_3_body", CubeListBuilder.create(), PartPose.offset(-5.0F, -16.5405F, -12.6118F));
        PartDefinition control_lever_3_body_r1 = control_lever_3_body.addOrReplaceChild("control_lever_3_body_r1", CubeListBuilder.create().texOffs(45, 93).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_4 = controls.addOrReplaceChild("control_lever_4", CubeListBuilder.create(), PartPose.offset(4.0F, -0.95F, 3.0F));
        PartDefinition control_lever_4_base_r1 = control_lever_4.addOrReplaceChild("control_lever_4_base_r1", CubeListBuilder.create().texOffs(9, 4).addBox(-1.0F, -0.0066F, -0.5248F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -16.687F, -11.5727F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_4_body = control_lever_4.addOrReplaceChild("control_lever_4_body", CubeListBuilder.create(), PartPose.offset(-5.0F, -16.5405F, -11.6118F));
        PartDefinition control_lever_4_body_r1 = control_lever_4_body.addOrReplaceChild("control_lever_4_body_r1", CubeListBuilder.create().texOffs(90, 88).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_5 = controls.addOrReplaceChild("control_lever_5", CubeListBuilder.create(), PartPose.offset(6.0F, -0.95F, 3.0F));
        PartDefinition control_lever_5_base_r1 = control_lever_5.addOrReplaceChild("control_lever_5_base_r1", CubeListBuilder.create().texOffs(9, 2).addBox(-1.0F, -0.0066F, -0.5248F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -16.687F, -11.5727F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_5_body = control_lever_5.addOrReplaceChild("control_lever_5_body", CubeListBuilder.create(), PartPose.offset(-5.0F, -16.5405F, -11.6118F));
        PartDefinition control_lever_5_body_r1 = control_lever_5_body.addOrReplaceChild("control_lever_5_body_r1", CubeListBuilder.create().texOffs(90, 73).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_6 = controls.addOrReplaceChild("control_lever_6", CubeListBuilder.create(), PartPose.offset(8.0F, -0.95F, 4.0F));
        PartDefinition control_lever_6_base_r1 = control_lever_6.addOrReplaceChild("control_lever_6_base_r1", CubeListBuilder.create().texOffs(9, 0).addBox(-1.0F, -0.0066F, -0.5248F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -16.687F, -12.5727F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_6_body = control_lever_6.addOrReplaceChild("control_lever_6_body", CubeListBuilder.create(), PartPose.offset(-5.0F, -16.5405F, -12.6118F));
        PartDefinition control_lever_6_body_r1 = control_lever_6_body.addOrReplaceChild("control_lever_6_body_r1", CubeListBuilder.create().texOffs(90, 61).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_7 = controls.addOrReplaceChild("control_lever_7", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, -17.3467F, 10.038F, 0.0F, 3.1416F, 0.0F));
        PartDefinition control_lever_7_base_r1 = control_lever_7.addOrReplaceChild("control_lever_7_base_r1", CubeListBuilder.create().texOffs(0, 8).addBox(-1.0F, 0.2522F, 0.4412F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.2903F, -0.5347F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_7_body = control_lever_7.addOrReplaceChild("control_lever_7_body", CubeListBuilder.create(), PartPose.offset(0.0F, -0.1438F, 0.4263F));
        PartDefinition control_lever_7_body_r1 = control_lever_7_body.addOrReplaceChild("control_lever_7_body_r1", CubeListBuilder.create().texOffs(90, 56).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_8 = controls.addOrReplaceChild("control_lever_8", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -17.3467F, 10.038F, 0.0F, 3.1416F, 0.0F));
        PartDefinition control_lever_8_base_r1 = control_lever_8.addOrReplaceChild("control_lever_8_base_r1", CubeListBuilder.create().texOffs(0, 5).addBox(-1.0F, -0.0066F, -0.5248F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.2903F, 0.4653F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_8_body = control_lever_8.addOrReplaceChild("control_lever_8_body", CubeListBuilder.create(), PartPose.offset(0.0F, -0.1438F, 0.4263F));
        PartDefinition control_lever_8_body_r1 = control_lever_8_body.addOrReplaceChild("control_lever_8_body_r1", CubeListBuilder.create().texOffs(0, 90).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_9 = controls.addOrReplaceChild("control_lever_9", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, -17.3467F, 10.038F, 0.0F, 3.1416F, 0.0F));
        PartDefinition control_lever_9_base_r1 = control_lever_9.addOrReplaceChild("control_lever_9_base_r1", CubeListBuilder.create().texOffs(0, 3).addBox(-1.0F, 0.2522F, 0.4412F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.2903F, -0.5347F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_lever_9_body = control_lever_9.addOrReplaceChild("control_lever_9_body", CubeListBuilder.create(), PartPose.offset(0.0F, -0.1438F, 0.4263F));
        PartDefinition control_lever_9_body_r1 = control_lever_9_body.addOrReplaceChild("control_lever_9_body_r1", CubeListBuilder.create().texOffs(45, 88).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.5F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_cradle_1 = controls.addOrReplaceChild("control_cradle_1", CubeListBuilder.create(), PartPose.offset(11.4F, -16.1756F, -7.25F));
        PartDefinition control_cradle_1_base_r1 = control_cradle_1.addOrReplaceChild("control_cradle_1_base_r1", CubeListBuilder.create().texOffs(5, 95).addBox(5.825F, -19.075F, -8.25F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.4F, 16.1756F, 7.25F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_cradle_1_body = control_cradle_1.addOrReplaceChild("control_cradle_1_body", CubeListBuilder.create(), PartPose.offset(-0.3941F, -0.3644F, -0.475F));
        PartDefinition control_cradle_1_body_r1 = control_cradle_1_body.addOrReplaceChild("control_cradle_1_body_r1", CubeListBuilder.create().texOffs(3, 21).addBox(-0.983F, -0.3706F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 0.5F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_cradle_2 = controls.addOrReplaceChild("control_cradle_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 14.5F));
        PartDefinition control_cradle_2_base_r1 = control_cradle_2.addOrReplaceChild("control_cradle_2_base_r1", CubeListBuilder.create().texOffs(90, 68).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.4F, -16.1756F, -7.25F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_cradle_2_body = control_cradle_2.addOrReplaceChild("control_cradle_2_body", CubeListBuilder.create(), PartPose.offset(11.0059F, -16.5401F, -7.725F));
        PartDefinition control_cradle_2_body_r1 = control_cradle_2_body.addOrReplaceChild("control_cradle_2_body_r1", CubeListBuilder.create().texOffs(0, 20).addBox(-0.958F, -0.3706F, -0.975F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4759F, -0.0065F, 0.475F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_button_1 = controls.addOrReplaceChild("control_button_1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_1_r1 = control_button_1.addOrReplaceChild("control_button_1_r1", CubeListBuilder.create().texOffs(12, 29).addBox(-3.5F, -19.3703F, -9.3842F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_button_2 = controls.addOrReplaceChild("control_button_2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_2_r1 = control_button_2.addOrReplaceChild("control_button_2_r1", CubeListBuilder.create().texOffs(0, 30).addBox(-2.0F, -19.3703F, -9.3842F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_button_3 = controls.addOrReplaceChild("control_button_3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_3_r1 = control_button_3.addOrReplaceChild("control_button_3_r1", CubeListBuilder.create().texOffs(9, 30).addBox(-0.5F, -19.3703F, -9.3842F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_button_4 = controls.addOrReplaceChild("control_button_4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_4_r1 = control_button_4.addOrReplaceChild("control_button_4_r1", CubeListBuilder.create().texOffs(3, 29).addBox(1.0F, -19.3703F, -9.3842F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_button_5 = controls.addOrReplaceChild("control_button_5", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_5_r1 = control_button_5.addOrReplaceChild("control_button_5_r1", CubeListBuilder.create().texOffs(9, 28).addBox(2.5F, -19.3703F, -9.3842F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_button_6 = controls.addOrReplaceChild("control_button_6", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_6_r1 = control_button_6.addOrReplaceChild("control_button_6_r1", CubeListBuilder.create().texOffs(0, 28).addBox(7.325F, -19.5002F, -5.8842F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0217F, 0.1299F, 0.1342F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_button_7 = controls.addOrReplaceChild("control_button_7", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_7_r1 = control_button_7.addOrReplaceChild("control_button_7_r1", CubeListBuilder.create().texOffs(12, 27).addBox(8.575F, -19.5002F, -5.8842F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0217F, 0.1299F, 0.1342F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_button_8 = controls.addOrReplaceChild("control_button_8", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_8_r1 = control_button_8.addOrReplaceChild("control_button_8_r1", CubeListBuilder.create().texOffs(3, 27).addBox(8.575F, -19.5002F, -4.6342F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0217F, 0.1299F, 0.1342F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_button_9 = controls.addOrReplaceChild("control_button_9", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_9_r1 = control_button_9.addOrReplaceChild("control_button_9_r1", CubeListBuilder.create().texOffs(9, 26).addBox(8.575F, -19.5002F, 3.3658F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0217F, 0.1299F, 0.1342F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_button_10 = controls.addOrReplaceChild("control_button_10", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_10_r1 = control_button_10.addOrReplaceChild("control_button_10_r1", CubeListBuilder.create().texOffs(0, 26).addBox(8.575F, -19.5002F, 4.6158F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0217F, 0.1299F, 0.1342F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_button_11 = controls.addOrReplaceChild("control_button_11", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_11_r1 = control_button_11.addOrReplaceChild("control_button_11_r1", CubeListBuilder.create().texOffs(9, 23).addBox(7.325F, -19.5002F, 4.6158F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0217F, 0.1299F, 0.1342F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_button_12 = controls.addOrReplaceChild("control_button_12", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_12_r1 = control_button_12.addOrReplaceChild("control_button_12_r1", CubeListBuilder.create().texOffs(3, 23).addBox(1.825F, -19.5002F, 3.6158F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0217F, 0.1299F, 0.1342F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_button_13 = controls.addOrReplaceChild("control_button_13", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_13_r1 = control_button_13.addOrReplaceChild("control_button_13_r1", CubeListBuilder.create().texOffs(12, 22).addBox(1.825F, -19.5002F, 2.3658F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0217F, 0.1299F, 0.1342F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_button_14 = controls.addOrReplaceChild("control_button_14", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_14_r1 = control_button_14.addOrReplaceChild("control_button_14_r1", CubeListBuilder.create().texOffs(0, 22).addBox(1.825F, -19.5002F, -4.8842F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0217F, 0.1299F, 0.1342F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_button_15 = controls.addOrReplaceChild("control_button_15", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_15_r1 = control_button_15.addOrReplaceChild("control_button_15_r1", CubeListBuilder.create().texOffs(9, 21).addBox(1.825F, -19.5002F, -3.6342F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0217F, 0.1299F, 0.1342F, 0.0F, 0.0F, 0.2618F));
        PartDefinition control_button_16 = controls.addOrReplaceChild("control_button_16", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_16_r1 = control_button_16.addOrReplaceChild("control_button_16_r1", CubeListBuilder.create().texOffs(0, 18).addBox(1.025F, -19.5002F, 2.1158F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0217F, 0.1299F, 0.1342F, -0.2618F, 0.0F, 0.0F));
        PartDefinition control_button_17 = controls.addOrReplaceChild("control_button_17", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_17_r1 = control_button_17.addOrReplaceChild("control_button_17_r1", CubeListBuilder.create().texOffs(12, 13).addBox(-0.475F, -19.5002F, 2.1158F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0217F, 0.1299F, 0.1342F, -0.2618F, 0.0F, 0.0F));
        PartDefinition control_button_18 = controls.addOrReplaceChild("control_button_18", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_18_r1 = control_button_18.addOrReplaceChild("control_button_18_r1", CubeListBuilder.create().texOffs(3, 13).addBox(-1.975F, -19.5002F, 2.1158F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0217F, 0.1299F, 0.1342F, -0.2618F, 0.0F, 0.0F));
        PartDefinition control_button_19 = controls.addOrReplaceChild("control_button_19", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_19_r1 = control_button_19.addOrReplaceChild("control_button_19_r1", CubeListBuilder.create().texOffs(9, 12).addBox(3.525F, -19.5002F, 2.1158F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0217F, 0.1299F, 0.1342F, -0.2618F, 0.0F, 0.0F));
        PartDefinition control_button_20 = controls.addOrReplaceChild("control_button_20", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_20_r1 = control_button_20.addOrReplaceChild("control_button_20_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-4.475F, -19.5002F, 2.1158F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0217F, 0.1299F, 0.1342F, -0.2618F, 0.0F, 0.0F));
        PartDefinition control_button_21 = controls.addOrReplaceChild("control_button_21", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_21_r1 = control_button_21.addOrReplaceChild("control_button_21_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-2.4967F, -19.3703F, -2.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_button_22 = controls.addOrReplaceChild("control_button_22", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_22_r1 = control_button_22.addOrReplaceChild("control_button_22_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-3.9967F, -19.3703F, -2.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_button_23 = controls.addOrReplaceChild("control_button_23", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_23_r1 = control_button_23.addOrReplaceChild("control_button_23_r1", CubeListBuilder.create().texOffs(0, 12).addBox(1.5033F, -19.3703F, -2.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition control_button_24 = controls.addOrReplaceChild("control_button_24", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition control_button_24_r1 = control_button_24.addOrReplaceChild("control_button_24_r1", CubeListBuilder.create().texOffs(0, 12).addBox(3.0033F, -19.3703F, -2.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    public void setupAnim(TardisConsoleToyotaBlockEntity tile) {
        try {
            if (tile.controlShields) this.activateLever("control_lever_1");
            if (tile.controlDoor) this.activateLever("control_lever_2");
            if (tile.controlHandbrake) this.activateHandbrake();
            if (tile.controlStarter) this.activateStarter();

            this.activateFacingControl(tile.controlFacing);

            // Buttons
            // this.activateButton("control_button_1");
            // this.activateButton("control_button_2");
            // this.activateButton("control_button_3");
            // this.activateButton("control_button_4");
            // this.activateButton("control_button_5");
            // this.activateButton("control_button_6");
            // this.activateButton("control_button_7");
            // this.activateButton("control_button_8");
            // this.activateButton("control_button_9");
            // this.activateButton("control_button_10");
            // this.activateButton("control_button_11");
            // this.activateButton("control_button_12");
            // this.activateButton("control_button_13");
            // this.activateButton("control_button_14");
            // this.activateButton("control_button_15");
            // this.activateButton("control_button_16");
            // this.activateButton("control_button_17");
            // this.activateButton("control_button_18");
            // this.activateButton("control_button_19");
            // this.activateButton("control_button_20");
            // this.activateButton("control_button_21");
            // this.activateButton("control_button_22");
            // this.activateButton("control_button_23");
            // this.activateButton("control_button_24");

            // Levers
            // this.activateLever("control_lever_3");
            // this.activateLever("control_lever_4");
            // this.activateLever("control_lever_5");
            // this.activateLever("control_lever_6");
            // this.activateLever("control_lever_7");
            // this.activateLever("control_lever_8");
            // this.activateLever("control_lever_9");

            // Cradles
            // this.activateRandomizer();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    private void activateButton(String name) {
        this.controls.getChild(name).y += 0.25F;
    }

    private void activateLever(String name) {
        this.controls.getChild(name).getChild(name + "_body").xRot -= 1.5F;
    }

    private void activateRandomizer() {
        ModelPart randomizer = this.controls.getChild("control_cradle_1").getChild("control_cradle_1_body").getChild("control_cradle_1_body_r1");
        randomizer.yRot += 1.57F;
    }

    private void activateFacingControl(int phase) {
        ModelPart facingControl = this.controls.getChild("control_cradle_2").getChild("control_cradle_2_body").getChild("control_cradle_2_body_r1");
        facingControl.yRot += 1.57F * phase;
    }

    private void activateHandbrake() {
        ModelPart handbrake = this.controls.getChild("control_handbrake").getChild("control_handbrake_body");
        handbrake.xRot -= 0.6F;
        handbrake.yRot += 1.4F;
        handbrake.zRot -= 0.9F;
    }

    private void activateStarter() {
        ModelPart starter = this.controls.getChild("control_starter").getChild("control_starter_body");
        starter.xRot -= 2.2F;
    }
}

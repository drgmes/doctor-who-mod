package net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.models;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.TardisToyotaSpinnerBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class TardisToyotaSpinnerModel extends Model {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(DWM.getIdentifier("textures/entity/tardis/misc/tardis_toyota_spinner.png"), "main");
    private final ModelPart platform;
    private final ModelPart rotor_1;
    private final ModelPart rotor_2;
    private final ModelPart rotor_3;

    public TardisToyotaSpinnerModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucent);

        this.platform = root.getChild("platform");
        this.rotor_1 = root.getChild("rotor_1");
        this.rotor_2 = root.getChild("rotor_2");
        this.rotor_3 = root.getChild("rotor_3");
    }

    @SuppressWarnings("unused")
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData platform = modelPartData.addChild("platform", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 13.3F, 0.0F));
        ModelPartData platform_15_r1 = platform.addChild("platform_15_r1", ModelPartBuilder.create().uv(0, 50).cuboid(-10.0547F, -5.0F, -2.0F, 1.0F, 10.0F, 4.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2849F, -0.274F, -0.8249F));
        ModelPartData platform_14_r1 = platform.addChild("platform_14_r1", ModelPartBuilder.create().uv(20, 80).cuboid(-10.0547F, -5.0F, -2.0F, 1.0F, 10.0F, 4.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
        ModelPartData platform_13_r1 = platform.addChild("platform_13_r1", ModelPartBuilder.create().uv(197, 27).cuboid(-10.0547F, -5.0F, -2.0F, 1.0F, 10.0F, 4.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2849F, 0.274F, -0.8249F));
        ModelPartData platform_12_r1 = platform.addChild("platform_12_r1", ModelPartBuilder.create().uv(32, 205).cuboid(9.0547F, -5.0F, -2.0F, 1.0F, 10.0F, 4.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2849F, -0.274F, 0.8249F));
        ModelPartData platform_11_r1 = platform.addChild("platform_11_r1", ModelPartBuilder.create().uv(42, 205).cuboid(9.0547F, -5.0F, -2.0F, 1.0F, 10.0F, 4.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
        ModelPartData platform_10_r1 = platform.addChild("platform_10_r1", ModelPartBuilder.create().uv(207, 81).cuboid(9.0547F, -5.0F, -2.0F, 1.0F, 10.0F, 4.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2849F, 0.274F, 0.8249F));
        ModelPartData platform_9_r1 = platform.addChild("platform_9_r1", ModelPartBuilder.create().uv(162, 211).cuboid(-2.0F, -5.0F, 9.0547F, 4.0F, 10.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7854F, -0.7854F, 0.0F));
        ModelPartData platform_8_r1 = platform.addChild("platform_8_r1", ModelPartBuilder.create().uv(16, 212).cuboid(-2.0F, -5.0F, 9.0547F, 4.0F, 10.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7854F, -0.3927F, 0.0F));
        ModelPartData platform_7_r1 = platform.addChild("platform_7_r1", ModelPartBuilder.create().uv(212, 146).cuboid(-2.0F, -5.0F, 9.0547F, 4.0F, 10.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));
        ModelPartData platform_6_r1 = platform.addChild("platform_6_r1", ModelPartBuilder.create().uv(91, 214).cuboid(-2.0F, -5.0F, 9.0547F, 4.0F, 10.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7854F, 0.3927F, 0.0F));
        ModelPartData platform_5_r1 = platform.addChild("platform_5_r1", ModelPartBuilder.create().uv(117, 214).cuboid(-2.0F, -5.0F, 9.0547F, 4.0F, 10.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7854F, 0.7854F, 0.0F));
        ModelPartData platform_4_r1 = platform.addChild("platform_4_r1", ModelPartBuilder.create().uv(214, 123).cuboid(-2.0F, -5.0F, -10.0547F, 4.0F, 10.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.7854F, -0.7854F, 0.0F));
        ModelPartData platform_3_r1 = platform.addChild("platform_3_r1", ModelPartBuilder.create().uv(214, 212).cuboid(-2.0F, -5.0F, -10.0547F, 4.0F, 10.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.7854F, -0.3927F, 0.0F));
        ModelPartData platform_2_r1 = platform.addChild("platform_2_r1", ModelPartBuilder.create().uv(101, 216).cuboid(-2.0F, -5.0F, -10.0547F, 4.0F, 10.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));
        ModelPartData platform_1_r1 = platform.addChild("platform_1_r1", ModelPartBuilder.create().uv(217, 75).cuboid(-2.0F, -5.0F, -10.0547F, 4.0F, 10.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.7854F, 0.3927F, 0.0F));
        ModelPartData platform_0_r1 = platform.addChild("platform_0_r1", ModelPartBuilder.create().uv(189, 217).cuboid(-2.0F, -5.0F, -10.0547F, 4.0F, 10.0F, 1.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.7854F, 0.7854F, 0.0F));
        ModelPartData rotor_1 = modelPartData.addChild("rotor_1", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 25.8F, 0.0F));
        ModelPartData rotor_1_body = rotor_1.addChild("rotor_1_body", ModelPartBuilder.create().uv(0, 186).cuboid(-3.0F, -13.5F, -15.082F, 6.0F, 5.0F, 6.0F).uv(114, 183).cuboid(-3.0F, -13.5F, 9.082F, 6.0F, 5.0F, 6.0F).uv(182, 0).cuboid(9.082F, -13.5F, -3.0F, 6.0F, 5.0F, 6.0F).uv(24, 175).cuboid(-15.082F, -13.5F, -3.0F, 6.0F, 5.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData rotor_1_body_15_r1 = rotor_1_body.addChild("rotor_1_body_15_r1", ModelPartBuilder.create().uv(0, 175).cuboid(-15.082F, -3.5F, -3.0F, 6.0F, 5.0F, 6.0F).uv(180, 128).cuboid(9.082F, -3.5F, -3.0F, 6.0F, 5.0F, 6.0F).uv(90, 183).cuboid(-3.0F, -3.5F, 9.082F, 6.0F, 5.0F, 6.0F).uv(138, 185).cuboid(-3.0F, -3.5F, -15.082F, 6.0F, 5.0F, 6.0F), ModelTransform.of(0.0F, -10.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
        ModelPartData rotor_1_body_13_r1 = rotor_1_body.addChild("rotor_1_body_13_r1", ModelPartBuilder.create().uv(72, 178).cuboid(-15.082F, -3.5F, -3.0F, 6.0F, 5.0F, 6.0F).uv(183, 16).cuboid(9.082F, -3.5F, -3.0F, 6.0F, 5.0F, 6.0F).uv(184, 109).cuboid(-3.0F, -3.5F, 9.082F, 6.0F, 5.0F, 6.0F).uv(187, 74).cuboid(-3.0F, -3.5F, -15.082F, 6.0F, 5.0F, 6.0F), ModelTransform.of(0.0F, -10.0F, 0.0F, 0.0F, 0.3927F, 0.0F));
        ModelPartData rotor_1_body_9_r1 = rotor_1_body.addChild("rotor_1_body_9_r1", ModelPartBuilder.create().uv(42, 183).cuboid(-3.0F, -3.5F, 9.082F, 6.0F, 5.0F, 6.0F).uv(184, 182).cuboid(-3.0F, -3.5F, -15.082F, 6.0F, 5.0F, 6.0F), ModelTransform.of(0.0F, -10.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        ModelPartData rotor_1_body_5_r1 = rotor_1_body.addChild("rotor_1_body_5_r1", ModelPartBuilder.create().uv(184, 167).cuboid(-3.0F, -3.5F, 9.082F, 6.0F, 5.0F, 6.0F).uv(162, 187).cuboid(-3.0F, -3.5F, -15.082F, 6.0F, 5.0F, 6.0F), ModelTransform.of(0.0F, -10.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        ModelPartData rotor_1_corner = rotor_1.addChild("rotor_1_corner", ModelPartBuilder.create().uv(202, 102).cuboid(-0.75F, -3.5F, -15.5957F, 1.0F, 6.0F, 7.0F).uv(16, 199).cuboid(-0.25F, -3.5F, 8.5957F, 1.0F, 6.0F, 7.0F).uv(92, 42).cuboid(8.5957F, -3.5F, 0.25F, 7.0F, 6.0F, -1.0F).uv(84, 207).cuboid(-15.5957F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F), ModelTransform.of(0.0F, -10.5F, 0.0F, 0.0F, -0.2182F, 0.0F));
        ModelPartData rotor_1_corner_15_r1 = rotor_1_corner.addChild("rotor_1_corner_15_r1", ModelPartBuilder.create().uv(207, 17).cuboid(-15.5957F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F).uv(211, 102).cuboid(8.5957F, -3.5F, -0.75F, 7.0F, 6.0F, 1.0F).uv(158, 198).cuboid(-0.25F, -3.5F, 8.5957F, 1.0F, 6.0F, 7.0F).uv(201, 188).cuboid(-0.75F, -3.5F, -15.5957F, 1.0F, 6.0F, 7.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
        ModelPartData rotor_1_corner_13_r1 = rotor_1_corner.addChild("rotor_1_corner_13_r1", ModelPartBuilder.create().uv(117, 207).cuboid(-15.5957F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F).uv(146, 211).cuboid(8.5957F, -3.5F, -0.75F, 7.0F, 6.0F, 1.0F).uv(188, 199).cuboid(-0.25F, -3.5F, 8.5957F, 1.0F, 6.0F, 7.0F).uv(64, 40).cuboid(0.25F, -3.5F, -15.5957F, -1.0F, 6.0F, 7.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));
        ModelPartData rotor_1_corner_9_r1 = rotor_1_corner.addChild("rotor_1_corner_9_r1", ModelPartBuilder.create().uv(198, 120).cuboid(-0.25F, -3.5F, 8.5957F, 1.0F, 6.0F, 7.0F).uv(201, 4).cuboid(-0.75F, -3.5F, -15.5957F, 1.0F, 6.0F, 7.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        ModelPartData rotor_1_corner_5_r1 = rotor_1_corner.addChild("rotor_1_corner_5_r1", ModelPartBuilder.create().uv(66, 200).cuboid(-0.25F, -3.5F, 8.5957F, 1.0F, 6.0F, 7.0F).uv(203, 133).cuboid(-0.75F, -3.5F, -15.5957F, 1.0F, 6.0F, 7.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        ModelPartData rotor_2 = modelPartData.addChild("rotor_2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 25.8F, 0.0F));
        ModelPartData rotor_2_body = rotor_2.addChild("rotor_2_body", ModelPartBuilder.create().uv(134, 174).cuboid(-4.0F, -10.5F, -20.1094F, 8.0F, 5.0F, 6.0F).uv(84, 167).cuboid(-4.0F, -10.5F, 14.1094F, 8.0F, 5.0F, 6.0F).uv(164, 115).cuboid(14.1094F, -10.5F, -4.0F, 6.0F, 5.0F, 8.0F).uv(0, 162).cuboid(-20.1094F, -10.5F, -4.0F, 6.0F, 5.0F, 8.0F), ModelTransform.pivot(0.0F, -8.0F, 0.0F));
        ModelPartData rotor_2_body_15_r1 = rotor_2_body.addChild("rotor_2_body_15_r1", ModelPartBuilder.create().uv(154, 161).cuboid(-20.1094F, -3.5F, -4.0F, 6.0F, 5.0F, 8.0F).uv(162, 102).cuboid(14.1094F, -3.5F, -4.0F, 6.0F, 5.0F, 8.0F).uv(166, 141).cuboid(-4.0F, -3.5F, 14.1094F, 8.0F, 5.0F, 6.0F).uv(106, 172).cuboid(-4.0F, -3.5F, -20.1094F, 8.0F, 5.0F, 6.0F), ModelTransform.of(0.0F, -7.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
        ModelPartData rotor_2_body_13_r1 = rotor_2_body.addChild("rotor_2_body_13_r1", ModelPartBuilder.create().uv(28, 162).cuboid(-20.1094F, -3.5F, -4.0F, 6.0F, 5.0F, 8.0F).uv(165, 29).cuboid(14.1094F, -3.5F, -4.0F, 6.0F, 5.0F, 8.0F).uv(171, 63).cuboid(-4.0F, -3.5F, 14.1094F, 8.0F, 5.0F, 6.0F).uv(174, 155).cuboid(-4.0F, -3.5F, -20.1094F, 8.0F, 5.0F, 6.0F), ModelTransform.of(0.0F, -7.0F, 0.0F, 0.0F, 0.3927F, 0.0F));
        ModelPartData rotor_2_body_9_r1 = rotor_2_body.addChild("rotor_2_body_9_r1", ModelPartBuilder.create().uv(166, 45).cuboid(-4.0F, -3.5F, 14.1094F, 8.0F, 5.0F, 6.0F).uv(50, 172).cuboid(-4.0F, -3.5F, -20.1094F, 8.0F, 5.0F, 6.0F), ModelTransform.of(0.0F, -7.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        ModelPartData rotor_2_body_5_r1 = rotor_2_body.addChild("rotor_2_body_5_r1", ModelPartBuilder.create().uv(171, 91).cuboid(-4.0F, -3.5F, 14.1094F, 8.0F, 5.0F, 6.0F).uv(162, 174).cuboid(-4.0F, -3.5F, -20.1094F, 8.0F, 5.0F, 6.0F), ModelTransform.of(0.0F, -7.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        ModelPartData rotor_2_corner = rotor_2.addChild("rotor_2_corner", ModelPartBuilder.create().uv(142, 196).cuboid(-0.75F, -3.5F, -20.8457F, 1.0F, 6.0F, 7.0F).uv(91, 194).cuboid(-0.25F, -3.5F, 13.8457F, 1.0F, 6.0F, 7.0F).uv(48, 91).cuboid(13.8457F, -3.5F, 0.25F, 7.0F, 6.0F, -1.0F).uv(166, 56).cuboid(-20.8457F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F), ModelTransform.of(0.0F, -15.5F, 0.0F, 0.0F, -0.2182F, 0.0F));
        ModelPartData rotor_2_corner_15_r1 = rotor_2_corner.addChild("rotor_2_corner_15_r1", ModelPartBuilder.create().uv(164, 22).cuboid(-20.8457F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F).uv(196, 152).cuboid(13.8457F, -3.5F, -0.75F, 7.0F, 6.0F, 1.0F).uv(49, 194).cuboid(-0.25F, -3.5F, 13.8457F, 1.0F, 6.0F, 7.0F).uv(196, 55).cuboid(-0.75F, -3.5F, -20.8457F, 1.0F, 6.0F, 7.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
        ModelPartData rotor_2_corner_13_r1 = rotor_2_corner.addChild("rotor_2_corner_13_r1", ModelPartBuilder.create().uv(100, 194).cuboid(-20.8457F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F).uv(205, 68).cuboid(13.8457F, -3.5F, -0.75F, 7.0F, 6.0F, 1.0F).uv(110, 194).cuboid(-0.25F, -3.5F, 13.8457F, 1.0F, 6.0F, 7.0F).uv(62, 34).cuboid(0.25F, -3.5F, -20.8457F, -1.0F, 6.0F, 7.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));
        ModelPartData rotor_2_corner_9_r1 = rotor_2_corner.addChild("rotor_2_corner_9_r1", ModelPartBuilder.create().uv(179, 193).cuboid(-0.25F, -3.5F, 13.8457F, 1.0F, 6.0F, 7.0F).uv(194, 139).cuboid(-0.75F, -3.5F, -20.8457F, 1.0F, 6.0F, 7.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        ModelPartData rotor_2_corner_5_r1 = rotor_2_corner.addChild("rotor_2_corner_5_r1", ModelPartBuilder.create().uv(126, 194).cuboid(-0.25F, -3.5F, 13.8457F, 1.0F, 6.0F, 7.0F).uv(0, 197).cuboid(-0.75F, -3.5F, -20.8457F, 1.0F, 6.0F, 7.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        ModelPartData rotor_3 = modelPartData.addChild("rotor_3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 25.8F, 0.0F));
        ModelPartData rotor_3_body = rotor_3.addChild("rotor_3_body", ModelPartBuilder.create().uv(58, 161).cuboid(-5.0F, -13.5F, -25.1367F, 10.0F, 5.0F, 6.0F).uv(148, 130).cuboid(-5.0F, -13.5F, 19.1367F, 10.0F, 5.0F, 6.0F).uv(0, 147).cuboid(19.1367F, -13.5F, -5.0F, 6.0F, 5.0F, 10.0F).uv(140, 107).cuboid(-25.1367F, -13.5F, -5.0F, 6.0F, 5.0F, 10.0F), ModelTransform.pivot(0.0F, -10.0F, 0.0F));
        ModelPartData rotor_3_body_15_r1 = rotor_3_body.addChild("rotor_3_body_15_r1", ModelPartBuilder.create().uv(139, 87).cuboid(-25.1367F, -3.5F, -5.0F, 6.0F, 5.0F, 10.0F).uv(144, 146).cuboid(19.1367F, -3.5F, -5.0F, 6.0F, 5.0F, 10.0F).uv(145, 69).cuboid(-5.0F, -3.5F, 19.1367F, 10.0F, 5.0F, 6.0F).uv(157, 11).cuboid(-5.0F, -3.5F, -25.1367F, 10.0F, 5.0F, 6.0F), ModelTransform.of(0.0F, -10.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
        ModelPartData rotor_3_body_13_r1 = rotor_3_body.addChild("rotor_3_body_13_r1", ModelPartBuilder.create().uv(144, 54).cuboid(-25.1367F, -3.5F, -5.0F, 6.0F, 5.0F, 10.0F).uv(32, 147).cuboid(19.1367F, -3.5F, -5.0F, 6.0F, 5.0F, 10.0F).uv(150, 0).cuboid(-5.0F, -3.5F, 19.1367F, 10.0F, 5.0F, 6.0F).uv(161, 80).cuboid(-5.0F, -3.5F, -25.1367F, 10.0F, 5.0F, 6.0F), ModelTransform.of(0.0F, -10.0F, 0.0F, 0.0F, 0.3927F, 0.0F));
        ModelPartData rotor_3_body_9_r1 = rotor_3_body.addChild("rotor_3_body_9_r1", ModelPartBuilder.create().uv(139, 40).cuboid(-5.0F, -3.5F, 19.1367F, 10.0F, 5.0F, 6.0F).uv(96, 156).cuboid(-5.0F, -3.5F, -25.1367F, 10.0F, 5.0F, 6.0F), ModelTransform.of(0.0F, -10.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        ModelPartData rotor_3_body_5_r1 = rotor_3_body.addChild("rotor_3_body_5_r1", ModelPartBuilder.create().uv(70, 150).cuboid(-5.0F, -3.5F, 19.1367F, 10.0F, 5.0F, 6.0F).uv(122, 161).cuboid(-5.0F, -3.5F, -25.1367F, 10.0F, 5.0F, 6.0F), ModelTransform.of(0.0F, -10.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
        ModelPartData rotor_3_corner = rotor_3.addChild("rotor_3_corner", ModelPartBuilder.create().uv(33, 192).cuboid(-0.75F, -3.5F, -25.8457F, 1.0F, 6.0F, 7.0F).uv(24, 186).cuboid(-0.25F, -3.5F, 18.8457F, 1.0F, 6.0F, 7.0F).uv(78, 31).cuboid(18.8457F, -3.5F, 0.25F, 7.0F, 6.0F, -1.0F).uv(76, 42).cuboid(-25.8457F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F), ModelTransform.of(0.0F, -20.5F, 0.0F, 0.0F, -0.2182F, 0.0F));
        ModelPartData rotor_3_corner_15_r1 = rotor_3_corner.addChild("rotor_3_corner_15_r1", ModelPartBuilder.create().uv(62, 0).cuboid(-25.8457F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F).uv(147, 122).cuboid(18.8457F, -3.5F, -0.75F, 7.0F, 6.0F, 1.0F).uv(87, 62).cuboid(-0.25F, -3.5F, 18.8457F, 1.0F, 6.0F, 7.0F).uv(75, 189).cuboid(-0.75F, -3.5F, -25.8457F, 1.0F, 6.0F, 7.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));
        ModelPartData rotor_3_corner_13_r1 = rotor_3_corner.addChild("rotor_3_corner_13_r1", ModelPartBuilder.create().uv(22, 147).cuboid(-25.8457F, -3.5F, -0.25F, 7.0F, 6.0F, 1.0F).uv(20, 162).cuboid(18.8457F, -3.5F, -0.75F, 7.0F, 6.0F, 1.0F).uv(187, 49).cuboid(-0.25F, -3.5F, 18.8457F, 1.0F, 6.0F, 7.0F).uv(22, 43).cuboid(0.25F, -3.5F, -25.8457F, -1.0F, 6.0F, 7.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));
        ModelPartData rotor_3_corner_9_r1 = rotor_3_corner.addChild("rotor_3_corner_9_r1", ModelPartBuilder.create().uv(64, 69).cuboid(-0.25F, -3.5F, 18.8457F, 1.0F, 6.0F, 7.0F).uv(188, 35).cuboid(-0.75F, -3.5F, -25.8457F, 1.0F, 6.0F, 7.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        ModelPartData rotor_3_corner_5_r1 = rotor_3_corner.addChild("rotor_3_corner_5_r1", ModelPartBuilder.create().uv(59, 187).cuboid(-0.25F, -3.5F, 18.8457F, 1.0F, 6.0F, 7.0F).uv(192, 95).cuboid(-0.75F, -3.5F, -25.8457F, 1.0F, 6.0F, 7.0F), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        return TexturedModelData.of(modelData, 256, 256);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        platform.render(matrixStack, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotor_1.render(matrixStack, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotor_2.render(matrixStack, vertexConsumer, light, overlay, red, green, blue, alpha);
        rotor_3.render(matrixStack, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    public void setupAnim(TardisToyotaSpinnerBlockEntity tile) {
        float angle = 0.035F;
        this.platform.yaw -= tile.tickInProgress * angle;
        this.rotor_1.yaw += tile.tickInProgress * angle;
        this.rotor_2.yaw -= tile.tickInProgress * angle;
        this.rotor_3.yaw += tile.tickInProgress * angle;
    }
}

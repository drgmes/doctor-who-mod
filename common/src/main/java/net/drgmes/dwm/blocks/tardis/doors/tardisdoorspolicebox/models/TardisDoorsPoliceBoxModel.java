package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.models;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class TardisDoorsPoliceBoxModel extends BaseTardisDoorsModel {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(DWM.getIdentifier("textures/block/tardis/doors/tardis_doors_police_box.png"), "main");

    public TardisDoorsPoliceBoxModel(ModelPart root) {
        super(root);
    }

    @SuppressWarnings("unused")
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 0).cuboid(-25.0F, -2.0F, -5.0F, 50.0F, 2.0F, 10.0F, new Dilation(0.0F))
            .uv(24, 12).cuboid(-23.0F, -74.0F, -3.0F, 6.0F, 72.0F, 6.0F, new Dilation(0.0F))
            .uv(0, 12).cuboid(17.0F, -74.0F, -3.0F, 6.0F, 72.0F, 6.0F, new Dilation(0.0F))
            .uv(48, 21).cuboid(-17.0F, -73.0F, -2.5F, 34.0F, 9.0F, 5.0F, new Dilation(0.0F))
            .uv(12, 90).cuboid(16.0F, -64.0F, -2.5F, 1.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(0, 90).cuboid(-17.0F, -64.0F, -2.5F, 1.0F, 62.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData label = base.addChild("label", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData label_5_r1 = label.addChild("label_5_r1", ModelPartBuilder.create().uv(48, 12).cuboid(-21.0F, -7.0F, 0.0F, 42.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -66.1F, -2.0F, -3.1416F, 0.0F, 3.1416F));

        ModelPartData label_4_r1 = label.addChild("label_4_r1", ModelPartBuilder.create().uv(4, 0).cuboid(-21.0F, -7.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -66.1F, -3.0F, -3.1416F, 0.0F, 3.1416F));

        ModelPartData label_3_r1 = label.addChild("label_3_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-21.0F, -7.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-41.0F, -66.1F, -3.0F, -3.1416F, 0.0F, 3.1416F));

        ModelPartData label_2_r1 = label.addChild("label_2_r1", ModelPartBuilder.create().uv(48, 43).cuboid(-60.0F, -6.0F, 1.0F, 40.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-40.0F, -67.1F, -3.0F, -3.1416F, 0.0F, 3.1416F));

        ModelPartData label_1_r1 = label.addChild("label_1_r1", ModelPartBuilder.create().uv(48, 41).cuboid(-60.0F, -6.0F, 1.0F, 40.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-40.0F, -61.1F, -3.0F, -3.1416F, 0.0F, 3.1416F));

        ModelPartData label_0_r1 = label.addChild("label_0_r1", ModelPartBuilder.create().uv(48, 35).cuboid(-60.0F, -7.0F, 1.0F, 40.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-40.0F, -65.1F, -2.25F, -3.1416F, 0.0F, 3.1416F));

        ModelPartData door_left = modelPartData.addChild("door_left", ModelPartBuilder.create().uv(48, 45).cuboid(1.2024F, -29.6429F, -0.3548F, 11.0F, 58.0F, 1.0F, new Dilation(0.0F))
            .uv(136, 45).cuboid(3.2024F, -7.6429F, -0.4548F, 7.0F, 9.0F, 1.0F, new Dilation(0.0F))
            .uv(110, 0).cuboid(3.2024F, -6.6429F, 0.5452F, 7.0F, 7.0F, 3.0F, new Dilation(0.0F))
            .uv(0, 12).cuboid(12.4524F, -4.6429F, -2.7048F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(7, 90).cuboid(12.2024F, -4.6429F, 1.6452F, 3.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(24, 90).cuboid(13.2024F, -31.6429F, -2.8548F, 2.0F, 62.0F, 3.0F, new Dilation(0.0F))
            .uv(96, 45).cuboid(13.2024F, -31.6429F, 0.1452F, 2.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(104, 45).cuboid(-0.7976F, -31.6429F, -1.8548F, 2.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(84, 104).cuboid(-0.7976F, -31.6429F, 0.1452F, 2.0F, 62.0F, 1.0F, new Dilation(0.0F))
            .uv(72, 104).cuboid(12.2024F, -31.6429F, -2.3548F, 1.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(60, 104).cuboid(12.2024F, -31.6429F, -0.3548F, 1.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(121, 21).cuboid(1.2024F, 28.3571F, -1.8548F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(130, 42).cuboid(1.2024F, 28.3571F, 0.1452F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(120, 117).cuboid(1.2024F, 15.3571F, -1.8548F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(130, 36).cuboid(1.2024F, 15.3571F, 0.1452F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(120, 113).cuboid(1.2024F, 2.3571F, -1.8548F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(130, 3).cuboid(1.2024F, 2.3571F, 0.1452F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(120, 109).cuboid(1.2024F, -10.6429F, -1.8548F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(122, 129).cuboid(1.2024F, -10.6429F, 0.1452F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(112, 105).cuboid(1.2024F, -31.6429F, -1.8548F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(96, 132).cuboid(1.2024F, -31.6429F, 0.1452F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-15.2024F, -8.3571F, 0.3548F));

        ModelPartData door_left_window = door_left.addChild("door_left_window", ModelPartBuilder.create().uv(112, 45).cuboid(-14.0F, -62.0F, -0.1F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(96, 109).cuboid(-14.0F, -62.0F, 0.15F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(129, 33).cuboid(-14.0F, -44.0F, -0.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(127, 0).cuboid(-14.0F, -62.0F, -0.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(102, 138).cuboid(-4.0F, -61.0F, -0.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(96, 138).cuboid(-14.0F, -61.0F, -0.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(136, 86).cuboid(-9.0F, -61.0F, -0.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(135, 83).cuboid(-13.0F, -50.0F, -0.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(135, 63).cuboid(-13.0F, -56.0F, -0.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(15.2024F, 32.3571F, -0.3548F));

        ModelPartData door_right = modelPartData.addChild("door_right", ModelPartBuilder.create().uv(72, 45).cuboid(-11.6864F, -29.7727F, -0.2818F, 11.0F, 58.0F, 1.0F, new Dilation(0.0F))
            .uv(18, 12).cuboid(-8.6864F, -5.7727F, -0.3818F, 5.0F, 5.0F, 1.0F, new Dilation(0.0F))
            .uv(44, 90).cuboid(-12.9364F, -6.2727F, -2.6318F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
            .uv(4, 8).cuboid(-12.8364F, 2.7273F, -2.3818F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(19, 90).cuboid(-14.6864F, -4.7727F, 1.7182F, 3.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 8).cuboid(-13.1864F, -4.2727F, 1.9682F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(34, 90).cuboid(-14.6864F, -31.7727F, -2.7818F, 2.0F, 62.0F, 3.0F, new Dilation(0.0F))
            .uv(44, 104).cuboid(-14.6864F, -31.7727F, 0.2182F, 2.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(52, 104).cuboid(-0.6864F, -31.7727F, -1.7818F, 2.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(90, 104).cuboid(-0.6864F, -31.7727F, 0.2182F, 2.0F, 62.0F, 1.0F, new Dilation(0.0F))
            .uv(78, 104).cuboid(-12.6864F, -31.7727F, -2.2818F, 1.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(66, 104).cuboid(-12.6864F, -31.7727F, -0.2818F, 1.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(128, 8).cuboid(-11.6864F, 28.2273F, -1.7818F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(120, 135).cuboid(-11.6864F, 28.2273F, 0.2182F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(126, 29).cuboid(-11.6864F, 15.2273F, -1.7818F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(136, 12).cuboid(-11.6864F, 15.2273F, 0.2182F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(126, 25).cuboid(-11.6864F, 2.2273F, -1.7818F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(136, 15).cuboid(-11.6864F, 2.2273F, 0.2182F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(120, 125).cuboid(-11.6864F, -10.7727F, -1.7818F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(96, 135).cuboid(-11.6864F, -10.7727F, 0.2182F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(120, 121).cuboid(-11.6864F, -31.7727F, -1.7818F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(120, 132).cuboid(-11.6864F, -31.7727F, 0.2182F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(14.6864F, -8.2273F, 0.2818F));

        ModelPartData door_right_window = door_right.addChild("door_right_window", ModelPartBuilder.create().uv(112, 85).cuboid(3.0F, -62.0F, -0.1F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(112, 65).cuboid(3.0F, -62.0F, 0.15F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(96, 129).cuboid(3.0F, -44.0F, -0.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(129, 39).cuboid(3.0F, -62.0F, -0.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(120, 138).cuboid(3.0F, -61.0F, -0.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(114, 138).cuboid(13.0F, -61.0F, -0.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(108, 138).cuboid(8.0F, -61.0F, -0.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(136, 55).cuboid(4.0F, -50.0F, -0.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(136, 18).cuboid(4.0F, -56.0F, -0.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-14.6864F, 32.2273F, -0.2818F));

        return TexturedModelData.of(modelData, 256, 256);
    }
}

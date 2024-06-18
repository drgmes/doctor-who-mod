package net.drgmes.dwm.blocks.tardis.doors.tardisdoorscapsule.tardisdoorsphonebox.models;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class TardisDoorsCapsuleModel extends BaseTardisDoorsModel {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(DWM.getIdentifier("textures/block/tardis/doors/tardis_doors_capsule.png"), "main");

    public TardisDoorsCapsuleModel(ModelPart root) {
        super(root);
    }

    @SuppressWarnings("unused")
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(84, 0).cuboid(-18.0F, -3.0F, -3.0F, 36.0F, 3.0F, 3.0F, new Dilation(0.0F))
            .uv(36, 72).cuboid(-18.0F, -3.0F, 0.0F, 36.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData borders = base.addChild("borders", ModelPartBuilder.create().uv(36, 78).cuboid(23.0F, -69.0F, -3.0F, 36.0F, 3.0F, 3.0F, new Dilation(0.0F))
            .uv(30, 66).cuboid(23.0F, -69.0F, 0.0F, 36.0F, 3.0F, 3.0F, new Dilation(0.0F))
            .uv(18, 66).cuboid(20.0F, -69.0F, -3.0F, 3.0F, 69.0F, 6.0F, new Dilation(0.0F))
            .uv(46, 84).cuboid(23.0F, -66.0F, -3.0F, 1.0F, 63.0F, 4.0F, new Dilation(0.0F))
            .uv(0, 66).cuboid(59.0F, -69.0F, -3.0F, 3.0F, 69.0F, 6.0F, new Dilation(0.0F))
            .uv(36, 84).cuboid(58.0F, -66.0F, -3.0F, 1.0F, 63.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-41.0F, 0.0F, 0.0F));

        ModelPartData door_left = modelPartData.addChild("door_left", ModelPartBuilder.create().uv(42, 0).cuboid(0.8F, -19.3F, -3.5F, 18.0F, 63.0F, 3.0F, new Dilation(0.0F))
            .uv(68, 84).cuboid(17.8F, -19.3F, -4.5F, 1.0F, 63.0F, 1.0F, new Dilation(0.0F))
            .uv(60, 84).cuboid(17.8F, -19.3F, -0.5F, 1.0F, 63.0F, 1.0F, new Dilation(0.0F))
            .uv(88, 6).cuboid(0.8F, -19.3F, -4.5F, 17.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(84, 84).cuboid(0.8F, -19.3F, -0.5F, 17.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-18.8F, -22.7F, 2.0F));

        ModelPartData door_left_decor = door_left.addChild("door_left_decor", ModelPartBuilder.create().uv(116, 98).cuboid(-6.0F, -61.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(116, 66).cuboid(-16.0F, -61.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(116, 50).cuboid(-6.0F, -51.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(116, 42).cuboid(-16.0F, -51.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(103, 32).cuboid(-13.0F, -60.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(103, 24).cuboid(-13.0F, -50.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(96, 116).cuboid(-5.0F, -58.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(116, 90).cuboid(-15.0F, -58.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(88, 41).cuboid(-13.0F, -58.0F, 1.0F, 7.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(100, 102).cuboid(-13.0F, -36.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(116, 34).cuboid(-6.0F, -37.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(116, 26).cuboid(-16.0F, -37.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(84, 114).cuboid(-16.0F, -47.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(113, 77).cuboid(-6.0F, -47.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(88, 33).cuboid(-13.0F, -44.0F, 1.0F, 7.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(112, 112).cuboid(-5.0F, -44.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(104, 112).cuboid(-15.0F, -44.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(100, 100).cuboid(-13.0F, -46.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(100, 98).cuboid(-13.0F, -22.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(112, 108).cuboid(-6.0F, -23.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(112, 104).cuboid(-16.0F, -23.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(96, 112).cuboid(-16.0F, -33.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(111, 70).cuboid(-6.0F, -33.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(88, 25).cuboid(-13.0F, -30.0F, 1.0F, 7.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(92, 110).cuboid(-5.0F, -30.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(108, 108).cuboid(-15.0F, -30.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(88, 61).cuboid(-13.0F, -32.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(88, 59).cuboid(-13.0F, -8.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(100, 108).cuboid(-6.0F, -9.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(108, 66).cuboid(-16.0F, -9.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(92, 106).cuboid(-16.0F, -19.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(42, 84).cuboid(-6.0F, -19.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(88, 17).cuboid(-13.0F, -16.0F, 1.0F, 7.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(88, 106).cuboid(-5.0F, -16.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(84, 106).cuboid(-15.0F, -16.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(88, 57).cuboid(-13.0F, -18.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(104, 34).cuboid(-12.0F, -49.0F, 1.0F, 5.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(104, 26).cuboid(-12.0F, -35.0F, 1.0F, 5.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(12, 66).cuboid(-12.0F, -21.0F, 1.0F, 5.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(88, 11).cuboid(-17.5F, -62.5F, 1.0F, 16.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(88, 9).cuboid(-17.5F, -5.5F, 1.0F, 16.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(72, 84).cuboid(-2.5F, -61.5F, 1.0F, 1.0F, 56.0F, 1.0F, new Dilation(0.0F))
            .uv(84, 6).cuboid(-17.5F, -61.5F, 1.0F, 1.0F, 56.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(18.8F, 46.7F, -2.0F));

        ModelPartData door_right = modelPartData.addChild("door_right", ModelPartBuilder.create().uv(0, 0).cuboid(-18.8F, -19.3F, -3.5F, 18.0F, 63.0F, 3.0F, new Dilation(0.0F))
            .uv(64, 84).cuboid(-18.8F, -19.3F, -4.5F, 1.0F, 63.0F, 1.0F, new Dilation(0.0F))
            .uv(56, 84).cuboid(-18.8F, -19.3F, -0.5F, 1.0F, 63.0F, 1.0F, new Dilation(0.0F))
            .uv(84, 87).cuboid(-17.8F, -19.3F, -4.5F, 17.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(84, 63).cuboid(-17.8F, -19.3F, -0.5F, 17.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(18.8F, -22.7F, 2.0F));

        ModelPartData door_right_decor = door_right.addChild("door_right_decor", ModelPartBuilder.create().uv(116, 120).cuboid(13.0F, -61.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(121, 77).cuboid(3.0F, -61.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(122, 13).cuboid(13.0F, -51.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(122, 9).cuboid(3.0F, -51.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(104, 19).cuboid(6.0F, -60.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(104, 21).cuboid(6.0F, -50.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(112, 120).cuboid(14.0F, -58.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(104, 120).cuboid(4.0F, -58.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(100, 90).cuboid(6.0F, -58.0F, 1.0F, 7.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(103, 60).cuboid(6.0F, -36.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(119, 81).cuboid(13.0F, -37.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(120, 17).cuboid(3.0F, -37.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(120, 62).cuboid(3.0F, -47.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(120, 85).cuboid(13.0F, -47.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(84, 98).cuboid(6.0F, -44.0F, 1.0F, 7.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(120, 89).cuboid(14.0F, -44.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(120, 102).cuboid(4.0F, -44.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(104, 17).cuboid(6.0F, -46.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(103, 56).cuboid(6.0F, -22.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(119, 22).cuboid(13.0F, -23.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(119, 30).cuboid(3.0F, -23.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(119, 38).cuboid(3.0F, -33.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(119, 46).cuboid(13.0F, -33.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(84, 90).cuboid(6.0F, -30.0F, 1.0F, 7.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(92, 118).cuboid(14.0F, -30.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(119, 54).cuboid(4.0F, -30.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(103, 58).cuboid(6.0F, -32.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(103, 40).cuboid(6.0F, -8.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(116, 112).cuboid(13.0F, -9.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(116, 116).cuboid(3.0F, -9.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(118, 73).cuboid(3.0F, -19.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(84, 118).cuboid(13.0F, -19.0F, 1.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(88, 49).cuboid(6.0F, -16.0F, 1.0F, 7.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(100, 116).cuboid(14.0F, -16.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(108, 116).cuboid(4.0F, -16.0F, 1.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(103, 48).cuboid(6.0F, -18.0F, 1.0F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(104, 42).cuboid(7.0F, -49.0F, 1.0F, 5.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(104, 50).cuboid(7.0F, -35.0F, 1.0F, 5.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(100, 104).cuboid(7.0F, -21.0F, 1.0F, 5.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(88, 15).cuboid(1.5F, -62.5F, 1.0F, 16.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(88, 13).cuboid(1.5F, -5.5F, 1.0F, 16.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(80, 84).cuboid(16.5F, -61.5F, 1.0F, 1.0F, 56.0F, 1.0F, new Dilation(0.0F))
            .uv(76, 84).cuboid(1.5F, -61.5F, 1.0F, 1.0F, 56.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-18.8F, 46.7F, -2.0F));

        return TexturedModelData.of(modelData, 256, 256);
    }
}

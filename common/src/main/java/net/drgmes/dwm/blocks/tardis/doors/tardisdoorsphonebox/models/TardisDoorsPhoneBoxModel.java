package net.drgmes.dwm.blocks.tardis.doors.tardisdoorsphonebox.models;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class TardisDoorsPhoneBoxModel extends BaseTardisDoorsModel {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(DWM.getIdentifier("textures/block/tardis/doors/tardis_doors_phone_box.png"), "main");

    public TardisDoorsPhoneBoxModel(ModelPart root) {
        super(root);
    }

    @SuppressWarnings("unused")
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 0).cuboid(-21.0F, -2.0F, -3.5F, 42.0F, 2.0F, 7.0F, new Dilation(0.0F))
            .uv(68, 19).cuboid(-15.0F, -3.0F, -2.5F, 30.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData corners = base.addChild("corners", ModelPartBuilder.create().uv(0, 68).cuboid(-20.0F, -64.0F, -2.5F, 5.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(48, 19).cuboid(15.0F, -64.0F, -2.5F, 5.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(0, 9).cuboid(-21.0F, -69.0F, -2.5F, 42.0F, 5.0F, 5.0F, new Dilation(0.0F))
            .uv(117, 85).cuboid(-20.0F, -69.0F, -3.5F, 5.0F, 5.0F, 7.0F, new Dilation(0.0F))
            .uv(112, 73).cuboid(15.0F, -69.0F, -3.5F, 5.0F, 5.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData strips = corners.addChild("strips", ModelPartBuilder.create().uv(96, 41).cuboid(-20.25F, -63.0F, -2.0F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(100, 41).cuboid(-20.25F, -63.0F, 1.0F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(68, 102).cuboid(-20.25F, -63.0F, -0.5F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(64, 86).cuboid(19.25F, -63.0F, -2.0F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(88, 41).cuboid(19.25F, -63.0F, 1.0F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(92, 41).cuboid(19.25F, -63.0F, -0.5F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(48, 86).cuboid(-19.5F, -63.0F, -2.75F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(36, 68).cuboid(18.5F, -63.0F, -2.75F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(80, 41).cuboid(17.0F, -63.0F, -2.75F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(52, 86).cuboid(15.5F, -63.0F, -2.75F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(56, 86).cuboid(-16.5F, -63.0F, -2.75F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(60, 86).cuboid(-18.0F, -63.0F, -2.75F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(84, 41).cuboid(15.5F, -63.0F, 1.75F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(76, 41).cuboid(17.0F, -63.0F, 1.75F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(72, 41).cuboid(18.5F, -63.0F, 1.75F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(40, 68).cuboid(-16.5F, -63.0F, 1.75F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(68, 41).cuboid(-19.5F, -63.0F, 1.75F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(44, 68).cuboid(-18.0F, -63.0F, 1.75F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData rondels = base.addChild("rondels", ModelPartBuilder.create().uv(28, 128).cuboid(-19.0F, -68.0F, -4.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(20, 128).cuboid(16.0F, -68.0F, -4.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(125, 97).cuboid(-19.0F, -68.0F, 3.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(127, 108).cuboid(16.0F, -68.0F, 3.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(127, 118).cuboid(-22.0F, -68.0F, -1.5F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F))
            .uv(117, 126).cuboid(21.0F, -68.0F, -1.5F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData door_left = modelPartData.addChild("door_left", ModelPartBuilder.create().uv(68, 25).cuboid(1.0F, 23.9F, -0.5F, 30.0F, 9.0F, 2.0F, new Dilation(0.0F))
            .uv(68, 36).cuboid(1.0F, -28.1F, -0.5F, 30.0F, 3.0F, 2.0F, new Dilation(0.0F))
            .uv(82, 102).cuboid(1.0F, -25.1F, -0.5F, 3.0F, 49.0F, 2.0F, new Dilation(0.0F))
            .uv(72, 102).cuboid(28.0F, -25.1F, -0.5F, 3.0F, 49.0F, 2.0F, new Dilation(0.0F))
            .uv(0, 19).cuboid(5.0F, -24.1F, -0.5F, 22.0F, 47.0F, 2.0F, new Dilation(0.0F))
            .uv(117, 97).cuboid(28.5F, -3.1F, -1.5F, 2.0F, 5.0F, 4.0F, new Dilation(0.0F))
            .uv(117, 106).cuboid(29.0F, -3.6F, -1.25F, 1.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-16.0F, -11.9F, 0.0F));

        ModelPartData door_left_decor = door_left.addChild("door_left_decor", ModelPartBuilder.create().uv(94, 13).cuboid(-12.0F, -5.0F, -1.0F, 24.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(91, 0).cuboid(-12.0F, -11.0F, -1.0F, 24.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(122, 121).cuboid(-12.0F, -10.0F, -1.0F, 1.0F, 5.0F, 3.0F, new Dilation(0.0F))
            .uv(117, 116).cuboid(11.0F, -10.0F, -1.0F, 1.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(16.0F, 35.9F, 0.0F));

        ModelPartData door_left_rods = door_left.addChild("door_left_rods", ModelPartBuilder.create().uv(109, 88).cuboid(-8.0F, -60.0F, -1.0F, 1.0F, 47.0F, 3.0F, new Dilation(0.0F))
            .uv(104, 41).cuboid(7.0F, -60.0F, -1.0F, 1.0F, 47.0F, 3.0F, new Dilation(0.0F))
            .uv(112, 69).cuboid(-11.0F, -19.0F, -1.0F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(112, 65).cuboid(-11.0F, -25.0F, -1.0F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(112, 61).cuboid(-11.0F, -31.0F, -1.0F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(112, 57).cuboid(-11.0F, -37.0F, -1.0F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(112, 53).cuboid(-11.0F, -43.0F, -1.0F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(112, 49).cuboid(-11.0F, -49.0F, -1.0F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(112, 45).cuboid(-11.0F, -55.0F, -1.0F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(16.0F, 35.9F, 0.0F));

        ModelPartData door_left_corners = door_left.addChild("door_left_corners", ModelPartBuilder.create().uv(92, 102).cuboid(-12.0F, -61.0F, -1.0F, 1.0F, 49.0F, 3.0F, new Dilation(0.0F))
            .uv(101, 99).cuboid(11.0F, -61.0F, -1.0F, 1.0F, 49.0F, 3.0F, new Dilation(0.0F))
            .uv(112, 41).cuboid(-11.0F, -13.0F, -1.0F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(98, 4).cuboid(-11.0F, -61.0F, -1.0F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(16.0F, 35.9F, 0.0F));

        ModelPartData door_left_borders = door_left.addChild("door_left_borders", ModelPartBuilder.create().uv(20, 68).cuboid(1.0F, -47.0F, 16.5F, 1.0F, 57.0F, 3.0F, new Dilation(0.0F))
            .uv(28, 68).cuboid(28.0F, -47.0F, 16.5F, 1.0F, 57.0F, 3.0F, new Dilation(0.0F))
            .uv(89, 9).cuboid(2.0F, -48.0F, 16.5F, 26.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(126, 103).cuboid(0.0F, 10.0F, 16.5F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(124, 113).cuboid(28.0F, 10.0F, 16.5F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(114, 85).cuboid(0.0F, -49.0F, 16.5F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(63, 19).cuboid(28.0F, -49.0F, 16.5F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, 20.9F, -17.5F));

        ModelPartData door_right = modelPartData.addChild("door_right", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -1.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        return TexturedModelData.of(modelData, 256, 256);
    }
}

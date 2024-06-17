package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorcapsule.models;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class TardisExteriorCapsuleModel extends BaseTardisExteriorModel {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(DWM.getIdentifier("textures/block/tardis/exteriors/tardis_exterior_capsule.png"), "main");

    public TardisExteriorCapsuleModel(ModelPart root) {
        super(root);
    }

    @SuppressWarnings("unused")
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData platform = base.addChild("platform", ModelPartBuilder.create().uv(94, 88).cuboid(-17.0F, -2.0F, -17.0F, 34.0F, 2.0F, 34.0F, new Dilation(0.0F))
            .uv(240, 69).cuboid(-19.0F, -2.0F, -17.0F, 2.0F, 2.0F, 36.0F, new Dilation(0.0F))
            .uv(23, 4).cuboid(-17.0F, -2.0F, 19.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(23, 0).cuboid(-14.0F, -2.0F, 21.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(0, 21).cuboid(-11.0F, -2.0F, 23.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(280, 0).cuboid(-21.0F, -2.0F, -14.0F, 2.0F, 2.0F, 31.0F, new Dilation(0.0F))
            .uv(320, 57).cuboid(-23.0F, -2.0F, -11.0F, 2.0F, 2.0F, 25.0F, new Dilation(0.0F))
            .uv(0, 21).cuboid(-25.0F, -2.0F, -8.0F, 2.0F, 2.0F, 19.0F, new Dilation(0.0F))
            .uv(7, 7).cuboid(23.0F, -2.0F, 8.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(0, 10).cuboid(21.0F, -2.0F, 11.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(7, 12).cuboid(19.0F, -2.0F, 14.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(192, 35).cuboid(-8.0F, -2.0F, 23.0F, 19.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(256, 274).cuboid(-11.0F, -2.0F, 21.0F, 25.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(248, 143).cuboid(-14.0F, -2.0F, 19.0F, 31.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(206, 193).cuboid(-17.0F, -2.0F, 17.0F, 36.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(105, 278).cuboid(19.0F, -2.0F, -17.0F, 2.0F, 2.0F, 31.0F, new Dilation(0.0F))
            .uv(213, 0).cuboid(21.0F, -2.0F, -14.0F, 2.0F, 2.0F, 25.0F, new Dilation(0.0F))
            .uv(0, 0).cuboid(23.0F, -2.0F, -11.0F, 2.0F, 2.0F, 19.0F, new Dilation(0.0F))
            .uv(0, 0).cuboid(-25.0F, -2.0F, -11.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(0, 5).cuboid(-23.0F, -2.0F, -14.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(7, 2).cuboid(-21.0F, -2.0F, -17.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(206, 189).cuboid(-19.0F, -2.0F, -19.0F, 36.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(240, 0).cuboid(17.0F, -2.0F, -19.0F, 2.0F, 2.0F, 36.0F, new Dilation(0.0F))
            .uv(152, 39).cuboid(-19.0F, -3.0F, -17.0F, 38.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(206, 201).cuboid(-17.0F, -3.0F, -19.0F, 34.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(98, 67).cuboid(-19.0F, -82.0F, -17.0F, 38.0F, 16.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData roof = base.addChild("roof", ModelPartBuilder.create().uv(0, 0).cuboid(-17.0F, -84.0F, -21.0F, 34.0F, 2.0F, 42.0F, new Dilation(0.0F))
            .uv(256, 264).cuboid(-14.0F, -84.0F, 21.0F, 28.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(196, 113).cuboid(-11.0F, -84.0F, 23.0F, 22.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(290, 215).cuboid(-9.0F, -84.0F, 25.0F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(288, 177).cuboid(-9.0F, -84.0F, -27.0F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(256, 260).cuboid(-14.0F, -84.0F, -23.0F, 28.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(196, 109).cuboid(-11.0F, -84.0F, -25.0F, 22.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(206, 149).cuboid(-19.0F, -84.0F, -19.0F, 2.0F, 2.0F, 38.0F, new Dilation(0.0F))
            .uv(256, 221).cuboid(-21.0F, -84.0F, -17.0F, 2.0F, 2.0F, 34.0F, new Dilation(0.0F))
            .uv(315, 0).cuboid(-23.0F, -84.0F, -14.0F, 2.0F, 2.0F, 28.0F, new Dilation(0.0F))
            .uv(327, 281).cuboid(-25.0F, -84.0F, -11.0F, 2.0F, 2.0F, 22.0F, new Dilation(0.0F))
            .uv(327, 305).cuboid(-27.0F, -84.0F, -9.0F, 2.0F, 2.0F, 18.0F, new Dilation(0.0F))
            .uv(206, 109).cuboid(17.0F, -84.0F, -19.0F, 2.0F, 2.0F, 38.0F, new Dilation(0.0F))
            .uv(164, 256).cuboid(19.0F, -84.0F, -17.0F, 2.0F, 2.0F, 34.0F, new Dilation(0.0F))
            .uv(300, 251).cuboid(21.0F, -84.0F, -14.0F, 2.0F, 2.0F, 28.0F, new Dilation(0.0F))
            .uv(326, 215).cuboid(23.0F, -84.0F, -11.0F, 2.0F, 2.0F, 22.0F, new Dilation(0.0F))
            .uv(0, 313).cuboid(25.0F, -84.0F, -9.0F, 2.0F, 2.0F, 18.0F, new Dilation(0.0F))
            .uv(162, 68).cuboid(23.0F, -86.0F, -9.0F, 2.0F, 2.0F, 18.0F, new Dilation(0.0F))
            .uv(320, 134).cuboid(21.0F, -86.0F, -11.0F, 2.0F, 2.0F, 22.0F, new Dilation(0.0F))
            .uv(290, 185).cuboid(19.0F, -86.0F, -14.0F, 2.0F, 2.0F, 28.0F, new Dilation(0.0F))
            .uv(252, 185).cuboid(17.0F, -86.0F, -17.0F, 2.0F, 2.0F, 34.0F, new Dilation(0.0F))
            .uv(196, 29).cuboid(14.0F, -86.0F, -19.0F, 3.0F, 2.0F, 38.0F, new Dilation(0.0F))
            .uv(240, 38).cuboid(-25.0F, -86.0F, -9.0F, 2.0F, 2.0F, 18.0F, new Dilation(0.0F))
            .uv(322, 173).cuboid(-23.0F, -86.0F, -11.0F, 2.0F, 2.0F, 22.0F, new Dilation(0.0F))
            .uv(294, 221).cuboid(-21.0F, -86.0F, -14.0F, 2.0F, 2.0F, 28.0F, new Dilation(0.0F))
            .uv(218, 252).cuboid(-19.0F, -86.0F, -17.0F, 2.0F, 2.0F, 34.0F, new Dilation(0.0F))
            .uv(196, 69).cuboid(-17.0F, -86.0F, -19.0F, 3.0F, 2.0F, 38.0F, new Dilation(0.0F))
            .uv(288, 137).cuboid(-9.0F, -86.0F, -25.0F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(184, 77).cuboid(-11.0F, -86.0F, -23.0F, 22.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(288, 173).cuboid(-9.0F, -86.0F, 23.0F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(184, 81).cuboid(-11.0F, -86.0F, 21.0F, 22.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(0, 44).cuboid(-14.0F, -86.0F, -21.0F, 28.0F, 2.0F, 42.0F, new Dilation(0.0F))
            .uv(0, 88).cuboid(-14.0F, -88.0F, -19.0F, 28.0F, 2.0F, 38.0F, new Dilation(0.0F))
            .uv(184, 69).cuboid(-11.0F, -88.0F, 19.0F, 22.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(240, 58).cuboid(-9.0F, -88.0F, 21.0F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(184, 73).cuboid(-11.0F, -88.0F, -21.0F, 22.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(240, 62).cuboid(-9.0F, -88.0F, -23.0F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(288, 104).cuboid(-19.0F, -88.0F, -14.0F, 2.0F, 2.0F, 28.0F, new Dilation(0.0F))
            .uv(320, 84).cuboid(-21.0F, -88.0F, -11.0F, 2.0F, 2.0F, 22.0F, new Dilation(0.0F))
            .uv(0, 44).cuboid(-23.0F, -88.0F, -9.0F, 2.0F, 2.0F, 18.0F, new Dilation(0.0F))
            .uv(288, 143).cuboid(17.0F, -88.0F, -14.0F, 2.0F, 2.0F, 28.0F, new Dilation(0.0F))
            .uv(320, 108).cuboid(19.0F, -88.0F, -11.0F, 2.0F, 2.0F, 22.0F, new Dilation(0.0F))
            .uv(0, 64).cuboid(21.0F, -88.0F, -9.0F, 2.0F, 2.0F, 18.0F, new Dilation(0.0F))
            .uv(248, 107).cuboid(-17.0F, -88.0F, -17.0F, 3.0F, 2.0F, 34.0F, new Dilation(0.0F))
            .uv(248, 149).cuboid(14.0F, -88.0F, -17.0F, 3.0F, 2.0F, 34.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData walls = base.addChild("walls", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_back = walls.addChild("wall_back", ModelPartBuilder.create().uv(204, 204).cuboid(-11.0F, -82.0F, 23.0F, 22.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(96, 225).cuboid(-9.0F, -82.0F, 25.0F, 18.0F, 82.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_back_corners = wall_back.addChild("wall_back_corners", ModelPartBuilder.create().uv(221, 292).cuboid(-14.0F, -82.0F, 21.0F, 3.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(211, 292).cuboid(-17.0F, -82.0F, 19.0F, 3.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(146, 311).cuboid(-19.0F, -82.0F, 17.0F, 2.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(138, 311).cuboid(17.0F, -82.0F, 17.0F, 2.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(191, 292).cuboid(11.0F, -82.0F, 21.0F, 3.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(201, 292).cuboid(14.0F, -82.0F, 19.0F, 3.0F, 80.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_left = walls.addChild("wall_left", ModelPartBuilder.create().uv(158, 124).cuboid(-11.0F, -82.0F, 23.0F, 22.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(158, 206).cuboid(-9.0F, -82.0F, 25.0F, 18.0F, 82.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData wall_left_corners = wall_left.addChild("wall_left_corners", ModelPartBuilder.create().uv(181, 292).cuboid(-14.0F, -82.0F, 21.0F, 3.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(171, 292).cuboid(-17.0F, -82.0F, 19.0F, 3.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(130, 311).cuboid(-19.0F, -82.0F, 17.0F, 2.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(80, 291).cuboid(11.0F, -82.0F, 21.0F, 3.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(70, 291).cuboid(14.0F, -82.0F, 19.0F, 3.0F, 80.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_right = walls.addChild("wall_right", ModelPartBuilder.create().uv(48, 128).cuboid(-11.0F, -82.0F, -25.0F, 22.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(0, 229).cuboid(-9.0F, -82.0F, -27.0F, 18.0F, 82.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData wall_right_corners = wall_right.addChild("wall_right_corners", ModelPartBuilder.create().uv(60, 291).cuboid(-14.0F, -82.0F, -23.0F, 3.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(50, 291).cuboid(-17.0F, -82.0F, -21.0F, 3.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(150, 225).cuboid(-19.0F, -82.0F, -19.0F, 2.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(276, 288).cuboid(11.0F, -82.0F, -23.0F, 3.0F, 80.0F, 2.0F, new Dilation(0.0F))
            .uv(266, 288).cuboid(14.0F, -82.0F, -21.0F, 3.0F, 80.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_front = walls.addChild("wall_front", ModelPartBuilder.create().uv(320, 33).cuboid(-11.0F, -82.0F, -25.0F, 22.0F, 16.0F, 8.0F, new Dilation(0.0F))
            .uv(327, 325).cuboid(-9.0F, -82.0F, -27.0F, 18.0F, 16.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_front_corners = wall_front.addChild("wall_front_corners", ModelPartBuilder.create().uv(18, 88).cuboid(-14.0F, -82.0F, -23.0F, 3.0F, 16.0F, 6.0F, new Dilation(0.0F))
            .uv(98, 44).cuboid(-17.0F, -82.0F, -21.0F, 3.0F, 16.0F, 4.0F, new Dilation(0.0F))
            .uv(0, 88).cuboid(11.0F, -82.0F, -23.0F, 3.0F, 16.0F, 6.0F, new Dilation(0.0F))
            .uv(94, 94).cuboid(14.0F, -82.0F, -21.0F, 3.0F, 16.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData door_left = modelPartData.addChild("door_left", ModelPartBuilder.create().uv(287, 285).cuboid(0.0F, -21.3333F, -0.1667F, 17.0F, 63.0F, 3.0F, new Dilation(0.0F))
            .uv(231, 292).cuboid(16.0F, -21.3333F, -1.1667F, 1.0F, 63.0F, 1.0F, new Dilation(0.0F))
            .uv(94, 91).cuboid(0.0F, -21.3333F, -1.1667F, 16.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-17.0F, -20.6667F, -16.8333F));

        ModelPartData door_right = modelPartData.addChild("door_right", ModelPartBuilder.create().uv(280, 38).cuboid(-17.0F, -21.3333F, -0.1667F, 17.0F, 63.0F, 3.0F, new Dilation(0.0F))
            .uv(106, 128).cuboid(-17.0F, -21.3333F, -1.1667F, 1.0F, 63.0F, 1.0F, new Dilation(0.0F))
            .uv(94, 88).cuboid(-16.0F, -21.3333F, -1.1667F, 16.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(17.0F, -20.6667F, -16.8333F));

        ModelPartData lamp = modelPartData.addChild("lamp", ModelPartBuilder.create().uv(288, 134).cuboid(-10.0F, -67.5F, -20.0F, 20.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(256, 278).cuboid(-10.0F, -67.5F, -22.0F, 20.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(110, 39).cuboid(-10.0F, -67.5F, -24.0F, 20.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData boti = modelPartData.addChild("boti", ModelPartBuilder.create().uv(48, 210).cuboid(-11.0F, -81.0F, 21.0F, 22.0F, 79.0F, 2.0F, new Dilation(0.0F))
            .uv(120, 311).cuboid(-14.0F, -81.0F, 19.0F, 3.0F, 79.0F, 2.0F, new Dilation(0.0F))
            .uv(90, 309).cuboid(-17.0F, -81.0F, 17.0F, 3.0F, 79.0F, 2.0F, new Dilation(0.0F))
            .uv(256, 288).cuboid(-19.0F, -81.0F, 14.0F, 2.0F, 79.0F, 3.0F, new Dilation(0.0F))
            .uv(246, 288).cuboid(-21.0F, -81.0F, 11.0F, 2.0F, 79.0F, 3.0F, new Dilation(0.0F))
            .uv(140, 225).cuboid(-21.0F, -81.0F, -14.0F, 2.0F, 79.0F, 3.0F, new Dilation(0.0F))
            .uv(110, 311).cuboid(11.0F, -81.0F, 19.0F, 3.0F, 79.0F, 2.0F, new Dilation(0.0F))
            .uv(100, 311).cuboid(14.0F, -81.0F, 17.0F, 3.0F, 79.0F, 2.0F, new Dilation(0.0F))
            .uv(236, 288).cuboid(17.0F, -81.0F, 14.0F, 2.0F, 79.0F, 3.0F, new Dilation(0.0F))
            .uv(40, 288).cuboid(19.0F, -81.0F, 11.0F, 2.0F, 79.0F, 3.0F, new Dilation(0.0F))
            .uv(96, 128).cuboid(19.0F, -81.0F, -14.0F, 2.0F, 79.0F, 3.0F, new Dilation(0.0F))
            .uv(0, 128).cuboid(-23.0F, -81.0F, -11.0F, 2.0F, 79.0F, 22.0F, new Dilation(0.0F))
            .uv(110, 124).cuboid(21.0F, -81.0F, -11.0F, 2.0F, 79.0F, 22.0F, new Dilation(0.0F))
            .uv(98, 44).cuboid(-23.0F, -82.0F, -11.0F, 46.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(110, 31).cuboid(-21.0F, -82.0F, 11.0F, 42.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(110, 27).cuboid(-21.0F, -82.0F, -14.0F, 42.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(256, 281).cuboid(-19.0F, -82.0F, 14.0F, 38.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(256, 257).cuboid(-17.0F, -82.0F, 17.0F, 34.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(256, 271).cuboid(-14.0F, -82.0F, 19.0F, 28.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(196, 117).cuboid(-11.0F, -82.0F, 21.0F, 22.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(110, 0).cuboid(-21.0F, -3.0F, -11.0F, 42.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(110, 23).cuboid(-21.0F, -3.0F, 11.0F, 42.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(110, 35).cuboid(-19.0F, -3.0F, -14.0F, 38.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(206, 197).cuboid(-17.0F, -3.0F, 14.0F, 34.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(256, 268).cuboid(-14.0F, -3.0F, 17.0F, 28.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(280, 33).cuboid(-11.0F, -3.0F, 19.0F, 22.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        return TexturedModelData.of(modelData, 512, 512);
    }
}

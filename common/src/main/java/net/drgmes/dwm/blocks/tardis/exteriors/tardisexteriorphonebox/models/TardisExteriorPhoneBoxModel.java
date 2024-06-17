package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorphonebox.models;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class TardisExteriorPhoneBoxModel extends BaseTardisExteriorModel {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(DWM.getIdentifier("textures/block/tardis/exteriors/tardis_exterior_phone_box.png"), "main");

    public TardisExteriorPhoneBoxModel(ModelPart root) {
        super(root);
    }

    @SuppressWarnings("unused")
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 52).cuboid(-21.0F, -2.0F, -21.0F, 42.0F, 2.0F, 42.0F, new Dilation(0.0F))
            .uv(322, 74).cuboid(-15.0F, -3.0F, -20.0F, 30.0F, 1.0F, 5.0F, new Dilation(0.0F))
            .uv(322, 68).cuboid(-15.0F, -3.0F, 15.0F, 30.0F, 1.0F, 5.0F, new Dilation(0.0F))
            .uv(282, 58).cuboid(-20.0F, -3.0F, -15.0F, 5.0F, 1.0F, 30.0F, new Dilation(0.0F))
            .uv(256, 280).cuboid(15.0F, -3.0F, -15.0F, 5.0F, 1.0F, 30.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData corners = base.addChild("corners", ModelPartBuilder.create().uv(140, 314).cuboid(-20.0F, -64.0F, -20.0F, 5.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(120, 314).cuboid(-20.0F, -64.0F, 15.0F, 5.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(268, 311).cuboid(15.0F, -64.0F, 15.0F, 5.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(248, 311).cuboid(15.0F, -64.0F, -20.0F, 5.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(242, 92).cuboid(-21.0F, -69.0F, 15.0F, 42.0F, 5.0F, 5.0F, new Dilation(0.0F))
            .uv(168, 41).cuboid(-21.0F, -69.0F, -20.0F, 42.0F, 5.0F, 5.0F, new Dilation(0.0F))
            .uv(239, 105).cuboid(-20.0F, -69.0F, -21.0F, 5.0F, 5.0F, 42.0F, new Dilation(0.0F))
            .uv(0, 210).cuboid(15.0F, -69.0F, -21.0F, 5.0F, 5.0F, 42.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData strips = corners.addChild("strips", ModelPartBuilder.create().uv(40, 367).cuboid(18.5F, -63.0F, 19.25F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(32, 367).cuboid(17.0F, -63.0F, 19.25F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(36, 367).cuboid(15.5F, -63.0F, 19.25F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(20, 367).cuboid(-16.5F, -63.0F, 19.25F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(24, 367).cuboid(-18.0F, -63.0F, 19.25F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(28, 367).cuboid(-19.5F, -63.0F, 19.25F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(16, 367).cuboid(-20.25F, -63.0F, 18.5F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(12, 367).cuboid(-20.25F, -63.0F, 17.0F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(8, 367).cuboid(-20.25F, -63.0F, 15.5F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(366, 365).cuboid(-20.25F, -63.0F, -19.5F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 367).cuboid(-20.25F, -63.0F, -16.5F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(4, 367).cuboid(-20.25F, -63.0F, -18.0F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(342, 365).cuboid(19.25F, -63.0F, 18.5F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(346, 365).cuboid(19.25F, -63.0F, 17.0F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(350, 365).cuboid(19.25F, -63.0F, 15.5F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(354, 365).cuboid(19.25F, -63.0F, -19.5F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(358, 365).cuboid(19.25F, -63.0F, -16.5F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(362, 365).cuboid(19.25F, -63.0F, -18.0F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(338, 348).cuboid(-19.5F, -63.0F, -20.25F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(90, 358).cuboid(18.5F, -63.0F, -20.25F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(94, 358).cuboid(17.0F, -63.0F, -20.25F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(98, 358).cuboid(15.5F, -63.0F, -20.25F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(102, 358).cuboid(-16.5F, -63.0F, -20.25F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F))
            .uv(106, 358).cuboid(-18.0F, -63.0F, -20.25F, 1.0F, 60.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData rondels = base.addChild("rondels", ModelPartBuilder.create().uv(126, 22).cuboid(-19.0F, -68.0F, -22.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(126, 18).cuboid(16.0F, -68.0F, -22.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(126, 10).cuboid(-19.0F, -68.0F, 21.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(126, 14).cuboid(16.0F, -68.0F, 21.0F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(16, 125).cuboid(-22.0F, -68.0F, 16.0F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F))
            .uv(8, 125).cuboid(-22.0F, -68.0F, -19.0F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F))
            .uv(123, 124).cuboid(21.0F, -68.0F, 16.0F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F))
            .uv(0, 125).cuboid(21.0F, -68.0F, -19.0F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData walls = base.addChild("walls", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_back = walls.addChild("wall_back", ModelPartBuilder.create().uv(222, 246).cuboid(-15.0F, -64.0F, 16.0F, 30.0F, 62.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_back_decor = wall_back.addChild("wall_back_decor", ModelPartBuilder.create().uv(352, 10).cuboid(-12.0F, -5.0F, 15.5F, 24.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(96, 177).cuboid(-12.0F, -11.0F, 15.5F, 24.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(30, 0).cuboid(-12.0F, -10.0F, 15.5F, 1.0F, 5.0F, 3.0F, new Dilation(0.0F))
            .uv(30, 8).cuboid(11.0F, -10.0F, 15.5F, 1.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_back_rods = wall_back.addChild("wall_back_rods", ModelPartBuilder.create().uv(312, 370).cuboid(-8.0F, -60.0F, 15.5F, 1.0F, 47.0F, 3.0F, new Dilation(0.0F))
            .uv(304, 370).cuboid(7.0F, -60.0F, 15.5F, 1.0F, 47.0F, 3.0F, new Dilation(0.0F))
            .uv(352, 63).cuboid(-11.0F, -19.0F, 15.5F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(352, 59).cuboid(-11.0F, -55.0F, 15.5F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_back_corners = wall_back.addChild("wall_back_corners", ModelPartBuilder.create().uv(228, 368).cuboid(-12.0F, -61.0F, 15.5F, 1.0F, 49.0F, 3.0F, new Dilation(0.0F))
            .uv(220, 368).cuboid(11.0F, -61.0F, 15.5F, 1.0F, 49.0F, 3.0F, new Dilation(0.0F))
            .uv(353, 92).cuboid(-11.0F, -13.0F, 15.5F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(353, 88).cuboid(-11.0F, -61.0F, 15.5F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_back_borders = wall_back.addChild("wall_back_borders", ModelPartBuilder.create().uv(110, 322).cuboid(1.0F, -47.0F, -2.0F, 1.0F, 57.0F, 3.0F, new Dilation(0.0F))
            .uv(64, 257).cuboid(28.0F, -47.0F, -2.0F, 1.0F, 57.0F, 3.0F, new Dilation(0.0F))
            .uv(344, 265).cuboid(2.0F, -48.0F, -2.0F, 26.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(7, 116).cuboid(28.0F, 10.0F, -2.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(0, 113).cuboid(0.0F, 10.0F, -2.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(31, 112).cuboid(28.0F, -49.0F, -2.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(21, 112).cuboid(0.0F, -49.0F, -2.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-15.0F, -15.0F, 17.5F));

        ModelPartData wall_left = walls.addChild("wall_left", ModelPartBuilder.create().uv(184, 280).cuboid(-18.0F, -12.0F, -15.0F, 2.0F, 9.0F, 30.0F, new Dilation(0.0F))
            .uv(291, 102).cuboid(-18.0F, -64.0F, -15.0F, 2.0F, 3.0F, 30.0F, new Dilation(0.0F))
            .uv(190, 368).cuboid(-18.0F, -61.0F, 12.0F, 2.0F, 49.0F, 3.0F, new Dilation(0.0F))
            .uv(180, 367).cuboid(-18.0F, -61.0F, -15.0F, 2.0F, 49.0F, 3.0F, new Dilation(0.0F))
            .uv(264, 19).cuboid(-18.0F, -60.0F, -11.0F, 2.0F, 47.0F, 22.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_left_decor = wall_left.addChild("wall_left_decor", ModelPartBuilder.create().uv(322, 43).cuboid(-18.5F, -5.0F, -12.0F, 3.0F, 1.0F, 24.0F, new Dilation(0.0F))
            .uv(190, 320).cuboid(-18.5F, -11.0F, -12.0F, 3.0F, 1.0F, 24.0F, new Dilation(0.0F))
            .uv(123, 118).cuboid(-18.5F, -10.0F, 11.0F, 3.0F, 5.0F, 1.0F, new Dilation(0.0F))
            .uv(123, 112).cuboid(-18.5F, -10.0F, -12.0F, 3.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_left_rods = wall_left.addChild("wall_left_rods", ModelPartBuilder.create().uv(378, 365).cuboid(-18.5F, -60.0F, 7.0F, 3.0F, 47.0F, 1.0F, new Dilation(0.0F))
            .uv(278, 378).cuboid(-18.5F, -60.0F, -8.0F, 3.0F, 47.0F, 1.0F, new Dilation(0.0F))
            .uv(288, 347).cuboid(-18.5F, -19.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(188, 345).cuboid(-18.5F, -25.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(344, 342).cuboid(-18.5F, -31.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(344, 319).cuboid(-18.5F, -37.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(344, 242).cuboid(-18.5F, -43.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(344, 214).cuboid(-18.5F, -49.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(344, 168).cuboid(-18.5F, -55.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_left_corners = wall_left.addChild("wall_left_corners", ModelPartBuilder.create().uv(262, 378).cuboid(-18.5F, -61.0F, 11.0F, 3.0F, 49.0F, 1.0F, new Dilation(0.0F))
            .uv(254, 378).cuboid(-18.5F, -61.0F, -12.0F, 3.0F, 49.0F, 1.0F, new Dilation(0.0F))
            .uv(160, 344).cuboid(-18.5F, -13.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(0, 344).cuboid(-18.5F, -61.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_left_borders = wall_left.addChild("wall_left_borders", ModelPartBuilder.create().uv(82, 358).cuboid(-2.0F, -47.0F, 1.0F, 3.0F, 57.0F, 1.0F, new Dilation(0.0F))
            .uv(74, 358).cuboid(-2.0F, -47.0F, 28.0F, 3.0F, 57.0F, 1.0F, new Dilation(0.0F))
            .uv(312, 215).cuboid(-2.0F, -48.0F, 2.0F, 3.0F, 1.0F, 26.0F, new Dilation(0.0F))
            .uv(123, 102).cuboid(-2.0F, 10.0F, 28.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(123, 98).cuboid(-2.0F, 10.0F, 0.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(30, 121).cuboid(-2.0F, -49.0F, 28.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(20, 121).cuboid(-2.0F, -49.0F, 0.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-16.5F, -15.0F, -15.0F));

        ModelPartData wall_right = walls.addChild("wall_right", ModelPartBuilder.create().uv(120, 275).cuboid(16.0F, -12.0F, -15.0F, 2.0F, 9.0F, 30.0F, new Dilation(0.0F))
            .uv(290, 0).cuboid(16.0F, -64.0F, -15.0F, 2.0F, 3.0F, 30.0F, new Dilation(0.0F))
            .uv(170, 367).cuboid(16.0F, -61.0F, 12.0F, 2.0F, 49.0F, 3.0F, new Dilation(0.0F))
            .uv(160, 367).cuboid(16.0F, -61.0F, -15.0F, 2.0F, 49.0F, 3.0F, new Dilation(0.0F))
            .uv(72, 253).cuboid(16.0F, -60.0F, -11.0F, 2.0F, 47.0F, 22.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_right_decor = wall_right.addChild("wall_right_decor", ModelPartBuilder.create().uv(160, 319).cuboid(15.5F, -5.0F, -12.0F, 3.0F, 1.0F, 24.0F, new Dilation(0.0F))
            .uv(0, 319).cuboid(15.5F, -11.0F, -12.0F, 3.0F, 1.0F, 24.0F, new Dilation(0.0F))
            .uv(123, 106).cuboid(15.5F, -10.0F, 11.0F, 3.0F, 5.0F, 1.0F, new Dilation(0.0F))
            .uv(34, 60).cuboid(15.5F, -10.0F, -12.0F, 3.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_right_rods = wall_right.addChild("wall_right_rods", ModelPartBuilder.create().uv(378, 273).cuboid(15.5F, -60.0F, 7.0F, 3.0F, 47.0F, 1.0F, new Dilation(0.0F))
            .uv(270, 378).cuboid(15.5F, -60.0F, -8.0F, 3.0F, 47.0F, 1.0F, new Dilation(0.0F))
            .uv(60, 335).cuboid(15.5F, -19.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(32, 334).cuboid(15.5F, -25.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(330, 145).cuboid(15.5F, -31.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(328, 296).cuboid(15.5F, -37.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(328, 273).cuboid(15.5F, -43.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(316, 325).cuboid(15.5F, -49.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(325, 89).cuboid(15.5F, -55.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_right_corners = wall_right.addChild("wall_right_corners", ModelPartBuilder.create().uv(246, 378).cuboid(15.5F, -61.0F, 11.0F, 3.0F, 49.0F, 1.0F, new Dilation(0.0F))
            .uv(370, 365).cuboid(15.5F, -61.0F, -12.0F, 3.0F, 49.0F, 1.0F, new Dilation(0.0F))
            .uv(288, 324).cuboid(15.5F, -13.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F))
            .uv(324, 0).cuboid(15.5F, -61.0F, -11.0F, 3.0F, 1.0F, 22.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_right_borders = wall_right.addChild("wall_right_borders", ModelPartBuilder.create().uv(66, 358).cuboid(-1.0F, -47.0F, 1.0F, 3.0F, 57.0F, 1.0F, new Dilation(0.0F))
            .uv(58, 358).cuboid(-1.0F, -47.0F, 28.0F, 3.0F, 57.0F, 1.0F, new Dilation(0.0F))
            .uv(312, 181).cuboid(-1.0F, -48.0F, 2.0F, 3.0F, 1.0F, 26.0F, new Dilation(0.0F))
            .uv(10, 121).cuboid(-1.0F, 10.0F, 0.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(0, 121).cuboid(-1.0F, 10.0F, 28.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(27, 117).cuboid(-1.0F, -49.0F, 0.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(17, 117).cuboid(-1.0F, -49.0F, 28.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(16.5F, -15.0F, -15.0F));

        ModelPartData labels = base.addChild("labels", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -3.9F, 0.0F));

        ModelPartData label_back = labels.addChild("label_back", ModelPartBuilder.create().uv(325, 115).cuboid(-15.0F, -72.1F, 23.5F, 30.0F, 5.0F, 1.0F, new Dilation(0.0F))
            .uv(38, 8).cuboid(-20.0F, -73.1F, 19.75F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(38, 0).cuboid(19.0F, -73.1F, 19.75F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(224, 179).cuboid(-15.0F, -73.1F, 23.75F, 30.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(168, 94).cuboid(-15.0F, -67.1F, 23.75F, 30.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, -4.75F));

        ModelPartData label_right = labels.addChild("label_right", ModelPartBuilder.create().uv(325, 121).cuboid(-15.0F, -72.1F, 23.5F, 30.0F, 5.0F, 1.0F, new Dilation(0.0F))
            .uv(126, 26).cuboid(-20.0F, -73.1F, 19.75F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(36, 102).cuboid(19.0F, -73.1F, 19.75F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(325, 129).cuboid(-15.0F, -73.1F, 23.75F, 30.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(325, 127).cuboid(-15.0F, -67.1F, 23.75F, 30.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-4.75F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData label_left = labels.addChild("label_left", ModelPartBuilder.create().uv(344, 191).cuboid(-15.0F, -72.1F, 23.5F, 30.0F, 5.0F, 1.0F, new Dilation(0.0F))
            .uv(126, 60).cuboid(-20.0F, -73.1F, 19.75F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(126, 52).cuboid(19.0F, -73.1F, 19.75F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(344, 205).cuboid(-15.0F, -73.1F, 23.75F, 30.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(344, 203).cuboid(-15.0F, -67.1F, 23.75F, 30.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(4.75F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData label_front = labels.addChild("label_front", ModelPartBuilder.create().uv(344, 197).cuboid(-15.0F, -72.1F, 23.5F, 30.0F, 5.0F, 1.0F, new Dilation(0.0F))
            .uv(126, 76).cuboid(-20.0F, -73.1F, 19.75F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(126, 68).cuboid(19.0F, -73.1F, 19.75F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(344, 239).cuboid(-15.0F, -73.1F, 23.75F, 30.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(344, 237).cuboid(-15.0F, -67.1F, 23.75F, 30.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 4.75F, 0.0F, 3.1416F, 0.0F));

        ModelPartData roof = base.addChild("roof", ModelPartBuilder.create().uv(125, 99).cuboid(-19.5F, -77.75F, -19.5F, 39.0F, 9.0F, 39.0F, new Dilation(0.0F))
            .uv(0, 0).cuboid(-21.0F, -87.75F, -21.0F, 42.0F, 10.0F, 42.0F, new Dilation(0.0F))
            .uv(0, 96).cuboid(-20.5F, -88.75F, -20.5F, 41.0F, 1.0F, 41.0F, new Dilation(0.0F))
            .uv(126, 0).cuboid(-20.0F, -89.75F, -20.0F, 40.0F, 1.0F, 40.0F, new Dilation(0.0F))
            .uv(126, 52).cuboid(-19.5F, -90.75F, -19.5F, 39.0F, 1.0F, 39.0F, new Dilation(0.0F))
            .uv(0, 138).cuboid(-19.0F, -91.75F, -19.0F, 38.0F, 1.0F, 38.0F, new Dilation(0.0F))
            .uv(116, 147).cuboid(-18.0F, -92.75F, -18.0F, 36.0F, 1.0F, 36.0F, new Dilation(0.0F))
            .uv(0, 177).cuboid(-16.0F, -93.75F, -16.0F, 32.0F, 1.0F, 32.0F, new Dilation(0.0F))
            .uv(224, 152).cuboid(-13.0F, -94.75F, -13.0F, 26.0F, 1.0F, 26.0F, new Dilation(0.0F))
            .uv(246, 0).cuboid(-9.0F, -95.75F, -9.0F, 18.0F, 1.0F, 18.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData roof_panels = roof.addChild("roof_panels", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData roof_panel_back = roof_panels.addChild("roof_panel_back", ModelPartBuilder.create().uv(312, 38).cuboid(-20.0F, 2.64F, 20.75F, 40.0F, 4.0F, 1.0F, new Dilation(0.0F))
            .uv(312, 211).cuboid(-18.0F, 0.64F, 20.75F, 36.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(325, 112).cuboid(-16.0F, -1.36F, 20.75F, 32.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(352, 55).cuboid(-12.0F, -3.36F, 19.75F, 24.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(0, 15).cuboid(-6.0F, -5.36F, 18.75F, 12.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(243, 89).cuboid(-20.0F, 5.64F, 21.75F, 40.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 66).cuboid(-6.0F, -5.36F, 21.75F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(26, 60).cuboid(19.0F, 2.64F, 21.75F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 56).cuboid(17.0F, 2.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(13, 125).cuboid(17.0F, 0.64F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(36, 54).cuboid(15.0F, 0.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(5, 125).cuboid(15.0F, -1.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(126, 8).cuboid(11.0F, -1.36F, 21.75F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 118).cuboid(11.0F, -3.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 111).cuboid(5.0F, -3.36F, 21.75F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(35, 99).cuboid(5.0F, -4.36F, 21.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(18, 52).cuboid(-20.0F, 2.64F, 21.75F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(36, 39).cuboid(-19.0F, 2.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(7, 113).cuboid(-18.0F, 0.64F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(36, 52).cuboid(-17.0F, 0.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(14, 116).cuboid(-16.0F, -1.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(126, 6).cuboid(-15.0F, -1.36F, 21.75F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(37, 117).cuboid(-12.0F, -3.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(27, 110).cuboid(-11.0F, -3.36F, 21.75F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 99).cuboid(-6.0F, -4.36F, 21.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -85.64F, 0.0F));

        ModelPartData roof_panel_left = roof_panels.addChild("roof_panel_left", ModelPartBuilder.create().uv(291, 140).cuboid(-20.0F, 2.64F, 20.75F, 40.0F, 4.0F, 1.0F, new Dilation(0.0F))
            .uv(312, 208).cuboid(-18.0F, 0.64F, 20.75F, 36.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(324, 26).cuboid(-16.0F, -1.36F, 20.75F, 32.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(352, 51).cuboid(-12.0F, -3.36F, 19.75F, 24.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(0, 10).cuboid(-6.0F, -5.36F, 18.75F, 12.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(224, 181).cuboid(-20.0F, 5.64F, 21.75F, 40.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 64).cuboid(-6.0F, -5.36F, 21.75F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 52).cuboid(19.0F, 2.64F, 21.75F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 38).cuboid(17.0F, 2.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(28, 112).cuboid(17.0F, 0.64F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(36, 32).cuboid(15.0F, 0.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(18, 111).cuboid(15.0F, -1.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(126, 4).cuboid(11.0F, -1.36F, 21.75F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(35, 96).cuboid(11.0F, -3.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(14, 109).cuboid(5.0F, -3.36F, 21.75F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 89).cuboid(5.0F, -4.36F, 21.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(36, 35).cuboid(-20.0F, 2.64F, 21.75F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(36, 30).cuboid(-19.0F, 2.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 96).cuboid(-18.0F, 0.64F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(36, 28).cuboid(-17.0F, 0.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(38, 88).cuboid(-16.0F, -1.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(126, 2).cuboid(-15.0F, -1.36F, 21.75F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(35, 86).cuboid(-12.0F, -3.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 109).cuboid(-11.0F, -3.36F, 21.75F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 83).cuboid(-6.0F, -4.36F, 21.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -85.64F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData roof_panel_right = roof_panels.addChild("roof_panel_right", ModelPartBuilder.create().uv(291, 135).cuboid(-20.0F, 2.64F, 20.75F, 40.0F, 4.0F, 1.0F, new Dilation(0.0F))
            .uv(242, 102).cuboid(-18.0F, 0.64F, 20.75F, 36.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(324, 23).cuboid(-16.0F, -1.36F, 20.75F, 32.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(352, 47).cuboid(-12.0F, -3.36F, 19.75F, 24.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(0, 5).cuboid(-6.0F, -5.36F, 18.75F, 12.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(126, 92).cuboid(-20.0F, 5.64F, 21.75F, 40.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 62).cuboid(-6.0F, -5.36F, 21.75F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(36, 23).cuboid(19.0F, 2.64F, 21.75F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(36, 21).cuboid(17.0F, 2.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 86).cuboid(17.0F, 0.64F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 36).cuboid(15.0F, 0.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(38, 82).cuboid(15.0F, -1.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(126, 0).cuboid(11.0F, -1.36F, 21.75F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(35, 80).cuboid(11.0F, -3.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(28, 92).cuboid(5.0F, -3.36F, 21.75F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(14, 77).cuboid(5.0F, -4.36F, 21.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(18, 35).cuboid(-20.0F, 2.64F, 21.75F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 34).cuboid(-19.0F, 2.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 80).cuboid(-18.0F, 0.64F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 31).cuboid(-17.0F, 0.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(29, 75).cuboid(-16.0F, -1.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(31, 125).cuboid(-15.0F, -1.36F, 21.75F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(14, 74).cuboid(-12.0F, -3.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(14, 92).cuboid(-11.0F, -3.36F, 21.75F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(23, 71).cuboid(-6.0F, -4.36F, 21.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -85.64F, 0.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData roof_panel_front = roof_panels.addChild("roof_panel_front", ModelPartBuilder.create().uv(290, 33).cuboid(-20.0F, 2.64F, 20.75F, 40.0F, 4.0F, 1.0F, new Dilation(0.0F))
            .uv(52, 210).cuboid(-18.0F, 0.64F, 20.75F, 36.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(322, 85).cuboid(-16.0F, -1.36F, 20.75F, 32.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(352, 43).cuboid(-12.0F, -3.36F, 19.75F, 24.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(0, 0).cuboid(-6.0F, -5.36F, 18.75F, 12.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(123, 96).cuboid(-20.0F, 5.64F, 21.75F, 40.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 60).cuboid(-6.0F, -5.36F, 21.75F, 12.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(18, 28).cuboid(19.0F, 2.64F, 21.75F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(27, 0).cuboid(17.0F, 2.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(33, 72).cuboid(17.0F, 0.64F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(18, 24).cuboid(15.0F, 0.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(29, 72).cuboid(15.0F, -1.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(21, 125).cuboid(11.0F, -1.36F, 21.75F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 71).cuboid(11.0F, -3.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 92).cuboid(5.0F, -3.36F, 21.75F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(8, 71).cuboid(5.0F, -4.36F, 21.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 27).cuboid(-20.0F, 2.64F, 21.75F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(18, 22).cuboid(-19.0F, 2.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(8, 68).cuboid(-18.0F, 0.64F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(18, 20).cuboid(-17.0F, 0.64F, 21.75F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 68).cuboid(-16.0F, -1.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(29, 19).cuboid(-15.0F, -1.36F, 21.75F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(36, 56).cuboid(-12.0F, -3.36F, 21.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(27, 16).cuboid(-11.0F, -3.36F, 21.75F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(26, 64).cuboid(-6.0F, -4.36F, 21.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -85.64F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData roof_corners = roof.addChild("roof_corners", ModelPartBuilder.create().uv(20, 96).cuboid(-23.0F, -83.75F, -23.0F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F))
            .uv(0, 96).cuboid(-22.5F, -84.75F, -22.5F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F))
            .uv(18, 53).cuboid(-22.0F, -85.75F, -22.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
            .uv(0, 52).cuboid(-21.5F, -86.75F, -21.5F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
            .uv(0, 86).cuboid(18.0F, -83.75F, -23.0F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F))
            .uv(20, 86).cuboid(17.5F, -84.75F, -22.5F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F))
            .uv(0, 34).cuboid(16.0F, -85.75F, -22.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
            .uv(18, 35).cuboid(15.5F, -86.75F, -21.5F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
            .uv(8, 68).cuboid(-23.0F, -83.75F, 18.0F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F))
            .uv(14, 74).cuboid(-22.5F, -84.75F, 17.5F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F))
            .uv(0, 20).cuboid(-22.0F, -85.75F, 16.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
            .uv(18, 21).cuboid(-21.5F, -86.75F, 15.5F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
            .uv(0, 80).cuboid(18.0F, -83.75F, 18.0F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F))
            .uv(20, 80).cuboid(17.5F, -84.75F, 17.5F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F))
            .uv(0, 27).cuboid(16.0F, -85.75F, 16.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
            .uv(18, 28).cuboid(15.5F, -86.75F, 15.5F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData roof_emblems = roof.addChild("roof_emblems", ModelPartBuilder.create().uv(14, 102).cuboid(-3.0F, -87.25F, -22.0F, 6.0F, 6.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 102).cuboid(-3.0F, -87.25F, 21.0F, 6.0F, 6.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 68).cuboid(-22.0F, -87.25F, -3.0F, 1.0F, 6.0F, 6.0F, new Dilation(0.0F))
            .uv(26, 60).cuboid(21.0F, -87.25F, -3.0F, 1.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData door_left = modelPartData.addChild("door_left", ModelPartBuilder.create().uv(30, 323).cuboid(1.0F, 23.9F, -1.0F, 30.0F, 9.0F, 2.0F, new Dilation(0.0F))
            .uv(322, 80).cuboid(1.0F, -28.1F, -1.0F, 30.0F, 3.0F, 2.0F, new Dilation(0.0F))
            .uv(210, 368).cuboid(1.0F, -25.1F, -1.0F, 3.0F, 49.0F, 2.0F, new Dilation(0.0F))
            .uv(200, 368).cuboid(28.0F, -25.1F, -1.0F, 3.0F, 49.0F, 2.0F, new Dilation(0.0F))
            .uv(296, 246).cuboid(5.0F, -24.1F, -1.0F, 22.0F, 47.0F, 2.0F, new Dilation(0.0F))
            .uv(0, 20).cuboid(28.5F, -3.1F, -2.0F, 2.0F, 5.0F, 1.0F, new Dilation(0.0F))
            .uv(38, 18).cuboid(29.0F, -3.6F, -1.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(29, 9).cuboid(29.0F, 1.4F, -1.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-16.0F, -11.9F, -17.0F));

        ModelPartData door_left_decor = door_left.addChild("door_left_decor", ModelPartBuilder.create().uv(352, 18).cuboid(-12.0F, -5.0F, -18.5F, 24.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(352, 14).cuboid(-12.0F, -11.0F, -18.5F, 24.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(28, 102).cuboid(-12.0F, -10.0F, -18.5F, 1.0F, 5.0F, 3.0F, new Dilation(0.0F))
            .uv(34, 72).cuboid(11.0F, -10.0F, -18.5F, 1.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(16.0F, 35.9F, 17.0F));

        ModelPartData door_left_rods = door_left.addChild("door_left_rods", ModelPartBuilder.create().uv(328, 370).cuboid(-8.0F, -60.0F, -18.5F, 1.0F, 47.0F, 3.0F, new Dilation(0.0F))
            .uv(320, 370).cuboid(7.0F, -60.0F, -18.5F, 1.0F, 47.0F, 3.0F, new Dilation(0.0F))
            .uv(358, 157).cuboid(-11.0F, -19.0F, -18.5F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(358, 153).cuboid(-11.0F, -25.0F, -18.5F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(358, 149).cuboid(-11.0F, -31.0F, -18.5F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(358, 145).cuboid(-11.0F, -37.0F, -18.5F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(355, 131).cuboid(-11.0F, -43.0F, -18.5F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(354, 29).cuboid(-11.0F, -49.0F, -18.5F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(353, 104).cuboid(-11.0F, -55.0F, -18.5F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(16.0F, 35.9F, 17.0F));

        ModelPartData door_left_corners = door_left.addChild("door_left_corners", ModelPartBuilder.create().uv(296, 370).cuboid(-12.0F, -61.0F, -18.5F, 1.0F, 49.0F, 3.0F, new Dilation(0.0F))
            .uv(288, 370).cuboid(11.0F, -61.0F, -18.5F, 1.0F, 49.0F, 3.0F, new Dilation(0.0F))
            .uv(353, 100).cuboid(-11.0F, -13.0F, -18.5F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(353, 96).cuboid(-11.0F, -61.0F, -18.5F, 22.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(16.0F, 35.9F, 17.0F));

        ModelPartData door_left_borders = door_left.addChild("door_left_borders", ModelPartBuilder.create().uv(238, 345).cuboid(1.0F, -47.0F, -1.0F, 1.0F, 57.0F, 3.0F, new Dilation(0.0F))
            .uv(50, 357).cuboid(28.0F, -47.0F, -1.0F, 1.0F, 57.0F, 3.0F, new Dilation(0.0F))
            .uv(344, 269).cuboid(2.0F, -48.0F, -1.0F, 26.0F, 1.0F, 3.0F, new Dilation(0.0F))
            .uv(11, 111).cuboid(0.0F, 10.0F, -1.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(15, 96).cuboid(28.0F, 10.0F, -1.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(15, 86).cuboid(0.0F, -49.0F, -1.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(15, 80).cuboid(28.0F, -49.0F, -1.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, 20.9F, -0.5F));

        ModelPartData door_right = modelPartData.addChild("door_right", ModelPartBuilder.create().uv(18, 56).cuboid(-0.5F, -1.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData lamp = modelPartData.addChild("lamp", ModelPartBuilder.create().uv(352, 8).cuboid(-14.0F, 20.5F, 19.25F, 28.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(352, 6).cuboid(-14.0F, 22.0F, 19.25F, 28.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(352, 4).cuboid(-14.0F, 23.5F, 19.25F, 28.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(300, 152).cuboid(-20.25F, 20.5F, -14.0F, 1.0F, 1.0F, 28.0F, new Dilation(0.0F))
            .uv(34, 294).cuboid(-20.25F, 22.0F, -14.0F, 1.0F, 1.0F, 28.0F, new Dilation(0.0F))
            .uv(298, 295).cuboid(-20.25F, 23.5F, -14.0F, 1.0F, 1.0F, 28.0F, new Dilation(0.0F))
            .uv(130, 184).cuboid(19.25F, 20.5F, -14.0F, 1.0F, 1.0F, 28.0F, new Dilation(0.0F))
            .uv(192, 184).cuboid(19.25F, 22.0F, -14.0F, 1.0F, 1.0F, 28.0F, new Dilation(0.0F))
            .uv(154, 275).cuboid(19.25F, 23.5F, -14.0F, 1.0F, 1.0F, 28.0F, new Dilation(0.0F))
            .uv(291, 145).cuboid(-14.0F, 20.5F, -20.25F, 28.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(352, 0).cuboid(-14.0F, 22.0F, -20.25F, 28.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(352, 2).cuboid(-14.0F, 23.5F, -20.25F, 28.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -65.0F, 0.0F));

        ModelPartData boti = modelPartData.addChild("boti", ModelPartBuilder.create().uv(222, 215).cuboid(-15.0F, -3.0F, -15.0F, 30.0F, 1.0F, 30.0F, new Dilation(0.0F))
            .uv(222, 184).cuboid(-15.0F, -65.0F, -15.0F, 30.0F, 1.0F, 30.0F, new Dilation(0.0F))
            .uv(160, 184).cuboid(-16.0F, -64.0F, -15.0F, 1.0F, 61.0F, 30.0F, new Dilation(0.0F))
            .uv(98, 184).cuboid(15.0F, -64.0F, -15.0F, 1.0F, 61.0F, 30.0F, new Dilation(0.0F))
            .uv(0, 257).cuboid(-15.0F, -64.0F, 15.0F, 30.0F, 61.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        return TexturedModelData.of(modelData, 512, 512);
    }
}

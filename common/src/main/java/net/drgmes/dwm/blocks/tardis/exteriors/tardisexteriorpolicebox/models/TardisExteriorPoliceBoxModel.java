package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.models;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class TardisExteriorPoliceBoxModel extends BaseTardisExteriorModel {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(DWM.getIdentifier("textures/block/tardis/exteriors/tardis_exterior_police_box.png"), "main");

    public TardisExteriorPoliceBoxModel(ModelPart root) {
        super(root);
    }

    @SuppressWarnings("unused")
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 0).cuboid(-25.0F, -2.0F, -25.0F, 50.0F, 2.0F, 50.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData corners = base.addChild("corners", ModelPartBuilder.create().uv(72, 248).cuboid(-23.0F, -74.0F, -23.0F, 6.0F, 72.0F, 6.0F, new Dilation(0.0F))
            .uv(48, 248).cuboid(-23.0F, -74.0F, 17.0F, 6.0F, 72.0F, 6.0F, new Dilation(0.0F))
            .uv(24, 248).cuboid(17.0F, -74.0F, 17.0F, 6.0F, 72.0F, 6.0F, new Dilation(0.0F))
            .uv(0, 248).cuboid(17.0F, -74.0F, -23.0F, 6.0F, 72.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData walls = base.addChild("walls", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_back = walls.addChild("wall_back", ModelPartBuilder.create().uv(144, 244).cuboid(-14.0F, -62.0F, 19.0F, 28.0F, 58.0F, 1.0F, new Dilation(0.0F))
            .uv(272, 178).cuboid(-2.0F, -64.0F, 17.5F, 4.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(314, 143).cuboid(-16.0F, -64.0F, 18.5F, 2.0F, 62.0F, 3.0F, new Dilation(0.0F))
            .uv(204, 311).cuboid(14.0F, -64.0F, 18.5F, 2.0F, 62.0F, 3.0F, new Dilation(0.0F))
            .uv(262, 299).cuboid(-17.0F, -64.0F, 17.5F, 1.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(250, 298).cuboid(16.0F, -64.0F, 17.5F, 1.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(274, 304).cuboid(2.0F, -64.0F, 18.0F, 1.0F, 62.0F, 4.0F, new Dilation(0.0F))
            .uv(184, 303).cuboid(-3.0F, -64.0F, 18.0F, 1.0F, 62.0F, 4.0F, new Dilation(0.0F))
            .uv(228, 158).cuboid(-17.0F, -73.0F, 17.5F, 34.0F, 9.0F, 5.0F, new Dilation(0.0F))
            .uv(317, 73).cuboid(-14.0F, -4.0F, 18.5F, 28.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(317, 68).cuboid(-14.0F, -17.0F, 18.5F, 28.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(317, 63).cuboid(-14.0F, -30.0F, 18.5F, 28.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(246, 25).cuboid(-14.0F, -43.0F, 18.5F, 28.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(246, 20).cuboid(-14.0F, -64.0F, 18.5F, 28.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_back_windows = wall_back.addChild("wall_back_windows", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_back_window_right = wall_back_windows.addChild("wall_back_window_right", ModelPartBuilder.create().uv(320, 98).cuboid(-14.0F, -62.0F, 19.1F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(318, 271).cuboid(-14.0F, -62.0F, 18.85F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(318, 291).cuboid(-14.0F, -44.0F, 18.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(288, 48).cuboid(-14.0F, -62.0F, 18.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(324, 202).cuboid(-14.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(324, 183).cuboid(-4.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(324, 164).cuboid(-9.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(323, 56).cuboid(-13.0F, -50.0F, 18.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(323, 53).cuboid(-13.0F, -56.0F, 18.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_back_window_left = wall_back_windows.addChild("wall_back_window_left", ModelPartBuilder.create().uv(320, 78).cuboid(3.0F, -62.0F, 19.1F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(270, 83).cuboid(3.0F, -62.0F, 18.85F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(288, 45).cuboid(3.0F, -44.0F, 18.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(286, 42).cuboid(3.0F, -62.0F, 18.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(324, 145).cuboid(3.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(323, 34).cuboid(13.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(323, 15).cuboid(8.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(323, 12).cuboid(4.0F, -50.0F, 18.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(323, 9).cuboid(4.0F, -56.0F, 18.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_right = walls.addChild("wall_right", ModelPartBuilder.create().uv(214, 239).cuboid(-14.0F, -62.0F, 19.0F, 28.0F, 58.0F, 1.0F, new Dilation(0.0F))
            .uv(114, 248).cuboid(-2.0F, -64.0F, 17.5F, 4.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(310, 78).cuboid(-16.0F, -64.0F, 18.5F, 2.0F, 62.0F, 3.0F, new Dilation(0.0F))
            .uv(308, 237).cuboid(14.0F, -64.0F, 18.5F, 2.0F, 62.0F, 3.0F, new Dilation(0.0F))
            .uv(238, 298).cuboid(-17.0F, -64.0F, 17.5F, 1.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(226, 298).cuboid(16.0F, -64.0F, 17.5F, 1.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(174, 303).cuboid(2.0F, -64.0F, 18.0F, 1.0F, 62.0F, 4.0F, new Dilation(0.0F))
            .uv(164, 303).cuboid(-3.0F, -64.0F, 18.0F, 1.0F, 62.0F, 4.0F, new Dilation(0.0F))
            .uv(228, 144).cuboid(-17.0F, -73.0F, 17.5F, 34.0F, 9.0F, 5.0F, new Dilation(0.0F))
            .uv(246, 15).cuboid(-14.0F, -4.0F, 18.5F, 28.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(246, 10).cuboid(-14.0F, -17.0F, 18.5F, 28.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(246, 5).cuboid(-14.0F, -30.0F, 18.5F, 28.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(246, 0).cuboid(-14.0F, -43.0F, 18.5F, 28.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(238, 34).cuboid(-14.0F, -64.0F, 18.5F, 28.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData wall_right_windows = wall_right.addChild("wall_right_windows", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_right_window_right = wall_right_windows.addChild("wall_right_window_right", ModelPartBuilder.create().uv(133, 150).cuboid(-14.0F, -62.0F, 19.1F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(109, 150).cuboid(-14.0F, -62.0F, 18.85F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(286, 39).cuboid(-14.0F, -44.0F, 18.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(278, 30).cuboid(-14.0F, -62.0F, 18.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(308, 19).cuboid(-14.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(308, 0).cuboid(-4.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(306, 142).cuboid(-9.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(323, 6).cuboid(-13.0F, -50.0F, 18.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(323, 3).cuboid(-13.0F, -56.0F, 18.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_right_window_left = wall_right_windows.addChild("wall_right_window_left", ModelPartBuilder.create().uv(150, 0).cuboid(3.0F, -62.0F, 19.1F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 150).cuboid(3.0F, -62.0F, 18.85F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(264, 103).cuboid(3.0F, -44.0F, 18.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(262, 40).cuboid(3.0F, -62.0F, 18.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(300, 78).cuboid(3.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(294, 83).cuboid(13.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(174, 0).cuboid(8.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(323, 0).cuboid(4.0F, -50.0F, 18.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(322, 321).cuboid(4.0F, -56.0F, 18.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_left = walls.addChild("wall_left", ModelPartBuilder.create().uv(214, 180).cuboid(-14.0F, -62.0F, 19.0F, 28.0F, 58.0F, 1.0F, new Dilation(0.0F))
            .uv(96, 248).cuboid(-2.0F, -64.0F, 17.5F, 4.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(194, 308).cuboid(-16.0F, -64.0F, 18.5F, 2.0F, 62.0F, 3.0F, new Dilation(0.0F))
            .uv(304, 304).cuboid(14.0F, -64.0F, 18.5F, 2.0F, 62.0F, 3.0F, new Dilation(0.0F))
            .uv(214, 298).cuboid(-17.0F, -64.0F, 17.5F, 1.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(296, 237).cuboid(16.0F, -64.0F, 17.5F, 1.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(154, 303).cuboid(2.0F, -64.0F, 18.0F, 1.0F, 62.0F, 4.0F, new Dilation(0.0F))
            .uv(144, 303).cuboid(-3.0F, -64.0F, 18.0F, 1.0F, 62.0F, 4.0F, new Dilation(0.0F))
            .uv(228, 120).cuboid(-17.0F, -73.0F, 17.5F, 34.0F, 9.0F, 5.0F, new Dilation(0.0F))
            .uv(235, 78).cuboid(-14.0F, -4.0F, 18.5F, 28.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(37, 180).cuboid(-14.0F, -17.0F, 18.5F, 28.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(37, 175).cuboid(-14.0F, -30.0F, 18.5F, 28.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(37, 170).cuboid(-14.0F, -43.0F, 18.5F, 28.0F, 2.0F, 3.0F, new Dilation(0.0F))
            .uv(37, 165).cuboid(-14.0F, -64.0F, 18.5F, 28.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData wall_left_windows = wall_left.addChild("wall_left_windows", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_left_window_right = wall_left_windows.addChild("wall_left_window_right", ModelPartBuilder.create().uv(132, 52).cuboid(-14.0F, -62.0F, 19.1F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 126).cuboid(-14.0F, -62.0F, 18.85F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(238, 39).cuboid(-14.0F, -44.0F, 18.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(235, 83).cuboid(-14.0F, -62.0F, 18.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(99, 163).cuboid(-14.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(156, 52).cuboid(-4.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(150, 106).cuboid(-9.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(322, 318).cuboid(-13.0F, -50.0F, 18.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(322, 315).cuboid(-13.0F, -56.0F, 18.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData wall_left_window_left = wall_left_windows.addChild("wall_left_window_left", ModelPartBuilder.create().uv(120, 106).cuboid(3.0F, -62.0F, 19.1F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 106).cuboid(3.0F, -62.0F, 18.85F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(109, 180).cuboid(3.0F, -44.0F, 18.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(133, 176).cuboid(3.0F, -62.0F, 18.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(24, 150).cuboid(3.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(144, 106).cuboid(13.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(24, 125).cuboid(8.0F, -61.0F, 18.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(322, 312).cuboid(4.0F, -50.0F, 18.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(322, 309).cuboid(4.0F, -56.0F, 18.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData doors = base.addChild("doors", ModelPartBuilder.create().uv(228, 106).cuboid(-17.0F, -73.0F, -22.5F, 34.0F, 9.0F, 5.0F, new Dilation(0.0F))
            .uv(132, 248).cuboid(16.0F, -64.0F, -22.5F, 1.0F, 62.0F, 5.0F, new Dilation(0.0F))
            .uv(202, 244).cuboid(-17.0F, -64.0F, -22.5F, 1.0F, 62.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData labels = base.addChild("labels", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData label_back = labels.addChild("label_back", ModelPartBuilder.create().uv(200, 43).cuboid(-21.0F, -73.1F, 22.0F, 42.0F, 7.0F, 2.0F, new Dilation(0.0F))
            .uv(124, 126).cuboid(-21.0F, -73.1F, 24.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(120, 126).cuboid(20.0F, -73.1F, 24.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(235, 76).cuboid(-20.0F, -73.1F, 24.0F, 40.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(235, 74).cuboid(-20.0F, -67.1F, 24.0F, 40.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(235, 58).cuboid(-20.0F, -72.1F, 23.25F, 40.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData label_right = labels.addChild("label_right", ModelPartBuilder.create().uv(176, 97).cuboid(-21.0F, -73.1F, 22.0F, 42.0F, 7.0F, 2.0F, new Dilation(0.0F))
            .uv(30, 125).cuboid(-21.0F, -73.1F, 24.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(40, 52).cuboid(20.0F, -73.1F, 24.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(235, 72).cuboid(-20.0F, -73.1F, 24.0F, 40.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(235, 70).cuboid(-20.0F, -67.1F, 24.0F, 40.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(235, 52).cuboid(-20.0F, -72.1F, 23.25F, 40.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData label_left = labels.addChild("label_left", ModelPartBuilder.create().uv(176, 88).cuboid(-21.0F, -73.1F, 22.0F, 42.0F, 7.0F, 2.0F, new Dilation(0.0F))
            .uv(4, 40).cuboid(-21.0F, -73.1F, 24.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 40).cuboid(20.0F, -73.1F, 24.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(235, 68).cuboid(-20.0F, -73.1F, 24.0F, 40.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(235, 66).cuboid(-20.0F, -67.1F, 24.0F, 40.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(228, 172).cuboid(-20.0F, -72.1F, 23.25F, 40.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData label_front = labels.addChild("label_front", ModelPartBuilder.create().uv(150, 34).cuboid(-21.0F, -73.1F, 22.0F, 42.0F, 7.0F, 2.0F, new Dilation(0.0F))
            .uv(4, 31).cuboid(-21.0F, -73.1F, 24.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 31).cuboid(20.0F, -73.1F, 24.0F, 1.0F, 7.0F, 1.0F, new Dilation(0.0F))
            .uv(235, 64).cuboid(-20.0F, -73.1F, 24.0F, 40.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(228, 140).cuboid(-20.0F, -67.1F, 24.0F, 40.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(228, 134).cuboid(-20.0F, -72.1F, 23.25F, 40.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData roof = base.addChild("roof", ModelPartBuilder.create().uv(0, 52).cuboid(-22.0F, -76.0F, -22.0F, 44.0F, 10.0F, 44.0F, new Dilation(0.0F))
            .uv(0, 106).cuboid(-20.0F, -79.1F, -20.0F, 40.0F, 4.0F, 40.0F, new Dilation(0.0F))
            .uv(120, 106).cuboid(-18.0F, -80.5F, -18.0F, 36.0F, 2.0F, 36.0F, new Dilation(0.0F))
            .uv(150, 0).cuboid(-16.0F, -82.0F, -16.0F, 32.0F, 2.0F, 32.0F, new Dilation(0.0F))
            .uv(79, 150).cuboid(15.0F, -79.0F, -21.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.0F))
            .uv(150, 20).cuboid(15.0F, -79.0F, 15.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.0F))
            .uv(18, 86).cuboid(-21.0F, -79.0F, 15.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.0F))
            .uv(26, 34).cuboid(-21.0F, -79.0F, -21.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData roof_lamp = roof.addChild("roof_lamp", ModelPartBuilder.create().uv(37, 150).cuboid(-7.0F, -83.0F, -7.0F, 14.0F, 1.0F, 14.0F, new Dilation(0.0F))
            .uv(132, 72).cuboid(-4.0F, -85.0F, -4.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F))
            .uv(120, 126).cuboid(-4.0F, -87.5F, -4.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F))
            .uv(0, 40).cuboid(-4.0F, -90.0F, -4.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F))
            .uv(0, 0).cuboid(-5.0F, -92.5F, -5.0F, 10.0F, 1.0F, 10.0F, new Dilation(0.0F))
            .uv(0, 31).cuboid(-4.0F, -93.5F, -4.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F))
            .uv(26, 43).cuboid(-3.0F, -94.5F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
            .uv(24, 81).cuboid(-2.0F, -95.5F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
            .uv(36, 106).cuboid(3.25F, -91.5F, 3.25F, 1.0F, 9.0F, 1.0F, new Dilation(0.0F))
            .uv(40, 79).cuboid(-4.25F, -91.5F, 3.25F, 1.0F, 9.0F, 1.0F, new Dilation(0.0F))
            .uv(4, 0).cuboid(-4.25F, -91.5F, -4.25F, 1.0F, 9.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 0).cuboid(3.25F, -91.5F, -4.25F, 1.0F, 9.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData door_left = modelPartData.addChild("door_left", ModelPartBuilder.create().uv(272, 245).cuboid(2.6F, -30.2F, -0.05F, 11.0F, 58.0F, 1.0F, new Dilation(0.0F))
            .uv(24, 52).cuboid(4.6F, -8.2F, -0.15F, 7.0F, 9.0F, 1.0F, new Dilation(0.0F))
            .uv(30, 0).cuboid(4.6F, -7.2F, 0.85F, 7.0F, 7.0F, 3.0F, new Dilation(0.0F))
            .uv(44, 34).cuboid(13.85F, -5.2F, -2.4F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
            .uv(24, 34).cuboid(13.6F, -5.2F, 1.95F, 3.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(284, 304).cuboid(14.6F, -32.2F, -2.55F, 2.0F, 62.0F, 3.0F, new Dilation(0.0F))
            .uv(314, 302).cuboid(14.6F, -32.2F, 0.45F, 2.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(104, 315).cuboid(0.6F, -32.2F, -1.55F, 2.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(317, 0).cuboid(0.6F, -32.2F, 0.45F, 2.0F, 62.0F, 1.0F, new Dilation(0.0F))
            .uv(132, 315).cuboid(13.6F, -32.2F, -2.05F, 1.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(120, 315).cuboid(13.6F, -32.2F, -0.05F, 1.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(150, 43).cuboid(2.6F, 27.8F, -1.55F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(320, 121).cuboid(2.6F, 27.8F, 0.45F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(120, 135).cuboid(2.6F, 14.8F, -1.55F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(320, 118).cuboid(2.6F, 14.8F, 0.45F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(132, 92).cuboid(2.6F, 1.8F, -1.55F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(318, 297).cuboid(2.6F, 1.8F, 0.45F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(132, 88).cuboid(2.6F, -11.2F, -1.55F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(318, 294).cuboid(2.6F, -11.2F, 0.45F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(132, 81).cuboid(2.6F, -32.2F, -1.55F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(320, 124).cuboid(2.6F, -32.2F, 0.45F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-16.6F, -7.8F, -19.95F));

        ModelPartData door_left_window = door_left.addChild("door_left_window", ModelPartBuilder.create().uv(24, 11).cuboid(-14.0F, -62.0F, -20.1F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 11).cuboid(-14.0F, -62.0F, -19.85F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(120, 139).cuboid(-14.0F, -44.0F, -20.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(24, 31).cuboid(-14.0F, -62.0F, -20.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(30, 62).cuboid(-4.0F, -61.0F, -20.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(24, 62).cuboid(-14.0F, -61.0F, -20.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(38, 60).cuboid(-9.0F, -61.0F, -20.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(321, 142).cuboid(-13.0F, -50.0F, -20.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(320, 300).cuboid(-13.0F, -56.0F, -20.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(16.6F, 31.8F, 19.95F));

        ModelPartData door_right = modelPartData.addChild("door_right", ModelPartBuilder.create().uv(290, 178).cuboid(-13.6F, -30.2F, -0.05F, 11.0F, 58.0F, 1.0F, new Dilation(0.0F))
            .uv(144, 125).cuboid(-10.6F, -6.2F, -0.15F, 5.0F, 5.0F, 1.0F, new Dilation(0.0F))
            .uv(36, 116).cuboid(-14.85F, -6.7F, -2.4F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F))
            .uv(28, 37).cuboid(-14.75F, 2.3F, -2.15F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(24, 43).cuboid(-16.6F, -5.2F, 1.95F, 3.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(24, 37).cuboid(-15.1F, -4.7F, 2.2F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
            .uv(294, 304).cuboid(-16.6F, -32.2F, -2.55F, 2.0F, 62.0F, 3.0F, new Dilation(0.0F))
            .uv(96, 315).cuboid(-16.6F, -32.2F, 0.45F, 2.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(112, 315).cuboid(-2.6F, -32.2F, -1.55F, 2.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(318, 208).cuboid(-2.6F, -32.2F, 0.45F, 2.0F, 62.0F, 1.0F, new Dilation(0.0F))
            .uv(138, 315).cuboid(-14.6F, -32.2F, -2.05F, 1.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(126, 315).cuboid(-14.6F, -32.2F, -0.05F, 1.0F, 62.0F, 2.0F, new Dilation(0.0F))
            .uv(109, 174).cuboid(-13.6F, 27.8F, -1.55F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(320, 133).cuboid(-13.6F, 27.8F, 0.45F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(174, 45).cuboid(-13.6F, 14.8F, -1.55F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(320, 136).cuboid(-13.6F, 14.8F, 0.45F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(133, 172).cuboid(-13.6F, 1.8F, -1.55F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(320, 139).cuboid(-13.6F, 1.8F, 0.45F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(109, 170).cuboid(-13.6F, -11.2F, -1.55F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(320, 130).cuboid(-13.6F, -11.2F, 0.45F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
            .uv(79, 159).cuboid(-13.6F, -32.2F, -1.55F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F))
            .uv(320, 127).cuboid(-13.6F, -32.2F, 0.45F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(16.6F, -7.8F, -19.95F));

        ModelPartData door_right_window = door_right.addChild("door_right_window", ModelPartBuilder.create().uv(0, 72).cuboid(3.0F, -62.0F, -20.1F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(0, 52).cuboid(3.0F, -62.0F, -19.85F, 11.0F, 19.0F, 1.0F, new Dilation(0.0F))
            .uv(150, 47).cuboid(3.0F, -44.0F, -20.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(150, 29).cuboid(3.0F, -62.0F, -20.75F, 11.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(34, 123).cuboid(3.0F, -61.0F, -20.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(30, 106).cuboid(13.0F, -61.0F, -20.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(24, 106).cuboid(8.0F, -61.0F, -20.75F, 1.0F, 17.0F, 2.0F, new Dilation(0.0F))
            .uv(322, 306).cuboid(4.0F, -50.0F, -20.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F))
            .uv(322, 303).cuboid(4.0F, -56.0F, -20.75F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-16.6F, 31.8F, 19.95F));

        ModelPartData lamp = modelPartData.addChild("lamp", ModelPartBuilder.create().uv(0, 170).cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 9.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -65.0F, 0.0F));

        ModelPartData boti = modelPartData.addChild("boti", ModelPartBuilder.create().uv(125, 144).cuboid(-17.0F, -64.0F, 2.1F, 34.0F, 1.0F, 35.0F, new Dilation(0.0F))
            .uv(132, 52).cuboid(-17.0F, -1.0F, 2.1F, 34.0F, 1.0F, 35.0F, new Dilation(0.0F))
            .uv(144, 180).cuboid(-17.0F, -63.0F, 37.1F, 34.0F, 63.0F, 1.0F, new Dilation(0.0F))
            .uv(72, 150).cuboid(-18.0F, -63.0F, 2.1F, 1.0F, 63.0F, 35.0F, new Dilation(0.0F))
            .uv(0, 150).cuboid(17.0F, -63.0F, 2.1F, 1.0F, 63.0F, 35.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 22.0F, -20.1F));

        return TexturedModelData.of(modelData, 512, 512);
    }
}

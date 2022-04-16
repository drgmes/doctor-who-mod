package net.drgmes.dwm.utils.helpers;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.data.client.ModBlockStateProvider;
import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.OBJLoaderBuilder;

public class ModelHelper {
    public static <T extends ModelBuilder<T>> OBJLoaderBuilder<T> applyExternalOBJModel(T builder, String name, boolean doFlip) {
        OBJLoaderBuilder<T> objLoaderBuilder = builder.customLoader(OBJLoaderBuilder::begin);

        objLoaderBuilder.modelLocation(new ResourceLocation(DWM.MODID, "models/" + name + ".obj"));
        objLoaderBuilder.flipV(doFlip);

        return objLoaderBuilder;
    }

    public static void createWallBlockModel(String name, WallBlock block, ModBlockStateProvider provider, ResourceLocation xTexture, ResourceLocation yTexture) {
        BlockModelBuilder post = provider.models().getBuilder(name + "_post");
        post.texture("particle", "#wall_x");
        post.texture("wall_x", xTexture);
        post.texture("wall_y", yTexture);
        post.element().from(4, 0, 4);
        post.element(0).to(12, 16, 12);
        post.element(0).face(Direction.UP).texture("#wall_y").cullface(Direction.UP);
        post.element(0).face(Direction.DOWN).texture("#wall_y").cullface(Direction.DOWN);
        post.element(0).face(Direction.NORTH).texture("#wall_x");
        post.element(0).face(Direction.SOUTH).texture("#wall_x");
        post.element(0).face(Direction.WEST).texture("#wall_x");
        post.element(0).face(Direction.EAST).texture("#wall_x");

        BlockModelBuilder side = provider.models().getBuilder(name + "_side");
        side.texture("particle", "#wall_x");
        side.texture("wall_x", xTexture);
        side.texture("wall_y", yTexture);
        side.element().from(5, 0, 0);
        side.element(0).to(11, 14, 8);
        side.element(0).face(Direction.UP).texture("#wall_y");
        side.element(0).face(Direction.DOWN).texture("#wall_y").cullface(Direction.DOWN);
        side.element(0).face(Direction.NORTH).texture("#wall_x").cullface(Direction.NORTH);
        side.element(0).face(Direction.SOUTH).texture("#wall_x");
        side.element(0).face(Direction.WEST).texture("#wall_x");
        side.element(0).face(Direction.EAST).texture("#wall_x");

        BlockModelBuilder sideTall = provider.models().getBuilder(name + "_side_tall");
        sideTall.texture("particle", "#wall_x");
        sideTall.texture("wall_x", xTexture);
        sideTall.texture("wall_y", yTexture);
        sideTall.element().from(5, 0, 0);
        sideTall.element(0).to(11, 16, 8);
        sideTall.element(0).face(Direction.UP).texture("#wall_y").cullface(Direction.UP);
        sideTall.element(0).face(Direction.DOWN).texture("#wall_y").cullface(Direction.DOWN);
        sideTall.element(0).face(Direction.NORTH).texture("#wall_x").cullface(Direction.NORTH);
        sideTall.element(0).face(Direction.SOUTH).texture("#wall_x");
        sideTall.element(0).face(Direction.WEST).texture("#wall_x");
        sideTall.element(0).face(Direction.EAST).texture("#wall_x");

        provider.wallBlock(block, post, side, sideTall);
    }

    public static void createWallItemModel(String name, ModItemModelProvider provider, ResourceLocation xTexture, ResourceLocation yTexture) {
        ModelFile parent = provider.getExistingFile(provider.mcLoc("block/block"));
        ItemModelBuilder builder = provider.getBuilder(name);

        builder.parent(parent);
        builder.ao(true);
        builder.texture("wall_x", xTexture);
        builder.texture("wall_y", yTexture);

        builder.transforms().transform(ModelBuilder.Perspective.GUI).rotation(30, 135, 0);
        builder.transforms().transform(ModelBuilder.Perspective.GUI).scale(0.625F, 0.625F, 0.625F);

        builder.transforms().transform(ModelBuilder.Perspective.FIXED).rotation(0, 90, 0);
        builder.transforms().transform(ModelBuilder.Perspective.FIXED).scale(0.5F, 0.5F, 0.5F);

        builder.element().from(4, 0, 4);
        builder.element(0).to(12, 16, 12);
        builder.element(0).face(Direction.UP).texture("#wall_y").uvs(4, 4, 12, 12);
        builder.element(0).face(Direction.DOWN).texture("#wall_y").uvs(4, 4, 12, 12).cullface(Direction.DOWN);
        builder.element(0).face(Direction.NORTH).texture("#wall_x").uvs(4, 0, 12, 16);
        builder.element(0).face(Direction.SOUTH).texture("#wall_x").uvs(4, 0, 12, 16);
        builder.element(0).face(Direction.WEST).texture("#wall_x").uvs(4, 0, 12, 16);
        builder.element(0).face(Direction.EAST).texture("#wall_x").uvs(4, 0, 12, 16);

        builder.element().from(5, 0, 0);
        builder.element(1).to(11, 13, 16);
        builder.element(1).face(Direction.UP).texture("#wall_y").uvs(5, 0, 11, 16);
        builder.element(1).face(Direction.DOWN).texture("#wall_y").uvs(5, 0, 11, 16).cullface(Direction.DOWN);
        builder.element(1).face(Direction.NORTH).texture("#wall_x").uvs(5, 3, 11, 16).cullface(Direction.NORTH);
        builder.element(1).face(Direction.SOUTH).texture("#wall_x").uvs(5, 3, 11, 16).cullface(Direction.SOUTH);
        builder.element(1).face(Direction.WEST).texture("#wall_x").uvs(0, 3, 16, 16);
        builder.element(1).face(Direction.EAST).texture("#wall_x").uvs(0, 3, 16, 16);
    }

    public static <T extends ModelBuilder<T>> void rotateToBlockStyle(T builder) {
        builder.transforms().transform(ModelBuilder.Perspective.GUI).rotation(30, 225, 0);
        builder.transforms().transform(ModelBuilder.Perspective.GUI).translation(3F, -1.5F, 0);
        builder.transforms().transform(ModelBuilder.Perspective.GUI).scale(0.625F, 0.625F, 0.625F);

        builder.transforms().transform(ModelBuilder.Perspective.GROUND).rotation(0, 0, 0);
        builder.transforms().transform(ModelBuilder.Perspective.GROUND).translation(0, 3F, 0);
        builder.transforms().transform(ModelBuilder.Perspective.GROUND).scale(0.25F, 0.25F, 0.25F);

        builder.transforms().transform(ModelBuilder.Perspective.FIXED).rotation(0, 0, 0);
        builder.transforms().transform(ModelBuilder.Perspective.FIXED).translation(0, 0, 0);
        builder.transforms().transform(ModelBuilder.Perspective.FIXED).scale(0.5F, 0.5F, 0.5F);

        builder.transforms().transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT).rotation(0, 90, 0);
        builder.transforms().transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT).translation(-2.5F, 1F, 0);
        builder.transforms().transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT).scale(0.4F, 0.4F, 0.4F);

        builder.transforms().transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT).rotation(75, 90, 0);
        builder.transforms().transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT).translation(-2.5F, 2.5F, 0);
        builder.transforms().transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT).scale(0.375F, 0.375F, 0.375F);
    }
}

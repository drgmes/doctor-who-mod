package net.drgmes.dwm.world.features;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.helpers.WorldHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class AbandonedTardisFeature extends Feature<DefaultFeatureConfig> {
    public AbandonedTardisFeature() {
        super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos blockPos = context.getOrigin().toImmutable();

        if (world.getBlockState(blockPos.down()).getMaterial().isReplaceable()) {
            blockPos = blockPos.down().toImmutable();
        }

        if (!WorldHelper.checkBlockIsSolid(world.getBlockState(blockPos.down()))) {
            return false;
        }

        Direction direction = null;
        for (Direction dir : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST}) {
            if (!WorldHelper.checkBlockIsSolid(world.getBlockState(blockPos.offset(dir))) && !WorldHelper.checkBlockIsSolid(world.getBlockState(blockPos.up().offset(dir)))) {
                direction = dir;
                break;
            }
        }

        if (direction == null) {
            return false;
        }

        BlockState tardisExteriorBlockState = ModBlocks.TARDIS_EXTERIOR_POLICE_BOX.getBlock().getDefaultState();
        tardisExteriorBlockState = tardisExteriorBlockState.with(BaseTardisExteriorBlock.HALF, DoubleBlockHalf.LOWER);
        tardisExteriorBlockState = tardisExteriorBlockState.with(BaseTardisExteriorBlock.FACING, direction);
        tardisExteriorBlockState = tardisExteriorBlockState.with(BaseTardisExteriorBlock.WATERLOGGED, world.getBlockState(blockPos).getFluidState().isIn(FluidTags.WATER));
        setBlockState(world, blockPos, tardisExteriorBlockState);

        BlockState tardisExteriorUpBlockState = tardisExteriorBlockState.with(BaseTardisExteriorBlock.HALF, DoubleBlockHalf.UPPER);
        tardisExteriorUpBlockState = tardisExteriorUpBlockState.with(BaseTardisExteriorBlock.WATERLOGGED, world.getBlockState(blockPos.up()).getFluidState().isIn(FluidTags.WATER));
        setBlockState(world, blockPos.up(), tardisExteriorUpBlockState);

        return true;
    }
}

package net.drgmes.dwm.blocks.tardis.engines;

import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedBlockWithEntity;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseTardisEngineBlock extends BaseRotatableWaterloggedBlockWithEntity {
    public BaseTardisEngineBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!TardisHelper.isTardisDimension(world)) return ActionResult.PASS;
        if (player.isSpectator()) return ActionResult.PASS;
        if (world.isClient) return ActionResult.SUCCESS;

        player.openHandledScreen(blockState.createScreenHandlerFactory(world, blockPos));
        return ActionResult.SUCCESS;
    }
}

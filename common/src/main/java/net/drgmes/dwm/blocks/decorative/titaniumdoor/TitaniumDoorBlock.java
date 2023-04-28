package net.drgmes.dwm.blocks.decorative.titaniumdoor;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class TitaniumDoorBlock extends DoorBlock {
    public TitaniumDoorBlock(AbstractBlock.Settings settings) {
        super(settings, BlockSetType.IRON);
    }

    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        blockState = blockState.cycle(OPEN);
        world.setBlockState(blockPos, blockState, Block.NOTIFY_ALL);
        world.syncWorldEvent(player, blockState.get(OPEN) ? 1005 : 1011, blockPos, 0);
        world.emitGameEvent(player, this.isOpen(blockState) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos);

        return ActionResult.success(world.isClient);
    }
}

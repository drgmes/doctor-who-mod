package net.drgmes.dwm.blocks.tardis.engines;

import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public abstract class BaseTardisEngineBlock extends BaseRotatableWaterloggedEntityBlock {
    public BaseTardisEngineBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (player.isSpectator()) return InteractionResult.PASS;

        if (level.getBlockEntity(blockPos) instanceof BaseTardisEngineBlockEntity tardisEngineBlockEntity) {
            NetworkHooks.openGui((ServerPlayer) player, tardisEngineBlockEntity, blockPos);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}

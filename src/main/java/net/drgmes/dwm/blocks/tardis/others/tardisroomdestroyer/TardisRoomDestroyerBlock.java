package net.drgmes.dwm.blocks.tardis.others.tardisroomdestroyer;

import net.drgmes.dwm.blocks.tardis.others.tardisroomdestroyer.screens.TardisRoomDestroyerScreen;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedEntityBlock;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class TardisRoomDestroyerBlock extends BaseRotatableWaterloggedEntityBlock {
    public TardisRoomDestroyerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TardisRoomDestroyerBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (!TardisHelper.isTardisDimension(level)) return InteractionResult.PASS;
        if (player.isSpectator()) return InteractionResult.PASS;
        if (!player.isShiftKeyDown()) return InteractionResult.PASS;
        if (!level.isClientSide) return InteractionResult.SUCCESS;

        if (level.getBlockEntity(blockPos) instanceof TardisRoomDestroyerBlockEntity tardisRoomDestroyerBlockEntity) {
            Minecraft mc = Minecraft.getInstance();
            mc.setScreen(new TardisRoomDestroyerScreen(tardisRoomDestroyerBlockEntity));
        }

        return InteractionResult.SUCCESS;
    }
}

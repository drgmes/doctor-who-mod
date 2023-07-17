package net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer;

import net.drgmes.dwm.network.client.ArsDestroyerOpenPacket;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedBlockWithEntity;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TardisArsDestroyerBlock extends BaseRotatableWaterloggedBlockWithEntity {
    public TardisArsDestroyerBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TardisArsDestroyerBlockEntity(blockPos, blockState);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if (!TardisHelper.isTardisDimension(world) || player.isSpectator() || !player.isSneaking()) return ActionResult.PASS;
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return ActionResult.SUCCESS;

        if (world.getBlockEntity(blockPos) instanceof TardisArsDestroyerBlockEntity tardisArsDestroyerBlockEntity) {
            new ArsDestroyerOpenPacket(blockPos, tardisArsDestroyerBlockEntity.arsStructureName).sendTo(serverPlayer);
        }

        return ActionResult.SUCCESS;
    }
}

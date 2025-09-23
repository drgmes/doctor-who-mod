package net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.ars.ArsStructures;
import net.drgmes.dwm.network.client.ArsDestroyerOpenPacket;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedBlockWithEntity;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class TardisArsDestroyerBlock extends BaseRotatableWaterloggedBlockWithEntity {
    public TardisArsDestroyerBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModBlockEntities.TARDIS_ARS_DESTROYER.getBlockEntityType().instantiate(blockPos, blockState);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, BlockHitResult blockHitResult) {
        if (!(world instanceof ServerWorld serverWorld) || !TardisHelper.isTardisDimension(serverWorld) || player.isSpectator()) return ActionResult.PASS;

        Optional<TardisStateManager> tardisHolder = TardisStateManager.get(serverWorld);
        if (tardisHolder.isEmpty()) return ActionResult.PASS;

        UUID ownerId = tardisHolder.get().getOwner();
        if (ownerId != null && !ownerId.equals(player.getUuid())) {
            if (player.isSneaking()) player.sendMessage(DWM.TEXTS.TARDIS_NOT_ALLOWED, true);
            return ActionResult.PASS;
        }

        if (!player.isSneaking()) {
            player.sendMessage(DWM.TEXTS.ARS_DESTROYER_MUST_BE_SNEAKING, true);
            return ActionResult.PASS;
        }

        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return ActionResult.SUCCESS;
        }

        if (world.getBlockEntity(blockPos) instanceof TardisArsDestroyerBlockEntity tardisArsDestroyerBlockEntity) {
            new ArsDestroyerOpenPacket(blockPos, ArsStructures.STRUCTURES.get(tardisArsDestroyerBlockEntity.arsStructureName)).sendTo(serverPlayer);
        }

        return ActionResult.SUCCESS;
    }
}

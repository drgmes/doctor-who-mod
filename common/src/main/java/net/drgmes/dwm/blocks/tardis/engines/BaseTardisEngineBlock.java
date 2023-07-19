package net.drgmes.dwm.blocks.tardis.engines;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.items.tardis.misc.repairkit.RepairKitItem;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedBlockWithEntity;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
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

        TardisStateManager.get((ServerWorld) world).ifPresent((tardis) -> {
            if (tardis.isBroken()) {
                ItemStack heldItem = player.getStackInHand(hand);

                if (heldItem.getItem() instanceof RepairKitItem) {
                    tardis.setBrokenState(false);
                    tardis.updateConsoleTiles();
                    tardis.updateRoomEntrancePortals();

                    if (!player.isCreative()) {
                        heldItem.decrement(1);
                        player.setStackInHand(hand, heldItem);
                    }

                    player.sendMessage(DWM.TEXTS.TARDIS_REPAIRED, true);
                    return;
                }

                player.sendMessage(DWM.TEXTS.TARDIS_BROKEN, true);
                return;
            }

            if (!tardis.checkAccess(player, true, false)) {
                ModSounds.playTardisBellSound(tardis.getWorld(), tardis.getMainConsolePosition());
                player.sendMessage(DWM.TEXTS.TARDIS_NOT_ALLOWED, true);
                return;
            }

            player.openHandledScreen(blockState.createScreenHandlerFactory(world, blockPos));
        });

        return ActionResult.SUCCESS;
    }
}

package net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer;

import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.screens.TardisArsDestroyerScreen;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedBlockWithEntity;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
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
    @Environment(EnvType.CLIENT)
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if (!TardisHelper.isTardisDimension(world)) return ActionResult.PASS;
        if (player.isSpectator()) return ActionResult.PASS;
        if (!player.isSneaking()) return ActionResult.PASS;
        if (!world.isClient) return ActionResult.SUCCESS;

        if (world.getBlockEntity(blockPos) instanceof TardisArsDestroyerBlockEntity tardisArsDestroyerBlockEntity) {
            MinecraftClient mc = MinecraftClient.getInstance();
            mc.setScreen(new TardisArsDestroyerScreen(tardisArsDestroyerBlockEntity));
        }

        return ActionResult.SUCCESS;
    }
}

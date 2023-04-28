package net.drgmes.dwm.blocks.tardis.misc.tardisarscreator;

import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.screens.TardisArsCreatorScreen;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedBlock;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TardisArsCreatorBlock extends BaseRotatableWaterloggedBlock {
    public TardisArsCreatorBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if (!TardisHelper.isTardisDimension(world)) return ActionResult.PASS;
        if (player.isSpectator()) return ActionResult.PASS;
        if (!world.isClient) return ActionResult.SUCCESS;

        MinecraftClient mc = MinecraftClient.getInstance();
        mc.setScreen(new TardisArsCreatorScreen(blockPos));

        return ActionResult.SUCCESS;
    }
}

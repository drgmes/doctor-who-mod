package net.drgmes.dwm.common.screwdriver.modes.tardis;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.common.screwdriver.modes.BaseScrewdriverMode;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Optional;

public class ScrewdriverTardisMode extends BaseScrewdriverMode {
    public static final ScrewdriverTardisMode INSTANCE = new ScrewdriverTardisMode();

    @Override
    public ActionResult interactWithBlockNative(World world, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (world.isClient) return ActionResult.SUCCESS;
        if (TardisHelper.isTardisDimension(world)) return ActionResult.SUCCESS;

        BlockPos blockPos = hitResult.getBlockPos().up();
        String tardisId = Screwdriver.getTardisId(player.getStackInHand(hand));
        ServerWorld tardisWorld = DimensionHelper.getModWorld(tardisId, world.getServer());
        if (tardisWorld == null || tardisWorld == world) return ActionResult.FAIL;

        Optional<TardisStateManager> tardisHolder = TardisStateManager.get(tardisWorld);
        if (tardisHolder.isEmpty()) return ActionResult.FAIL;

        MutableText x = Text.literal("" + blockPos.getX()).formatted(Formatting.YELLOW);
        MutableText y = Text.literal("" + blockPos.getY()).formatted(Formatting.YELLOW);
        MutableText z = Text.literal("" + blockPos.getZ()).formatted(Formatting.YELLOW);

        player.sendMessage(Text.translatable("message." + DWM.MODID + ".screwdriver.tardis_relocated", x, y, z), true);
        tardisHolder.get().setDestinationFacing(Direction.fromRotation(player.getHeadYaw()).getOpposite());
        tardisHolder.get().setDestinationDimension(world.getRegistryKey());
        tardisHolder.get().setDestinationPosition(blockPos);
        tardisHolder.get().updateConsoleTiles();
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult interactWithEntity(World world, PlayerEntity player, Hand hand, EntityHitResult hitResult, boolean isAlternativeAction) {
        return ActionResult.PASS;
    }
}

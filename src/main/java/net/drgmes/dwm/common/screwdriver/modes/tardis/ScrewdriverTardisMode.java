package net.drgmes.dwm.common.screwdriver.modes.tardis;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.common.screwdriver.modes.BaseScrewdriverMode;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;

public class ScrewdriverTardisMode extends BaseScrewdriverMode {
    public static ScrewdriverTardisMode INSTANCE = new ScrewdriverTardisMode();

    @Override
    public boolean interactWithBlockNative(Level level, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!this.checkIsValidHitBlock(level.getBlockState(hitResult.getBlockPos()))) return false;

        BlockPos blockPos = hitResult.getBlockPos().above();
        String tardisLevelUUID = Screwdriver.getTardisUUID(player.getItemInHand(hand));
        ServerLevel tardisLevel = DimensionHelper.getLevel(level.getServer(), tardisLevelUUID);
        if (tardisLevel == null || tardisLevel == level) return false;

        Optional<ITardisLevelData> tardis = tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).resolve();
        if (tardis.isEmpty()) return false;

        MutableComponent x = Component.literal("" + blockPos.getX());
        MutableComponent y = Component.literal("" + blockPos.getY());
        MutableComponent z = Component.literal("" + blockPos.getZ());
        x.setStyle(x.getStyle().withColor(ChatFormatting.YELLOW));
        y.setStyle(y.getStyle().withColor(ChatFormatting.YELLOW));
        z.setStyle(z.getStyle().withColor(ChatFormatting.YELLOW));

        player.displayClientMessage(Component.translatable("message." + DWM.MODID + ".screwdriver.tardis_relocated", x, y, z), true);
        tardis.get().setDestinationFacing(Direction.fromYRot(player.getYHeadRot()).getOpposite());
        tardis.get().setDestinationDimension(level.dimension());
        tardis.get().setDestinationPosition(blockPos);
        return true;
    }
}

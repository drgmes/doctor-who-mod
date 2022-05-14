package net.drgmes.dwm.common.screwdriver.modes.tardis;

import java.util.Optional;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.common.screwdriver.modes.BaseScrewdriverMode;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class ScrewdriverTardisMode extends BaseScrewdriverMode {
    public static ScrewdriverTardisMode INSTANCE = new ScrewdriverTardisMode();

    @Override
    public boolean interactWithBlockNative(Level level, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos blockPos = hitResult.getBlockPos().above();
        String tardisDimUUID = Screwdriver.getTardisUUID(player.getItemInHand(hand));
        ServerLevel tardisLevel = DimensionHelper.getLevel(level.getServer(), tardisDimUUID);
        if (tardisLevel == null || tardisLevel == level) return false;

        Optional<ITardisLevelData> tardis = tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).resolve();
        if (tardis.isEmpty()) return false;

        TextComponent x = new TextComponent("" + blockPos.getX());
        TextComponent y = new TextComponent("" + blockPos.getY());
        TextComponent z = new TextComponent("" + blockPos.getZ());
        x.setStyle(x.getStyle().withColor(ChatFormatting.YELLOW));
        y.setStyle(y.getStyle().withColor(ChatFormatting.YELLOW));
        z.setStyle(z.getStyle().withColor(ChatFormatting.YELLOW));

        player.displayClientMessage(new TranslatableComponent("message." + DWM.MODID + ".screwdriver.tardis_relocated", x, y, z), true);
        tardis.get().setDestinationFacing(Direction.fromYRot(player.getYHeadRot()).getOpposite());
        tardis.get().setDestinationDimension(level.dimension());
        tardis.get().setDestinationPosition(blockPos);
        return true;
    }
}

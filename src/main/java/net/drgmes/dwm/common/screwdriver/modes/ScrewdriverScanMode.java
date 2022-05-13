package net.drgmes.dwm.common.screwdriver.modes;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class ScrewdriverScanMode extends BaseScrewdriverMode {
    public static ScrewdriverScanMode INSTANCE = new ScrewdriverScanMode();

    @Override
    public boolean interactWithBlock(Level level, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!super.interactWithBlock(level, player, hand, hitResult)) return false;

        BlockPos blockPos = hitResult.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);

        MutableComponent component = blockState.getBlock().getName().copy();
        component.setStyle(component.getStyle().withColor(ChatFormatting.AQUA));

        player.displayClientMessage(component, true);
        return true;
    }

    @Override
    public boolean interactWithEntity(Level level, Player player, InteractionHand hand, EntityHitResult hitResult) {
        if (!super.interactWithEntity(level, player, hand, hitResult)) return false;

        Entity entity = hitResult.getEntity();
        MutableComponent component = entity.getType().getDescription().copy();
        component.setStyle(component.getStyle().withColor(ChatFormatting.GOLD));

        if (!entity.getDisplayName().getString().equals(component.getString())) {
            component = entity.getDisplayName().copy().append(" (").append(component).append(")");
        }

        player.displayClientMessage(component, true);
        return true;
    }
}

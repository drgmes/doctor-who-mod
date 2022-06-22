package net.drgmes.dwm.common.screwdriver.modes.scan;

import net.drgmes.dwm.common.screwdriver.modes.BaseScrewdriverMode;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.ArrayList;
import java.util.List;

public class ScrewdriverScanMode extends BaseScrewdriverMode {
    public static ScrewdriverScanMode INSTANCE = new ScrewdriverScanMode();

    @Override
    public boolean interactWithBlockNative(Level level, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!this.checkIsValidHitBlock(level.getBlockState(hitResult.getBlockPos()))) return false;

        BlockPos blockPos = hitResult.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);
        BlockEntity blockEntity = level.getBlockEntity(blockPos);

        List<Component> lines = new ArrayList<>();
        MutableComponent title = blockState.getBlock().getName().copy();
        title.setStyle(title.getStyle().withColor(ChatFormatting.AQUA));

        if (blockEntity instanceof Container container) {
            int size = container.getContainerSize();
            int countItems = 0;
            for (int i = 0; i < size; i++) {
                if (!container.getItem(i).isEmpty()) {
                    countItems++;
                }
            }

            if (size > 0) lines.add(Component.literal("Container: " + countItems + " / " + size));
        }

        if (blockEntity instanceof JukeboxBlockEntity jukeboxBlockEntity) {
            ItemStack itemStack = jukeboxBlockEntity.getRecord();
            if (!itemStack.isEmpty()) {
                RecordItem record = (RecordItem) itemStack.getItem();
                lines.add(record.getDisplayName());
            }
        }

        ScrewdriverScanModeOverlay.setup(player, title, lines);
        return true;
    }

    @Override
    public boolean interactWithEntityNative(Level level, Player player, InteractionHand hand, EntityHitResult hitResult) {
        if (!this.checkIsValidHitEntity(hitResult.getEntity())) return false;

        Entity entity = hitResult.getEntity();
        List<Component> lines = new ArrayList<>();
        MutableComponent title = entity.getType().getDescription().copy();
        title.setStyle(title.getStyle().withColor(ChatFormatting.GOLD));

        if (!entity.getDisplayName().getString().equals(title.getString())) {
            title = entity.getDisplayName().copy().append(" (").append(title).append(")");
        }

        if (entity instanceof LivingEntity livingEntity) {
            double health = livingEntity.getHealth();
            double maxHealth = livingEntity.getMaxHealth();
            lines.add(Component.literal("Health: " + String.format("%.1f", health) + " / " + String.format("%.1f", maxHealth)));

            int armor = livingEntity.getArmorValue();
            if (armor > 0) lines.add(Component.literal("Armor: " + armor));
        }

        ScrewdriverScanModeOverlay.setup(player, title, lines);
        return true;
    }
}

package net.drgmes.dwm.common.screwdriver.modes.scan;

import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.common.screwdriver.modes.BaseScrewdriverMode;
import net.drgmes.dwm.network.ScrewdriverRemoteCallablePackets;
import net.drgmes.dwm.utils.helpers.PacketHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ScrewdriverScanMode extends BaseScrewdriverMode {
    public static final ScrewdriverScanMode INSTANCE = new ScrewdriverScanMode();

    @Override
    public ActionResult interactWithBlockNative(World world, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (!this.checkIsValidHitBlock(world.getBlockState(hitResult.getBlockPos()))) return ActionResult.PASS;
        if (world.isClient) return ActionResult.SUCCESS;

        BlockPos blockPos = hitResult.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        BlockEntity blockEntity = world.getBlockEntity(blockPos);

        List<Text> lines = new ArrayList<>();
        MutableText title = blockState.getBlock().getName().copy().formatted(Formatting.AQUA);

        if (blockEntity instanceof Inventory inventory) {
            int size = inventory.size();
            int countItems = 0;
            for (int i = 0; i < size; i++) {
                if (!inventory.getStack(i).isEmpty()) {
                    countItems++;
                }
            }

            if (size > 0) lines.add(Text.literal("Container: " + countItems + " / " + size));
        }

        if (blockEntity instanceof JukeboxBlockEntity jukeboxBlockEntity) {
            ItemStack itemStack = jukeboxBlockEntity.getRecord();
            if (!itemStack.isEmpty()) {
                MusicDiscItem record = (MusicDiscItem) itemStack.getItem();
                lines.add(record.getName());
            }
        }

        updateScrewdriverData(player, hand, title, lines);
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult interactWithEntityNative(World world, PlayerEntity player, Hand hand, EntityHitResult hitResult) {
        if (!this.checkIsValidHitEntity(hitResult.getEntity())) return ActionResult.PASS;
        if (world.isClient) return ActionResult.SUCCESS;

        Entity entity = hitResult.getEntity();
        List<Text> lines = new ArrayList<>();
        MutableText title = entity.getType().getName().copy().formatted(Formatting.GOLD);

        if (!entity.getDisplayName().getString().equals(title.getString())) {
            title = entity.getDisplayName().copy().append(" (").append(title).append(")");
        }

        if (entity instanceof LivingEntity livingEntity) {
            double health = livingEntity.getHealth();
            double maxHealth = livingEntity.getMaxHealth();
            lines.add(Text.literal("Health: " + String.format("%.1f", health) + " / " + String.format("%.1f", maxHealth)));

            int armor = livingEntity.getArmor();
            if (armor > 0) lines.add(Text.literal("Armor: " + armor));
        }

        updateScrewdriverData(player, hand, title, lines);
        return ActionResult.SUCCESS;
    }

    private void updateScrewdriverData(PlayerEntity player, Hand hand, Text title, List<Text> lines) {
        ItemStack screwdriverItemStack = player.getStackInHand(hand);
        if (!Screwdriver.checkItemStackIsScrewdriver(screwdriverItemStack)) return;

        NbtCompound tag = Screwdriver.getData(screwdriverItemStack);
        tag.putString("title", Text.Serializer.toJson(title));
        tag.putLong("time", player.world.getTime());

        AtomicInteger i = new AtomicInteger();
        NbtCompound linesTag = new NbtCompound();
        for (Text line : lines) linesTag.putString(String.format("%1$" + 5 + "s", i.incrementAndGet()).replace(' ', '0'), Text.Serializer.toJson(line));
        tag.put("linesTag", linesTag);

        PacketHelper.sendToServer(
            ScrewdriverRemoteCallablePackets.class,
            "updateScrewdriverData",
            screwdriverItemStack, hand == Hand.MAIN_HAND
        );
    }
}

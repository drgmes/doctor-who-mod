package net.drgmes.dwm.common.sonicdevice.modes.scan;

import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.common.sonicdevice.modes.BaseSonicDeviceMode;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SonicDeviceScanMode extends BaseSonicDeviceMode {
    public static final SonicDeviceScanMode INSTANCE = new SonicDeviceScanMode();

    @Override
    public ActionResult interactWithBlockNative(World world, PlayerEntity player, EquipmentSlot slot, BlockHitResult hitResult) {
        if (!checkIsValidHitBlock(world.getBlockState(hitResult.getBlockPos()))) return ActionResult.PASS;
        if (world.isClient) return ActionResult.SUCCESS;

        BlockPos blockPos = hitResult.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        BlockEntity blockEntity = world.getBlockEntity(blockPos);

        List<Text> lines = new ArrayList<>();
        MutableText title = blockState.getBlock().getName().copy().formatted(Formatting.AQUA);

        if (player.isSneaking()) {
            String modid = Registries.BLOCK.getId(blockState.getBlock()).toString();
            lines.add(Text.translatable("[%s]", modid).formatted(Formatting.GRAY));
        }

        if (blockEntity instanceof JukeboxBlockEntity jukeboxBlockEntity) {
            ItemStack itemStack = jukeboxBlockEntity.getStack();
            if (!itemStack.isEmpty()) {
                MusicDiscItem record = (MusicDiscItem) itemStack.getItem();
                lines.add(record.getDescription());
            }
        }

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

        if (blockState.getBlock() instanceof CropBlock cropBlock) {
            lines.add(Text.literal("Age: " + blockState.get(CropBlock.AGE) + " / " + cropBlock.getMaxAge()));
        }

        if (blockState.getBlock() instanceof BeehiveBlock) {
            lines.add(Text.literal("Honey: " + blockState.get(Properties.HONEY_LEVEL) + " / " + BeehiveBlock.FULL_HONEY_LEVEL));
        }

        if (blockState.contains(Properties.POWER)) {
            lines.add(Text.literal("Power: " + blockState.get(Properties.POWER)));
        }

        if (blockState.contains(Properties.STAGE)) {
            lines.add(Text.literal("Stage: " + blockState.get(Properties.STAGE)));
        }

        if (blockState.contains(Properties.CAN_SUMMON)) {
            lines.add(Text.literal("Can Summon: " + (blockState.get(Properties.CAN_SUMMON) ? "Yes" : "No")));
        }

        updateSonicDeviceData((ServerPlayerEntity) player, slot, title, lines);
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult interactWithEntityNative(World world, PlayerEntity player, EquipmentSlot slot, EntityHitResult hitResult) {
        if (!checkIsValidHitEntity(hitResult.getEntity())) return ActionResult.PASS;
        if (world.isClient) return ActionResult.SUCCESS;

        Entity entity = hitResult.getEntity();
        List<Text> lines = new ArrayList<>();
        MutableText title = entity.getType().getName().copy().formatted(Formatting.GOLD);

        if (!entity.getDisplayName().getString().equals(title.getString())) {
            title = entity.getDisplayName().copy().append(" (").append(title).append(")");
        }

        if (player.isSneaking()) {
            String modid = Registries.ENTITY_TYPE.getId(entity.getType()).toString();
            lines.add(Text.translatable("[%s]", modid).formatted(Formatting.GRAY));
        }

        if (entity instanceof LivingEntity livingEntity) {
            double health = livingEntity.getHealth();
            double maxHealth = livingEntity.getMaxHealth();
            lines.add(Text.literal("Health: " + String.format("%.1f", health) + " / " + String.format("%.1f", maxHealth)));

            int armor = livingEntity.getArmor();
            if (armor > 0) lines.add(Text.literal("Armor: " + armor));
        }

        updateSonicDeviceData((ServerPlayerEntity) player, slot, title, lines);
        return ActionResult.SUCCESS;
    }

    private void updateSonicDeviceData(ServerPlayerEntity player, EquipmentSlot slot, Text title, List<Text> lines) {
        ItemStack sonicDeviceItemStack = player.getEquippedStack(slot);
        if (!SonicDevice.checkItemStackIsSonicDevice(sonicDeviceItemStack)) return;

        NbtCompound tag = SonicDevice.getData(sonicDeviceItemStack);
        tag.putString("title", Text.Serialization.toJsonString(title));
        tag.putLong("time", player.getServerWorld().getTime());

        AtomicInteger i = new AtomicInteger();
        NbtCompound linesTag = new NbtCompound();
        for (Text line : lines) linesTag.putString(String.format("%1$" + 5 + "s", i.incrementAndGet()).replace(' ', '0'), Text.Serialization.toJsonString(line));
        tag.put("linesTag", linesTag);

        player.equipStack(slot, sonicDeviceItemStack);
    }
}

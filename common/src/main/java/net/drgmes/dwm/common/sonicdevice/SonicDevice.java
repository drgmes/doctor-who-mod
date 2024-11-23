package net.drgmes.dwm.common.sonicdevice;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicdevice.modes.BaseSonicDeviceMode;
import net.drgmes.dwm.enums.SonicDeviceMode;
import net.drgmes.dwm.items.sonicdevices.ISonicDeviceItem;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.PlayerHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class SonicDevice {
    public static boolean checkItemStackIsSonicDevice(ItemStack itemStack) {
        return itemStack.getItem() instanceof ISonicDeviceItem;
    }

    public static ActionResult interact(World world, PlayerEntity player, EquipmentSlot slot, boolean isAlternativeAction) {
        ActionResult result = ActionResult.PASS;
        ItemStack itemStack = player.getEquippedStack(slot);
        if (!checkItemStackIsSonicDevice(itemStack)) return ActionResult.FAIL;
        if (player.getItemCooldownManager().isCoolingDown(itemStack.getItem())) return ActionResult.CONSUME;

        BaseSonicDeviceMode mode = getInteractionMode(itemStack).getInstance();
        HitResult hitResult = PlayerHelper.pick(player, getInteractionDistance(itemStack));
        if (hitResult == null || hitResult.getType() == HitResult.Type.MISS) return ActionResult.PASS;

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            if (!(result = mode.interactWithBlock(world, player, slot, (BlockHitResult) hitResult, isAlternativeAction)).shouldSwingHand()) {
                if (isAlternativeAction) result = mode.interactWithBlockAlternative(world, player, slot, (BlockHitResult) hitResult);
                else result = mode.interactWithBlockNative(world, player, slot, (BlockHitResult) hitResult);
            }
        }
        else if (hitResult.getType() == HitResult.Type.ENTITY) {
            if (!(result = mode.interactWithEntity(world, player, slot, (EntityHitResult) hitResult, isAlternativeAction)).shouldSwingHand()) {
                if (isAlternativeAction) result = mode.interactWithEntityAlternative(world, player, slot, (EntityHitResult) hitResult);
                else result = mode.interactWithEntityNative(world, player, slot, (EntityHitResult) hitResult);
            }
        }

        if (result.shouldSwingHand()) {
            Vec3d pos = hitResult.getPos();
            player.getItemCooldownManager().set(itemStack.getItem(), DWM.TIMINGS.SONIC_DEVICE_TIMEOUT);
            mode.generateVibration(world, player, new BlockPos((int) pos.x, (int) pos.y, (int) pos.z));
        }

        return result;
    }

    public static NbtCompound getData(ItemStack itemStack) {
        if (!checkItemStackIsSonicDevice(itemStack)) return new NbtCompound();
        NbtCompound tag = CommonHelper.getItemStackData(itemStack).copyNbt();
        return tag.contains("sonicDeviceData") ? tag.getCompound("sonicDeviceData") : new NbtCompound();
    }

    public static void updateData(ItemStack itemStack, Consumer<NbtCompound> consumer) {
        if (!checkItemStackIsSonicDevice(itemStack)) return;

        CommonHelper.updateItemStackData(itemStack, (tag) -> {
            NbtCompound dataTag = tag.contains("sonicDeviceData") ? tag.getCompound("sonicDeviceData") : new NbtCompound();
            consumer.accept(dataTag);
            tag.put("sonicDeviceData", dataTag);
        });
    }

    public static void setInteractionMode(ItemStack itemStack, SonicDeviceMode mode) {
        SonicDevice.updateData(itemStack, (tag) -> {
            tag.putString("prevMode", getInteractionMode(itemStack).name());
            tag.putString("mode", mode.name());
        });
    }

    public static SonicDeviceMode getInteractionMode(ItemStack itemStack) {
        SonicDeviceMode mode = null;
        NbtCompound tag = getData(itemStack);
        if (tag.contains("mode")) mode = SonicDeviceMode.valueOf(tag.getString("mode"));

        return mode != null ? mode : SonicDeviceMode.SCAN;
    }

    public static void setTardisId(ItemStack itemStack, World world) {
        if (!TardisHelper.isTardisDimension(world)) return;

        SonicDevice.updateData(itemStack, (tag) -> {
            tag.putString("tardisId", DimensionHelper.getWorldId(world));
        });
    }

    public static String getTardisId(ItemStack itemStack) {
        NbtCompound tag = getData(itemStack);
        return !tag.contains("tardisId") ? "" : tag.getString("tardisId");
    }

    public static double getInteractionDistance(ItemStack itemStack) {
        NbtCompound tag = getData(itemStack);
        return !tag.contains("interactionDistance") ? 100D : tag.getDouble("interactionDistance");
    }
}

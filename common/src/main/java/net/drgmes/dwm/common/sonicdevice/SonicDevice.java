package net.drgmes.dwm.common.sonicdevice;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicdevice.modes.BaseSonicDeviceMode;
import net.drgmes.dwm.common.sonicdevice.modes.scan.SonicDeviceScanMode;
import net.drgmes.dwm.common.sonicdevice.modes.setting.SonicDeviceSettingMode;
import net.drgmes.dwm.common.sonicdevice.modes.tardis.SonicDeviceTardisMode;
import net.drgmes.dwm.items.sonicdevices.ISonicDeviceItem;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.PlayerHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SonicDevice {
    public enum EMode {
        SCAN(SonicDeviceScanMode.INSTANCE, DWM.TEXTS.SONIC_DEVICE_MODE_SCAN, DWM.TEXTS.SONIC_DEVICE_MODE_SCAN_DESCRIPTION),
        SETTING(SonicDeviceSettingMode.INSTANCE, DWM.TEXTS.SONIC_DEVICE_MODE_SETTING, DWM.TEXTS.SONIC_DEVICE_MODE_SETTING_DESCRIPTION),
        TARDIS_RELOCATION(SonicDeviceTardisMode.INSTANCE, DWM.TEXTS.SONIC_DEVICE_MODE_TARDIS_RELOCATION, DWM.TEXTS.SONIC_DEVICE_MODE_TARDIS_RELOCATION_DESCRIPTION);

        private final BaseSonicDeviceMode mode;
        private final Text title;
        private final Text description;

        EMode(BaseSonicDeviceMode mode, Text title, Text description) {
            this.mode = mode;
            this.title = title;
            this.description = description;
        }

        public BaseSonicDeviceMode getInstance() {
            return this.mode;
        }

        public Text getTitle() {
            return this.title;
        }

        public Text getDescription() {
            return this.description;
        }
    }

    public static boolean checkItemStackIsSonicDevice(ItemStack itemStack) {
        return itemStack.getItem() instanceof ISonicDeviceItem;
    }

    public static ActionResult interact(World world, PlayerEntity player, EquipmentSlot slot, boolean isAlternativeAction) {
        ActionResult wasUsed = ActionResult.PASS;
        ItemStack sonicDeviceItemStack = player.getEquippedStack(slot);
        if (!checkItemStackIsSonicDevice(sonicDeviceItemStack)) return ActionResult.FAIL;
        if (player.getItemCooldownManager().isCoolingDown(sonicDeviceItemStack.getItem())) return ActionResult.CONSUME;

        BaseSonicDeviceMode mode = getInteractionMode(sonicDeviceItemStack).getInstance();
        HitResult hitResult = PlayerHelper.pick(player, getInteractionDistance(sonicDeviceItemStack));
        if (hitResult == null || hitResult.getType() == HitResult.Type.MISS) return ActionResult.PASS;

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            if (!(wasUsed = mode.interactWithBlock(world, player, slot, (BlockHitResult) hitResult, isAlternativeAction)).shouldSwingHand()) {
                if (isAlternativeAction) wasUsed = mode.interactWithBlockAlternative(world, player, slot, (BlockHitResult) hitResult);
                else wasUsed = mode.interactWithBlockNative(world, player, slot, (BlockHitResult) hitResult);
            }
        }
        else if (hitResult.getType() == HitResult.Type.ENTITY) {
            if (!(wasUsed = mode.interactWithEntity(world, player, slot, (EntityHitResult) hitResult, isAlternativeAction)).shouldSwingHand()) {
                if (isAlternativeAction) wasUsed = mode.interactWithEntityAlternative(world, player, slot, (EntityHitResult) hitResult);
                else wasUsed = mode.interactWithEntityNative(world, player, slot, (EntityHitResult) hitResult);
            }
        }

        if (wasUsed.shouldSwingHand()) {
            Vec3d pos = hitResult.getPos();
            player.getItemCooldownManager().set(sonicDeviceItemStack.getItem(), DWM.TIMINGS.SONIC_DEVICE_TIMEOUT);
            mode.generateVibration(world, player, new BlockPos((int) pos.x, (int) pos.y, (int) pos.z));
        }

        return wasUsed;
    }

    public static NbtCompound getData(ItemStack sonicDeviceItemStack) {
        return sonicDeviceItemStack.getOrCreateSubNbt("sonicDeviceData");
    }

    public static void setInteractionMode(ItemStack sonicDeviceItemStack, EMode mode) {
        if (!checkItemStackIsSonicDevice(sonicDeviceItemStack)) return;
        getData(sonicDeviceItemStack).putString("prevMode", getInteractionMode(sonicDeviceItemStack).name());
        getData(sonicDeviceItemStack).putString("mode", mode.name());
    }

    public static EMode getInteractionMode(ItemStack sonicDeviceItemStack) {
        if (!checkItemStackIsSonicDevice(sonicDeviceItemStack)) return EMode.SCAN;

        EMode mode = null;
        NbtCompound tag = getData(sonicDeviceItemStack);
        if (tag.contains("mode")) mode = EMode.valueOf(tag.getString("mode"));

        return mode != null ? mode : EMode.SCAN;
    }

    public static void setTardisId(ItemStack sonicDeviceItemStack, World world) {
        if (!checkItemStackIsSonicDevice(sonicDeviceItemStack)) return;
        if (!TardisHelper.isTardisDimension(world)) return;
        getData(sonicDeviceItemStack).putString("tardisId", DimensionHelper.getWorldId(world));
    }

    public static String getTardisId(ItemStack sonicDeviceItemStack) {
        if (!checkItemStackIsSonicDevice(sonicDeviceItemStack)) return "";
        NbtCompound tag = getData(sonicDeviceItemStack);
        return !tag.contains("tardisId") ? "" : tag.getString("tardisId");
    }

    public static double getInteractionDistance(ItemStack sonicDeviceItemStack) {
        NbtCompound tag = getData(sonicDeviceItemStack);
        return !tag.contains("interactionDistance") ? 100D : tag.getDouble("interactionDistance");
    }
}

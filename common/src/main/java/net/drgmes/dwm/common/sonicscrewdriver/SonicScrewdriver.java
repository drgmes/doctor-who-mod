package net.drgmes.dwm.common.sonicscrewdriver;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicscrewdriver.modes.BaseSonicScrewdriverMode;
import net.drgmes.dwm.common.sonicscrewdriver.modes.scan.SonicScrewdriverScanMode;
import net.drgmes.dwm.common.sonicscrewdriver.modes.setting.SonicScrewdriverSettingMode;
import net.drgmes.dwm.common.sonicscrewdriver.modes.tardis.SonicScrewdriverTardisMode;
import net.drgmes.dwm.items.sonicscrewdriver.SonicScrewdriverItem;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.PlayerHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SonicScrewdriver {
    public enum EMode {
        SCAN(SonicScrewdriverScanMode.INSTANCE, DWM.TEXTS.SONIC_SCREWDRIVER_MODE_SCAN, DWM.TEXTS.SONIC_SCREWDRIVER_MODE_SCAN_DESCRIPTION),
        SETTING(SonicScrewdriverSettingMode.INSTANCE, DWM.TEXTS.SONIC_SCREWDRIVER_MODE_SETTING, DWM.TEXTS.SONIC_SCREWDRIVER_MODE_SETTING_DESCRIPTION),
        TARDIS_RELOCATION(SonicScrewdriverTardisMode.INSTANCE, DWM.TEXTS.SONIC_SCREWDRIVER_MODE_TARDIS_RELOCATION, DWM.TEXTS.SONIC_SCREWDRIVER_MODE_TARDIS_RELOCATION_DESCRIPTION);

        private final BaseSonicScrewdriverMode mode;
        private final Text title;
        private final Text description;

        EMode(BaseSonicScrewdriverMode mode, Text title, Text description) {
            this.mode = mode;
            this.title = title;
            this.description = description;
        }

        public BaseSonicScrewdriverMode getInstance() {
            return this.mode;
        }

        public Text getTitle() {
            return this.title;
        }

        public Text getDescription() {
            return this.description;
        }
    }

    public static boolean checkItemStackIsSonicScrewdriver(ItemStack itemStack) {
        return itemStack.getItem() instanceof SonicScrewdriverItem;
    }

    public static ActionResult interact(World world, PlayerEntity player, Hand hand, boolean isAlternativeAction) {
        ActionResult wasUsed = ActionResult.PASS;
        ItemStack sonicScrewdriverItemStack = player.getStackInHand(hand);
        if (!checkItemStackIsSonicScrewdriver(sonicScrewdriverItemStack)) return ActionResult.FAIL;
        if (player.getItemCooldownManager().isCoolingDown(sonicScrewdriverItemStack.getItem())) return ActionResult.CONSUME;

        BaseSonicScrewdriverMode mode = getInteractionMode(sonicScrewdriverItemStack).getInstance();
        HitResult hitResult = PlayerHelper.pick(player, getInteractionDistance(sonicScrewdriverItemStack));
        if (hitResult == null || hitResult.getType() == HitResult.Type.MISS) return ActionResult.PASS;

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            if (!(wasUsed = mode.interactWithBlock(world, player, hand, (BlockHitResult) hitResult, isAlternativeAction)).shouldSwingHand()) {
                if (isAlternativeAction) wasUsed = mode.interactWithBlockAlternative(world, player, hand, (BlockHitResult) hitResult);
                else wasUsed = mode.interactWithBlockNative(world, player, hand, (BlockHitResult) hitResult);
            }
        }
        else if (hitResult.getType() == HitResult.Type.ENTITY) {
            if (!(wasUsed = mode.interactWithEntity(world, player, hand, (EntityHitResult) hitResult, isAlternativeAction)).shouldSwingHand()) {
                if (isAlternativeAction) wasUsed = mode.interactWithEntityAlternative(world, player, hand, (EntityHitResult) hitResult);
                else wasUsed = mode.interactWithEntityNative(world, player, hand, (EntityHitResult) hitResult);
            }
        }

        if (wasUsed.shouldSwingHand()) {
            Vec3d pos = hitResult.getPos();
            player.getItemCooldownManager().set(sonicScrewdriverItemStack.getItem(), DWM.TIMINGS.SONIC_SCREWDRIVER_TIMEOUT);
            mode.generateVibration(world, player, new BlockPos((int) pos.x, (int) pos.y, (int) pos.z));
        }

        return wasUsed;
    }

    public static NbtCompound getData(ItemStack sonicScrewdriverItemStack) {
        return sonicScrewdriverItemStack.getOrCreateSubNbt("sonicScrewdriverData");
    }

    public static void setInteractionMode(ItemStack sonicScrewdriverItemStack, EMode mode) {
        if (!checkItemStackIsSonicScrewdriver(sonicScrewdriverItemStack)) return;
        getData(sonicScrewdriverItemStack).putString("prevMode", getInteractionMode(sonicScrewdriverItemStack).name());
        getData(sonicScrewdriverItemStack).putString("mode", mode.name());
    }

    public static EMode getInteractionMode(ItemStack sonicScrewdriverItemStack) {
        if (!checkItemStackIsSonicScrewdriver(sonicScrewdriverItemStack)) return EMode.SCAN;

        EMode mode = null;
        NbtCompound tag = getData(sonicScrewdriverItemStack);
        if (tag.contains("mode")) mode = EMode.valueOf(tag.getString("mode"));

        return mode != null ? mode : EMode.SCAN;
    }

    public static void setTardisId(ItemStack sonicScrewdriverItemStack, World world) {
        if (!checkItemStackIsSonicScrewdriver(sonicScrewdriverItemStack)) return;
        if (!TardisHelper.isTardisDimension(world)) return;
        getData(sonicScrewdriverItemStack).putString("tardisId", DimensionHelper.getWorldId(world));
    }

    public static String getTardisId(ItemStack sonicScrewdriverItemStack) {
        if (!checkItemStackIsSonicScrewdriver(sonicScrewdriverItemStack)) return "";
        NbtCompound tag = getData(sonicScrewdriverItemStack);
        return !tag.contains("tardisId") ? "" : tag.getString("tardisId");
    }

    public static double getInteractionDistance(ItemStack sonicScrewdriverItemStack) {
        NbtCompound tag = getData(sonicScrewdriverItemStack);
        return !tag.contains("interactionDistance") ? 100D : tag.getDouble("interactionDistance");
    }
}

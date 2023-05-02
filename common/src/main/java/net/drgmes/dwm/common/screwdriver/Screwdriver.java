package net.drgmes.dwm.common.screwdriver;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.screwdriver.modes.BaseScrewdriverMode;
import net.drgmes.dwm.common.screwdriver.modes.scan.ScrewdriverScanMode;
import net.drgmes.dwm.common.screwdriver.modes.setting.ScrewdriverSettingMode;
import net.drgmes.dwm.common.screwdriver.modes.tardis.ScrewdriverTardisMode;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItem;
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

public class Screwdriver {
    public enum EScrewdriverMode {
        SCAN(ScrewdriverScanMode.INSTANCE, DWM.TEXTS.SCREWDRIVER_MODE_SCAN, DWM.TEXTS.SCREWDRIVER_MODE_SCAN_DESCRIPTION),
        SETTING(ScrewdriverSettingMode.INSTANCE, DWM.TEXTS.SCREWDRIVER_MODE_SETTING, DWM.TEXTS.SCREWDRIVER_MODE_SETTING_DESCRIPTION),
        TARDIS_RELOCATION(ScrewdriverTardisMode.INSTANCE, DWM.TEXTS.SCREWDRIVER_MODE_TARDIS_RELOCATION, DWM.TEXTS.SCREWDRIVER_MODE_TARDIS_RELOCATION_DESCRIPTION);

        private final BaseScrewdriverMode mode;
        private final Text title;
        private final Text description;

        EScrewdriverMode(BaseScrewdriverMode mode, Text title, Text description) {
            this.mode = mode;
            this.title = title;
            this.description = description;
        }

        public BaseScrewdriverMode getInstance() {
            return this.mode;
        }

        public Text getTitle() {
            return this.title;
        }

        public Text getDescription() {
            return this.description;
        }
    }

    public static boolean checkItemStackIsScrewdriver(ItemStack itemStack) {
        return itemStack.getItem() instanceof ScrewdriverItem;
    }

    public static ActionResult interact(World world, PlayerEntity player, Hand hand, boolean isAlternativeAction) {
        ActionResult wasUsed = ActionResult.PASS;
        ItemStack screwdriverItemStack = player.getStackInHand(hand);
        if (!checkItemStackIsScrewdriver(screwdriverItemStack)) return ActionResult.FAIL;
        if (player.getItemCooldownManager().isCoolingDown(screwdriverItemStack.getItem())) return ActionResult.CONSUME;

        BaseScrewdriverMode mode = getInteractionMode(screwdriverItemStack).getInstance();
        HitResult hitResult = PlayerHelper.pick(player, getInteractionDistance(screwdriverItemStack));
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
            player.getItemCooldownManager().set(screwdriverItemStack.getItem(), DWM.TIMINGS.SCREWDRIVER_TIMEOUT);
            mode.generateVibration(world, player, new BlockPos((int) pos.x, (int) pos.y, (int) pos.z));
        }

        return wasUsed;
    }

    public static NbtCompound getData(ItemStack screwdriverItemStack) {
        return screwdriverItemStack.getOrCreateSubNbt("screwdriverData");
    }

    public static void setInteractionMode(ItemStack screwdriverItemStack, EScrewdriverMode mode) {
        if (!checkItemStackIsScrewdriver(screwdriverItemStack)) return;
        getData(screwdriverItemStack).putString("mode", mode.name());
    }

    public static EScrewdriverMode getInteractionMode(ItemStack screwdriverItemStack) {
        if (!checkItemStackIsScrewdriver(screwdriverItemStack)) return EScrewdriverMode.SCAN;

        EScrewdriverMode mode = null;
        NbtCompound tag = getData(screwdriverItemStack);
        if (tag.contains("mode")) mode = EScrewdriverMode.valueOf(tag.getString("mode"));

        return mode != null ? mode : EScrewdriverMode.SCAN;
    }

    public static void setTardisId(ItemStack screwdriverItemStack, World world) {
        if (!checkItemStackIsScrewdriver(screwdriverItemStack)) return;
        if (!TardisHelper.isTardisDimension(world)) return;
        getData(screwdriverItemStack).putString("tardisId", DimensionHelper.getWorldId(world));
    }

    public static String getTardisId(ItemStack screwdriverItemStack) {
        if (!checkItemStackIsScrewdriver(screwdriverItemStack)) return "";
        NbtCompound tag = getData(screwdriverItemStack);
        return !tag.contains("tardisId") ? "" : tag.getString("tardisId");
    }

    public static double getInteractionDistance(ItemStack screwdriverItemStack) {
        NbtCompound tag = getData(screwdriverItemStack);
        return !tag.contains("interactionDistance") ? 100D : tag.getDouble("interactionDistance");
    }
}

package net.drgmes.dwm.common.screwdriver;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.screwdriver.modes.BaseScrewdriverMode;
import net.drgmes.dwm.common.screwdriver.modes.scan.ScrewdriverScanMode;
import net.drgmes.dwm.common.screwdriver.modes.setting.ScrewdriverSettingMode;
import net.drgmes.dwm.common.screwdriver.modes.tardis.ScrewdriverTardisMode;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItem;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.PlayerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class Screwdriver {
    public static boolean interact(Level level, Player player, InteractionHand hand, boolean isAlternativeAction) {
        boolean wasUsed = false;
        ItemStack itemStack = player.getItemInHand(hand);
        if (!(itemStack.getItem() instanceof ScrewdriverItem screwdriverItem)) return false;
        if (player.getCooldowns().isOnCooldown(screwdriverItem)) return false;

        BaseScrewdriverMode mode = getInteractionMode(itemStack).getInstance();
        HitResult hitResult = PlayerHelper.pick(player, getInteractionDistance(itemStack));
        if (hitResult == null || hitResult.getType() == HitResult.Type.MISS) return false;

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            if (!(wasUsed = mode.interactWithBlock(level, player, hand, (BlockHitResult) hitResult, isAlternativeAction))) {
                if (isAlternativeAction)
                    wasUsed = mode.interactWithBlockAlternative(level, player, hand, (BlockHitResult) hitResult);
                else wasUsed = mode.interactWithBlockNative(level, player, hand, (BlockHitResult) hitResult);
            }
        } else if (hitResult.getType() == HitResult.Type.ENTITY) {
            if (!(wasUsed = mode.interactWithEntity(level, player, hand, (EntityHitResult) hitResult, isAlternativeAction))) {
                if (isAlternativeAction)
                    wasUsed = mode.interactWithEntityAlternative(level, player, hand, (EntityHitResult) hitResult);
                else wasUsed = mode.interactWithEntityNative(level, player, hand, (EntityHitResult) hitResult);
            }
        }

        if (wasUsed) {
            player.getCooldowns().addCooldown(screwdriverItem, getInteractionCooldownTime(itemStack));
            mode.generateVibration(level, player, new BlockPos(hitResult.getLocation()));
        }

        return wasUsed;
    }

    public static int getInteractionCooldownTime(ItemStack itemStack) {
        return 4;
    }

    public static double getInteractionDistance(ItemStack itemStack) {
        return 100D;
    }

    public static CompoundTag getData(ItemStack itemStack) {
        return itemStack.getOrCreateTagElement("screwdriverData");
    }

    public static ScrewdriverMode getInteractionMode(ItemStack itemStack) {
        ScrewdriverMode mode = null;
        CompoundTag tag = getData(itemStack);
        if (tag.contains("mode")) mode = ScrewdriverMode.valueOf(tag.getString("mode"));

        return mode != null ? mode : ScrewdriverMode.SCAN;
    }

    public static ScrewdriverMode setInteractionMode(ItemStack itemStack, ScrewdriverMode mode) {
        getData(itemStack).putString("mode", mode.name());
        return mode;
    }

    public static String getTardisUUID(ItemStack itemStack) {
        CompoundTag tag = getData(itemStack);
        return !tag.contains("tardisLevelUUID") ? "" : tag.getString("tardisLevelUUID");
    }

    public static void assingTardisUUID(ItemStack itemStack, Level level) {
        if (!DimensionHelper.isTardisDimension(level)) return;
        Screwdriver.getData(itemStack).putString("tardisLevelUUID", level.dimension().location().getPath());
    }

    public enum ScrewdriverMode {
        SCAN(ScrewdriverScanMode.INSTANCE, DWM.TEXTS.SCREWDRIVER_MODE_SCAN),
        SETTING(ScrewdriverSettingMode.INSTANCE, DWM.TEXTS.SCREWDRIVER_MODE_SETTING),
        TARDIS_RELOCATION(ScrewdriverTardisMode.INSTANCE, DWM.TEXTS.SCREWDRIVER_MODE_TARDIS_RELOCATION);

        private final BaseScrewdriverMode mode;
        private final Component title;

        ScrewdriverMode(BaseScrewdriverMode mode, Component title) {
            this.mode = mode;
            this.title = title;
        }

        public BaseScrewdriverMode getInstance() {
            return this.mode;
        }

        public Component getTitle() {
            return this.title;
        }

        public ScrewdriverMode next() {
            ScrewdriverMode[] values = ScrewdriverMode.values();
            return values[(this.ordinal() + 1) % values.length];
        }
    }
}

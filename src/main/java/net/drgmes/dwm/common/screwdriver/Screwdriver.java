package net.drgmes.dwm.common.screwdriver;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.screwdriver.modes.BaseScrewdriverMode;
import net.drgmes.dwm.common.screwdriver.modes.ScrewdriverScanMode;
import net.drgmes.dwm.common.screwdriver.modes.ScrewdriverSettingMode;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItem;
import net.drgmes.dwm.utils.helpers.PlayerHelper;
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
    public static enum ScrewdriverMode {
        SCAN(ScrewdriverScanMode.INSTANCE, DWM.TEXTS.SCREWDRIVER_MODE_SCAN),
        SETTING(ScrewdriverSettingMode.INSTANCE, DWM.TEXTS.SCREWDRIVER_MODE_SETTING);

        private BaseScrewdriverMode mode;
        private Component title;

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

    public static boolean interact(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!(itemStack.getItem() instanceof ScrewdriverItem)) return false;

        BaseScrewdriverMode mode = getInteractionMode(itemStack).getInstance();
        HitResult hitResult = PlayerHelper.pick(player, getInteractionDistance(itemStack));

        if (hitResult == null || hitResult.getType() == HitResult.Type.MISS) {
            return false;
        }

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return mode.interactWithBlock(level, player, hand, (BlockHitResult) hitResult);
        }

        if (hitResult.getType() == HitResult.Type.ENTITY) {
            return mode.interactWithEntity(level, player, hand, (EntityHitResult) hitResult);
        }

        return false;
    }

    public static double getInteractionDistance(ItemStack itemStack) {
        return 100D;
    }

    public static ScrewdriverMode getInteractionMode(ItemStack itemStack) {
        ScrewdriverMode mode = null;
        CompoundTag tag = getCompoundTag(itemStack);
        if (tag.contains("mode")) mode = ScrewdriverMode.valueOf(tag.getString("mode"));

        return mode != null ? mode : ScrewdriverMode.SCAN;
    }

    public static ScrewdriverMode setInteractionMode(ItemStack itemStack, ScrewdriverMode mode) {
        getCompoundTag(itemStack).putString("mode", mode.name());
        return mode;
    }

    private static CompoundTag getCompoundTag(ItemStack itemStack) {
        return itemStack.getOrCreateTagElement("screwdriverData");
    }
}

package net.drgmes.dwm.items.screwdriver;

import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.items.screwdriver.screens.ScrewdriverInterfaceMainScreen;
import net.drgmes.dwm.setup.ModKeys;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class ScrewdriverItem extends Item {
    public ScrewdriverItem(Item.Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.empty());

        MutableComponent mode = Screwdriver.getInteractionMode(itemStack).getTitle().copy();
        MutableComponent modeText = Component.translatable("title.dwm.screwdriver.mode", mode.setStyle(mode.getStyle().withColor(ChatFormatting.GOLD)));
        list.add(modeText.setStyle(mode.getStyle().withColor(ChatFormatting.GRAY)));

        String tardisLevelUUID = Screwdriver.getTardisUUID(itemStack);
        if (tardisLevelUUID != "") {
            MutableComponent tardis = Component.literal(tardisLevelUUID.substring(0, 8));
            MutableComponent tardisText = Component.translatable("title.dwm.tardis_uuid", tardis.setStyle(mode.getStyle().withColor(ChatFormatting.GOLD)));
            list.add(tardisText.setStyle(mode.getStyle().withColor(ChatFormatting.GRAY)));
        }

        super.appendHoverText(itemStack, level, list, tooltipFlag);
    }

    @Override
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        return 0;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotIdx, boolean flag) {
        Minecraft mc = Minecraft.getInstance();
        if (!entity.level.isClientSide || mc.screen != null || !(entity instanceof Player player)) return;

        if (ModKeys.SCREWDRIVER_SETTINGS.isDown()) {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).equals(itemStack, false)) {
                mc.setScreen(new ScrewdriverInterfaceMainScreen(itemStack, true));
                return;
            }

            if (player.getItemInHand(InteractionHand.OFF_HAND).equals(itemStack, false)) {
                mc.setScreen(new ScrewdriverInterfaceMainScreen(itemStack, false));
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return this.useScrewdriver(level, player, hand, false);
    }

    public InteractionResultHolder<ItemStack> useScrewdriver(Level level, Player player, InteractionHand hand, boolean isAlternativeAction) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (Screwdriver.interact(level, player, hand, isAlternativeAction)) {
            level.gameEvent(player, GameEvent.ITEM_INTERACT_FINISH, player.blockPosition());
            ModSounds.playScrewdriverMainSound(level, player.blockPosition());
            return InteractionResultHolder.success(itemStack);
        }

        return InteractionResultHolder.pass(itemStack);
    }
}

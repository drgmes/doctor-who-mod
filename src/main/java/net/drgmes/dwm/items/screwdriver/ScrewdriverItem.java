package net.drgmes.dwm.items.screwdriver;

import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.items.screwdriver.screens.ScrewdriverInterfaceMainScreen;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ScrewdriverItem extends Item {
    public ScrewdriverItem(Item.Properties props) {
        super(props);
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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return this.useScrewdriver(level, player, hand, false);
    }

    public InteractionResultHolder<ItemStack> useScrewdriver(Level level, Player player, InteractionHand hand, boolean isAlternativeAction) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (player.isShiftKeyDown() && !isAlternativeAction) {
            if (level.isClientSide) {
                Minecraft mc = Minecraft.getInstance();
                mc.setScreen(new ScrewdriverInterfaceMainScreen(itemStack, hand == InteractionHand.MAIN_HAND));
            }

            return InteractionResultHolder.consume(itemStack);
        }

        if (Screwdriver.interact(level, player, hand, isAlternativeAction)) {
            ModSounds.playScrewdriverMainSound(level, player.blockPosition());
            return InteractionResultHolder.success(itemStack);
        }

        return InteractionResultHolder.pass(itemStack);
    }
}

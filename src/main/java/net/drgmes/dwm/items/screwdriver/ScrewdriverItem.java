package net.drgmes.dwm.items.screwdriver;

import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.PlayerHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ScrewdriverItem extends Item {
    public ScrewdriverItem(Item.Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (Screwdriver.interact(level, player, hand, PlayerHelper.pick(player, 40D))) {
            level.playSound(null, player.blockPosition(), ModSounds.SCREWDRIVER_MAIN.get(), SoundSource.PLAYERS, 0.25F, 1F);
            return InteractionResultHolder.success(itemStack);
        }

        return InteractionResultHolder.pass(itemStack);
    }
}

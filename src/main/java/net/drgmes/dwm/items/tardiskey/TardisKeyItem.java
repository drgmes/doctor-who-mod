package net.drgmes.dwm.items.tardiskey;

import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TardisKeyItem extends Item {
    public TardisKeyItem(Item.Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        CompoundTag tag = itemStack.getOrCreateTag();
        InteractionResultHolder<ItemStack> pass = InteractionResultHolder.pass(itemStack);

        if (level.isClientSide) return pass;
        if (!tag.contains("tardisDimUUID")) return pass;

        ServerLevel tardisLevel = DimensionHelper.getLevel(level.getServer(), tag.getString("tardisDimUUID"));
        if (tardisLevel != null) {
            tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
                if (!tardis.isValid()) return;
                if (tardis.isDoorsLocked()) return;
                if (tardis.getLevel() != level && tardis.getCurrentExteriorDimension() != level.dimension()) return;
                if (tardis.getLevel() != level && tardis.getCurrentExteriorPosition().distManhattan(player.blockPosition()) > 10) return;

                if (tardis.setDoorsOpenState(!tardis.isDoorsOpened())) {
                    tardis.updateConsoleTiles();
                }
            });

            return InteractionResultHolder.consume(itemStack);
        }

        return pass;
    }
}

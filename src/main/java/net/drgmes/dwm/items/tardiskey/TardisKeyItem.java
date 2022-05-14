package net.drgmes.dwm.items.tardiskey;

import java.util.List;

import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class TardisKeyItem extends Item {
    public TardisKeyItem(Item.Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag) {
        CompoundTag tag = itemStack.getOrCreateTag();

        if (tag.contains("tardisDimUUID")) {
            MutableComponent tardis = new TextComponent(tag.getString("tardisDimUUID").substring(0, 8));
            MutableComponent tardisText = new TranslatableComponent("title.dwm.tardis_uuid", tardis.setStyle(tardis.getStyle().withColor(ChatFormatting.GOLD)));
            list.add(tardisText.setStyle(tardis.getStyle().withColor(ChatFormatting.GRAY)));
        }

        super.appendHoverText(itemStack, level, list, tooltipFlag);
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

package net.drgmes.dwm.items.tardis.tardiskey;

import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class TardisKeyItem extends Item {
    public TardisKeyItem(Item.Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag) {
        CompoundTag tag = itemStack.getOrCreateTag();

        if (tag.contains("tardisLevelUUID")) {
            MutableComponent tardis = Component.literal(tag.getString("tardisLevelUUID").substring(0, 8));
            MutableComponent tardisText = Component.translatable("title.dwm.tardis_uuid", tardis.setStyle(tardis.getStyle().withColor(ChatFormatting.GOLD)));
            list.add(tardisText.setStyle(tardis.getStyle().withColor(ChatFormatting.GRAY)));
        }

        super.appendHoverText(itemStack, level, list, tooltipFlag);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        CompoundTag tag = itemStack.getOrCreateTag();
        InteractionResultHolder<ItemStack> pass = InteractionResultHolder.pass(itemStack);
        if (!tag.contains("tardisLevelUUID")) return pass;

        ServerLevel tardisLevel = DimensionHelper.getLevel(level.getServer(), tag.getString("tardisLevelUUID"));
        if (tardisLevel != null) {
            tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
                if (!tardis.isValid()) return;
                BlockPos blockPos = tardis.getCurrentExteriorPosition();

                if (player.isShiftKeyDown()) {
                    if (!DimensionHelper.isTardisDimension(level) && player.hasPermissions(2)) {
                        tardis.setDestinationFacing(Direction.fromYRot(player.getYHeadRot()));
                        tardis.setDestinationDimension(level.dimension());
                        tardis.setDestinationPosition(player.blockPosition());
                        tardis.getSystem(TardisSystemFlight.class).setFlight(true);
                    }

                    return;
                }

                if (tardis.getLevel() != level && tardis.getCurrentExteriorDimension() != level.dimension()) return;
                if (tardis.getLevel() != level && blockPos.distManhattan(player.blockPosition()) > 10) return;

                if (!tardis.isDoorsLocked()) {
                    if (tardis.setDoorsOpenState(!tardis.isDoorsOpened())) {
                        level.gameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos);
                        tardis.updateConsoleTiles();
                    }
                }
            });

            return InteractionResultHolder.consume(itemStack);
        }

        return InteractionResultHolder.success(itemStack);
    }
}

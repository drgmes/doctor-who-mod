package net.drgmes.dwm.items.tardiskey;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockEntity;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

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
            tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
                if (!provider.isValid()) return;
                if (provider.isDoorsLocked()) return;
                if (provider.getLevel() != level && provider.getCurrentExteriorDimension() != level.dimension()) return;
                if (provider.getLevel() != level && provider.getCurrentExteriorPosition().distManhattan(player.blockPosition()) > 10) return;

                if (provider.setDoorsOpenState(!provider.isDoorsOpened())) {
                    provider.updateConsoleTiles();
                }
            });
        }

        return pass;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ServerLevel tardisLevel = null;
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        ItemStack itemStack = context.getItemInHand();
        CompoundTag tag = itemStack.getOrCreateTag();

        if (blockState.getBlock() instanceof BaseTardisExteriorBlock && blockState.getValue(BaseTardisExteriorBlock.HALF) == DoubleBlockHalf.UPPER) {
            blockPos = blockPos.below();
            blockState = level.getBlockState(blockPos);
        }

        if (level.getBlockEntity(blockPos) instanceof BaseTardisDoorsBlockEntity tardisDoorsBlockEntity && TardisHelper.isTardisDimension(level)) {
            if (level.isClientSide) return InteractionResult.FAIL;
            tardisLevel = (ServerLevel) level;
        }

        if (level.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            if (level.isClientSide) return InteractionResult.FAIL;
            tardisLevel = tardisExteriorBlockEntity.getTardisLevel(level);
        }

        if (tardisLevel != null) {
            tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
                if (!provider.isValid()) return;

                String tardisDimUUID = provider.getLevel().dimension().location().getPath();

                if (!tag.contains("tardisDimUUID")) {
                    if (!provider.getOwnerUUID().equals(player.getUUID())) return;
                    tag.putString("tardisDimUUID", tardisDimUUID);
                }

                if (!tag.getString("tardisDimUUID").equalsIgnoreCase(tardisDimUUID)) {
                    return;
                }

                if (provider.setDoorsLockState(!provider.isDoorsLocked(), null)) {
                    player.displayClientMessage(provider.isDoorsLocked() ? DWM.TEXTS.TARDIS_DOORS_LOCKED : DWM.TEXTS.TARDIS_DOORS_UNLOCKED, true);
                }
            });

            return InteractionResult.FAIL;
        }

        return InteractionResult.PASS;
    }
}

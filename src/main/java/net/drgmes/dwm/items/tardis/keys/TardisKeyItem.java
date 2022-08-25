package net.drgmes.dwm.items.tardis.keys;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public class TardisKeyItem extends Item {
    public TardisKeyItem(Item.Settings props) {
        super(props);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (player.getItemCooldownManager().isCoolingDown(itemStack.getItem())) return TypedActionResult.fail(itemStack);

        NbtCompound tag = itemStack.getOrCreateNbt();
        TypedActionResult<ItemStack> pass = TypedActionResult.pass(itemStack);
        if (!tag.contains("tardisId")) return pass;

        ServerWorld tardisWorld = DimensionHelper.getModWorld(tag.getString("tardisId"));
        if (tardisWorld != null) {
            TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
                if (!tardis.isValid()) return;
                BlockPos blockPos = tardis.getCurrentExteriorPosition();

                if (player.isSneaking()) {
                    if (!TardisHelper.isTardisDimension(world) && player.hasPermissionLevel(2)) {
                        if (tardis.getSystem(TardisSystemFlight.class).inProgress()) return;
                        if (tardis.getSystem(TardisSystemMaterialization.class).inProgress()) return;

                        tardis.getSystem(TardisSystemMaterialization.class).setSafeDirection(TardisSystemMaterialization.ESafeDirection.TOP);
                        tardis.setDestinationFacing(Direction.fromRotation(player.getHeadYaw()));
                        tardis.setDestinationDimension(world.getRegistryKey());
                        tardis.setDestinationPosition(player.getBlockPos());
                        tardis.getSystem(TardisSystemFlight.class).setFlight(true);
                        player.getItemCooldownManager().set(itemStack.getItem(), (int) (DWM.TIMINGS.DEMAT + DWM.TIMINGS.REMAT + 10));
                    }

                    return;
                }

                if (tardis.getWorld() != world && tardis.getCurrentExteriorDimension() != world.getRegistryKey()) return;
                if (tardis.getWorld() != world && blockPos.getManhattanDistance(player.getBlockPos()) > 10) return;

                if (!tardis.isDoorsLocked()) {
                    if (tardis.setDoorsOpenState(!tardis.isDoorsOpened())) {
                        world.emitGameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos);
                        tardis.updateConsoleTiles();
                    }
                }
            });

            return TypedActionResult.consume(itemStack);
        }

        return TypedActionResult.success(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> list, TooltipContext context) {
        NbtCompound tag = itemStack.getOrCreateNbt();

        if (tag.contains("tardisId")) {
            MutableText tardis = Text.literal(tag.getString("tardisId").substring(0, 8));
            MutableText tardisText = Text.translatable("title.dwm.tardis_id", tardis.formatted(Formatting.GOLD));
            list.add(tardisText.formatted(Formatting.GRAY));
        }

        super.appendTooltip(itemStack, world, list, context);
    }
}

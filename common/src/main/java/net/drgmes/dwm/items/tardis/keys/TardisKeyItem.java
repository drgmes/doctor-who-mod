package net.drgmes.dwm.items.tardis.keys;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.enums.TardisVerticalScanning;
import net.drgmes.dwm.setup.ModConfig;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
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
    public TardisKeyItem(Settings props) {
        super(props);
    }

    @Override
    public boolean damage(DamageSource source) {
        return super.damage(source) && !source.isIn(DamageTypeTags.IS_EXPLOSION);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (player.getItemCooldownManager().isCoolingDown(itemStack.getItem())) return TypedActionResult.fail(itemStack);

        NbtCompound tag = itemStack.getOrCreateNbt();
        TypedActionResult<ItemStack> pass = TypedActionResult.pass(itemStack);
        if (!tag.contains("tardisId")) return pass;

        if (!world.isClient) {
            ServerWorld tardisWorld = DimensionHelper.getModWorld(tag.getString("tardisId"), world.getServer());

            if (tardisWorld != null) {
                TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
                    BlockPos blockPos = tardis.getCurrentExteriorPosition();
                    tag.putString("tardisPos", tardis.getCurrentExteriorPosition().toShortString());

                    if (player.isSneaking()) {
                        if (TardisHelper.isTardisDimension(world)) return;

                        if (!ModConfig.COMMON.tardisRecallOperatorOnly.get() || player.hasPermissionLevel(2)) {
                            TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);
                            TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);
                            if (flightSystem.inProgress() || materializationSystem.inProgress()) return;

                            materializationSystem.setVerticalScanning(TardisVerticalScanning.TOP);
                            tardis.setDestinationFacing(Direction.fromRotation(player.getHeadYaw()));
                            tardis.setDestinationDimension(world.getRegistryKey());
                            tardis.setDestinationPosition(player.getBlockPos());
                            flightSystem.setFlight(true);

                            if (materializationSystem.inProgress()) {
                                float duration = DWM.TIMINGS.DEMAT_DURATION + DWM.TIMINGS.REMAT_DURATION + flightSystem.getFlightDuration();
                                player.sendMessage(DWM.TEXTS.TARDIS_ARRIVE_TIMER.apply(duration / 20), true);
                                player.getItemCooldownManager().set(itemStack.getItem(), (int) (DWM.TIMINGS.DEMAT_DURATION + DWM.TIMINGS.REMAT_DURATION + DWM.TIMINGS.FLIGHT_LOOP));

                                flightSystem.onFail(() -> {
                                    String tardisPos = tardis.getDestinationExteriorPosition().toShortString();

                                    tag.putString("tardisPos", tardisPos);
                                    player.sendMessage(DWM.TEXTS.TARDIS_ARRIVE_FAILED.apply(tardisPos));
                                    player.getItemCooldownManager().remove(itemStack.getItem());
                                });
                            }
                            else if (tardis.getFuelAmount() == 0 && tardis.getEnergyAmount() == 0) {
                                player.sendMessage(DWM.TEXTS.TARDIS_NO_FUEL, true);
                            }
                        }
                        else {
                            String tardisPos = tardis.getCurrentExteriorPosition().toShortString();
                            player.sendMessage(DWM.TEXTS.TARDIS_POS.apply(tardisPos, Formatting.AQUA), true);
                        }

                        return;
                    }

                    if (tardis.getWorld() != world && tardis.getCurrentExteriorDimension() != world.getRegistryKey()) return;
                    if (tardis.getWorld() != world && blockPos.getManhattanDistance(player.getBlockPos()) > 10) return;

                    if (!tardis.isDoorsLocked()) {
                        if (tardis.setDoorsOpenState(!tardis.isDoorsOpened())) {
                            world.emitGameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos);
                            tardis.markConsoleTilesUpdated();
                        }
                    }
                });

                return TypedActionResult.consume(itemStack);
            }
        }

        return TypedActionResult.success(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltips, TooltipContext context) {
        NbtCompound tag = itemStack.getOrCreateNbt();

        if (tag.contains("tardisId")) {
            String tardisId = tag.getString("tardisId");

            tooltips.add(Text.empty());
            tooltips.add(DWM.TEXTS.TARDIS_ID.apply(tardisId.substring(0, 8), Formatting.GOLD).copy().formatted(Formatting.GRAY));

            if (tag.contains("tardisPos")) {
                String tardisPos = tag.getString("tardisPos");
                tooltips.add(DWM.TEXTS.TARDIS_LAST_POS.apply(tardisPos, Formatting.GOLD).copy().formatted(Formatting.GRAY));
            }
        }

        super.appendTooltip(itemStack, world, tooltips, context);
    }
}

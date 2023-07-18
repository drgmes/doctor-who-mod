package net.drgmes.dwm.common.sonicdevice.modes.tardis;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.common.sonicdevice.modes.BaseSonicDeviceMode;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Optional;

public class SonicDeviceTardisMode extends BaseSonicDeviceMode {
    public static final SonicDeviceTardisMode INSTANCE = new SonicDeviceTardisMode();

    @Override
    public ActionResult interactWithBlockNative(World world, PlayerEntity player, EquipmentSlot slot, BlockHitResult hitResult) {
        if (world.isClient) return ActionResult.SUCCESS;
        if (TardisHelper.isTardisDimension(world)) return ActionResult.SUCCESS;

        BlockPos blockPos = hitResult.getBlockPos().up();
        String tardisId = SonicDevice.getTardisId(player.getEquippedStack(slot));
        ServerWorld tardisWorld = DimensionHelper.getModWorld(tardisId, world.getServer());
        if (tardisWorld == null || tardisWorld == world) return ActionResult.FAIL;

        Optional<TardisStateManager> tardisHolder = TardisStateManager.get(tardisWorld);
        if (tardisHolder.isEmpty()) return ActionResult.FAIL;

        MutableText x = Text.literal(String.valueOf(blockPos.getX())).formatted(Formatting.YELLOW);
        MutableText y = Text.literal(String.valueOf(blockPos.getY())).formatted(Formatting.YELLOW);
        MutableText z = Text.literal(String.valueOf(blockPos.getZ())).formatted(Formatting.YELLOW);

        player.sendMessage(Text.translatable("message." + DWM.MODID + ".sonic_device.tardis_relocated", x, y, z), true);
        tardisHolder.get().setDestinationFacing(Direction.fromRotation(player.getHeadYaw()).getOpposite());
        tardisHolder.get().setDestinationDimension(world.getRegistryKey());
        tardisHolder.get().setDestinationPosition(blockPos);
        tardisHolder.get().updateConsoleTiles();
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult interactWithEntity(World world, PlayerEntity player, EquipmentSlot slot, EntityHitResult hitResult, boolean isAlternativeAction) {
        return ActionResult.PASS;
    }
}

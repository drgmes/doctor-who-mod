package net.drgmes.dwm.network;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.TardisToyotaSpinnerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class TardisMiscRemoteCallablePackets {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    // /////////// //
    // Clientbound //
    // /////////// //

    public static void updateTardisToyotaSpinnerData(BlockPos blockPos, boolean inProgress) {
        if (mc.world.getBlockEntity(blockPos) instanceof TardisToyotaSpinnerBlockEntity tardisToyotaSpinnerBlockEntity) {
            tardisToyotaSpinnerBlockEntity.inProgress = inProgress;
            tardisToyotaSpinnerBlockEntity.markDirty();
        }
    }

    // /////////// //
    // Serverbound //
    // /////////// //

    public static void initTardisInteriorDoors(ServerPlayerEntity player, BlockPos blockPos) {
        if (player.world.getBlockEntity(blockPos) instanceof BaseTardisDoorsBlockEntity tardisDoorsBlockEntity) {
            tardisDoorsBlockEntity.init();
        }
    }
}

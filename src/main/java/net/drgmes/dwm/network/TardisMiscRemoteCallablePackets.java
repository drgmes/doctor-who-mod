package net.drgmes.dwm.network;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.TardisRoundelBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.TardisToyotaSpinnerBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class TardisMiscRemoteCallablePackets {
    // /////////// //
    // Clientbound //
    // /////////// //

    @Environment(EnvType.CLIENT)
    public static void updateTardisToyotaSpinnerData(BlockPos blockPos, boolean inProgress) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(blockPos) instanceof TardisToyotaSpinnerBlockEntity tardisToyotaSpinnerBlockEntity) {
            tardisToyotaSpinnerBlockEntity.inProgress = inProgress;
            tardisToyotaSpinnerBlockEntity.markDirty();
        }
    }

    @Environment(EnvType.CLIENT)
    public static void updateTardisRoundelData(BlockPos blockPos, boolean uncovered, boolean lightMode) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(blockPos) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
            tardisRoundelBlockEntity.uncovered = uncovered;
            tardisRoundelBlockEntity.lightMode = lightMode;
            tardisRoundelBlockEntity.markDirty();
        }
    }

    @Environment(EnvType.CLIENT)
    public static void updateTardisRoundelBlockTemplate(BlockPos blockPos, Identifier blockTemplate) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(blockPos) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
            tardisRoundelBlockEntity.blockTemplate = blockTemplate;
            tardisRoundelBlockEntity.markDirty();
        }
    }

    @Environment(EnvType.CLIENT)
    public static void clearTardisRoundelBlockTemplate(BlockPos blockPos) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(blockPos) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
            tardisRoundelBlockEntity.blockTemplate = null;
            tardisRoundelBlockEntity.markDirty();
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

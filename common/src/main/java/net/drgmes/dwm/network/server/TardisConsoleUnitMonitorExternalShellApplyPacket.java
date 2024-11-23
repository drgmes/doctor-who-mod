package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriorEntry;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriors;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.network.IPacket;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record TardisConsoleUnitMonitorExternalShellApplyPacket(
    String tardisId,
    String exteriorTypeId
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_console_unit_monitor_external_shell_apply");
    public static final CustomPayload.Id<TardisConsoleUnitMonitorExternalShellApplyPacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisConsoleUnitMonitorExternalShellApplyPacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, TardisConsoleUnitMonitorExternalShellApplyPacket::tardisId,
        PacketCodecs.STRING, TardisConsoleUnitMonitorExternalShellApplyPacket::exteriorTypeId,
        TardisConsoleUnitMonitorExternalShellApplyPacket::new
    );

    public static void handle(TardisConsoleUnitMonitorExternalShellApplyPacket payload, NetworkManager.PacketContext context) {
        ServerWorld tardisWorld = DimensionHelper.getModWorld(payload.tardisId, context.getPlayer().getServer());
        PlayerEntity player = context.getPlayer();

        TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
            if (!tardis.checkAccess(player, false, true)) {
                ModSounds.playTardisBellSound(tardis.getWorld(), tardis.getMainConsolePosition());
                player.sendMessage(DWM.TEXTS.TARDIS_NOT_ALLOWED, true);
                return;
            }

            TardisExteriorEntry exteriorType = TardisExteriors.TYPES.get(payload.exteriorTypeId);
            if (exteriorType == null) {
                ModSounds.playTardisBellSound(tardis.getWorld(), tardis.getMainConsolePosition());
                return;
            }

            TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);
            if (!materializationSystem.isEnabled()) {
                ModSounds.playTardisBellSound(tardis.getWorld(), tardis.getMainConsolePosition());
                player.sendMessage(DWM.TEXTS.MATERIALIZATION_SYSTEM_NOT_INSTALLED, true);
                return;
            }

            if (materializationSystem.inProgress()) {
                ModSounds.playTardisBellSound(tardis.getWorld(), tardis.getMainConsolePosition());
                player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_MATERIALIZED, true);
                return;
            }

            tardis.setExteriorType(exteriorType);
            materializationSystem.demat(materializationSystem::remat);

            BlockEntity doorsTile = tardis.getMainInteriorDoorsTile();
            if (doorsTile == null) return;

            Block newDoorsBlock = exteriorType.getDoorsBlock();
            BlockState blockState = doorsTile.getCachedState();
            BlockPos blockPos = doorsTile.getPos();

            tardisWorld.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
            tardisWorld.setBlockState(blockPos.up(), Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);

            BlockState newBlockState = newDoorsBlock.getDefaultState();
            newBlockState = newBlockState.with(BaseTardisDoorsBlock.WATERLOGGED, blockState.get(BaseTardisDoorsBlock.WATERLOGGED));
            newBlockState = newBlockState.with(BaseTardisDoorsBlock.FACING, blockState.get(BaseTardisDoorsBlock.FACING));
            newBlockState = newBlockState.with(BaseTardisDoorsBlock.HALF, blockState.get(BaseTardisDoorsBlock.HALF));
            newBlockState = newBlockState.with(BaseTardisDoorsBlock.OPEN, blockState.get(BaseTardisDoorsBlock.OPEN));

            tardisWorld.setBlockState(blockPos, newBlockState, Block.NOTIFY_ALL);
            tardisWorld.setBlockState(blockPos.up(), newBlockState.with(BaseTardisDoorsBlock.HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
        });
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}

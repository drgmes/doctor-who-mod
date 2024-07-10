package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriorEntry;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriors;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.setup.ModNetwork;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class TardisConsoleUnitMonitorExternalShellApplyPacket extends BaseC2SMessage {
    private final String tardisId;
    private final String exteriorTypeId;

    public TardisConsoleUnitMonitorExternalShellApplyPacket(String tardisId, String exteriorTypeId) {
        this.tardisId = tardisId;
        this.exteriorTypeId = exteriorTypeId;
    }

    public static TardisConsoleUnitMonitorExternalShellApplyPacket create(PacketByteBuf buf) {
        return new TardisConsoleUnitMonitorExternalShellApplyPacket(buf.readString(), buf.readString());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_CONSOLE_UNIT_MONITOR_EXTERNAL_SHELL_APPLY;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.tardisId);
        buf.writeString(this.exteriorTypeId);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ServerWorld tardisWorld = DimensionHelper.getModWorld(this.tardisId, context.getPlayer().getServer());
        PlayerEntity player = context.getPlayer();

        TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
            if (!tardis.checkAccess(player, false, true)) {
                ModSounds.playTardisBellSound(tardis.getWorld(), tardis.getMainConsolePosition());
                player.sendMessage(DWM.TEXTS.TARDIS_NOT_ALLOWED, true);
                return;
            }

            TardisExteriorEntry exteriorType = TardisExteriors.TYPES.get(this.exteriorTypeId);
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
}

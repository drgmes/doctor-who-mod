package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.network.IPacket;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket(
    RegistryKey<World> dimension,
    DyeColor dyeColor,
    BlockPos blockPos
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_console_unit_telepathic_interface_map_banner_apply");
    public static final CustomPayload.Id<TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket> PACKET_CODEC = PacketCodec.tuple(
        RegistryKey.createPacketCodec(RegistryKeys.WORLD), TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket::dimension,
        DyeColor.PACKET_CODEC, TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket::dyeColor,
        BlockPos.PACKET_CODEC, TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket::blockPos,
        TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public static void handle(TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            PlayerEntity player = context.getPlayer();

            ServerWorld serverWorld = (ServerWorld) player.getWorld();
            if (!TardisHelper.isTardisDimension(serverWorld)) return;

            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);
                TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);

                if (!flightSystem.isEnabled()) {
                    player.sendMessage(DWM.TEXTS.FLIGHT_SYSTEM_NOT_INSTALLED, true);
                    return;
                }

                if (flightSystem.inProgress()) {
                    player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_LANDED, true);
                    return;
                }

                if (materializationSystem.inProgress()) {
                    player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_MATERIALIZED, true);
                    return;
                }

                String color = payload.dyeColor.getName().toUpperCase().replace("_", " ");
                player.sendMessage(DWM.TEXTS.TELEPATHIC_INTERFACE_MAP_BANNER_LOADED.apply(color), true);

                tardis.setDestinationDimension(payload.dimension);
                tardis.setDestinationPosition(payload.blockPos);
                tardis.markConsoleTilesUpdated();
            });
        });
    }
}

package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.network.IPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record TardisConsoleUnitSonicScrewdriverSlotUpdatePacket(
    BlockPos blockPos,
    ItemStack itemStack
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_console_unit_sonic_screwdriver_slot_update");
    public static final CustomPayload.Id<TardisConsoleUnitSonicScrewdriverSlotUpdatePacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<RegistryByteBuf, TardisConsoleUnitSonicScrewdriverSlotUpdatePacket> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, TardisConsoleUnitSonicScrewdriverSlotUpdatePacket::blockPos,
        ItemStack.OPTIONAL_PACKET_CODEC, TardisConsoleUnitSonicScrewdriverSlotUpdatePacket::itemStack,
        TardisConsoleUnitSonicScrewdriverSlotUpdatePacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    @Environment(EnvType.CLIENT)
    public static void handle(TardisConsoleUnitSonicScrewdriverSlotUpdatePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            final MinecraftClient mc = MinecraftClient.getInstance();

            if (mc.world.getBlockEntity(payload.blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
                tardisConsoleUnitBlockEntity.sonicScrewdriverItemStack = payload.itemStack;
            }
        });
    }
}

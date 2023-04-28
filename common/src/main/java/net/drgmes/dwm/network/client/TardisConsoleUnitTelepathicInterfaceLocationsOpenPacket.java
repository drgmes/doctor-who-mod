package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.blocks.tardis.consoleunits.screens.TardisConsoleUnitTelepathicInterfaceLocationsScreen;
import net.drgmes.dwm.setup.ModNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket extends BaseS2CMessage {
    private final BlockPos blockPos;
    private final NbtCompound tag;

    public TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket(BlockPos blockPos, NbtCompound tag) {
        this.blockPos = blockPos;
        this.tag = tag;
    }

    public static TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket create(PacketByteBuf buf) {
        return new TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket(buf.readBlockPos(), buf.readNbt());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_CONSOLE_UNIT_TELEPATHIC_INTERFACE_LOCATIONS_OPEN;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeNbt(this.tag);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(this.blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            List<Map.Entry<Identifier, TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType>> locations = new ArrayList<>();
            List<String> keys = new ArrayList<>(this.tag.getKeys().stream().toList());

            keys.sort(Comparator.comparing((key) -> key));
            keys.forEach((key) -> {
                locations.add(Map.entry(
                    new Identifier(this.tag.getCompound(key).getString("id")),
                    TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType.valueOf(this.tag.getCompound(key).getString("type"))
                ));
            });

            mc.setScreen(new TardisConsoleUnitTelepathicInterfaceLocationsScreen(tardisConsoleUnitBlockEntity, locations));
        }
    }
}

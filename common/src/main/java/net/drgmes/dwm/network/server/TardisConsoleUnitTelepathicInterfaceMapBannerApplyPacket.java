package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.setup.ModNetwork;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.map.MapBannerMarker;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket extends BaseC2SMessage {
    private final NbtCompound tag;

    public TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket(NbtCompound tag) {
        this.tag = tag;
    }

    public static TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket create(PacketByteBuf buf) {
        return new TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket(buf.readNbt());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_CONSOLE_UNIT_TELEPATHIC_INTERFACE_MAP_BANNER_APPLY;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeNbt(this.tag);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();

        TardisStateManager.get((ServerWorld) player.world).ifPresent((tardis) -> {
            if (!tardis.isValid()) return;

            MapState mapState = MapState.fromNbt(tag.getCompound("mapState"));
            MapBannerMarker mapBannerMarker = MapBannerMarker.fromNbt(tag.getCompound("mapBannerMarker"));

            String color = "§e" + mapBannerMarker.getColor().getName().toUpperCase().replace("_", " ");
            player.sendMessage(Text.translatable("message." + DWM.MODID + ".tardis.telepathic_interface.map.loaded.banner", color), true);

            tardis.setDestinationDimension(mapState.dimension);
            tardis.setDestinationPosition(mapBannerMarker.getPos());
            tardis.updateConsoleTiles();
        });
    }
}

package net.drgmes.dwm.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModCapabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.maps.MapBanner;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundTardisConsoleTelepathicInterfaceMapBannersApplyPacket {
    public final MapItemSavedData mapData;
    public final MapBanner banner;

    public ServerboundTardisConsoleTelepathicInterfaceMapBannersApplyPacket(MapItemSavedData mapData, MapBanner banner) {
        this.mapData = mapData;
        this.banner = banner;
    }

    public ServerboundTardisConsoleTelepathicInterfaceMapBannersApplyPacket(FriendlyByteBuf buffer) {
        this(MapItemSavedData.load(buffer.readNbt()), MapBanner.load(buffer.readNbt()));
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.mapData.save(new CompoundTag()));
        buffer.writeNbt(this.banner.save());
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> {
            if (!(ctx.get().getSender().level instanceof ServerLevel level)) return;

            level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
                if (!provider.isValid()) return;

                String color = "\u00A7e" + banner.getColor().getName().toUpperCase().replace("_", " ");
                ctx.get().getSender().displayClientMessage(new TranslatableComponent("message." + DWM.MODID + ".tardis.telepathic_interface.map.loaded.banner", color), true);

                provider.setDestinationDimension(mapData.dimension);
                provider.setDestinationPosition(banner.getPos());
                provider.updateConsoleTiles();
            });

            success.set(true);
        });

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

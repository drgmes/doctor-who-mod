package net.drgmes.dwm.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.ars.ArsCategories;
import net.drgmes.dwm.common.tardis.ars.ArsRoom;
import net.drgmes.dwm.common.tardis.ars.ArsRooms;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundTardisRoomsDestroyerApplyPacket {
    public final ArsRoom room;
    public final BlockPos blockPos;

    public ServerboundTardisRoomsDestroyerApplyPacket(ArsRoom room, BlockPos blockPos) {
        this.room = room;
        this.blockPos = blockPos;
    }

    public ServerboundTardisRoomsDestroyerApplyPacket(FriendlyByteBuf buffer) {
        this(ArsRooms.ROOMS.get(ArsCategories.CATEGORIES.get(buffer.readUtf())).get(buffer.readUtf()), buffer.readBlockPos());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.room.getCategory().getPath());
        buffer.writeUtf(this.room.getName());
        buffer.writeBlockPos(this.blockPos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> {
            if (!(ctx.get().getSender().level instanceof ServerLevel level)) return;

            boolean isRoomGenerated = room.destroy(ctx.get().getSender(), level, blockPos);
            ctx.get().getSender().displayClientMessage(Component.translatable("message." + DWM.MODID + ".tardis.ars_interface.destroy." + (isRoomGenerated ? "success" : "failed")), true);
            success.set(true);
        });

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.ars.ArsCategories;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.common.tardis.ars.ArsStructures;
import net.drgmes.dwm.setup.ModNetwork;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class ArsCreatorApplyPacket extends BaseC2SMessage {
    private final BlockPos blockPos;
    private final String arsCategoryName;
    private final String arsStructureName;

    public ArsCreatorApplyPacket(BlockPos blockPos, String arsCategoryName, String arsStructureName) {
        this.blockPos = blockPos;
        this.arsCategoryName = arsCategoryName;
        this.arsStructureName = arsStructureName;
    }

    public static ArsCreatorApplyPacket create(PacketByteBuf buf) {
        return new ArsCreatorApplyPacket(buf.readBlockPos(), buf.readString(), buf.readString());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.ARS_CREATOR_APPLY;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeString(this.arsCategoryName);
        buf.writeString(this.arsStructureName);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();

        ServerWorld serverWorld = (ServerWorld) player.world;
        if (!TardisHelper.isTardisDimension(serverWorld)) return;

        TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
            if (tardis.isBroken()) {
                player.sendMessage(DWM.TEXTS.TARDIS_BROKEN, true);
                return;
            }

            ArsStructure arsStructure = ArsStructures.STRUCTURES.get(ArsCategories.CATEGORIES.get(arsCategoryName)).get(arsStructureName);
            boolean isArsStructureGenerated = arsStructure.place(player, tardis, blockPos);
            player.sendMessage(Text.translatable("message." + DWM.MODID + ".tardis.ars_interface.generated." + (isArsStructureGenerated ? "success" : "failed")), true);
        });
    }
}

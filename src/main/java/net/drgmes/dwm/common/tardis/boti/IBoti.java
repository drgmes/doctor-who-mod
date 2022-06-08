package net.drgmes.dwm.common.tardis.boti;

import net.drgmes.dwm.common.tardis.boti.storage.BotiStorage;
import net.drgmes.dwm.network.ClientboundBotiUpdatePacket;
import net.minecraft.core.BlockPos;

public interface IBoti {
    void setBotiStorage(BotiStorage botiStorage);
    BotiStorage getBotiStorage();
    void updateBoti();

    default ClientboundBotiUpdatePacket getBotiUpdatePacket(BlockPos blockPos) {
        return new ClientboundBotiUpdatePacket(blockPos, getBotiStorage());
    }
}

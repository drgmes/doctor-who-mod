package net.drgmes.dwm.common.tardis.boti;

import net.drgmes.dwm.common.tardis.boti.storage.BotiStorage;
import net.drgmes.dwm.network.ClientboundBotiUpdatePacket;
import net.minecraft.core.BlockPos;

public interface IBoti {
    BotiStorage getBotiStorage();

    void setBotiStorage(BotiStorage botiStorage);

    void updateBoti();

    default ClientboundBotiUpdatePacket getBotiUpdatePacket(BlockPos blockPos) {
        return new ClientboundBotiUpdatePacket(blockPos, getBotiStorage());
    }
}

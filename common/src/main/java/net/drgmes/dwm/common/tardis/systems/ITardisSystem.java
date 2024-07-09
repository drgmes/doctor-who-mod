package net.drgmes.dwm.common.tardis.systems;

import net.minecraft.nbt.NbtCompound;

public interface ITardisSystem {
    boolean isEnabled();
    boolean inProgress();

    void readNbt(NbtCompound tag);
    NbtCompound writeNbt(NbtCompound tag);

    void tick();
}

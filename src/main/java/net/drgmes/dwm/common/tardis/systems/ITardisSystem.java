package net.drgmes.dwm.common.tardis.systems;

import net.minecraft.nbt.NbtCompound;

public interface ITardisSystem {
    boolean isEnabled();
    boolean inProgress();

    void load(NbtCompound tag);
    NbtCompound save();

    void tick();
}

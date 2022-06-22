package net.drgmes.dwm.common.tardis.systems;

import net.minecraft.nbt.CompoundTag;

public interface ITardisSystem {
    void tick();

    CompoundTag save();

    void load(CompoundTag tag);
}

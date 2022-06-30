package net.drgmes.dwm.common.tardis.systems;

import net.minecraft.nbt.CompoundTag;

public interface ITardisSystem {
    boolean isEnabled();
    boolean inProgress();

    void load(CompoundTag tag);
    CompoundTag save();

    void tick();
}

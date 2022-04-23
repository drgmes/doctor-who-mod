package net.drgmes.dwm.common.tardis.systems;

import net.minecraft.nbt.CompoundTag;

public interface ITardisSystem {
    public void tick();

    public CompoundTag save();
    public void load(CompoundTag tag);
}

package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.utils.helpers.EntityHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import java.util.UUID;

public abstract class TardisBaseSystem {
    protected final TardisStateManager tardis;

    public TardisBaseSystem(TardisStateManager tardis) {
        this.tardis = tardis;
    }

    public boolean isEnabled() {
        return this.tardis.isSystemEnabled(this.getClass());
    }

    public boolean inProgress() {
        return false;
    }

    public void readNbt(NbtCompound tag) {
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        return tag;
    }

    public void tick() {
    }

    protected void notify(Text message, UUID playerId) {
        EntityHelper.sendMessage(this.tardis.getWorld(), this.tardis.getOwner(), message);
        if (this.tardis.getOwner() != playerId) EntityHelper.sendMessage(this.tardis.getWorld(), playerId, message);
    }
}

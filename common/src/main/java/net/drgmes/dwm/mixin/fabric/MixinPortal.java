package net.drgmes.dwm.mixin.fabric;

import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.types.IMixinPortal;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.portal.Portal;

import java.util.Objects;

@Mixin(Portal.class)
public class MixinPortal implements IMixinPortal {
    public String tardisId;
    public boolean isTardisEntrance;
    public boolean isTardisRoomsEntrance;

    @Override
    public String getTardisId() {
        return this.tardisId;
    }

    @Override
    public void setTardisId(String tardisId) {
        this.tardisId = tardisId;
    }

    @Override
    public IMixinPortal markAsTardisEntrance() {
        this.isTardisEntrance = true;
        return this;
    }

    @Override
    public IMixinPortal markAsTardisRoomsEntrance() {
        this.isTardisRoomsEntrance = true;
        return this;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        Portal $this = (Portal) (Object) this;
        if ($this.getWorld().isClient || $this.isRemoved()) return;

        boolean isTardisIdEmpty = this.tardisId == null || Objects.equals(this.tardisId, "");

        if (this.isTardisEntrance || this.isTardisRoomsEntrance) {
            if (isTardisIdEmpty) {
                $this.discard();
                return;
            }

            ServerWorld tardisWorld = DimensionHelper.getModWorld(this.tardisId, $this.getServer());
            if (!TardisHelper.isTardisDimension(tardisWorld)) {
                $this.discard();
                return;
            }

            if (this.isTardisEntrance) {
                TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
                    if (!tardis.getPortalsState().checkIsEntrancePortalEquals($this)) $this.discard();
                });
            }

            if (this.isTardisRoomsEntrance) {
                TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
                    if (!tardis.getPortalsState().checkIsRoomEntrancePortalEquals($this)) $this.discard();
                });
            }
        }
        else if (!isTardisIdEmpty) {
            $this.discard();
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readCustomDataFromNbt(NbtCompound tag, CallbackInfo ci) {
        if (tag.contains("tardisId")) this.tardisId = tag.getString("tardisId");
        this.isTardisEntrance = tag.getBoolean("isTardisEntrance");
        this.isTardisRoomsEntrance = tag.getBoolean("isTardisRoomsEntrance");
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeCustomDataToNbt(NbtCompound tag, CallbackInfo ci) {
        if (this.tardisId != null) tag.putString("tardisId", this.tardisId);
        tag.putBoolean("isTardisEntrance", this.isTardisEntrance);
        tag.putBoolean("isTardisRoomsEntrance", this.isTardisRoomsEntrance);
    }
}

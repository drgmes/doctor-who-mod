package net.drgmes.dwm.mixin;

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

    @Override
    public String getTardisId() {
        return this.tardisId;
    }

    @Override
    public void setTardisId(String tardisId) {
        this.tardisId = tardisId;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        Portal $this = (Portal) (Object) this;
        if ($this.world.isClient || $this.isRemoved()) return;

        if (this.getTardisId() == null || Objects.equals(this.getTardisId(), "")) {
            $this.discard();
            return;
        }

        ServerWorld tardisWorld = DimensionHelper.getModWorld(this.getTardisId());
        if (TardisHelper.isTardisDimension(tardisWorld)) {
            TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
                if (!tardis.checkIsPortalValid($this)) $this.discard();
            });
        }
        else {
            $this.discard();
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readCustomDataFromNbt(NbtCompound tag, CallbackInfo ci) {
        this.setTardisId(tag.getString("tardisId"));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeCustomDataToNbt(NbtCompound tag, CallbackInfo ci) {
        tag.putString("tardisId", this.getTardisId());
    }
}

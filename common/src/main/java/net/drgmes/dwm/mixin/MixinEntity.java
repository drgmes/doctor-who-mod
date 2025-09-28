package net.drgmes.dwm.mixin;

import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.utils.helpers.EntityHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {
    @Inject(method = "tickInVoid", at = @At("HEAD"), cancellable = true)
    private void tickInVoid(CallbackInfo ci) {
        Entity $this = (Entity) (Object) this;
        if (!TardisHelper.isTardisDimension($this.getWorld())) return;

        ci.cancel();
        if ($this.getWorld().isClient) return;

        TardisStateManager.get((ServerWorld) $this.getWorld()).ifPresent((tardis) -> {
            Vec3d pos = Vec3d.ofBottomCenter(tardis.getEntrancePosition().offset(tardis.getEntranceFacing()));
            EntityHelper.teleport($this, tardis.getWorld(), pos, tardis.getEntranceFacing().asRotation());
        });
    }
}

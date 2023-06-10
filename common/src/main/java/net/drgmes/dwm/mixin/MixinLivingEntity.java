package net.drgmes.dwm.mixin;

import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity $this = (LivingEntity) (Object) this;
        if ($this.getWorld().isClient) return;

        if (TardisHelper.isTardisDimension($this.getWorld())) {
            if (source.isOf(DamageTypes.OUT_OF_WORLD) && amount != Float.MAX_VALUE) {
                TardisStateManager.get((ServerWorld) $this.getWorld()).ifPresent((tardis) -> {
                    BlockPos entrancePosition = tardis.getEntrancePosition();
                    $this.teleport(entrancePosition.getX() + 0.5, entrancePosition.getY(), entrancePosition.getZ() + 0.5);
                });

                cir.setReturnValue(false);
            }

            if (source.isOf(DamageTypes.FALL)) {
                cir.setReturnValue(false);
            }
        }
    }
}

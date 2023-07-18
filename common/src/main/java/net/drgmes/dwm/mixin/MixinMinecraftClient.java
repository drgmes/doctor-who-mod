package net.drgmes.dwm.mixin;

import net.drgmes.dwm.common.sonicscrewdriver.SonicScrewdriver;
import net.drgmes.dwm.items.sonicscrewdriver.SonicScrewdriverItem;
import net.drgmes.dwm.network.server.SonicScrewdriverUsePacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    public HitResult crosshairTarget;

    @Inject(method = "doAttack", at = @At("HEAD"))
    private void doAttack(CallbackInfoReturnable<Boolean> cir) {
        if (this.crosshairTarget.getType() == HitResult.Type.MISS) {
            ItemStack sonicScrewdriverItemStack = this.player.getMainHandStack();

            if (SonicScrewdriver.checkItemStackIsSonicScrewdriver(sonicScrewdriverItemStack)) {
                if (((SonicScrewdriverItem) sonicScrewdriverItemStack.getItem()).useSonicScrewdriver(this.player.getWorld(), this.player, Hand.MAIN_HAND, true).getResult().shouldSwingHand()) {
                    new SonicScrewdriverUsePacket(sonicScrewdriverItemStack, true, true).sendToServer();
                }
            }
        }
    }
}

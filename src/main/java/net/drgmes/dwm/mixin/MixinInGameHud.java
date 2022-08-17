package net.drgmes.dwm.mixin;

import net.drgmes.dwm.common.screwdriver.modes.scan.ScrewdriverScanModeOverlay;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Inject(method = "render", at = @At("TAIL"))
    public void render(MatrixStack matrixStack, float tickDelta, CallbackInfo ci) {
        ScrewdriverScanModeOverlay.INSTANCE.render(matrixStack, tickDelta);
    }
}

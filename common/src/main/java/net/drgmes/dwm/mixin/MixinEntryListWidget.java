package net.drgmes.dwm.mixin;

import net.drgmes.dwm.utils.base.screens.BaseListWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntryListWidget.class)
public class MixinEntryListWidget {
    @Inject(method = "drawMenuListBackground", at = @At("HEAD"), cancellable = true)
    private void drawMenuListBackground(DrawContext context, CallbackInfo ci) {
        EntryListWidget<?> $this = (EntryListWidget<?>) (Object) this;

        if ($this instanceof BaseListWidget) {
            ci.cancel();
        }
    }
}

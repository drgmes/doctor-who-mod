package net.drgmes.dwm.fabric.datagen.common;

import net.drgmes.dwm.DWM;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class ModWorldgenProvider extends FabricDynamicRegistryProvider {
    public ModWorldgenProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return DWM.MODID;
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg, Entries entries) {
        entries.addAll(arg.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE));
        entries.addAll(arg.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE));
    }
}

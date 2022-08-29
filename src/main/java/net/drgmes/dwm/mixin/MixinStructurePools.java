package net.drgmes.dwm.mixin;

import com.mojang.datafixers.util.Pair;
import net.drgmes.dwm.DWM;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructurePools.class)
public class MixinStructurePools {
    @Inject(method = "register", at = @At("HEAD"))
    private static void register(StructurePool templatePool, CallbackInfoReturnable<RegistryEntry<StructurePool>> cir) {
        addElementToPool(templatePool, new Identifier("village/plains/houses"), DWM.getIdentifier("village/plains/houses/engineer"), 8);
        addElementToPool(templatePool, new Identifier("village/savanna/houses"), DWM.getIdentifier("village/savanna/houses/engineer"), 8);
        addElementToPool(templatePool, new Identifier("village/snowy/houses"), DWM.getIdentifier("village/snowy/houses/engineer"), 8);
        addElementToPool(templatePool, new Identifier("village/taiga/houses"), DWM.getIdentifier("village/taiga/houses/engineer"), 8);

        addElementToPool(templatePool, new Identifier("ancient_city/structures"), DWM.getIdentifier("tardis_exteriors/ancient_city/tardis_exterior_police_box"), 1);
        addElementToPool(templatePool, new Identifier("bastion/treasure/bases/centers"), DWM.getIdentifier("tardis_exteriors/bastion/tardis_exterior_police_box"), 1);
    }

    private static void addElementToPool(StructurePool pool, Identifier targetPool, Identifier elementId, int weight) {
        if (!targetPool.equals(pool.getId())) return;

        StructurePoolElement element = StructurePoolElement.ofProcessedLegacySingle(elementId.toString(), StructureProcessorLists.EMPTY).apply(StructurePool.Projection.RIGID);
        for (int i = 0; i < weight; i++) ((IMixinStructurePool) pool).getElements().add(element);
        ((IMixinStructurePool) pool).getElementCounts().add(Pair.of(element, weight));
    }
}

package net.drgmes.dwm.fabric.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.commands.types.TardisDimensionArgumentType;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;

public class ModCommandsFabric {
    public static void setup() {
        ArgumentTypeRegistry.registerArgumentType(DWM.getIdentifier("tardis_dimension"), TardisDimensionArgumentType.class, ConstantArgumentSerializer.of(TardisDimensionArgumentType::new));
    }
}

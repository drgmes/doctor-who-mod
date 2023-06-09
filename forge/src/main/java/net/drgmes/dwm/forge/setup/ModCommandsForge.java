package net.drgmes.dwm.forge.setup;

import net.drgmes.dwm.commands.types.TardisDimensionArgumentType;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;

public class ModCommandsForge {
    public static void setup() {
        ArgumentTypes.registerByClass(TardisDimensionArgumentType.class, ConstantArgumentSerializer.of(TardisDimensionArgumentType::new));
    }
}

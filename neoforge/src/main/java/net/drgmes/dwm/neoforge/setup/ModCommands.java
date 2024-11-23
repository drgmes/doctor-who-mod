package net.drgmes.dwm.neoforge.setup;

import net.drgmes.dwm.commands.types.TardisDimensionArgumentType;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;

public class ModCommands {
    public static void setup() {
        ArgumentTypes.registerByClass(TardisDimensionArgumentType.class, ConstantArgumentSerializer.of(TardisDimensionArgumentType::new));
    }
}

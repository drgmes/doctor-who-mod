package net.drgmes.dwm.setup;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.commands.TardisDebugCommand;
import net.drgmes.dwm.commands.TardisRemoveCommand;
import net.drgmes.dwm.commands.types.TardisDimensionArgumentType;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;

public class ModCommands {
    public static void setup() {
        ArgumentTypeRegistry.registerArgumentType(DWM.getIdentifier("tardis_dimension"), TardisDimensionArgumentType.class, ConstantArgumentSerializer.of(TardisDimensionArgumentType::new));

        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            TardisDebugCommand.register(dispatcher);
            TardisRemoveCommand.register(dispatcher);
        });
    }
}

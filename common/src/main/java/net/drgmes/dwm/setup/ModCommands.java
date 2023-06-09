package net.drgmes.dwm.setup;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.drgmes.dwm.commands.TardisDebugCommand;
import net.drgmes.dwm.commands.TardisRemoveCommand;

public class ModCommands {
    public static void setup() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            TardisDebugCommand.register(dispatcher);
            TardisRemoveCommand.register(dispatcher);
        });
    }
}

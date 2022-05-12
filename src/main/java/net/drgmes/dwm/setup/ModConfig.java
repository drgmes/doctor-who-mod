package net.drgmes.dwm.setup;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig {
    public static Client CLIENT;
    public static ForgeConfigSpec CLIENT_SPEC;

    static {
        Pair<Client, ForgeConfigSpec> specClientPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specClientPair.getValue();
        CLIENT = specClientPair.getKey();
    }

    public static class Client {
        public ForgeConfigSpec.BooleanValue enableBoti;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("Client Settings");

            enableBoti = builder.comment("Toggle \"The Bigger On the Inside\" effect on the Tardis Interior Door and Tardis Exterior")
            .translation("config.dwm.tardis.enable_boti")
            .define("enableBoti", false);

            builder.pop();
        }
    }
}

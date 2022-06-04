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
        public ForgeConfigSpec.BooleanValue botiEnabled;
        public ForgeConfigSpec.IntValue botiExteriorDistance;
        public ForgeConfigSpec.IntValue botiExteriorRadius;
        public ForgeConfigSpec.IntValue botiInteriorDistance;
        public ForgeConfigSpec.IntValue botiInteriorRadius;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("Client Settings");

            builder.push("BOTI");

            botiEnabled = builder.comment("Toggle \"The Bigger On the Inside\" effect on the Tardis Interior Door and Tardis Exterior")
            .translation("config.dwm.tardis.botiEnabled")
            .define("botiEnabled", true);

            botiExteriorDistance = builder.comment("Max distance for rendering \"The Bigger On the Inside\" effect in the Tardis")
            .translation("config.dwm.tardis.botiExteriorDistance")
            .defineInRange("botiExteriorDistance", 26, 0, 100);

            botiExteriorRadius = builder.comment("Max radius for rendering \"The Bigger On the Inside\" effect in the Tardis")
            .translation("config.dwm.tardis.botiExteriorRadius")
            .defineInRange("botiExteriorRadius", 5, 0, 100);

            botiInteriorDistance = builder.comment("Max distance for rendering \"The Bigger On the Inside\" effect outside the Tardis")
            .translation("config.dwm.tardis.botiInteriorDistance")
            .defineInRange("botiInteriorDistance", 26, 0, 100);

            botiInteriorRadius = builder.comment("Max radius for rendering \"The Bigger On the Inside\" effect outside the Tardis")
            .translation("config.dwm.tardis.botiInteriorRadius")
            .defineInRange("botiInteriorRadius", 5, 0, 100);

            builder.pop();

            builder.pop();
        }
    }
}

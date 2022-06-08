package net.drgmes.dwm.setup;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig {
    public static Common COMMON;
    public static ForgeConfigSpec COMMON_SPEC;

    public static Client CLIENT;
    public static ForgeConfigSpec CLIENT_SPEC;

    public static Server SERVER;
    public static ForgeConfigSpec SERVER_SPEC;

    static {
        Pair<Common, ForgeConfigSpec> specCommonPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specCommonPair.getValue();
        COMMON = specCommonPair.getKey();

        Pair<Client, ForgeConfigSpec> specClientPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specClientPair.getValue();
        CLIENT = specClientPair.getKey();

        Pair<Server, ForgeConfigSpec> specServerPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specServerPair.getValue();
        SERVER = specServerPair.getKey();
    }

    public static class Common {
        public ForgeConfigSpec.IntValue tardisMaxFlightTime;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("Common Settings");

            builder.push("TARDIS");

            tardisMaxFlightTime = builder.comment("Max flight time for Tardis")
            .translation("config.dwm.tardis.tardisMaxFlightTime")
            .defineInRange("tardisMaxFlightTime", 4000, 1, Integer.MAX_VALUE);

            builder.pop();

            builder.pop();
        }
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

    public static class Server {
        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("Server Settings");

            builder.pop();
        }
    }
}

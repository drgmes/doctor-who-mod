package net.drgmes.dwm.setup;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

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
            {
                builder.push("TARDIS");
                {
                    tardisMaxFlightTime = builder.comment("Max flight time for Tardis")
                        .translation("config.dwm.tardis.tardisMaxFlightTime")
                        .defineInRange("tardisMaxFlightTime", 4000, 1, Integer.MAX_VALUE);
                }
                builder.pop();
            }
            builder.pop();
        }
    }

    public static class Client {
        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("Client Settings");
            {
            }
            builder.pop();
        }
    }

    public static class Server {
        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("Server Settings");
            {
            }
            builder.pop();
        }
    }
}

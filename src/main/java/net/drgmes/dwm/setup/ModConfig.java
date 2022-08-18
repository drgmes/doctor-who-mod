package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {
    public static Common COMMON;
    public static Client CLIENT;
    public static Server SERVER;

    public static void setup() {
        Pair<Common, ForgeConfigSpec> specCommonPair = new ForgeConfigSpec.Builder().configure(Common::new);
        Pair<Client, ForgeConfigSpec> specClientPair = new ForgeConfigSpec.Builder().configure(Client::new);
        Pair<Server, ForgeConfigSpec> specServerPair = new ForgeConfigSpec.Builder().configure(Server::new);

        ModLoadingContext.registerConfig(DWM.MODID, net.minecraftforge.fml.config.ModConfig.Type.COMMON, specCommonPair.getValue());
        ModLoadingContext.registerConfig(DWM.MODID, net.minecraftforge.fml.config.ModConfig.Type.CLIENT, specClientPair.getValue());
        ModLoadingContext.registerConfig(DWM.MODID, net.minecraftforge.fml.config.ModConfig.Type.SERVER, specServerPair.getValue());

        COMMON = specCommonPair.getKey();
        CLIENT = specClientPair.getKey();
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

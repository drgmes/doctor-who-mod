package net.drgmes.dwm.setup;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ModConfig {
    public static Common COMMON;
    public static ForgeConfigSpec COMMON_SPEC;

    public static Client CLIENT;
    public static ForgeConfigSpec CLIENT_SPEC;

    public static Server SERVER;
    public static ForgeConfigSpec SERVER_SPEC;

    public static void setup() {
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
        public ForgeConfigSpec.ConfigValue<List<? extends String>> dimensionsBlacklist;
        public ForgeConfigSpec.BooleanValue hideTheEndConditionally;
        public ForgeConfigSpec.BooleanValue tardisRecallOperatorOnly;
        public ForgeConfigSpec.IntValue tardisFlightDistanceRate;
        public ForgeConfigSpec.IntValue tardisMaxFlightTime;
        public ForgeConfigSpec.IntValue tardisFuelRefillTiming;
        public ForgeConfigSpec.IntValue tardisFuelConsumeTiming;
        public ForgeConfigSpec.IntValue tardisFuelToEnergyRating;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("CommonSettings");
            {
                builder.push("TARDIS");
                {
                    dimensionsBlacklist = builder
                        .comment("List of Dimensions that will not be displayed in the TARDIS Dimensions List", "Example: minecraft:the_end")
                        .translation("config.dwm.tardis.dimensionsBlacklist")
                        .defineList("dimensionsBlacklist", Lists.newArrayList(
                            "immersive_portals:alternate1",
                            "immersive_portals:alternate2",
                            "immersive_portals:alternate3",
                            "immersive_portals:alternate4",
                            "immersive_portals:alternate5"
                        ), String.class::isInstance);

                    hideTheEndConditionally = builder
                        .comment("Hide The End from the TARDIS Dimensions List until the Dragon is defeated")
                        .translation("config.dwm.tardis.hideTheEndConditionally")
                        .define("hideTheEndConditionally", true);

                    tardisRecallOperatorOnly = builder
                        .comment("Allow only operators to recall the TARDIS by key")
                        .translation("config.dwm.tardis.tardisRecallOperatorOnly")
                        .define("tardisRecallOperatorOnly", false);

                    tardisFlightDistanceRate = builder
                        .comment("Time ratio divider in ticks per distance traveled (aka TARDIS speed)")
                        .translation("config.dwm.tardis.tardisFlightDistanceRate")
                        .defineInRange("tardisFlightDistanceRate", 1000, 1, Integer.MAX_VALUE);

                    tardisMaxFlightTime = builder
                        .comment("Max flight time in ticks for TARDIS")
                        .translation("config.dwm.tardis.tardisMaxFlightTime")
                        .defineInRange("tardisMaxFlightTime", 4000, 1, Integer.MAX_VALUE);

                    tardisFuelRefillTiming = builder
                        .comment("Frequency of restoration of 1 unit of fuel")
                        .translation("config.dwm.tardis.tardisFuelRefillTiming")
                        .defineInRange("tardisFuelRefillTiming", 40, 1, Integer.MAX_VALUE);

                    tardisFuelConsumeTiming = builder
                        .comment("Frequency of consumption of 1 unit of fuel")
                        .translation("config.dwm.tardis.tardisFuelConsumeTiming")
                        .defineInRange("tardisFuelConsumeTiming", 20, 1, Integer.MAX_VALUE);

                    tardisFuelToEnergyRating = builder
                        .comment("Price of energy per flight tick when fuel is over")
                        .translation("config.dwm.tardis.tardisFuelToEnergyRating")
                        .defineInRange("tardisFuelToEnergyRating", 5000, 1, Integer.MAX_VALUE);
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

package net.drgmes.dwm.setup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.drgmes.dwm.world.generator.TardisChunkGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.function.Supplier;

public class ModDimensions {
    public static ArrayList<RegistryKey<World>> WORLDS = new ArrayList<>();
    public static Supplier<Codec<? extends ChunkGenerator>> TARDIS_CHUNK_GENERATOR = Registration.registerChunkGenerator("tardis", () -> TardisChunkGenerator.CODEC);

    public static void init() {
    }

    public static void addWorldToRegistry(MinecraftServer server, RegistryKey<World> worldKey) {
        if (WORLDS.contains(worldKey)) return;
        WORLDS.add(worldKey);
        saveWorldsRegistry(server);
    }

    public static void removeWorldFromRegistry(MinecraftServer server, RegistryKey<World> worldKey) {
        if (!WORLDS.contains(worldKey)) return;
        WORLDS.remove(worldKey);
        saveWorldsRegistry(server);
    }

    public static File getWorldsRegistryFile(MinecraftServer server) {
        return new File(server.getSavePath(WorldSavePath.ROOT).toFile(), DWM.MODID + "_worlds.json");
    }

    public static void saveWorldsRegistry(MinecraftServer server) {
        JsonArray json = new JsonArray();
        for (RegistryKey<World> world : WORLDS) {
            json.add(world.getValue().getPath());
            DWM.LOGGER.info("Saved " + world.getValue().getPath());
        }

        try (PrintWriter printWriter = new PrintWriter(new FileWriter(getWorldsRegistryFile(server)))) {
            printWriter.write(json.toString());
        } catch (Exception e) {
            DWM.LOGGER.error("Error in saving worlds registry (" + e.getMessage() + ")");
        }
    }

    public static void loadWorldsRegistry(MinecraftServer server) {
        WORLDS.clear();

        try (FileInputStream fileInputStream = new FileInputStream(getWorldsRegistryFile(server))) {
            String content = IOUtils.toString(fileInputStream);
            JsonArray json = JsonParser.parseString(content).getAsJsonArray();

            for (JsonElement worldId : json) {
                DWM.LOGGER.info("Loaded " + worldId);
                WORLDS.add(RegistryKey.of(RegistryKeys.WORLD, DWM.getIdentifier(worldId.getAsString())));
                DimensionHelper.getOrCreateWorld(worldId.getAsString(), server, TardisHelper::tardisDimensionBuilder);
            }
        } catch (Exception e) {
            DWM.LOGGER.error("Error in loading worlds registry (" + e.getMessage() + ")");
        }
    }

    public static class ModDimensionTypes {
        public static final RegistryKey<DimensionType> TARDIS = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, DWM.LOCS.TARDIS);
    }
}

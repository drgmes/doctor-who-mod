package net.drgmes.dwm.setup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.ars.ArsCategories;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.common.tardis.ars.ArsStructures;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRooms;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class ModResourcePacks {
    public static void setup(ResourceManager manager) {
        Predicate<Identifier> jsonPredicate = (path) -> path.getPath().endsWith(".json");

        clear();
        loadConsoleRooms(manager.findResources("tardis/console_rooms", jsonPredicate));
        loadArsCategories(manager.findResources("tardis/ars_categories", jsonPredicate));
        loadArsRooms(manager.findResources("tardis/ars_rooms", jsonPredicate));
    }

    private static void clear() {
        TardisConsoleRooms.CONSOLE_ROOMS.clear();
    }

    private static void loadConsoleRooms(Map<Identifier, Resource> consoleRoomsResources) {
        AtomicInteger count = new AtomicInteger(0);

        consoleRoomsResources.forEach((id, resource) -> {
            try {
                InputStream stream = resource.getInputStream();
                String path = id.getPath().replace("tardis/console_rooms/", "");
                if (path.contains("/")) return;

                String consoleRoomsName = path.replace(".json", "");
                InputStreamReader inputStreamReader = new InputStreamReader(stream);
                JsonObject data = JsonHelper.deserialize(inputStreamReader);

                if (data.has("disable") && data.get("disable").getAsBoolean()) return;

                String title = data.has("title") ? data.get("title").getAsString() : consoleRoomsName;
                JsonArray centerArray = data.has("center") ? data.get("center").getAsJsonArray() : null;
                JsonArray entranceArray = data.has("entrance") ? data.get("entrance").getAsJsonArray() : null;
                int spawnChance = data.has("spawnChance") ? data.get("spawnChance").getAsInt() : 1;

                BlockPos center = centerArray != null && centerArray.size() == 3
                    ? new BlockPos(centerArray.get(0).getAsInt(), centerArray.get(1).getAsInt(), centerArray.get(2).getAsInt())
                    : (BlockPos) BlockPos.ZERO;

                BlockPos entrance = entranceArray != null && entranceArray.size() == 3
                    ? new BlockPos(entranceArray.get(0).getAsInt(), entranceArray.get(1).getAsInt(), entranceArray.get(2).getAsInt())
                    : (BlockPos) BlockPos.ZERO;

                TardisConsoleRoomEntry consoleRoom = TardisConsoleRoomEntry.create(consoleRoomsName, title, center, entrance, spawnChance);
                consoleRoom.setHidden(data.has("hidden") && data.get("hidden").getAsBoolean());
                if (data.has("image")) consoleRoom.setImageUrl(data.get("image").getAsString());
                if (data.has("repair_to")) consoleRoom.setRepairTo(data.get("repair_to").getAsString());
                if (data.has("decorator_block")) consoleRoom.setDecoratorBlock(data.get("decorator_block").getAsString());
                if (data.has("teleporter_room")) consoleRoom.setTeleporterRoom(data.get("teleporter_room").getAsString());

                count.getAndIncrement();
            } catch (Exception e) {
                DWM.LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
            }
        });

        DWM.LOGGER.info("Loaded " + count.get() + "/" + consoleRoomsResources.size() + " console rooms.");
    }

    private static void loadArsCategories(Map<Identifier, Resource> arsCategoriesResources) {
        AtomicInteger count = new AtomicInteger(0);

        arsCategoriesResources.forEach((id, resource) -> {
            try {
                InputStream stream = resource.getInputStream();
                String path = id.getPath().replace("tardis/ars_categories/", "");
                if (path.contains("/")) return;

                String arsCategoryName = path.replace(".json", "");
                InputStreamReader inputStreamReader = new InputStreamReader(stream);
                JsonObject data = JsonHelper.deserialize(inputStreamReader);

                if (data.has("disable") && data.get("disable").getAsBoolean()) return;

                String parent = data.has("parent") ? data.get("parent").getAsString() : "";
                String title = data.has("title") ? data.get("title").getAsString() : arsCategoryName;
                String tag = data.has("tag") ? data.get("tag").getAsString() : arsCategoryName;

                ArsCategories.register(arsCategoryName, title, tag, parent);
                count.getAndIncrement();
            } catch (Exception e) {
                DWM.LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
            }
        });

        DWM.LOGGER.info("Loaded " + count.get() + "/" + arsCategoriesResources.size() + " ars categories.");
    }

    private static void loadArsRooms(Map<Identifier, Resource> arsRoomsResources) {
        AtomicInteger count = new AtomicInteger(0);

        arsRoomsResources.forEach((id, resource) -> {
            try {
                InputStream stream = resource.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(stream);
                JsonObject data = JsonHelper.deserialize(inputStreamReader);

                if (data.has("disable") && data.get("disable").getAsBoolean()) return;

                String path = id.getPath().replace("tardis/ars_rooms/", "");
                String[] pathParts = path.split("/");

                String categoryName = String.join("_", Arrays.copyOfRange(pathParts, 0, pathParts.length - 1));
                String arsRoomName = (!categoryName.equals("") ? categoryName + "_" : "") + pathParts[pathParts.length - 1].replace(".json", "");

                String title = data.has("title") ? data.get("title").getAsString() : arsRoomName;
                String structure = data.has("structure") ? data.get("structure").getAsString() : null;
                Map<String, JsonElement> replaceables = data.has("replaces") ? data.get("replaces").getAsJsonObject().asMap() : null;
                if (structure == null) return;

                ArsStructure arsStructure = ArsStructures.register(arsRoomName, structure, title, categoryName);
                if (replaceables != null) arsStructure.setReplaceables(replaceables);

                count.getAndIncrement();
            } catch (Exception e) {
                DWM.LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
            }
        });

        DWM.LOGGER.info("Loaded " + count.get() + "/" + arsRoomsResources.size() + " ars rooms.");
    }
}

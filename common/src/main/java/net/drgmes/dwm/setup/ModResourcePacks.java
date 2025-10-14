package net.drgmes.dwm.setup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.ars.ArsCategories;
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
        loadArsEntries(manager.findResources("tardis/ars", jsonPredicate));
    }

    private static void clear() {
        TardisConsoleRooms.CONSOLE_ROOMS.clear();
        ArsCategories.CATEGORIES.clear();
        ArsStructures.STRUCTURES.clear();
    }

    private static void loadConsoleRooms(Map<Identifier, Resource> consoleRoomResources) {
        AtomicInteger count = new AtomicInteger(0);

        consoleRoomResources.forEach((id, resource) -> {
            try {
                InputStream stream = resource.getInputStream();
                String path = id.getPath().replace("tardis/console_rooms/", "");
                if (path.contains("/")) return;

                String consoleRoomName = path.replace(".json", "");
                InputStreamReader inputStreamReader = new InputStreamReader(stream);
                JsonObject data = JsonHelper.deserialize(inputStreamReader);

                if (data.has("disable") && data.get("disable").getAsBoolean()) return;

                String title = data.has("title") ? data.get("title").getAsString() : consoleRoomName;
                JsonArray centerArray = data.has("center") ? data.get("center").getAsJsonArray() : null;
                JsonArray entranceArray = data.has("entrance") ? data.get("entrance").getAsJsonArray() : null;
                String structure = data.has("structure") ? data.get("structure").getAsString() : null;
                int spawnChance = data.has("spawnChance") ? data.get("spawnChance").getAsInt() : 1;

                BlockPos center = centerArray != null && centerArray.size() == 3
                    ? new BlockPos(centerArray.get(0).getAsInt(), centerArray.get(1).getAsInt(), centerArray.get(2).getAsInt())
                    : (BlockPos) BlockPos.ZERO;

                BlockPos entrance = entranceArray != null && entranceArray.size() == 3
                    ? new BlockPos(entranceArray.get(0).getAsInt(), entranceArray.get(1).getAsInt(), entranceArray.get(2).getAsInt())
                    : (BlockPos) BlockPos.ZERO;

                TardisConsoleRoomEntry consoleRoom = new TardisConsoleRoomEntry(consoleRoomName, title, structure, spawnChance, center, entrance);
                consoleRoom.setHidden(data.has("hidden") && data.get("hidden").getAsBoolean());
                if (data.has("image")) consoleRoom.setImageUrl(data.get("image").getAsString());
                if (data.has("repair_to")) consoleRoom.setRepairTo(data.get("repair_to").getAsString());
                if (data.has("doors_block")) consoleRoom.setDoorsBlock(data.get("doors_block").getAsString());
                if (data.has("decorator_block")) consoleRoom.setDecoratorBlock(data.get("decorator_block").getAsString());
                if (data.has("teleporter_room")) consoleRoom.setTeleporterRoom(data.get("teleporter_room").getAsString());

                TardisConsoleRooms.CONSOLE_ROOMS.put(consoleRoomName, consoleRoom);
                count.getAndIncrement();
            } catch (Exception e) {
                DWM.LOGGER.error("Error occurred while loading resource json {}", id.toString(), e);
            }
        });

        DWM.LOGGER.info("Loaded {}/{} console rooms.", count.get(), consoleRoomResources.size());
    }

    private static void loadArsEntries(Map<Identifier, Resource> arsEntryResources) {
        AtomicInteger categoriesCount = new AtomicInteger(0);
        AtomicInteger roomsCount = new AtomicInteger(0);

        arsEntryResources.forEach((id, resource) -> {
            try {
                InputStream stream = resource.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(stream);
                JsonObject data = JsonHelper.deserialize(inputStreamReader);

                if (data.has("disable") && data.get("disable").getAsBoolean()) return;

                String[] pathParts = id.getPath().replace("tardis/ars/", "").split("/");
                String categoryName = String.join("_", Arrays.copyOfRange(pathParts, 0, pathParts.length - 1));
                String entryName = pathParts[pathParts.length - 1].replace(".json", "").replaceAll("^[0-9]+-", "");

                if (entryName.equals("_category")) {
                    String parent;
                    if (data.has("parent")) parent = data.get("parent").getAsString();
                    else parent = String.join("_", Arrays.copyOfRange(pathParts, 0, pathParts.length - 2));

                    String title = data.has("title") ? data.get("title").getAsString() : categoryName;
                    String tag = data.has("tag") ? data.get("tag").getAsString() : categoryName;

                    ArsCategories.register(categoryName, title, tag, parent, categoriesCount.getAndIncrement());
                }
                else if (!entryName.startsWith("_")) {
                    String roomName;
                    if (data.has("id")) roomName = data.get("id").getAsString();
                    else roomName = (!categoryName.isEmpty() ? categoryName + "_" : "") + entryName;

                    String title = data.has("title") ? data.get("title").getAsString() : roomName;
                    String structure = data.has("structure") ? data.get("structure").getAsString() : null;
                    Map<String, JsonElement> substitutes = data.has("substitutes") ? data.get("substitutes").getAsJsonObject().asMap() : null;
                    if (structure == null) return;

                    ArsStructures.register(roomName, structure, title, categoryName, roomsCount.getAndIncrement()).setSubstitutes(substitutes);
                }
            } catch (Exception e) {
                DWM.LOGGER.error("Error occurred while loading resource json {}", id.toString(), e);
            }
        });

        DWM.LOGGER.info("Loaded {} ars categories.", categoriesCount.get());
        DWM.LOGGER.info("Loaded {} ars rooms.", roomsCount.get());
    }
}

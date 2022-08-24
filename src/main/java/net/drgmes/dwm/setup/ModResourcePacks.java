package net.drgmes.dwm.setup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRooms;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.Blocks;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class ModResourcePacks {
    public static void setup() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return DWM.getIdentifier("resources");
            }

            @Override
            public void reload(ResourceManager manager) {
                clear();

                DWM.LOGGER.info("Loading console rooms");
                loadConsoleRooms(manager.findResources("console_rooms", (path) -> path.getPath().endsWith(".json")));
            }
        });
    }

    private static void clear() {
        TardisConsoleRooms.CONSOLE_ROOMS.clear();
    }

    private static void loadConsoleRooms(Map<Identifier, Resource> consoleRoomsResources) {
        consoleRoomsResources.forEach((id, resource) -> {
            try {
                InputStream stream = resource.getInputStream();
                String path = id.getPath().replace("console_rooms/", "");
                if (path.contains("/")) return;

                String consoleRoomsName = path.replace(".json", "");
                InputStreamReader inputStreamReader = new InputStreamReader(stream);
                JsonObject data = JsonHelper.deserialize(inputStreamReader);

                String title = data.has("title") ? data.get("title").getAsString() : consoleRoomsName;
                JsonArray centerArray = data.has("center") ? data.get("center").getAsJsonArray() : null;
                JsonArray entranceArray = data.has("entrance") ? data.get("entrance").getAsJsonArray() : null;
                String decoratorBlock = data.has("decorator_block") ? data.get("decorator_block").getAsString() : Registry.BLOCK.getId(Blocks.GRAY_TERRACOTTA).toString();
                String imageUrl = data.has("image") ? data.get("image").getAsString() : "";
                boolean isHidden = data.has("hidden") && data.get("hidden").getAsBoolean();

                BlockPos center = centerArray != null && centerArray.size() == 3
                    ? new BlockPos(centerArray.get(0).getAsInt(), centerArray.get(1).getAsInt(), centerArray.get(2).getAsInt())
                    : (BlockPos) BlockPos.ZERO;

                BlockPos entrance = entranceArray != null && entranceArray.size() == 3
                    ? new BlockPos(entranceArray.get(0).getAsInt(), entranceArray.get(1).getAsInt(), entranceArray.get(2).getAsInt())
                    : (BlockPos) BlockPos.ZERO;

                TardisConsoleRoomEntry.create(consoleRoomsName, title, center, entrance, decoratorBlock, imageUrl, isHidden);
            } catch (Exception e) {
                DWM.LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
            }
        });
    }
}

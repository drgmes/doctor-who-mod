package net.drgmes.dwm.network;

import com.mojang.datafixers.util.Pair;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.blocks.tardis.consoles.screens.TardisConsoleMonitorConsoleRoomsScreen;
import net.drgmes.dwm.blocks.tardis.consoles.screens.TardisConsoleTelepathicInterfaceLocationsScreen;
import net.drgmes.dwm.blocks.tardis.consoles.screens.TardisConsoleTelepathicInterfaceMapBannersScreen;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRooms;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.PacketHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapBannerMarker;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.structure.Structure;

import java.util.*;

public class TardisConsoleRemoteCallablePackets {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    // /////////// //
    // Clientbound //
    // /////////// //

    public static void updateTardisConsoleData(BlockPos blockPos, NbtCompound tag) {
        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
            tardisConsoleBlockEntity.readNbt(tag);
            tardisConsoleBlockEntity.markDirty();
        }
    }

    public static void updateTardisConsoleControlsStates(BlockPos blockPos, NbtCompound tag) {
        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
            TardisConsoleControlsStorage controlsStorage = new TardisConsoleControlsStorage();
            controlsStorage.load(tag);

            tardisConsoleBlockEntity.controlsStorage = controlsStorage;
            tardisConsoleBlockEntity.markDirty();
        }
    }

    public static void updateTardisConsoleMonitorPage(BlockPos blockPos, int monitorPage) {
        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
            tardisConsoleBlockEntity.monitorPage = monitorPage;
            tardisConsoleBlockEntity.markDirty();
        }
    }

    public static void updateTardisConsoleScrewdriverSlot(BlockPos blockPos, ItemStack screwdriverItemStack) {
        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
            tardisConsoleBlockEntity.screwdriverItemStack = screwdriverItemStack;
            tardisConsoleBlockEntity.markDirty();
        }
    }

    public static void clearTardisConsoleScrewdriverSlot(BlockPos blockPos) {
        updateTardisConsoleScrewdriverSlot(blockPos, ItemStack.EMPTY);
    }

    public static void openTardisConsoleMonitor(BlockPos blockPos, String tardisId, String consoleRoomId) {
        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
            mc.setScreen(new TardisConsoleMonitorConsoleRoomsScreen(tardisConsoleBlockEntity, tardisId, consoleRoomId));
        }
    }

    public static void openTardisConsoleTelepathicInterfaceLocations(BlockPos blockPos, NbtCompound tag) {
        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
            List<Map.Entry<Identifier, TardisConsoleTelepathicInterfaceLocationsScreen.EDataType>> locations = new ArrayList<>();
            List<String> keys = new ArrayList<>(tag.getKeys().stream().toList());
            keys.sort(Comparator.comparing((key) -> key));

            keys.forEach((key) -> {
                locations.add(Map.entry(
                    new Identifier(tag.getCompound(key).getString("id")),
                    TardisConsoleTelepathicInterfaceLocationsScreen.EDataType.valueOf(tag.getCompound(key).getString("type"))
                ));
            });

            mc.setScreen(new TardisConsoleTelepathicInterfaceLocationsScreen(tardisConsoleBlockEntity, locations));
        }
    }

    public static void openTardisConsoleTelepathicInterfaceMapBanners(BlockPos blockPos, NbtCompound tag) {
        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
            mc.setScreen(new TardisConsoleTelepathicInterfaceMapBannersScreen(tardisConsoleBlockEntity, MapState.fromNbt(tag)));
        }
    }

    // /////////// //
    // Serverbound //
    // /////////// //

    public static void initTardisConsoleData(ServerPlayerEntity player, BlockPos blockPos) {
        if (player.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
            TardisStateManager.get((ServerWorld) player.world).ifPresent((tardis) -> {
                if (!tardis.isValid()) return;

                NbtCompound tardisData = tardisConsoleBlockEntity.createNbt();

                tardis.applyDataToControlsStorage(tardisConsoleBlockEntity.controlsStorage);
                tardisConsoleBlockEntity.controlsStorage.save(tardisData);
                tardis.writeNbt(tardisData);

                tardisConsoleBlockEntity.readNbt(tardisData);
                tardisConsoleBlockEntity.markDirty();

                PacketHelper.sendToClient(
                    TardisConsoleRemoteCallablePackets.class,
                    "updateTardisConsoleData",
                    player.world.getWorldChunk(blockPos),
                    blockPos, tardisData
                );
            });
        }
    }

    public static void applyTardisConsoleMonitorConsoleRoom(ServerPlayerEntity player, String tardisId, String consoleRoomId) {
        Text failMessage = Text.translatable("message." + DWM.MODID + ".tardis.monitor.console_rooms.failed");

        if (!TardisConsoleRooms.CONSOLE_ROOMS.containsKey(consoleRoomId)) {
            player.sendMessage(failMessage, true);
            return;
        }

        ServerWorld tardisWorld = DimensionHelper.getModWorld(tardisId);
        if (tardisWorld == null) { // TODO || tardisWorld == player.world
            player.sendMessage(failMessage, true);
            return;
        }

        TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
            TardisConsoleRoomEntry consoleRoom = TardisConsoleRooms.getConsoleRoom(consoleRoomId);
            boolean isConsoleRoomGenerated = consoleRoom.place(tardis);

            if (isConsoleRoomGenerated) {
                tardis.setConsoleRoom(consoleRoom);
                tardis.updateEntrancePortals();
            }

            player.sendMessage(Text.translatable("message." + DWM.MODID + ".tardis.monitor.console_rooms." + (isConsoleRoomGenerated ? "success" : "failed")), true);
        });
    }

    public static void applyTardisConsoleTelepathicInterfaceLocation(ServerPlayerEntity player, Identifier id, String type) {
        TardisStateManager.get((ServerWorld) player.world).ifPresent((tardis) -> {
            if (!tardis.isValid()) return;

            Text message = null;
            TardisConsoleTelepathicInterfaceLocationsScreen.EDataType dataType = TardisConsoleTelepathicInterfaceLocationsScreen.EDataType.valueOf(type);

            if (dataType == TardisConsoleTelepathicInterfaceLocationsScreen.EDataType.BIOME) {
                String msg = findBiome(id, tardis) ? "found" : "not_found";
                message = Text.translatable("message." + DWM.MODID + ".tardis.telepathic_interface.biome." + msg);
            }
            else if (dataType == TardisConsoleTelepathicInterfaceLocationsScreen.EDataType.STRUCTURE) {
                String msg = findStructure(id, tardis) ? "found" : "not_found";
                message = Text.translatable("message." + DWM.MODID + ".tardis.telepathic_interface.structure." + msg);
            }

            if (message != null) player.sendMessage(message, true);
        });
    }

    public static void applyTardisConsoleTelepathicInterfaceMapBanner(ServerPlayerEntity player, NbtCompound tag) {
        TardisStateManager.get((ServerWorld) player.world).ifPresent((tardis) -> {
            if (!tardis.isValid()) return;

            MapState mapState = MapState.fromNbt(tag.getCompound("mapState"));
            MapBannerMarker mapBannerMarker = MapBannerMarker.fromNbt(tag.getCompound("mapBannerMarker"));

            String color = "\u00A7e" + mapBannerMarker.getColor().getName().toUpperCase().replace("_", " ");
            player.sendMessage(Text.translatable("message." + DWM.MODID + ".tardis.telepathic_interface.map.loaded.banner", color), true);

            tardis.setDestinationDimension(mapState.dimension);
            tardis.setDestinationPosition(mapBannerMarker.getPos());
            tardis.updateConsoleTiles();
        });
    }

    // ///// //
    // Utils //
    // ///// //

    private static boolean findBiome(Identifier id, TardisStateManager tardis) {
        BlockPos exteriorPos = tardis.getDestinationExteriorPosition();
        ServerWorld exteriorWorld = DimensionHelper.getWorld(tardis.getDestinationExteriorDimension());
        if (exteriorWorld == null) return false;

        Pair<BlockPos, RegistryEntry<Biome>> pair = exteriorWorld.locateBiome((entry) -> entry.matchesId(id), exteriorPos, 6400, 32, 64);
        if (pair == null) return false;

        boolean isUnderground = false;
        if (exteriorWorld.getRegistryKey() == World.NETHER) isUnderground = true;

        BlockPos blockPos = pair.getFirst().withY(exteriorWorld.getTopY() - 2);
        if (isUnderground) blockPos = blockPos.withY(exteriorWorld.getBottomY());

        tardis.getSystem(TardisSystemMaterialization.class).setSafeDirection(isUnderground
            ? TardisSystemMaterialization.ESafeDirection.BOTTOM
            : TardisSystemMaterialization.ESafeDirection.TOP
        );

        tardis.setDestinationPosition(blockPos);
        tardis.updateConsoleTiles();
        return true;
    }

    private static boolean findStructure(Identifier id, TardisStateManager tardis) {
        BlockPos exteriorPos = tardis.getDestinationExteriorPosition();
        ServerWorld exteriorWorld = DimensionHelper.getWorld(tardis.getDestinationExteriorDimension());
        if (exteriorWorld == null) return false;

        Registry<Structure> registry = exteriorWorld.getRegistryManager().get(Registry.STRUCTURE_KEY);

        Structure structure = registry.get(id);
        if (structure == null) return false;

        Optional<RegistryKey<Structure>> structureKeyHolder = registry.getKey(structure);
        if (structureKeyHolder.isEmpty()) return false;

        Optional<RegistryEntry<Structure>> structureEntry = registry.getEntry(structureKeyHolder.get());
        if (structureEntry.isEmpty()) return false;

        Pair<BlockPos, RegistryEntry<Structure>> pair = exteriorWorld.getChunkManager().getChunkGenerator().locateStructure(exteriorWorld, RegistryEntryList.of(structureEntry.get()), exteriorPos, 512, false);
        if (pair == null) return false;

        boolean isUnderground = false;
        if (exteriorWorld.getRegistryKey() == World.NETHER) isUnderground = true;
        else if (structure.getFeatureGenerationStep() == GenerationStep.Feature.STRONGHOLDS) isUnderground = true;
        else if (structure.getFeatureGenerationStep() == GenerationStep.Feature.UNDERGROUND_DECORATION) isUnderground = true;
        else if (structure.getFeatureGenerationStep() == GenerationStep.Feature.UNDERGROUND_STRUCTURES) isUnderground = true;

        BlockPos blockPos = pair.getFirst().withY(exteriorWorld.getTopY() - 2);
        if (isUnderground) blockPos = blockPos.withY(exteriorWorld.getBottomY());

        tardis.getSystem(TardisSystemMaterialization.class).setSafeDirection(isUnderground
            ? TardisSystemMaterialization.ESafeDirection.BOTTOM
            : TardisSystemMaterialization.ESafeDirection.TOP
        );

        tardis.setDestinationPosition(blockPos);
        tardis.updateConsoleTiles();
        return true;
    }
}

package net.drgmes.dwm.network;

import com.mojang.datafixers.util.Pair;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.blocks.tardis.consoleunits.screens.TardisConsoleUnitMonitorConsoleRoomsScreen;
import net.drgmes.dwm.blocks.tardis.consoleunits.screens.TardisConsoleUnitTelepathicInterfaceLocationsScreen;
import net.drgmes.dwm.blocks.tardis.consoleunits.screens.TardisConsoleUnitTelepathicInterfaceMapBannersScreen;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRooms;
import net.drgmes.dwm.common.tardis.consoleunits.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.PacketHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

public class TardisConsoleUnitRemoteCallablePackets {
    // /////////// //
    // Clientbound //
    // /////////// //

    @Environment(EnvType.CLIENT)
    public static void updateTardisConsoleUnitData(BlockPos blockPos, NbtCompound tag) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            tardisConsoleUnitBlockEntity.readNbt(tag);
            tardisConsoleUnitBlockEntity.markDirty();
        }
    }

    @Environment(EnvType.CLIENT)
    public static void updateTardisConsoleUnitControlsStates(BlockPos blockPos, NbtCompound tag) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            TardisConsoleControlsStorage controlsStorage = new TardisConsoleControlsStorage();
            controlsStorage.load(tag);

            tardisConsoleUnitBlockEntity.controlsStorage = controlsStorage;
            tardisConsoleUnitBlockEntity.markDirty();
        }
    }

    @Environment(EnvType.CLIENT)
    public static void updateTardisConsoleUnitMonitorPage(BlockPos blockPos, int monitorPage) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            tardisConsoleUnitBlockEntity.monitorPage = monitorPage;
            tardisConsoleUnitBlockEntity.markDirty();
        }
    }

    @Environment(EnvType.CLIENT)
    public static void updateTardisConsoleUnitScrewdriverSlot(BlockPos blockPos, ItemStack screwdriverItemStack) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            tardisConsoleUnitBlockEntity.screwdriverItemStack = screwdriverItemStack;
            tardisConsoleUnitBlockEntity.markDirty();
        }
    }

    @Environment(EnvType.CLIENT)
    public static void clearTardisConsoleUnitScrewdriverSlot(BlockPos blockPos) {
        updateTardisConsoleUnitScrewdriverSlot(blockPos, ItemStack.EMPTY);
    }

    @Environment(EnvType.CLIENT)
    public static void openTardisConsoleUnitMonitor(BlockPos blockPos, String tardisId, String consoleRoomId, NbtCompound tag) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            NbtCompound consoleRoomsTag = tag.getCompound("consoleRooms");
            List<TardisConsoleRoomEntry> consoleRooms = new ArrayList<>();
            List<String> keys = new ArrayList<>(consoleRoomsTag.getKeys().stream().toList());

            keys.sort(Comparator.comparing((key) -> key));
            keys.forEach((key) -> consoleRooms.add(TardisConsoleRoomEntry.create(consoleRoomsTag.getCompound(key), false)));

            mc.setScreen(new TardisConsoleUnitMonitorConsoleRoomsScreen(tardisConsoleUnitBlockEntity, tardisId, consoleRoomId, tag.getInt("selectedConsoleRoomIndex"), consoleRooms));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void openTardisConsoleUnitTelepathicInterfaceLocations(BlockPos blockPos, NbtCompound tag) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            List<Map.Entry<Identifier, TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType>> locations = new ArrayList<>();
            List<String> keys = new ArrayList<>(tag.getKeys().stream().toList());
            keys.sort(Comparator.comparing((key) -> key));

            keys.forEach((key) -> {
                locations.add(Map.entry(
                    new Identifier(tag.getCompound(key).getString("id")),
                    TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType.valueOf(tag.getCompound(key).getString("type"))
                ));
            });

            mc.setScreen(new TardisConsoleUnitTelepathicInterfaceLocationsScreen(tardisConsoleUnitBlockEntity, locations));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void openTardisConsoleUnitTelepathicInterfaceMapBanners(BlockPos blockPos, NbtCompound tag) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            mc.setScreen(new TardisConsoleUnitTelepathicInterfaceMapBannersScreen(tardisConsoleUnitBlockEntity, MapState.fromNbt(tag)));
        }
    }

    // /////////// //
    // Serverbound //
    // /////////// //

    public static void initTardisConsoleUnitData(ServerPlayerEntity player, BlockPos blockPos) {
        if (player.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            TardisStateManager.get((ServerWorld) player.world).ifPresent((tardis) -> {
                if (!tardis.isValid()) return;

                NbtCompound tardisData = tardisConsoleUnitBlockEntity.createNbt();

                tardis.applyDataToControlsStorage(tardisConsoleUnitBlockEntity.controlsStorage);
                tardisConsoleUnitBlockEntity.controlsStorage.save(tardisData);
                tardis.writeNbt(tardisData);

                tardisConsoleUnitBlockEntity.readNbt(tardisData);
                tardisConsoleUnitBlockEntity.markDirty();

                PacketHelper.sendToClient(
                    TardisConsoleUnitRemoteCallablePackets.class,
                    "updateTardisConsoleUnitData",
                    player.world.getWorldChunk(blockPos),
                    blockPos, tardisData
                );
            });
        }
    }

    public static void applyTardisConsoleUnitMonitorConsoleRoom(ServerPlayerEntity player, String tardisId, String consoleRoomId) {
        Text failMessage = Text.translatable("message." + DWM.MODID + ".tardis.monitor.console_rooms.failed");

        if (!TardisConsoleRooms.CONSOLE_ROOMS.containsKey(consoleRoomId)) {
            player.sendMessage(failMessage, true);
            return;
        }

        ServerWorld tardisWorld = DimensionHelper.getModWorld(tardisId);
        if (tardisWorld == null) {
            player.sendMessage(failMessage, true);
            return;
        }

        TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
            TardisConsoleRoomEntry consoleRoom = TardisConsoleRooms.getConsoleRoom(consoleRoomId, tardis.isBroken());
            boolean isConsoleRoomGenerated = consoleRoom.place(tardis);

            if (isConsoleRoomGenerated) {
                tardis.setConsoleRoom(consoleRoom);
                tardis.updateConsoleTiles();
                tardis.updateEntrancePortals();
                tardis.updateRoomsEntrancesPortals();
            }

            player.sendMessage(Text.translatable("message." + DWM.MODID + ".tardis.monitor.console_rooms." + (isConsoleRoomGenerated ? "success" : "failed")), true);
        });
    }

    public static void applyTardisConsoleUnitTelepathicInterfaceLocation(ServerPlayerEntity player, Identifier id, String type) {
        TardisStateManager.get((ServerWorld) player.world).ifPresent((tardis) -> {
            if (!tardis.isValid()) return;

            Text message = null;
            TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType dataType = TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType.valueOf(type);

            if (dataType == TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType.BIOME) {
                String msg = findBiome(id, tardis) ? "found" : "not_found";
                message = Text.translatable("message." + DWM.MODID + ".tardis.telepathic_interface.biome." + msg);
            }
            else if (dataType == TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType.STRUCTURE) {
                String msg = findStructure(id, tardis) ? "found" : "not_found";
                message = Text.translatable("message." + DWM.MODID + ".tardis.telepathic_interface.structure." + msg);
            }

            if (message != null) player.sendMessage(message, true);
        });
    }

    public static void applyTardisConsoleUnitTelepathicInterfaceMapBanner(ServerPlayerEntity player, NbtCompound tag) {
        TardisStateManager.get((ServerWorld) player.world).ifPresent((tardis) -> {
            if (!tardis.isValid()) return;

            MapState mapState = MapState.fromNbt(tag.getCompound("mapState"));
            MapBannerMarker mapBannerMarker = MapBannerMarker.fromNbt(tag.getCompound("mapBannerMarker"));

            String color = "§e" + mapBannerMarker.getColor().getName().toUpperCase().replace("_", " ");
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

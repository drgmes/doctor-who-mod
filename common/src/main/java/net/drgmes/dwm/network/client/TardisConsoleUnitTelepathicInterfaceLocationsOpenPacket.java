package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.blocks.tardis.consoleunits.screens.TardisConsoleUnitTelepathicInterfaceLocationsScreen;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.TardisSystemResearch;
import net.drgmes.dwm.enums.TardisTelepathicInterfaceDataType;
import net.drgmes.dwm.network.IPacket;
import net.drgmes.dwm.setup.ModConfig;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public record TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket(
    BlockPos blockPos,
    NbtCompound tag
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_console_unit_telepathic_interface_locations_open");
    public static final CustomPayload.Id<TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket::blockPos,
        PacketCodecs.NBT_COMPOUND, TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket::tag,
        TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket::new
    );

    public TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket(BlockPos blockPos, ServerWorld originWorld, @Nullable ServerWorld destinationWorld) {
        this(blockPos, createLocationsListTag(originWorld, destinationWorld));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    @Environment(EnvType.CLIENT)
    public static void handle(TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
                List<Map.Entry<Identifier, TardisTelepathicInterfaceDataType>> locations = new ArrayList<>();
                List<String> keys = new ArrayList<>(payload.tag.getKeys());

                keys.sort(Comparator.comparing((key) -> key));
                keys.forEach((key) -> {
                    locations.add(Map.entry(
                        Identifier.of(payload.tag.getCompound(key).getString("id")),
                        TardisTelepathicInterfaceDataType.valueOf(payload.tag.getCompound(key).getString("type"))
                    ));
                });

                MinecraftClient.getInstance().setScreen(new TardisConsoleUnitTelepathicInterfaceLocationsScreen(tardisConsoleUnitBlockEntity, locations));
            }
        });
    }

    private static NbtCompound createLocationsListTag(ServerWorld world, @Nullable ServerWorld destinationWorld) {
        NbtCompound tag = new NbtCompound();
        if (!TardisHelper.isTardisDimension(world)) return tag;

        Optional<TardisStateManager> tardisHolder = TardisStateManager.get(world);
        if (tardisHolder.isEmpty()) return tag;

        TardisStateManager tardis = tardisHolder.get();
        TardisSystemResearch researchSystem = tardis.getSystem(TardisSystemResearch.class);

        List<RegistryKey<Biome>> biomeKeys = researchSystem.getAvailableBiomes();
        List<RegistryKey<Biome>> biomesByDimensionKeys = null;

        if (destinationWorld != null) {
            Set<RegistryEntry<Biome>> biomesByDimensionEntries = destinationWorld.getChunkManager().getChunkGenerator().getBiomeSource().getBiomes();
            biomesByDimensionKeys = biomesByDimensionEntries.stream().filter((entry) -> entry.getKey().isPresent()).map((entry) -> entry.getKey().get()).toList();
            biomeKeys = biomeKeys.stream().filter(biomesByDimensionKeys::contains).toList();
        }

        final List<RegistryKey<Biome>> finalBiomeKeys = biomeKeys;
        final List<RegistryKey<Biome>> finalBiomesByDimensionKeys = biomesByDimensionKeys;
        Registry<Structure> structureRegistry = world.getRegistryManager().get(RegistryKeys.STRUCTURE);

        List<RegistryKey<Structure>> structureKeys = researchSystem.getAvailableStructures().stream().filter((key) -> {
            Structure structure = structureRegistry.get(key);
            if (structure == null) return false;

            return structure.getValidBiomes().stream().anyMatch((biome) -> {
                if (biome.getKey().isEmpty()) return false;
                if (ModConfig.COMMON.filterStructuresByVisitedBiomes.get()) return finalBiomeKeys.contains(biome.getKey().get());
                if (destinationWorld != null) return finalBiomesByDimensionKeys.contains(biome.getKey().get());
                return false;
            });
        }).toList();

        biomeKeys = new ArrayList<>(biomeKeys);
        biomeKeys.sort(Comparator.comparing((key) -> key.getValue().getPath()));

        structureKeys = new ArrayList<>(structureKeys);
        structureKeys.sort(Comparator.comparing((key) -> key.getValue().getPath()));

        List<Map.Entry<Identifier, TardisTelepathicInterfaceDataType>> list = new ArrayList<>();
        list.addAll(biomeKeys.stream().map((entry) -> Map.entry(entry.getValue(), TardisTelepathicInterfaceDataType.BIOME)).toList());
        list.addAll(structureKeys.stream().map((entry) -> Map.entry(entry.getValue(), TardisTelepathicInterfaceDataType.STRUCTURE)).toList());

        AtomicInteger i = new AtomicInteger();
        list.forEach((entry) -> {
            NbtCompound pair = new NbtCompound();
            pair.putString("id", entry.getKey().toString());
            pair.putString("type", entry.getValue().name());
            tag.put(CommonHelper.formatIndexString(i.incrementAndGet()), pair);
        });

        return tag;
    }
}

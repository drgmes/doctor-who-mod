package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.blocks.tardis.consoleunits.screens.TardisConsoleUnitTelepathicInterfaceLocationsScreen;
import net.drgmes.dwm.enums.TardisTelepathicInterfaceDataType;
import net.drgmes.dwm.network.IPacket;
import net.drgmes.dwm.setup.ModDimensions;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
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
import java.util.function.Function;

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
        this(blockPos, createLocationsListFromRegistry(originWorld, destinationWorld));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    @Environment(EnvType.CLIENT)
    public static void handle(TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            final MinecraftClient mc = MinecraftClient.getInstance();

            if (mc.world.getBlockEntity(payload.blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
                List<Map.Entry<Identifier, TardisTelepathicInterfaceDataType>> locations = new ArrayList<>();
                List<String> keys = new ArrayList<>(payload.tag.getKeys());

                keys.sort(Comparator.comparing((key) -> key));
                keys.forEach((key) -> {
                    locations.add(Map.entry(
                        Identifier.of(payload.tag.getCompound(key).getString("id")),
                        TardisTelepathicInterfaceDataType.valueOf(payload.tag.getCompound(key).getString("type"))
                    ));
                });

                mc.setScreen(new TardisConsoleUnitTelepathicInterfaceLocationsScreen(tardisConsoleUnitBlockEntity, locations));
            }
        });
    }

    private static NbtCompound createLocationsListFromRegistry(ServerWorld world, @Nullable ServerWorld destinationWorld) {
        List<Map.Entry<Identifier, TardisTelepathicInterfaceDataType>> list = new ArrayList<>();

        List<Identifier> biomeIds;
        Registry<Structure> structureRegistry;

        if (destinationWorld != null) {
            Set<RegistryEntry<Biome>> biomeEntries = destinationWorld.getChunkManager().getChunkGenerator().getBiomeSource().getBiomes();
            biomeIds = biomeEntries.stream().filter((b) -> b.getKey().isPresent()).map((b) -> b.getKey().get().getValue()).toList();
            structureRegistry = world.getRegistryManager().get(RegistryKeys.STRUCTURE);
        }
        else {
            biomeIds = null;
            structureRegistry = null;
        }

        list.addAll(getLocationsForRegistry(
            TardisTelepathicInterfaceDataType.BIOME,
            RegistryKeys.BIOME,
            world,
            (entry) -> (
                !entry.getValue().equals(ModDimensions.ModDimensionTypes.TARDIS.getValue()) && (biomeIds == null || biomeIds.contains(entry.getValue()))
            )
        ));

        list.addAll(getLocationsForRegistry(
            TardisTelepathicInterfaceDataType.STRUCTURE,
            RegistryKeys.STRUCTURE,
            world,
            (entry) -> {
                boolean flag = false;

                if (structureRegistry != null) {
                    Structure structure = structureRegistry.get(entry.getValue());
                    if (structure != null) {
                        flag = structure.getValidBiomes().stream().anyMatch((b) -> b.getKey().isPresent() && biomeIds.contains(b.getKey().get().getValue()));
                    }
                }

                return flag;
            }
        ));

        AtomicInteger i = new AtomicInteger();
        NbtCompound tag = new NbtCompound();
        list.forEach((entry) -> {
            NbtCompound pair = new NbtCompound();
            pair.putString("id", entry.getKey().toString());
            pair.putString("type", entry.getValue().name());
            tag.put(CommonHelper.formatIndexString(i.incrementAndGet()), pair);
        });

        return tag;
    }

    private static <T> List<Map.Entry<Identifier, TardisTelepathicInterfaceDataType>> getLocationsForRegistry(TardisTelepathicInterfaceDataType dataType, RegistryKey<Registry<T>> registryKey, ServerWorld world, Function<RegistryKey<T>, Boolean> entryChecker) {
        Registry<T> registry = world.getRegistryManager().get(registryKey);

        List<Map.Entry<Identifier, TardisTelepathicInterfaceDataType>> list = new ArrayList<>(
            registry.getKeys().stream().filter(entryChecker::apply).map((res) -> Map.entry(res.getValue(), dataType)).toList()
        );

        if (!list.isEmpty()) {
            list.sort(Comparator.comparing(a -> a.getKey().getPath()));
        }

        return list;
    }
}

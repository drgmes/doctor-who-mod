package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.blocks.tardis.consoleunits.screens.TardisConsoleUnitTelepathicInterfaceLocationsScreen;
import net.drgmes.dwm.setup.ModDimensions;
import net.drgmes.dwm.setup.ModNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
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

public class TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket extends BaseS2CMessage {
    private final BlockPos blockPos;
    private final NbtCompound tag;

    public TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket(BlockPos blockPos, NbtCompound tag) {
        this.blockPos = blockPos;
        this.tag = tag;
    }

    public TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket(BlockPos blockPos, ServerWorld originWorld, @Nullable ServerWorld destinationWorld) {
        this(blockPos, createLocationsListFromRegistry(originWorld, destinationWorld));
    }

    public static TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket create(PacketByteBuf buf) {
        return new TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket(buf.readBlockPos(), buf.readNbt());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_CONSOLE_UNIT_TELEPATHIC_INTERFACE_LOCATIONS_OPEN;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeNbt(this.tag);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(this.blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            List<Map.Entry<Identifier, TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType>> locations = new ArrayList<>();
            List<String> keys = new ArrayList<>(this.tag.getKeys().stream().toList());

            keys.sort(Comparator.comparing((key) -> key));
            keys.forEach((key) -> {
                locations.add(Map.entry(
                    new Identifier(this.tag.getCompound(key).getString("id")),
                    TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType.valueOf(this.tag.getCompound(key).getString("type"))
                ));
            });

            mc.setScreen(new TardisConsoleUnitTelepathicInterfaceLocationsScreen(tardisConsoleUnitBlockEntity, locations));
        }
    }

    private static NbtCompound createLocationsListFromRegistry(ServerWorld world, @Nullable ServerWorld destinationWorld) {
        List<Map.Entry<Identifier, TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType>> list = new ArrayList<>();

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
            TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType.BIOME,
            RegistryKeys.BIOME,
            world,
            (entry) -> (
                !entry.getValue().equals(ModDimensions.ModDimensionTypes.TARDIS.getValue()) && (biomeIds == null || biomeIds.contains(entry.getValue()))
            )
        ));

        list.addAll(getLocationsForRegistry(
            TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType.STRUCTURE,
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
            tag.put(String.format("%1$" + 5 + "s", i.incrementAndGet()).replace(' ', '0'), pair);
        });

        return tag;
    }

    private static <T> List<Map.Entry<Identifier, TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType>> getLocationsForRegistry(TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType type, RegistryKey<Registry<T>> registryKey, ServerWorld world, Function<RegistryKey<T>, Boolean> entryChecker) {
        Registry<T> registry = world.getRegistryManager().get(registryKey);

        List<Map.Entry<Identifier, TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType>> list = new ArrayList<>(
            registry.getKeys().stream().filter(entryChecker::apply).map((res) -> Map.entry(res.getValue(), type)).toList()
        );

        if (list.size() > 0) {
            list.sort(Comparator.comparing(a -> a.getKey().getPath()));
        }

        return list;
    }
}

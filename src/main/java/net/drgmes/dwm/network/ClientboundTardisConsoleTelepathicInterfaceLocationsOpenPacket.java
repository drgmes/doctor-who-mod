package net.drgmes.dwm.network;

import com.google.common.collect.Lists;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.blocks.tardis.consoles.screens.TardisConsoleTelepathicInterfaceLocationsScreen;
import net.drgmes.dwm.blocks.tardis.consoles.screens.TardisConsoleTelepathicInterfaceLocationsScreen.DataType;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ClientboundTardisConsoleTelepathicInterfaceLocationsOpenPacket {
    private final BlockPos blockPos;
    private final List<Entry<ResourceLocation, DataType>> locations;

    public ClientboundTardisConsoleTelepathicInterfaceLocationsOpenPacket(BlockPos blockPos, List<Entry<ResourceLocation, DataType>> locations) {
        this.blockPos = blockPos;
        this.locations = locations;
    }

    public ClientboundTardisConsoleTelepathicInterfaceLocationsOpenPacket(BlockPos blockPos) {
        this(blockPos, createLocationsListFromRegistry());
    }

    public ClientboundTardisConsoleTelepathicInterfaceLocationsOpenPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), createLocationsListFromBuffer(buffer));
    }

    private static List<Entry<ResourceLocation, DataType>> createLocationsListFromBuffer(FriendlyByteBuf buffer) {
        return buffer.readCollection(Lists::newArrayListWithCapacity, (bf) -> {
            return Map.entry(bf.readResourceLocation(), DataType.valueOf(bf.readUtf()));
        });
    }

    private static List<Entry<ResourceLocation, DataType>> createLocationsListFromRegistry() {
        List<Entry<ResourceLocation, DataType>> list = new ArrayList<>();
        getLocationsForRegistry(Registry.BIOME_REGISTRY, DataType.BIOME).forEach(list::add);
        getLocationsForRegistry(Registry.STRUCTURE_REGISTRY, DataType.STRUCTURE).forEach(list::add);

        return list;
    }

    private static <T> List<Entry<ResourceLocation, DataType>> getLocationsForRegistry(ResourceKey<Registry<T>> registryKey, DataType type) {
        Minecraft mc = Minecraft.getInstance();
        Optional<? extends Registry<T>> registry = mc.level.registryAccess().registry(registryKey);
        if (registry.isEmpty()) return new ArrayList<>();

        List<Entry<ResourceLocation, DataType>> list = new ArrayList<>(
            registry.get().keySet().stream()
                .filter((res) -> !res.equals(ModDimensionTypes.TARDIS.location()))
                .map((res) -> Map.entry(res, type)).toList()
        );

        if (list.size() > 0) {
            Collections.sort(list, new Comparator<Entry<ResourceLocation, DataType>>() {
                @Override
                public int compare(Entry<ResourceLocation, DataType> a, Entry<ResourceLocation, DataType> b) {
                    return a.getKey().getPath().compareTo(b.getKey().getPath());
                }
            });
        }

        return list;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
        buffer.writeCollection(this.locations, (bf, entry) -> {
            bf.writeResourceLocation(entry.getKey());
            bf.writeUtf(entry.getValue().name());
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                final Minecraft mc = Minecraft.getInstance();

                if (mc.level.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
                    mc.setScreen(new TardisConsoleTelepathicInterfaceLocationsScreen(tardisConsoleBlockEntity, locations));
                    success.set(true);
                }
            }
        }));

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

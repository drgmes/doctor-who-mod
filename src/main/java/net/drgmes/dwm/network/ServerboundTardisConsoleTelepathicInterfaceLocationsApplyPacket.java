package net.drgmes.dwm.network;

import com.mojang.datafixers.util.Pair;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.screens.TardisConsoleTelepathicInterfaceLocationsScreen.DataType;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization.TardisSystemMaterializationSafeDirection;
import net.drgmes.dwm.setup.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.Tags;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ServerboundTardisConsoleTelepathicInterfaceLocationsApplyPacket {
    public final Entry<ResourceLocation, DataType> entry;

    public ServerboundTardisConsoleTelepathicInterfaceLocationsApplyPacket(Entry<ResourceLocation, DataType> entry) {
        this.entry = entry;
    }

    public ServerboundTardisConsoleTelepathicInterfaceLocationsApplyPacket(FriendlyByteBuf buffer) {
        this(Map.entry(buffer.readResourceLocation(), DataType.valueOf(buffer.readUtf())));
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.entry.getKey());
        buffer.writeUtf(this.entry.getValue().name());
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> {
            if (!(ctx.get().getSender().level instanceof ServerLevel level)) return;

            level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
                if (!tardis.isValid()) return;
                Component message = null;

                if (this.entry.getValue() == DataType.BIOME) {
                    String msg = this.findBiome(level, tardis) ? "found" : "not_found";
                    message = Component.translatable("message." + DWM.MODID + ".tardis.telepathic_interface.biome." + msg);
                } else if (this.entry.getValue() == DataType.STRUCTURE) {
                    String msg = this.findStructure(level, tardis) ? "found" : "not_found";
                    message = Component.translatable("message." + DWM.MODID + ".tardis.telepathic_interface.structure." + msg);
                }

                if (message != null) ctx.get().getSender().displayClientMessage(message, true);
            });

            success.set(true);
        });

        ctx.get().setPacketHandled(true);
        return success.get();
    }

    private boolean findBiome(Level level, ITardisLevelData provider) {
        BlockPos exteriorPos = provider.getDestinationExteriorPosition();
        ServerLevel exteriorLevel = level.getServer().getLevel(provider.getDestinationExteriorDimension());
        if (exteriorLevel == null) return false;

        Registry<Biome> registry = exteriorLevel.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);

        Biome biome = registry.get(this.entry.getKey());
        if (biome == null) return false;

        Optional<Holder<Biome>> holder = registry.getHolder(registry.getId(biome));
        if (!holder.isPresent()) return false;

        Pair<BlockPos, Holder<Biome>> pair = exteriorLevel.findClosestBiome3d((h) -> h.is(this.entry.getKey()), exteriorPos, 6400, 32, 64);
        if (pair == null) return false;

        boolean isUnderground = false;
        BlockPos blockPos = pair.getFirst().atY(exteriorLevel.getMaxBuildHeight());

        if (exteriorLevel.dimension() == Level.NETHER) isUnderground = true;
        else if (pair.getSecond().is(Tags.Biomes.IS_UNDERGROUND)) isUnderground = true;

        if (isUnderground) {
            blockPos = blockPos.atY(exteriorLevel.getMinBuildHeight());
        }

        if (provider.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization materializationSystem) {
            materializationSystem.setSafeDirection(isUnderground ? TardisSystemMaterializationSafeDirection.BOTTOM : TardisSystemMaterializationSafeDirection.TOP);
        }

        provider.setDestinationPosition(blockPos);
        provider.updateConsoleTiles();
        return true;
    }

    private boolean findStructure(Level level, ITardisLevelData provider) {
        BlockPos exteriorPos = provider.getDestinationExteriorPosition();
        ServerLevel exteriorLevel = level.getServer().getLevel(provider.getDestinationExteriorDimension());
        if (exteriorLevel == null) return false;

        Registry<Structure> registry = exteriorLevel.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);

        Structure structure = registry.get(this.entry.getKey());
        if (structure == null) return false;

        Optional<Holder<Structure>> holder = registry.getHolder(registry.getId(structure));
        if (!holder.isPresent()) return false;

        Pair<BlockPos, Holder<Structure>> pair = exteriorLevel.getChunkSource().getGenerator().findNearestMapStructure(exteriorLevel, HolderSet.direct(holder.get()), exteriorPos, 512, false);
        if (pair == null) return false;

        boolean isUnderground = false;
        BlockPos blockPos = pair.getFirst().atY(exteriorLevel.getMaxBuildHeight());

        if (exteriorLevel.dimension() == Level.NETHER) isUnderground = true;
        else if (structure.step() == Decoration.STRONGHOLDS) isUnderground = true;
        else if (structure.step() == Decoration.UNDERGROUND_DECORATION) isUnderground = true;
        else if (structure.step() == Decoration.UNDERGROUND_STRUCTURES) isUnderground = true;

        if (isUnderground) {
            blockPos = blockPos.atY(exteriorLevel.getMinBuildHeight());
        }

        if (provider.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization materializationSystem) {
            materializationSystem.setSafeDirection(isUnderground ? TardisSystemMaterializationSafeDirection.BOTTOM : TardisSystemMaterializationSafeDirection.TOP);
        }

        provider.setDestinationPosition(blockPos);
        provider.updateConsoleTiles();
        return true;
    }
}

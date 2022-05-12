package net.drgmes.dwm.network;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleTelepathicInterfaceScreen.DataType;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization.TardisSystemMaterializationSafeDirection;
import net.drgmes.dwm.setup.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundTardisConsoleTelepathicInterfaceUpdatePacket {
    public final Entry<ResourceLocation, DataType> entry;

    public ServerboundTardisConsoleTelepathicInterfaceUpdatePacket(Entry<ResourceLocation, DataType> entry) {
        this.entry = entry;
    }

    public ServerboundTardisConsoleTelepathicInterfaceUpdatePacket(FriendlyByteBuf buffer) {
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

            level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
                if (!provider.isValid()) return;

                if (this.entry.getValue() == DataType.BIOME) {
                    String msg = this.findBiome(level, provider) ? "found" : "not_found";
                    ctx.get().getSender().displayClientMessage(new TranslatableComponent("message." + DWM.MODID + ".tardis.telepathic_interface.biome." + msg), true);
                }
                else if (this.entry.getValue() == DataType.STRUCTURE) {
                    String msg = this.findStructure(level, provider) ? "found" : "not_found";
                    ctx.get().getSender().displayClientMessage(new TranslatableComponent("message." + DWM.MODID + ".tardis.telepathic_interface.structure." + msg), true);
                }

                success.set(true);
            });
        });

        ctx.get().setPacketHandled(true);
        return success.get();
    }

    @SuppressWarnings("deprecation")
    private boolean findBiome(Level level, ITardisLevelData provider) {
        BlockPos exteriorPos = provider.getDestinationExteriorPosition();
        ServerLevel exteriorLevel = level.getServer().getLevel(provider.getDestinationExteriorDimension());
        if (exteriorLevel == null) return false;

        Registry<Biome> registry = exteriorLevel.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);

        Biome biome = registry.get(this.entry.getKey());
        if (biome == null) return false;

        Optional<Holder<Biome>> holder = registry.getHolder(registry.getId(biome));
        if (!holder.isPresent()) return false;

        Pair<BlockPos, Holder<Biome>> pair = exteriorLevel.findNearestBiome((h) -> h.is(this.entry.getKey()), exteriorPos, 6400, 8);
        if (pair == null) return false;

        boolean isUndeground = false;
        BlockPos blockPos = pair.getFirst().atY(exteriorLevel.getMaxBuildHeight());

        if (exteriorLevel.dimension() == Level.NETHER) isUndeground = true;
        else if (Biome.getBiomeCategory(holder.get()) == BiomeCategory.UNDERGROUND) isUndeground = true;

        if (isUndeground) {
            blockPos = blockPos.atY(exteriorLevel.getMinBuildHeight());
        }

        if (provider.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization materializationSystem) {
            materializationSystem.setSafeDirection(isUndeground ? TardisSystemMaterializationSafeDirection.BOTTOM : TardisSystemMaterializationSafeDirection.TOP);
        }

        provider.setDestinationPosition(blockPos);
        provider.updateConsoleTiles();
        return true;
    }

    private boolean findStructure(Level level, ITardisLevelData provider) {
        BlockPos exteriorPos = provider.getDestinationExteriorPosition();
        ServerLevel exteriorLevel = level.getServer().getLevel(provider.getDestinationExteriorDimension());
        if (exteriorLevel == null) return false;

        Registry<ConfiguredStructureFeature<?, ?>> registry = exteriorLevel.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);

        ConfiguredStructureFeature<?, ?> configuredFeature = registry.get(this.entry.getKey());
        if (configuredFeature == null) return false;

        Optional<Holder<ConfiguredStructureFeature<?, ?>>> holder = registry.getHolder(registry.getId(configuredFeature));
        if (!holder.isPresent()) return false;

        Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> pair = exteriorLevel.getChunkSource().getGenerator().findNearestMapFeature(exteriorLevel, HolderSet.direct(holder.get()), exteriorPos, 512, false);
        if (pair == null) return false;

        boolean isUndeground = false;
        BlockPos blockPos = pair.getFirst().atY(exteriorLevel.getMaxBuildHeight());

        if (exteriorLevel.dimension() == Level.NETHER) isUndeground = true;
        else if (configuredFeature.feature.step() == Decoration.STRONGHOLDS) isUndeground = true;
        else if (configuredFeature.feature.step() == Decoration.UNDERGROUND_DECORATION) isUndeground = true;
        else if (configuredFeature.feature.step() == Decoration.UNDERGROUND_STRUCTURES) isUndeground = true;

        if (isUndeground) {
            blockPos = blockPos.atY(exteriorLevel.getMinBuildHeight());
        }

        if (provider.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization materializationSystem) {
            materializationSystem.setSafeDirection(isUndeground ? TardisSystemMaterializationSafeDirection.BOTTOM : TardisSystemMaterializationSafeDirection.TOP);
        }

        provider.setDestinationPosition(blockPos);
        provider.updateConsoleTiles();
        return true;
    }
}

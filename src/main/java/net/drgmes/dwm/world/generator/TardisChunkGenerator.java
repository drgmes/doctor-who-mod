package net.drgmes.dwm.world.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.drgmes.dwm.setup.ModBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class TardisChunkGenerator extends ChunkGenerator {
    public static final Codec<TardisChunkGenerator> CODEC = RecordCodecBuilder.create((builder) -> {
        return commonCodec(builder)
            .and(RegistryOps.retrieveRegistry(Registry.BIOME_REGISTRY).forGetter(TardisChunkGenerator::getBiomeRegistry))
            .apply(builder, builder.stable(TardisChunkGenerator::new));
    });

    private final Registry<Biome> biomeRegistry;

    public TardisChunkGenerator(MinecraftServer server) {
        this(server.registryAccess().registryOrThrow(Registry.STRUCTURE_SET_REGISTRY), server.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY));
    }

    public TardisChunkGenerator(Registry<StructureSet> structureSets, Registry<Biome> biomes) {
        super(structureSets, Optional.empty(), new FixedBiomeSource(biomes.getHolderOrThrow(ModBiomes.TARDIS_KEY)));
        this.biomeRegistry = biomes;
    }

    public Registry<Biome> getBiomeRegistry() {
        return this.biomeRegistry;
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getGenDepth() {
        return 319;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinY() {
        return -64;
    }

    @Override
    public int getSpawnHeight(LevelHeightAccessor level) {
        return 1;
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types types, LevelHeightAccessor level, RandomState randomState) {
        return 0;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {
        return new NoiseColumn(0, new BlockState[0]);
    }

    @Override
    public void addDebugScreenInfo(List<String> list, RandomState randomState, BlockPos blockPos) {
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel world, ChunkAccess chunkAccess, StructureManager structureManager) {
    }

    @Override
    public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunkAccess, Carving carvingType) {
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess) {
    }

    @Override
    public void createReferences(WorldGenLevel world, StructureManager structureManager, ChunkAccess chunk) {
    }

    @Override
    public void createStructures(RegistryAccess registries, RandomState randomState, StructureManager structureManager, ChunkAccess chunk, StructureTemplateManager structureTemplateManager, long seed) {
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
    }
}

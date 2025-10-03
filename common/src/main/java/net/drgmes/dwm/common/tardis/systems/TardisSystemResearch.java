package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.setup.ModBiomes;
import net.drgmes.dwm.setup.ModConfig;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.drgmes.dwm.utils.helpers.WorldHelper;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TardisSystemResearch extends TardisBaseSystem {
    private final List<RegistryKey<World>> visitedWorldKeys = new ArrayList<>();
    private final List<RegistryKey<Biome>> visitedBiomeKeys = new ArrayList<>();
    private final List<RegistryKey<Structure>> visitedStructureKeys = new ArrayList<>();

    public TardisSystemResearch(TardisStateManager tardis) {
        super(tardis);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        if (tag.contains("visitedWorlds")) {
            this.visitedWorldKeys.clear();

            NbtCompound visitedWorldsTag = tag.getCompound("visitedWorlds");
            List<String> keys = new ArrayList<>(visitedWorldsTag.getKeys());
            keys.sort(Comparator.comparing((key) -> key));

            keys.forEach((key) -> {
                String value = visitedWorldsTag.getString(key);
                if (value == null || value.isEmpty()) return;

                RegistryKey<World> entry = DimensionHelper.getWorldKey(value);
                if (entry == null || entry.getValue().getPath() == null || entry.getValue().getPath().isEmpty()) return;

                if (!this.visitedWorldKeys.contains(entry)) this.visitedWorldKeys.add(entry);
            });
        }

        if (tag.contains("visitedBiomes")) {
            this.visitedBiomeKeys.clear();

            NbtCompound visitedBiomesTag = tag.getCompound("visitedBiomes");
            List<String> keys = new ArrayList<>(visitedBiomesTag.getKeys());
            keys.sort(Comparator.comparing((key) -> key));

            keys.forEach((key) -> {
                String value = visitedBiomesTag.getString(key);
                if (value == null || value.isEmpty()) return;

                RegistryKey<Biome> entry = RegistryKey.of(RegistryKeys.BIOME, Identifier.of(value));
                if (entry == null || entry.getValue().getPath() == null || entry.getValue().getPath().isEmpty()) return;

                if (!this.visitedBiomeKeys.contains(entry)) this.visitedBiomeKeys.add(entry);
            });
        }

        if (tag.contains("visitedStructures")) {
            this.visitedStructureKeys.clear();

            NbtCompound visitedStructuresTag = tag.getCompound("visitedStructures");
            List<String> keys = new ArrayList<>(visitedStructuresTag.getKeys());
            keys.sort(Comparator.comparing((key) -> key));

            keys.forEach((key) -> {
                String value = visitedStructuresTag.getString(key);
                if (value == null || value.isEmpty()) return;

                RegistryKey<Structure> entry = RegistryKey.of(RegistryKeys.STRUCTURE, Identifier.of(value));
                if (entry == null || entry.getValue().getPath() == null || entry.getValue().getPath().isEmpty()) return;

                if (!this.visitedStructureKeys.contains(entry)) this.visitedStructureKeys.add(entry);
            });
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        AtomicInteger i1 = new AtomicInteger();
        NbtCompound visitedWorldsTag = new NbtCompound();
        this.visitedWorldKeys.forEach((entry) -> visitedWorldsTag.putString(CommonHelper.formatIndexString(i1.incrementAndGet()), entry.getValue().toString()));
        tag.put("visitedWorlds", visitedWorldsTag);

        AtomicInteger i2 = new AtomicInteger();
        NbtCompound visitedBiomesTag = new NbtCompound();
        this.visitedBiomeKeys.forEach((entry) -> visitedBiomesTag.putString(CommonHelper.formatIndexString(i2.incrementAndGet()), entry.getValue().toString()));
        tag.put("visitedBiomes", visitedBiomesTag);

        AtomicInteger i3 = new AtomicInteger();
        NbtCompound visitedStructuresTag = new NbtCompound();
        this.visitedStructureKeys.forEach((entry) -> visitedStructuresTag.putString(CommonHelper.formatIndexString(i3.incrementAndGet()), entry.getValue().toString()));
        tag.put("visitedStructures", visitedStructuresTag);

        return tag;
    }

    // ///////////////// //
    // Dimension methods //
    // ///////////////// //

    public List<RegistryKey<World>> getValidDimensions() {
        Iterable<ServerWorld> worlds = tardis.getWorld().getServer().getWorlds();
        List<RegistryKey<World>> worldKeys = new ArrayList<>();

        worlds.forEach((world) -> {
            if (TardisHelper.isTardisDimension(world)) return;
            if (!world.getServer().isWorldAllowed(world)) return;
            if (world.getRegistryKey().equals(this.tardis.getWorld().getRegistryKey())) return;
            if (ModConfig.COMMON.dimensionsBlacklist.get().contains(world.getRegistryKey().getValue().toString())) return;

            if (world.getRegistryKey() == World.END && ModConfig.COMMON.hideTheEndConditionally.get()) {
                EnderDragonFight enderDragonFight = world.getEnderDragonFight();
                boolean enderDragonWasKilled = enderDragonFight != null && enderDragonFight.hasPreviouslyKilled() && !enderDragonFight.toData().needsStateScanning();
                if (!enderDragonWasKilled) return;
            }

            worldKeys.add(world.getRegistryKey());
        });

        return worldKeys;
    }

    public List<RegistryKey<World>> getAvailableDimensions() {
        List<RegistryKey<World>> worldKeys = this.getValidDimensions();
        if (ModConfig.COMMON.dimensionsAlwaysAvailable.get()) return worldKeys;

        return worldKeys.stream().filter((worldKey) -> {
            if (worldKey.equals(this.tardis.getCurrentExteriorDimension())) return true;
            return this.visitedWorldKeys.contains(worldKey);
        }).toList();
    }

    public boolean hasVisitedWorld(RegistryKey<World> worldKey) {
        return worldKey != null && this.visitedWorldKeys.contains(worldKey);
    }

    public boolean addVisitedWorld(RegistryKey<World> worldKey, @Nullable UUID initiatorId, boolean suppressNotification) {
        if (worldKey == null || TardisHelper.isTardisDimension(worldKey)) return false;
        if (this.visitedWorldKeys.contains(worldKey)) return false;

        if (!ModConfig.COMMON.dimensionsAlwaysAvailable.get() && !suppressNotification) {
            this.notify(DWM.TEXTS.RESEARCH_SYSTEM_DIMENSION_LEARNED.apply(CommonHelper.formatRegistryKey(worldKey)), initiatorId);
        }

        this.visitedWorldKeys.add(worldKey);
        this.tardis.markConsoleTilesUpdated();
        this.tardis.markDirty();
        return true;
    }

    public void updateVisitedWorlds(UUID initiatorId) {
        this.addVisitedWorld(this.tardis.getCurrentExteriorDimension(), initiatorId, false);
        this.addVisitedWorld(this.tardis.getPreviousExteriorDimension(), initiatorId, false);
    }

    // ///////////// //
    // Biome methods //
    // ///////////// //

    public List<RegistryKey<Biome>> getValidBiomes() {
        Registry<Biome> registry = this.tardis.getWorld().getRegistryManager().get(RegistryKeys.BIOME);
        return registry.getKeys().stream().toList();
    }

    public List<RegistryKey<Biome>> getAvailableBiomes() {
        List<RegistryKey<Biome>> biomeKeys = this.getValidBiomes();
        if (ModConfig.COMMON.biomesAlwaysAvailable.get()) return biomeKeys;
        return biomeKeys.stream().filter(this.visitedBiomeKeys::contains).toList();
    }

    public boolean hasVisitedBiome(RegistryKey<Biome> biomeKey) {
        return biomeKey != null && this.visitedBiomeKeys.contains(biomeKey);
    }

    public boolean addVisitedBiome(RegistryKey<Biome> biomeKey, @Nullable UUID initiatorId, boolean suppressNotification) {
        if (biomeKey == null || biomeKey.equals(ModBiomes.TARDIS_KEY)) return false;
        if (this.visitedBiomeKeys.contains(biomeKey)) return false;

        if (!ModConfig.COMMON.biomesAlwaysAvailable.get() && !suppressNotification) {
            this.notify(DWM.TEXTS.RESEARCH_SYSTEM_BIOME_LEARNED.apply(CommonHelper.formatRegistryKey(biomeKey)), initiatorId);
        }

        this.visitedBiomeKeys.add(biomeKey);
        this.tardis.markDirty();
        return true;
    }

    public void updateVisitedBiomes(UUID initiatorId) {
        Optional<RegistryKey<Biome>> biomeKeyHolder = WorldHelper.locateBiome(this.tardis.getExteriorWorld(), this.tardis.getDestinationExteriorPosition());
        biomeKeyHolder.ifPresent((biomeKey) -> this.addVisitedBiome(biomeKey, initiatorId, false));
    }

    // ///////////////// //
    // Structure methods //
    // ///////////////// //

    public List<RegistryKey<Structure>> getValidStructures() {
        Registry<Structure> registry = this.tardis.getWorld().getRegistryManager().get(RegistryKeys.STRUCTURE);
        return registry.getKeys().stream().toList();
    }

    public List<RegistryKey<Structure>> getAvailableStructures() {
        List<RegistryKey<Structure>> structureKeys = this.getValidStructures();
        if (ModConfig.COMMON.structuresAlwaysAvailable.get()) return structureKeys;
        return structureKeys.stream().filter(this.visitedStructureKeys::contains).toList();
    }

    public boolean hasVisitedStructure(RegistryKey<Structure> structureKey) {
        return structureKey != null && this.visitedStructureKeys.contains(structureKey);
    }

    public boolean addVisitedStructure(RegistryKey<Structure> structureKey, @Nullable UUID initiatorId, boolean suppressNotification) {
        if (structureKey == null || this.visitedStructureKeys.contains(structureKey)) return false;

        if (!ModConfig.COMMON.structuresAlwaysAvailable.get() && !suppressNotification) {
            this.notify(DWM.TEXTS.RESEARCH_SYSTEM_STRUCTURE_LEARNED.apply(CommonHelper.formatRegistryKey(structureKey)), initiatorId);
        }

        this.visitedStructureKeys.add(structureKey);
        this.tardis.markDirty();
        return true;
    }

    public void updateVisitedStructures(UUID initiatorId) {
        Optional<RegistryKey<Structure>> structureKeyHolder = WorldHelper.locateStructure(this.tardis.getExteriorWorld(), this.tardis.getDestinationExteriorPosition());
        structureKeyHolder.ifPresent((structureKey) -> this.addVisitedStructure(structureKey, initiatorId, false));
    }
}

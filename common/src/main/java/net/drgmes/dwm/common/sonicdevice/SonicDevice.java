package net.drgmes.dwm.common.sonicdevice;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicdevice.modes.BaseSonicDeviceMode;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.TardisSystemResearch;
import net.drgmes.dwm.enums.SonicDeviceMode;
import net.drgmes.dwm.items.sonicdevices.ISonicDeviceItem;
import net.drgmes.dwm.setup.ModBiomes;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.EntityHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.Structure;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SonicDevice {
    public static boolean checkItemStackIsSonicDevice(ItemStack itemStack) {
        return itemStack.getItem() instanceof ISonicDeviceItem;
    }

    public static ActionResult interact(World world, PlayerEntity player, EquipmentSlot slot, boolean isAlternativeAction) {
        ActionResult result = ActionResult.PASS;
        ItemStack itemStack = player.getEquippedStack(slot);
        if (!checkItemStackIsSonicDevice(itemStack)) return ActionResult.FAIL;
        if (player.getItemCooldownManager().isCoolingDown(itemStack.getItem())) return ActionResult.CONSUME;

        BaseSonicDeviceMode mode = getInteractionMode(itemStack).getInstance();
        HitResult hitResult = EntityHelper.pick(player, getInteractionDistance(itemStack));
        if (hitResult == null || hitResult.getType() == HitResult.Type.MISS) return ActionResult.PASS;

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            if (!(result = mode.interactWithBlock(world, player, slot, (BlockHitResult) hitResult, isAlternativeAction)).shouldSwingHand()) {
                if (isAlternativeAction) result = mode.interactWithBlockAlternative(world, player, slot, (BlockHitResult) hitResult);
                else result = mode.interactWithBlockNative(world, player, slot, (BlockHitResult) hitResult);
            }
        }
        else if (hitResult.getType() == HitResult.Type.ENTITY) {
            if (!(result = mode.interactWithEntity(world, player, slot, (EntityHitResult) hitResult, isAlternativeAction)).shouldSwingHand()) {
                if (isAlternativeAction) result = mode.interactWithEntityAlternative(world, player, slot, (EntityHitResult) hitResult);
                else result = mode.interactWithEntityNative(world, player, slot, (EntityHitResult) hitResult);
            }
        }

        if (result.shouldSwingHand()) {
            Vec3d pos = hitResult.getPos();
            player.getItemCooldownManager().set(itemStack.getItem(), DWM.TIMINGS.SONIC_DEVICE_TIMEOUT);
            mode.generateVibration(world, player, new BlockPos((int) pos.x, (int) pos.y, (int) pos.z));
        }

        return result;
    }

    public static NbtCompound getData(ItemStack itemStack) {
        if (!checkItemStackIsSonicDevice(itemStack)) return new NbtCompound();
        NbtCompound tag = CommonHelper.getItemStackData(itemStack).copyNbt();
        return tag.contains("sonicDeviceData") ? tag.getCompound("sonicDeviceData") : new NbtCompound();
    }

    public static void updateData(ItemStack itemStack, Consumer<NbtCompound> consumer) {
        if (!checkItemStackIsSonicDevice(itemStack)) return;

        CommonHelper.updateItemStackData(itemStack, (tag) -> {
            NbtCompound dataTag = tag.contains("sonicDeviceData") ? tag.getCompound("sonicDeviceData") : new NbtCompound();
            consumer.accept(dataTag);
            tag.put("sonicDeviceData", dataTag);
        });
    }

    public static SonicDeviceMode getInteractionMode(ItemStack itemStack) {
        SonicDeviceMode mode = null;
        NbtCompound tag = getData(itemStack);
        if (tag.contains("mode")) mode = SonicDeviceMode.valueOf(tag.getString("mode"));

        return mode != null ? mode : SonicDeviceMode.SCAN;
    }

    public static void setInteractionMode(ItemStack itemStack, SonicDeviceMode mode) {
        SonicDevice.updateData(itemStack, (tag) -> {
            tag.putString("prevMode", getInteractionMode(itemStack).name());
            tag.putString("mode", mode.name());
        });
    }

    public static double getInteractionDistance(ItemStack itemStack) {
        NbtCompound tag = getData(itemStack);
        return !tag.contains("interactionDistance") ? 100D : tag.getDouble("interactionDistance");
    }

    public static String getTardisId(ItemStack itemStack) {
        NbtCompound tag = getData(itemStack);
        return !tag.contains("tardisId") ? "" : tag.getString("tardisId");
    }

    public static void setTardisId(ItemStack itemStack, World world) {
        if (!TardisHelper.isTardisDimension(world)) return;

        SonicDevice.updateData(itemStack, (tag) -> {
            tag.putString("tardisId", DimensionHelper.getWorldId(world));
        });
    }

    public static List<RegistryKey<World>> getDiscoveredWorlds(ItemStack itemStack) {
        List<RegistryKey<World>> worldKeys = new ArrayList<>();

        NbtCompound tag = getData(itemStack);
        if (!tag.contains("discoveredWorlds")) return worldKeys;

        NbtCompound discoveredWorldsTag = tag.getCompound("discoveredWorlds");
        List<String> keys = new ArrayList<>(discoveredWorldsTag.getKeys());
        keys.sort(Comparator.comparing((key) -> key));

        keys.forEach((key) -> {
            RegistryKey<World> entry = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(key));
            if (entry == null || entry.getValue().getPath() == null || entry.getValue().getPath().isEmpty()) return;
            if (!worldKeys.contains(entry)) worldKeys.add(entry);
        });

        return worldKeys;
    }

    public static void addDiscoveredWorld(ItemStack itemStack, PlayerEntity player, RegistryKey<World> worldKey) {
        if (worldKey == null || TardisHelper.isTardisDimension(worldKey)) return;

        SonicDevice.updateData(itemStack, (tag) -> {
            NbtCompound discoveredWorldsTag = tag.getCompound("discoveredWorlds");
            if (discoveredWorldsTag.contains(worldKey.getValue().toString())) return;

            discoveredWorldsTag.putBoolean(worldKey.getValue().toString(), true);
            tag.put("discoveredWorlds", discoveredWorldsTag);
            player.sendMessage(DWM.TEXTS.SONIC_DEVICE_DIMENSION_DISCOVERED.apply(CommonHelper.formatRegistryKey(worldKey)), true);
        });
    }

    public static List<RegistryKey<Biome>> getDiscoveredBiomes(ItemStack itemStack) {
        List<RegistryKey<Biome>> biomeKeys = new ArrayList<>();

        NbtCompound tag = getData(itemStack);
        if (!tag.contains("discoveredBiomes")) return biomeKeys;

        NbtCompound discoveredBiomesTag = tag.getCompound("discoveredBiomes");
        List<String> keys = new ArrayList<>(discoveredBiomesTag.getKeys());
        keys.sort(Comparator.comparing((key) -> key));

        keys.forEach((key) -> {
            RegistryKey<Biome> entry = RegistryKey.of(RegistryKeys.BIOME, Identifier.of(key));
            if (entry == null || entry.getValue().getPath() == null || entry.getValue().getPath().isEmpty()) return;
            if (!biomeKeys.contains(entry)) biomeKeys.add(entry);
        });

        return biomeKeys;
    }

    public static void addDiscoveredBiome(ItemStack itemStack, PlayerEntity player, RegistryKey<Biome> biomeKey) {
        if (biomeKey == null || biomeKey.equals(ModBiomes.TARDIS_KEY)) return;

        SonicDevice.updateData(itemStack, (tag) -> {
            NbtCompound discoveredBiomesTag = tag.getCompound("discoveredBiomes");
            if (discoveredBiomesTag.contains(biomeKey.getValue().toString())) return;

            discoveredBiomesTag.putBoolean(biomeKey.getValue().toString(), true);
            tag.put("discoveredBiomes", discoveredBiomesTag);
            player.sendMessage(DWM.TEXTS.SONIC_DEVICE_BIOME_DISCOVERED.apply(CommonHelper.formatRegistryKey(biomeKey)), true);
        });
    }

    public static List<RegistryKey<Structure>> getDiscoveredStructures(ItemStack itemStack) {
        List<RegistryKey<Structure>> structureKeys = new ArrayList<>();

        NbtCompound tag = getData(itemStack);
        if (!tag.contains("discoveredStructures")) return structureKeys;

        NbtCompound discoveredStructuresTag = tag.getCompound("discoveredStructures");
        List<String> keys = new ArrayList<>(discoveredStructuresTag.getKeys());
        keys.sort(Comparator.comparing((key) -> key));

        keys.forEach((key) -> {
            RegistryKey<Structure> entry = RegistryKey.of(RegistryKeys.STRUCTURE, Identifier.of(key));
            if (entry == null || entry.getValue().getPath() == null || entry.getValue().getPath().isEmpty()) return;
            if (!structureKeys.contains(entry)) structureKeys.add(entry);
        });

        return structureKeys;
    }

    public static void addDiscoveredStructure(ItemStack itemStack, PlayerEntity player, RegistryKey<Structure> structureKey) {
        if (structureKey == null) return;

        SonicDevice.updateData(itemStack, (tag) -> {
            NbtCompound discoveredStructuresTag = tag.getCompound("discoveredStructures");
            if (discoveredStructuresTag.contains(structureKey.getValue().toString())) return;

            discoveredStructuresTag.putBoolean(structureKey.getValue().toString(), true);
            tag.put("discoveredStructures", discoveredStructuresTag);
            player.sendMessage(DWM.TEXTS.SONIC_DEVICE_STRUCTURE_DISCOVERED.apply(CommonHelper.formatRegistryKey(structureKey)), true);
        });
    }

    public static void loadDiscoveredLocations(ItemStack itemStack, PlayerEntity player, String sonicDeviceTardisId, String worldTardisId) {
        Optional<TardisStateManager> tardisHolder = TardisStateManager.get(DimensionHelper.getModWorld(worldTardisId, player.getServer()));
        if (tardisHolder.isEmpty()) return;

        TardisStateManager tardis = tardisHolder.get();
        TardisSystemResearch researchSystem = tardis.getSystem(TardisSystemResearch.class);

        SonicDevice.getDiscoveredWorlds(itemStack).forEach((dimensionKey) -> researchSystem.addVisitedWorld(dimensionKey, player.getUuid(), false));
        SonicDevice.getDiscoveredBiomes(itemStack).forEach((biomeKey) -> researchSystem.addVisitedBiome(biomeKey, player.getUuid(), false));
        SonicDevice.getDiscoveredStructures(itemStack).forEach((structureKey) -> researchSystem.addVisitedStructure(structureKey, player.getUuid(), false));

        if (sonicDeviceTardisId.equals(worldTardisId)) {
            SonicDevice.updateData(itemStack, (tag) -> {
                tag.remove("discoveredWorlds");
                tag.remove("discoveredBiomes");
                tag.remove("discoveredStructures");
            });
        }
        else {
            Optional<TardisStateManager> otherTardisHolder = TardisStateManager.get(DimensionHelper.getModWorld(sonicDeviceTardisId, player.getServer()));
            Optional<TardisSystemResearch> otherResearchSystemHolder = otherTardisHolder.map((tardisStateManager) -> tardisStateManager.getSystem(TardisSystemResearch.class));

            researchSystem.getAvailableDimensions().forEach((dimensionKey) -> {
                if (otherResearchSystemHolder.isPresent() && otherResearchSystemHolder.get().hasVisitedWorld(dimensionKey)) return;
                SonicDevice.addDiscoveredWorld(itemStack, player, dimensionKey);
            });

            researchSystem.getAvailableBiomes().forEach((biomeKey) -> {
                if (otherResearchSystemHolder.isPresent() && otherResearchSystemHolder.get().hasVisitedBiome(biomeKey)) return;
                SonicDevice.addDiscoveredBiome(itemStack, player, biomeKey);
            });

            researchSystem.getAvailableStructures().forEach((structureKey) -> {
                if (otherResearchSystemHolder.isPresent() && otherResearchSystemHolder.get().hasVisitedStructure(structureKey)) return;
                SonicDevice.addDiscoveredStructure(itemStack, player, structureKey);
            });
        }
    }
}

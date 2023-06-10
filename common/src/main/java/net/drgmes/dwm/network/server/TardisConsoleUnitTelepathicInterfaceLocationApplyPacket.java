package net.drgmes.dwm.network.server;

import com.mojang.datafixers.util.Pair;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.screens.TardisConsoleUnitTelepathicInterfaceLocationsScreen;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.setup.ModNetwork;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.structure.Structure;

import java.util.Optional;

public class TardisConsoleUnitTelepathicInterfaceLocationApplyPacket extends BaseC2SMessage {
    private final Identifier id;
    private final String type;

    public TardisConsoleUnitTelepathicInterfaceLocationApplyPacket(Identifier id, String type) {
        this.id = id;
        this.type = type;
    }

    public static TardisConsoleUnitTelepathicInterfaceLocationApplyPacket create(PacketByteBuf buf) {
        return new TardisConsoleUnitTelepathicInterfaceLocationApplyPacket(buf.readIdentifier(), buf.readString());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_CONSOLE_UNIT_TELEPATHIC_INTERFACE_LOCATION_APPLY;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(this.id);
        buf.writeString(this.type);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();

        TardisStateManager.get((ServerWorld) player.getWorld()).ifPresent((tardis) -> {
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

    private static boolean findBiome(Identifier id, TardisStateManager tardis) {
        BlockPos exteriorPos = tardis.getDestinationExteriorPosition();
        ServerWorld exteriorWorld = DimensionHelper.getWorld(tardis.getDestinationExteriorDimension(), tardis.getWorld().getServer());
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
        ServerWorld exteriorWorld = DimensionHelper.getWorld(tardis.getDestinationExteriorDimension(), tardis.getWorld().getServer());
        if (exteriorWorld == null) return false;

        Registry<Structure> registry = exteriorWorld.getRegistryManager().get(RegistryKeys.STRUCTURE);

        Structure structure = registry.get(id);
        if (structure == null) return false;

        Optional<RegistryKey<Structure>> structureKeyHolder = registry.getKey(structure);
        if (structureKeyHolder.isEmpty()) return false;

        Optional<RegistryEntry.Reference<Structure>> structureEntry = registry.getEntry(structureKeyHolder.get());
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

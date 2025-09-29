package net.drgmes.dwm.network.server;

import com.mojang.datafixers.util.Pair;
import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.enums.TardisTelepathicInterfaceDataType;
import net.drgmes.dwm.enums.TardisVerticalScanning;
import net.drgmes.dwm.network.IPacket;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
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

public record TardisConsoleUnitTelepathicInterfaceLocationApplyPacket(
    String id,
    String type
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_console_unit_telepathic_interface_location_apply");
    public static final CustomPayload.Id<TardisConsoleUnitTelepathicInterfaceLocationApplyPacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisConsoleUnitTelepathicInterfaceLocationApplyPacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, TardisConsoleUnitTelepathicInterfaceLocationApplyPacket::id,
        PacketCodecs.STRING, TardisConsoleUnitTelepathicInterfaceLocationApplyPacket::type,
        TardisConsoleUnitTelepathicInterfaceLocationApplyPacket::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public static void handle(TardisConsoleUnitTelepathicInterfaceLocationApplyPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            PlayerEntity player = context.getPlayer();

            ServerWorld serverWorld = (ServerWorld) player.getWorld();
            if (!TardisHelper.isTardisDimension(serverWorld)) return;

            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);
                TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);

                if (!flightSystem.isEnabled()) {
                    player.sendMessage(DWM.TEXTS.FLIGHT_SYSTEM_NOT_INSTALLED, true);
                    return;
                }

                if (flightSystem.inProgress()) {
                    player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_LANDED, true);
                    return;
                }

                if (materializationSystem.inProgress()) {
                    player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_MATERIALIZED, true);
                    return;
                }

                Text message = null;
                TardisTelepathicInterfaceDataType dataType = TardisTelepathicInterfaceDataType.valueOf(payload.type);

                if (dataType == TardisTelepathicInterfaceDataType.BIOME) {
                    String msg = tryFindBiome(Identifier.of(payload.id), tardis) ? "found" : "not_found";
                    message = Text.translatable("message.dwm.tardis.telepathic_interface.biome." + msg);
                }
                else if (dataType == TardisTelepathicInterfaceDataType.STRUCTURE) {
                    String msg = tryFindStructure(Identifier.of(payload.id), tardis) ? "found" : "not_found";
                    message = Text.translatable("message.dwm.tardis.telepathic_interface.structure." + msg);
                }

                if (message != null) player.sendMessage(message, true);
            });
        });
    }

    private static boolean tryFindBiome(Identifier id, TardisStateManager tardis) {
        BlockPos exteriorPos = tardis.getDestinationExteriorPosition();
        ServerWorld exteriorWorld = DimensionHelper.getWorld(tardis.getDestinationExteriorDimension(), tardis.getWorld().getServer());
        if (exteriorWorld == null) return false;

        Pair<BlockPos, RegistryEntry<Biome>> pair = exteriorWorld.locateBiome((entry) -> entry.matchesId(id), exteriorPos, 6400, 32, 64);
        if (pair == null) return false;

        boolean isUnderground = exteriorWorld.getRegistryKey() == World.NETHER;

        TardisVerticalScanning verticalScanning = TardisVerticalScanning.TOP;
        if (isUnderground) verticalScanning = TardisVerticalScanning.BOTTOM;

        BlockPos blockPos = pair.getFirst().withY(exteriorWorld.getTopY() - 2);
        if (isUnderground) blockPos = blockPos.withY(exteriorWorld.getBottomY());

        tardis.getSystem(TardisSystemMaterialization.class).setVerticalScanning(verticalScanning);
        tardis.setDestinationPosition(blockPos);
        tardis.markConsoleTilesUpdated();
        return true;
    }

    private static boolean tryFindStructure(Identifier id, TardisStateManager tardis) {
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

        boolean isUnderground = exteriorWorld.getRegistryKey() == World.NETHER;
        if (structure.getFeatureGenerationStep() == GenerationStep.Feature.STRONGHOLDS) isUnderground = true;
        else if (structure.getFeatureGenerationStep() == GenerationStep.Feature.UNDERGROUND_DECORATION) isUnderground = true;
        else if (structure.getFeatureGenerationStep() == GenerationStep.Feature.UNDERGROUND_STRUCTURES) isUnderground = true;

        TardisVerticalScanning verticalScanning = TardisVerticalScanning.TOP;
        if (isUnderground) verticalScanning = TardisVerticalScanning.BOTTOM;

        BlockPos blockPos = pair.getFirst().withY(exteriorWorld.getTopY() - 2);
        if (isUnderground) blockPos = blockPos.withY(exteriorWorld.getBottomY());

        tardis.getSystem(TardisSystemMaterialization.class).setVerticalScanning(verticalScanning);
        tardis.setDestinationPosition(blockPos);
        tardis.markConsoleTilesUpdated();
        return true;
    }
}

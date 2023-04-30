package net.drgmes.dwm.mixin.fabric;

import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.teleportation.ServerTeleportationManager;

import java.util.List;

@Mixin(ServerTeleportationManager.class)
public class MixinServerTeleportationManager {
    @Inject(method = "teleportPlayer", at = @At("TAIL"))
    private void teleportPlayer(ServerPlayerEntity player, RegistryKey<World> dimensionTo, Vec3d newEyePos, CallbackInfo ci) {
        ServerWorld world = DimensionHelper.getWorld(dimensionTo, player.server);
        if (!TardisHelper.isTardisDimension(world)) return;

        int radius = 3;
        ChunkPos playerChunkPos = new ChunkPos(new BlockPos((int) newEyePos.x, (int) newEyePos.y, (int) newEyePos.z));
        TardisStateManager.get(world).ifPresent((tardis) -> {
            List<ChunkPos> consoleTilesChunks = tardis.getConsoleTiles().stream().map((consoleTile) -> new ChunkPos(consoleTile.getPos())).toList();

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    ChunkPos chunkPos = new ChunkPos(playerChunkPos.x + x, playerChunkPos.z + z);

                    if (consoleTilesChunks.contains(chunkPos)) {
                        tardis.updateConsoleTiles();
                        return;
                    }
                }
            }
        });
    }
}

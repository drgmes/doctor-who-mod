package net.drgmes.dwm.common.boti;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BotiBlocksStorage {
    public final Map<BlockPos, BlockState> blockEntries = new HashMap<>();
    public boolean isUpdated;

    private static Map<String, BotiBlocksStorage> storages = new HashMap<>();
    private int radius;
    private int distance;

    public static BotiBlocksStorage getStorage(String id, int radius, int distance) {
        if (storages.containsKey(id)) {
            return storages.get(id);
        }

        BotiBlocksStorage storage = new BotiBlocksStorage(radius, distance);
        storage.gatherBlocks();
        storages.put(id, storage);
        return storage;
    }

    public static BotiBlocksStorage getStorage(String id) {
        return BotiBlocksStorage.getStorage(id, 5, 15);
    }

    private BotiBlocksStorage(int radius, int distance) {
        this.radius = radius;
        this.distance = distance;
    }

    public void gatherBlocks() {
        this.blockEntries.clear();
        this.isUpdated = true;

        for (int z = 0; z <= this.distance; z++) {
            int yRadius = z + this.radius;
            int xRadius = z + this.radius;

            for (int x = -xRadius; x <= xRadius; x++) {
                for (int y = 0; y >= -yRadius; y--) {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    BlockState blockState = Blocks.GRASS_BLOCK.defaultBlockState();
                    this.blockEntries.put(blockPos, blockState);
                }

                // for (int y = -yRadius; y <= yRadius; y++) {
                //     if (y > -2) continue; // TODO tmp
                //     BlockPos blockPos = new BlockPos(x, y, z);
                //     BlockState blockState = Blocks.GRASS_BLOCK.defaultBlockState();
                //     this.blockEntries.put(blockPos, blockState);
                // }
            }
        }
    }
}

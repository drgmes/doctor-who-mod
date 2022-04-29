package net.drgmes.dwm.common.boti;

import java.util.HashMap;
import java.util.Map;

import net.drgmes.dwm.utils.DWMUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
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
        storages.put(id, storage);
        return storage;
    }

    public static BotiBlocksStorage getStorage(String id) {
        return BotiBlocksStorage.getStorage(id, 5, 30);
    }

    private BotiBlocksStorage(int radius, int distance) {
        this.radius = radius;
        this.distance = distance;
    }

    public void gatherBlocks(Level level, BlockPos entraceBlockPos, Direction face) {
        this.blockEntries.clear();
        this.isUpdated = true;

        BlockPos initialBlockPos = entraceBlockPos.relative(face).immutable();
        Rotation rotation;

        switch (face) {
            case WEST:
                rotation = Rotation.CLOCKWISE_90;
                break;
            case EAST:
                rotation = Rotation.COUNTERCLOCKWISE_90;
                break;
            case NORTH:
                rotation = Rotation.CLOCKWISE_180;
                break;
            default:
                rotation = Rotation.NONE;
                break;
        }

        for (int z = 0; z <= this.distance; z++) {
            int yRadius = z + this.radius;
            int xRadius = z + this.radius;

            for (int x = -xRadius; x <= xRadius; x++) {
                for (int y = -yRadius; y <= yRadius; y++) {
                    BlockPos blockPos = new BlockPos(x, y, z).immutable();

                    BlockPos realBlockPos = initialBlockPos.offset(blockPos.rotate(rotation)).immutable();
                    BlockPos realUpBlockPos = initialBlockPos.offset(blockPos.above().rotate(rotation)).immutable();
                    BlockPos realDownBlockPos = initialBlockPos.offset(blockPos.below().rotate(rotation)).immutable();
                    BlockPos realWestBlockPos = initialBlockPos.offset(blockPos.west().rotate(rotation)).immutable();
                    BlockPos realEastBlockPos = initialBlockPos.offset(blockPos.east().rotate(rotation)).immutable();
                    BlockPos realFrontBlockPos = initialBlockPos.offset(blockPos.north().rotate(rotation)).immutable();

                    BlockState upBlockState = level.getBlockState(realUpBlockPos);
                    BlockState downBlockState = level.getBlockState(realDownBlockPos);
                    BlockState westBlockState = level.getBlockState(realWestBlockPos);
                    BlockState eastBlockState = level.getBlockState(realEastBlockPos);
                    BlockState frontBlockState = level.getBlockState(realFrontBlockPos);

                    boolean isUpSolid = !DWMUtils.checkBlockIsTransparent(upBlockState);
                    boolean isDownSolid = !DWMUtils.checkBlockIsTransparent(downBlockState);
                    boolean isWestSolid = !DWMUtils.checkBlockIsTransparent(westBlockState);
                    boolean isEastSolid = !DWMUtils.checkBlockIsTransparent(eastBlockState);
                    boolean isFrontSolid = z > 0 && !DWMUtils.checkBlockIsTransparent(frontBlockState);

                    if ((y > 0 ? isDownSolid : isUpSolid) && (x > 0 ? isWestSolid : isEastSolid) && isFrontSolid) {
                        continue;
                    }

                    this.blockEntries.put(blockPos, level.getBlockState(realBlockPos));
                }
            }
        }
    }
}

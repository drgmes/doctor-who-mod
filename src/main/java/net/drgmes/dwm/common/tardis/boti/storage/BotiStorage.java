package net.drgmes.dwm.common.tardis.boti.storage;

import net.drgmes.dwm.common.tardis.boti.storage.wrappers.BotiBlockEntityWrapper;
import net.drgmes.dwm.common.tardis.boti.storage.wrappers.BotiBlockWrapper;
import net.drgmes.dwm.utils.DWMUtils;
import net.drgmes.dwm.utils.helpers.LevelHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map.Entry;

public class BotiStorage {
    public final HashMap<BlockPos, BotiBlockWrapper> blocks = new HashMap<>();
    public final HashMap<BlockPos, BotiBlockEntityWrapper> blockEntities = new HashMap<>();
    public boolean isUpdated = true;

    private int radius = 10;
    private int distance = 20;
    private Direction direction = Direction.NORTH;

    public static BotiStorage create(FriendlyByteBuf buffer) {
        BotiStorage botiStorage = new BotiStorage();
        botiStorage.readFromBuffer(buffer);
        return botiStorage;
    }

    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeInt(this.radius);
        buffer.writeInt(this.distance);
        buffer.writeInt(this.direction.get2DDataValue());

        buffer.writeInt(this.blocks.size());
        for (Entry<BlockPos, BotiBlockWrapper> entry : this.blocks.entrySet()) {
            buffer.writeBlockPos(entry.getKey());
            buffer.writeNbt(entry.getValue().serialize());
        }

        buffer.writeInt(this.blockEntities.size());
        for (Entry<BlockPos, BotiBlockEntityWrapper> entry : this.blockEntities.entrySet()) {
            buffer.writeBlockPos(entry.getKey());
            buffer.writeNbt(entry.getValue().serialize());
        }
    }

    public void readFromBuffer(FriendlyByteBuf buffer) {
        this.blocks.clear();
        this.blockEntities.clear();

        this.radius = buffer.readInt();
        this.distance = buffer.readInt();
        this.direction = Direction.from2DDataValue(buffer.readInt());

        int blocksCount = buffer.readInt();
        for (int i = 0; i < blocksCount; ++i) {
            this.blocks.put(buffer.readBlockPos(), BotiBlockWrapper.load(buffer.readNbt()));
        }

        int blockEntitiesCount = buffer.readInt();
        for (int i = 0; i < blockEntitiesCount; ++i) {
            this.blockEntities.put(buffer.readBlockPos(), BotiBlockEntityWrapper.load(buffer.readNbt()));
        }
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void updateBoti(Level level, BlockPos blockPos) {
        if (level.isClientSide) return;
        this.gatherBlocks(level, blockPos.immutable());
    }

    private void gatherBlocks(Level level, BlockPos initialBlockPos) {
        this.blocks.clear();
        this.blockEntities.clear();

        Rotation rotation = LevelHelper.getRotation(this.direction);

        for (int z = 0; z <= this.distance; z++) {
            int xRadius = z + this.radius;
            int yRadius = z + this.radius;

            for (int xMod = -1; xMod <= 1; xMod++) {
                if (xMod == 0) continue;

                for (int yMod = -1; yMod <= 1; yMod++) {
                    if (yMod == 0) continue;

                    for (int x = 0; xMod > 0 ? x <= xRadius : x >= -xRadius; x += xMod) {
                        for (int y = 0; yMod > 0 ? y <= yRadius : y >= -yRadius; y += yMod) {
                            BlockPos blockPos = new BlockPos(x, y, z).immutable();
                            BlockPos rotatedBlockPos = blockPos.rotate(rotation).immutable();

                            if (blockPos.equals(BlockPos.ZERO)) continue;
                            if (this.blocks.containsKey(rotatedBlockPos)) continue;

                            BlockPos realBlockPos = initialBlockPos.offset(rotatedBlockPos).immutable();
                            BlockState blockState = level.getBlockState(realBlockPos);
                            if (blockState.isAir()) continue;

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

                            this.blocks.put(rotatedBlockPos, BotiBlockWrapper.create(level, realBlockPos, blockState));

                            BlockEntity blockEntity = level.getBlockEntity(realBlockPos);
                            if (blockEntity != null)
                                this.blockEntities.put(rotatedBlockPos, new BotiBlockEntityWrapper(realBlockPos, blockState, blockEntity));
                        }
                    }
                }
            }
        }
    }
}

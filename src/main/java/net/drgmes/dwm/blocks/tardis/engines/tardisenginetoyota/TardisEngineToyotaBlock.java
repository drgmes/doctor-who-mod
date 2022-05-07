package net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota;

import net.drgmes.dwm.blocks.tardis.engines.BaseTardisEngineBlock;
import net.drgmes.dwm.network.ClientboundTardisEngineUpdatePacket;
import net.drgmes.dwm.setup.ModPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class TardisEngineToyotaBlock extends BaseTardisEngineBlock {
    public TardisEngineToyotaBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TardisEngineToyotaBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!(level.getBlockEntity(blockPos) instanceof TardisEngineToyotaBlockEntity tardisEngineBlockEntity)) return InteractionResult.PASS;

        boolean isChanged = false;
        Direction facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        Direction direction = Direction.fromYRot(result.getDirection().toYRot() - facing.toYRot()).getOpposite();

        Vec3 hit = result.getLocation().subtract(blockPos.getX(), blockPos.getY(), blockPos.getZ()).scale(10);
        if (hit.y >= 8) return InteractionResult.PASS;

        double x = switch (direction) {
            case NORTH -> hit.x;
            case SOUTH -> hit.x;
            case WEST -> hit.z;
            case EAST -> hit.z;
            default -> -1;
        };

        if (level.isClientSide && x > 0) return InteractionResult.SUCCESS;
        if (level.isClientSide && x < 0) return InteractionResult.PASS;

        if (x > 0 && x <= 3) {
            x += (8 - hit.y) / 2;
        }
        else if (x > 7 && x <= 10) {
            x -= (8 - hit.y) / 2;
        }

        if (x > 0 && x <= 3) {
            if (direction == Direction.NORTH) {
                tardisEngineBlockEntity.isOpenedCover2 = !tardisEngineBlockEntity.isOpenedCover2;
                isChanged = true;
            }
            else if (direction == Direction.WEST) {
                tardisEngineBlockEntity.isOpenedCover2 = !tardisEngineBlockEntity.isOpenedCover2;
                isChanged = true;
            }
            else if (direction == Direction.SOUTH) {
                tardisEngineBlockEntity.isOpenedCover4 = !tardisEngineBlockEntity.isOpenedCover4;
                isChanged = true;
            }
            else if (direction == Direction.EAST) {
                tardisEngineBlockEntity.isOpenedCover8 = !tardisEngineBlockEntity.isOpenedCover8;
                isChanged = true;
            }
        }
        else if (x > 3 && x <= 7) {
            if (direction == Direction.NORTH) {
                tardisEngineBlockEntity.isOpenedCover1 = !tardisEngineBlockEntity.isOpenedCover1;
                isChanged = true;
            }
            else if (direction == Direction.WEST) {
                tardisEngineBlockEntity.isOpenedCover3 = !tardisEngineBlockEntity.isOpenedCover3;
                isChanged = true;
            }
            else if (direction == Direction.SOUTH) {
                tardisEngineBlockEntity.isOpenedCover5 = !tardisEngineBlockEntity.isOpenedCover5;
                isChanged = true;
            }
            else if (direction == Direction.EAST) {
                tardisEngineBlockEntity.isOpenedCover7 = !tardisEngineBlockEntity.isOpenedCover7;
                isChanged = true;
            }
        }
        else if (x > 7 && x <= 10) {
            if (direction == Direction.NORTH) {
                tardisEngineBlockEntity.isOpenedCover8 = !tardisEngineBlockEntity.isOpenedCover8;
                isChanged = true;
            }
            else if (direction == Direction.WEST) {
                tardisEngineBlockEntity.isOpenedCover4 = !tardisEngineBlockEntity.isOpenedCover4;
                isChanged = true;
            }
            else if (direction == Direction.SOUTH) {
                tardisEngineBlockEntity.isOpenedCover6 = !tardisEngineBlockEntity.isOpenedCover6;
                isChanged = true;
            }
            else if (direction == Direction.EAST) {
                tardisEngineBlockEntity.isOpenedCover6 = !tardisEngineBlockEntity.isOpenedCover6;
                isChanged = true;
            }
        }

        if (isChanged) {
            CompoundTag tag = new CompoundTag();
            tardisEngineBlockEntity.saveAdditional(tag);

            ClientboundTardisEngineUpdatePacket packet = new ClientboundTardisEngineUpdatePacket(blockPos, tag);
            ModPackets.send(level.getChunkAt(blockPos), packet);
        }

        return InteractionResult.SUCCESS;
    }
}

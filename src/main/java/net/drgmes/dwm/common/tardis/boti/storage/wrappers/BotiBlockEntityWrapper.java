package net.drgmes.dwm.common.tardis.boti.storage.wrappers;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class BotiBlockEntityWrapper {
    private final CompoundTag blockEntityTag;
    private final BlockState blockState;
    private final BlockPos blockPos;

    public BotiBlockEntityWrapper(BlockPos blockPos, BlockState blockState, CompoundTag blockEntityTag) {
        this.blockEntityTag = blockEntityTag;
        this.blockState = blockState;
        this.blockPos = blockPos.immutable();
    }

    public BotiBlockEntityWrapper(BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        this(blockPos, blockState, blockEntity.serializeNBT());
    }

    public static BotiBlockEntityWrapper load(CompoundTag tag) {
        CompoundTag blockEntityTag = tag.getCompound("blockEntity");
        BlockState blockState = NbtUtils.readBlockState(tag.getCompound("blockState"));
        BlockPos blockPos = BlockPos.of(tag.getLong("blockPos"));

        return new BotiBlockEntityWrapper(blockPos, blockState, blockEntityTag);
    }

    public CompoundTag getBlockEntityTag() {
        return this.blockEntityTag;
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public BlockEntity createBlockEntity(Level level) {
        ResourceLocation idKey = new ResourceLocation(this.blockEntityTag.getString("id"));
        BlockEntity blockEntity = ForgeRegistries.BLOCK_ENTITIES.getValue(idKey).create(blockPos, blockState);

        if (blockEntity != null) {
            blockEntity.deserializeNBT(this.blockEntityTag);
            blockEntity.setLevel(level);
        }

        return blockEntity;
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.put("blockEntity", this.blockEntityTag);
        tag.put("blockState", NbtUtils.writeBlockState(this.blockState));
        tag.putLong("blockPos", this.blockPos.asLong());
        return tag;
    }
}

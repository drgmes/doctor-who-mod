package net.drgmes.dwm.common.tardis.boti.storage.wrappers;

import java.util.Optional;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class BotiBlockWrapper {
    private int light;
    private int skyLight;
    private BlockPos blockPos;
    private BlockState blockState;
    private FluidState fluidState;

    public BotiBlockWrapper(int light, int skyLight, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        this.light = light;
        this.skyLight = skyLight;
        this.blockPos = blockPos;
        this.fluidState = fluidState;
        this.blockState = blockState;
    }

    public BotiBlockWrapper(int light, int skyLight, BlockPos blockPos, BlockState blockState) {
        this(light, skyLight, blockPos, blockState, Fluids.EMPTY.defaultFluidState());
    }

    public BotiBlockWrapper(int light, int skyLight, BlockPos blockPos) {
        this(light, skyLight, blockPos, Blocks.AIR.defaultBlockState(), Fluids.EMPTY.defaultFluidState());
    }

    public static BotiBlockWrapper create(Level level, BlockPos blockPos, BlockState blockState) {
        FluidState fluidState = level.getFluidState(blockPos);
        int skyLight = level.getBrightness(LightLayer.SKY, blockPos);
        int light = Math.max(level.getChunkSource().getLightEngine().getLayerListener(LightLayer.BLOCK).getLightValue(blockPos), blockState.getLightBlock(level, blockPos));

        return new BotiBlockWrapper(light, skyLight, blockPos, blockState, fluidState);
    }

    public static BotiBlockWrapper load(CompoundTag tag) {
        int light = tag.getInt("light");
        int skyLight = tag.getInt("skyLight");
        BlockPos blockPos = BlockPos.of(tag.getLong("blockPos"));
        BlockState blockState = NbtUtils.readBlockState(tag.getCompound("blockState"));
        FluidState fluidState = Fluids.EMPTY.defaultFluidState();

        Optional<Pair<FluidState, Tag>> fluidStateHolder = FluidState.CODEC.decode(NbtOps.INSTANCE, tag.get("fluidState")).result();
        if (!fluidStateHolder.isEmpty()) fluidState = fluidStateHolder.get().getFirst();

        return new BotiBlockWrapper(light, skyLight, blockPos, blockState, fluidState);
    }

    public int getLight() {
        return this.light;
    }

    public int getSkyLight() {
        return this.skyLight;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public FluidState getFluidState() {
        return this.fluidState;
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("light", this.light);
        tag.putInt("skyLight", this.skyLight);
        tag.putLong("blockPos", this.blockPos.asLong());
        tag.put("blockState", NbtUtils.writeBlockState(blockState));
        tag.put("fluidState", (CompoundTag) FluidState.CODEC.encodeStart(NbtOps.INSTANCE, this.fluidState).result().orElse(new CompoundTag()));
        return tag;
    }
}

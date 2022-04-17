package net.drgmes.dwm.caps;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class TardisLevelCapability implements ITardisLevelData {
    private ResourceKey<Level> exteriorDim;
    private BlockPos exteriorPosition;
    private Direction exteriorFacing;

    public TardisLevelCapability() {
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        if (this.exteriorDim != null) tag.putString("exteriorDim", this.exteriorDim.location().toString());
        if (this.exteriorFacing != null) tag.putString("exteriorFacing", this.exteriorFacing.getName());
        if (this.exteriorPosition != null) tag.putInt("exteriorPosX", this.exteriorPosition.getX());
        if (this.exteriorPosition != null) tag.putInt("exteriorPosY", this.exteriorPosition.getY());
        if (this.exteriorPosition != null) tag.putInt("exteriorPosZ", this.exteriorPosition.getZ());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.setExteriorDim(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(tag.getString("exteriorDim"))));
        this.setExteriorFacing(Direction.byName(tag.getString("exteriorFacing")));
        this.setExteriorPosition(new BlockPos(tag.getInt("exteriorPosX"), tag.getInt("exteriorPosY"), tag.getInt("exteriorPosZ")));
    }

    @Override
    public ResourceKey<Level> getExteriorDim() {
        return this.exteriorDim;
    }

    @Override
    public BlockPos getExteriorPosition() {
        return this.exteriorPosition;
    }

    @Override
    public BlockPos getExteriorRelativePosition() {
        return this.getExteriorPosition().relative(this.getExteriorFacing());
    }

    @Override
    public Direction getExteriorFacing() {
        return this.exteriorFacing;
    }

    @Override
    public void setExteriorDim(ResourceKey<Level> levelKey) {
        this.exteriorDim = levelKey;
    }

    @Override
    public void setExteriorPosition(BlockPos blockPos) {
        this.exteriorPosition = blockPos.immutable();
    }

    @Override
    public void setExteriorFacing(Direction direction) {
        this.exteriorFacing = direction;
    }
}

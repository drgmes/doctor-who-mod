package net.drgmes.dwm.caps;

import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlsStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class TardisLevelCapability implements ITardisLevelData {
    private ResourceKey<Level> prevExteriorDimension;
    private ResourceKey<Level> currExteriorDimension;
    private ResourceKey<Level> destExteriorDimension;

    private Direction prevExteriorFacing;
    private Direction currExteriorFacing;
    private Direction destExteriorFacing;

    private BlockPos prevExteriorPosition;
    private BlockPos currExteriorPosition;
    private BlockPos destExteriorPosition;

    public TardisLevelCapability() {
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        CompoundTag tdTag = new CompoundTag();

        tdTag.putString("prevExteriorDimension", this.getPreviousExteriorDimension().location().toString());
        tdTag.putString("currExteriorDimension", this.getCurrentExteriorDimension().location().toString());
        tdTag.putString("destExteriorDimension", this.getDestinationExteriorDimension().location().toString());

        tdTag.putString("prevExteriorFacing", this.getPreviousExteriorFacing().getName());
        tdTag.putString("currExteriorFacing", this.getCurrentExteriorFacing().getName());
        tdTag.putString("destExteriorFacing", this.getDestinationExteriorFacing().getName());

        tdTag.putInt("prevExteriorPositionX", this.getPreviousExteriorPosition().getX());
        tdTag.putInt("prevExteriorPositionY", this.getPreviousExteriorPosition().getY());
        tdTag.putInt("prevExteriorPositionZ", this.getPreviousExteriorPosition().getZ());

        tdTag.putInt("currExteriorPositionX", this.getCurrentExteriorPosition().getX());
        tdTag.putInt("currExteriorPositionY", this.getCurrentExteriorPosition().getY());
        tdTag.putInt("currExteriorPositionZ", this.getCurrentExteriorPosition().getZ());

        tdTag.putInt("destExteriorPositionX", this.getDestinationExteriorPosition().getX());
        tdTag.putInt("destExteriorPositionY", this.getDestinationExteriorPosition().getY());
        tdTag.putInt("destExteriorPositionZ", this.getDestinationExteriorPosition().getZ());

        tag.put("tardisdim", tdTag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        CompoundTag tdTag = tag.getCompound("tardisdim");

        this.prevExteriorDimension = this.getDimensionByKey(tdTag, "prevExteriorDimension");
        this.currExteriorDimension = this.getDimensionByKey(tdTag, "currExteriorDimension");
        this.destExteriorDimension = this.getDimensionByKey(tdTag, "destExteriorDimension");

        this.prevExteriorFacing = this.getDirectionByKey(tdTag, "prevExteriorFacing");
        this.currExteriorFacing = this.getDirectionByKey(tdTag, "currExteriorFacing");
        this.destExteriorFacing = this.getDirectionByKey(tdTag, "destExteriorFacing");

        this.prevExteriorPosition = this.getBlockPosByKey(tdTag, "prevExteriorPosition");
        this.currExteriorPosition = this.getBlockPosByKey(tdTag, "currExteriorPosition");
        this.destExteriorPosition = this.getBlockPosByKey(tdTag, "destExteriorPosition");
    }

    @Override
    public boolean isValid() {
        return this.currExteriorDimension != null;
    }

    @Override
    public ResourceKey<Level> getPreviousExteriorDimension() {
        return this.prevExteriorDimension != null ? this.prevExteriorDimension : this.getCurrentExteriorDimension();
    }

    @Override
    public ResourceKey<Level> getCurrentExteriorDimension() {
        return this.currExteriorDimension;
    }

    @Override
    public ResourceKey<Level> getDestinationExteriorDimension() {
        return this.destExteriorDimension != null ? this.destExteriorDimension : this.getCurrentExteriorDimension();
    }

    @Override
    public Direction getPreviousExteriorFacing() {
        return this.prevExteriorFacing != null ? this.prevExteriorFacing : this.getCurrentExteriorFacing();
    }

    @Override
    public Direction getCurrentExteriorFacing() {
        return this.currExteriorFacing;
    }

    @Override
    public Direction getDestinationExteriorFacing() {
        return this.destExteriorFacing != null ? this.destExteriorFacing : this.getCurrentExteriorFacing();
    }

    @Override
    public BlockPos getPreviousExteriorPosition() {
        return this.prevExteriorPosition != null ? this.prevExteriorPosition : this.getCurrentExteriorPosition();
    }

    @Override
    public BlockPos getCurrentExteriorPosition() {
        return this.currExteriorPosition;
    }

    @Override
    public BlockPos getCurrentExteriorRelativePosition() {
        return this.currExteriorPosition.relative(this.currExteriorFacing);
    }

    @Override
    public BlockPos getDestinationExteriorPosition() {
        return this.destExteriorPosition != null ? this.destExteriorPosition : this.getCurrentExteriorPosition();
    }

    @Override
    public void updateDimension(ResourceKey<Level> dimension) {
        this.prevExteriorDimension = this.currExteriorDimension;
        this.currExteriorDimension = dimension;
    }

    @Override
    public void updateFacing(Direction direction) {
        this.prevExteriorFacing = this.currExteriorFacing;
        this.currExteriorFacing = direction;
    }

    @Override
    public void updatePosition(BlockPos blockPos) {
        this.prevExteriorPosition = this.currExteriorPosition;
        this.currExteriorPosition = blockPos;
    }

    @Override
    public void applyControlsStorage(TardisConsoleControlsStorage controlsStorage) {
        int xyzStep = 1;
        if (this.destExteriorDimension == null) this.destExteriorDimension = this.currExteriorDimension;
        if (this.destExteriorFacing == null) this.destExteriorFacing = this.currExteriorFacing;
        if (this.destExteriorPosition == null) this.destExteriorPosition = this.currExteriorPosition;

        int xSet = (int) controlsStorage.get(TardisConsoleControlRoles.XSET);
        if (xSet != 0) this.destExteriorPosition = xSet > 0 ? this.destExteriorPosition.east(xyzStep) : this.destExteriorPosition.west(xyzStep);

        int ySet = (int) controlsStorage.get(TardisConsoleControlRoles.YSET);
        if (ySet != 0) this.destExteriorPosition = ySet > 0 ? this.destExteriorPosition.above(xyzStep) : this.destExteriorPosition.below(xyzStep);

        int zSet = (int) controlsStorage.get(TardisConsoleControlRoles.ZSET);
        if (zSet != 0) this.destExteriorPosition = zSet > 0 ? this.destExteriorPosition.south(xyzStep) : this.destExteriorPosition.north(xyzStep);

        int facing = (int) controlsStorage.get(TardisConsoleControlRoles.FACING);
        this.destExteriorFacing = Direction.values()[(facing < 0 ? TardisConsoleControlRoles.FACING.maxIntValue + facing : facing) + 2];

        if ((int) controlsStorage.get(TardisConsoleControlRoles.RANDOMIZER) != 0) {
            boolean facingRandom = Math.random() * 10 > 5;

            if (facingRandom) this.destExteriorPosition = this.destExteriorPosition.east((int) Math.round(Math.random() * 10 * xyzStep));
            else this.destExteriorPosition = this.destExteriorPosition.west((int) Math.round(Math.random() * 10 * xyzStep));

            if (facingRandom) this.destExteriorPosition = this.destExteriorPosition.south((int) Math.round(Math.random() * 10 * xyzStep));
            else this.destExteriorPosition = this.destExteriorPosition.north((int) Math.round(Math.random() * 10 * xyzStep));
        }
    }

    private ResourceKey<Level> getDimensionByKey(CompoundTag tag, String key) {
        return ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(tag.getString(key)));
    }

    private Direction getDirectionByKey(CompoundTag tag, String key) {
        return Direction.byName(tag.getString(key));
    }

    private BlockPos getBlockPosByKey(CompoundTag tag, String key) {
        return new BlockPos(tag.getInt(key + "X"), tag.getInt(key + "Y"), tag.getInt(key + "Z"));
    }
}

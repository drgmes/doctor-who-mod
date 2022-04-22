package net.drgmes.dwm.caps;

import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.setup.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface ITardisLevelData extends INBTSerializable<CompoundTag> {
    boolean isValid();

    ResourceKey<Level> getPreviousExteriorDimension();
    ResourceKey<Level> getCurrentExteriorDimension();
    ResourceKey<Level> getDestinationExteriorDimension();

    Direction getPreviousExteriorFacing();
    Direction getCurrentExteriorFacing();
    Direction getDestinationExteriorFacing();

    BlockPos getPreviousExteriorPosition();
    BlockPos getCurrentExteriorPosition();
    BlockPos getCurrentExteriorRelativePosition();
    BlockPos getDestinationExteriorPosition();

    void updateDimension(ResourceKey<Level> dimension);
    void updateFacing(Direction direction);
    void updatePosition(BlockPos blockPos);

    void applyControlsStorage(TardisConsoleControlsStorage controlsStorage);

    public static class TardisLevelProvider implements ICapabilitySerializable<CompoundTag> {
        final private LazyOptional<ITardisLevelData> holder;
        private ITardisLevelData data;

        public TardisLevelProvider(Level level) {
            this.data = new TardisLevelCapability();
            this.holder = LazyOptional.of(() -> this.data);
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == ModCapabilities.TARDIS_DATA ? this.holder.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.data.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.data.deserializeNBT(nbt);
        }

        public Tag writeNBT(Capability<ITardisLevelData> capability, ITardisLevelData instance, Direction side) {
            return instance.serializeNBT();
        }

        public void readNBT(Capability<ITardisLevelData> capability, ITardisLevelData instance, Direction side, Tag nbt) {
            instance.deserializeNBT((CompoundTag) nbt);
        }
    }
}

package net.drgmes.dwm.caps;

import net.drgmes.dwm.setup.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface ITardisChunkLoader extends INBTSerializable<CompoundTag> {
    ServerLevel getLevel();

    void add(SectionPos sectionPos, BlockPos blockPos);

    void remove(SectionPos sectionPos, BlockPos blockPos);

    void tick();

    class TardisChunkLoaderProvider implements ICapabilitySerializable<CompoundTag> {
        public final LazyOptional<ITardisChunkLoader> holder;
        private final ITardisChunkLoader chunkLoader;

        public TardisChunkLoaderProvider(ServerLevel level) {
            this.chunkLoader = new TardisChunkLoaderCapability(level);
            this.holder = LazyOptional.of(() -> this.chunkLoader);
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == ModCapabilities.TARDIS_CHUNK_LOADER ? this.holder.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.chunkLoader.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.chunkLoader.deserializeNBT(nbt);
        }

        public Tag writeNBT(Capability<ITardisChunkLoader> capability, ITardisChunkLoader instance, Direction side) {
            return instance.serializeNBT();
        }

        public void readNBT(Capability<ITardisChunkLoader> capability, ITardisChunkLoader instance, Direction side, Tag nbt) {
            instance.deserializeNBT((CompoundTag) nbt);
        }
    }
}

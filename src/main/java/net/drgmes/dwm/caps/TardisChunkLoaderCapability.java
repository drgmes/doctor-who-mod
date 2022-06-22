package net.drgmes.dwm.caps;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;

import java.util.*;
import java.util.stream.Collectors;

public class TardisChunkLoaderCapability implements ITardisChunkLoader {
    private final Map<SectionPos, List<BlockPos>> chunks = new HashMap<>();

    private final ServerLevel level;

    public TardisChunkLoaderCapability(ServerLevel level) {
        this.level = level;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        for (Map.Entry<SectionPos, List<BlockPos>> entry : this.chunks.entrySet()) {
            CompoundTag chunkTag = new CompoundTag();
            chunkTag.putLong("chunk", entry.getKey().chunk().toLong());

            LongArrayTag blocks = new LongArrayTag(entry.getValue().stream().map(BlockPos::asLong).collect(Collectors.toList()));
            chunkTag.put("blocks", blocks);

            tag.put(entry.getKey().getX() + ";" + entry.getKey().getZ(), chunkTag);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        for (String key : tag.getAllKeys()) {
            CompoundTag chunkTag = tag.getCompound(key);
            SectionPos chunk = SectionPos.of(new ChunkPos(chunkTag.getLong("chunk")), 0);

            LongArrayTag blocks = (LongArrayTag) chunkTag.get("blocks");
            Arrays.stream(blocks.getAsLongArray()).mapToObj(BlockPos::of).forEach(pos -> this.add(chunk, pos));
        }
    }

    @Override
    public ServerLevel getLevel() {
        return this.level;
    }

    @Override
    public void add(SectionPos sectionPos, BlockPos blockPos) {
        if (this.chunks.containsKey(sectionPos) && this.chunks.get(sectionPos).contains(blockPos)) {
            return;
        }

        if (!this.chunks.containsKey(sectionPos)) {
            this.chunks.put(sectionPos, new LinkedList<>());
            this.level.setChunkForced(sectionPos.getX(), sectionPos.getZ(), true);
        }

        this.chunks.get(sectionPos).add(blockPos);
    }

    @Override
    public void remove(SectionPos sectionPos, BlockPos blockPos) {
        if (!this.chunks.containsKey(sectionPos) || !this.chunks.get(sectionPos).contains(blockPos)) {
            return;
        }

        if (this.chunks.get(sectionPos).size() == 1) {
            this.level.setChunkForced(sectionPos.getX(), sectionPos.getZ(), false);
            this.chunks.remove(sectionPos);
        } else {
            this.chunks.get(sectionPos).remove(blockPos);
        }
    }

    @Override
    public void tick() {
        ServerChunkCache chunkProvider = this.level.getChunkSource();
        int tickSpeed = this.level.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
        if (tickSpeed <= 0) return;

        for (SectionPos pos : this.chunks.keySet()) {
            if (chunkProvider.chunkMap.getPlayers(pos.chunk(), false).size() == 0) {
                this.level.tickChunk(this.level.getChunk(pos.getX(), pos.getZ()), tickSpeed);
            }
        }
    }
}

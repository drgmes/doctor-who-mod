package net.drgmes.dwm.caps;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;

public class TardisChunkLoaderCapability implements ITardisChunkLoader {
    private Map<ChunkPos, List<BlockPos>> chunks = new HashMap<>();

    private ServerLevel level;

    public TardisChunkLoaderCapability(ServerLevel level) {
        this.level = level;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        for (Map.Entry<ChunkPos, List<BlockPos>> entry : this.chunks.entrySet()) {
            CompoundTag chunkTag = new CompoundTag();
            chunkTag.putLong("chunk", entry.getKey().toLong());

            LongArrayTag blocks = new LongArrayTag(entry.getValue().stream().map(BlockPos::asLong).collect(Collectors.toList()));
            chunkTag.put("blocks", blocks);

            tag.put(entry.getKey().x + ";" + entry.getKey().z, chunkTag);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        for (String key : tag.getAllKeys()) {
            CompoundTag chunkTag = tag.getCompound(key);
            ChunkPos chunk = new ChunkPos(chunkTag.getLong("chunk"));

            LongArrayTag blocks = (LongArrayTag) chunkTag.get("blocks");
            Arrays.stream(blocks.getAsLongArray()).mapToObj(BlockPos::of).forEach(pos -> this.add(chunk, pos));
        }
    }

    @Override
    public ServerLevel getLevel() {
        return this.level;
    }

    @Override
    public void add(ChunkPos chunkPos, BlockPos blockPos) {
        if (this.chunks.containsKey(chunkPos) && this.chunks.get(chunkPos).contains(blockPos)) {
            return;
        }

        if (!this.chunks.containsKey(chunkPos)) {
            this.chunks.put(chunkPos, new LinkedList<>());
            this.level.setChunkForced(chunkPos.x, chunkPos.z, true);
        }

        this.chunks.get(chunkPos).add(blockPos);
    }

    @Override
    public void remove(ChunkPos chunkPos, BlockPos blockPos) {
        if (!this.chunks.containsKey(chunkPos) || !this.chunks.get(chunkPos).contains(blockPos)) {
            return;
        }

        if (this.chunks.get(chunkPos).size() == 1) {
            this.level.setChunkForced(chunkPos.x, chunkPos.z, false);
            this.chunks.remove(chunkPos);
        }
        else {
            this.chunks.get(chunkPos).remove(blockPos);
        }
    }

    @Override
    public void tick() {
        ServerChunkCache chunkProvider = this.level.getChunkSource();
        int tickSpeed = this.level.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
        if (tickSpeed <= 0) return;

        for (ChunkPos pos : this.chunks.keySet()) {
            if (chunkProvider.chunkMap.getPlayers(pos, false).size() == 0) {
                this.level.tickChunk(this.level.getChunk(pos.x, pos.z), tickSpeed);
            }
        }
    }
}

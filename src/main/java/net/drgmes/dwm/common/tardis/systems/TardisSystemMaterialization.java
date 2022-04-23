package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlock;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlockEntity;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.setup.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class TardisSystemMaterialization implements ITardisSystem {
    public boolean isMaterialized = true;
    private final ITardisLevelData tardisData;

    public TardisSystemMaterialization(ITardisLevelData tardisData) {
        this.tardisData = tardisData;
    }

    @Override
    public void load(CompoundTag tag) {
        this.isMaterialized = tag.getBoolean("isMaterialized");
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isMaterialized", this.isMaterialized);
        return tag;
    }

    @Override
    public void tick() {
    }

    public boolean demat() {
        if (!this.isMaterialized) return true;

        ServerLevel level = this.tardisData.getLevel();
        if (level != null) {
            ServerLevel exteriorLevel = level.getServer().getLevel(this.tardisData.getCurrentExteriorDimension());
            BlockPos exteriorBlockPos = this.tardisData.getCurrentExteriorPosition();
            BlockState exteriorBlockState = exteriorLevel.getBlockState(exteriorBlockPos);

            if (exteriorBlockState.getBlock() instanceof TardisExteriorBlock) {
                exteriorLevel.setBlock(exteriorBlockPos, Blocks.AIR.defaultBlockState(), 3);
                exteriorLevel.setBlock(exteriorBlockPos.above(), Blocks.AIR.defaultBlockState(), 3);

                this.tardisData.setDoorsState(false, true);
                this.isMaterialized = false;
                this.tardisData.updateConsoleTiles();
                return true;
            }
        }

        return false;
    }

    public boolean remat() {
        if (this.isMaterialized) return true;

        ServerLevel level = this.tardisData.getLevel();
        if (level != null) {
            ServerLevel exteriorLevel = level.getServer().getLevel(this.tardisData.getCurrentExteriorDimension());
            BlockPos exteriorBlockPos = this.tardisData.getCurrentExteriorPosition();
            BlockState exteriorBlockState = exteriorLevel.getBlockState(exteriorBlockPos);

            if (exteriorBlockState.getBlock() instanceof AirBlock) {
                BlockState tardisExteriorBlockState = ModBlocks.TARDIS_EXTERIOR.get().defaultBlockState();
                tardisExteriorBlockState = tardisExteriorBlockState.setValue(TardisExteriorBlock.FACING, this.tardisData.getCurrentExteriorFacing());

                exteriorLevel.setBlock(exteriorBlockPos, tardisExteriorBlockState, 3);
                exteriorLevel.setBlock(exteriorBlockPos.above(), tardisExteriorBlockState.setValue(TardisExteriorBlock.HALF, DoubleBlockHalf.UPPER), 3);

                if (exteriorLevel.getBlockEntity(exteriorBlockPos) instanceof TardisExteriorBlockEntity tardisExteriorBlockEntity) {
                    tardisExteriorBlockEntity.tardisLevelUUID = level.dimension().location().getPath();

                    this.isMaterialized = true;
                    this.tardisData.updateConsoleTiles();
                    return true;
                }

                this.demat();
            }
        }

        return false;
    }
}

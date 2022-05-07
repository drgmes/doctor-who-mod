package net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota;

import net.drgmes.dwm.blocks.tardis.engines.BaseTardisEngineBlockEntity;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class TardisEngineToyotaBlockEntity extends BaseTardisEngineBlockEntity {
    public boolean isOpenedCover1 = false;
    public boolean isOpenedCover2 = false;
    public boolean isOpenedCover3 = false;
    public boolean isOpenedCover4 = false;
    public boolean isOpenedCover5 = false;
    public boolean isOpenedCover6 = false;
    public boolean isOpenedCover7 = false;
    public boolean isOpenedCover8 = false;

    public TardisEngineToyotaBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_ENGINE_TOYOTA.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putBoolean("isOpenedCover1", this.isOpenedCover1);
        tag.putBoolean("isOpenedCover2", this.isOpenedCover2);
        tag.putBoolean("isOpenedCover3", this.isOpenedCover3);
        tag.putBoolean("isOpenedCover4", this.isOpenedCover4);
        tag.putBoolean("isOpenedCover5", this.isOpenedCover5);
        tag.putBoolean("isOpenedCover6", this.isOpenedCover6);
        tag.putBoolean("isOpenedCover7", this.isOpenedCover7);
        tag.putBoolean("isOpenedCover8", this.isOpenedCover8);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        
        this.isOpenedCover1 = tag.getBoolean("isOpenedCover1");
        this.isOpenedCover2 = tag.getBoolean("isOpenedCover2");
        this.isOpenedCover3 = tag.getBoolean("isOpenedCover3");
        this.isOpenedCover4 = tag.getBoolean("isOpenedCover4");
        this.isOpenedCover5 = tag.getBoolean("isOpenedCover5");
        this.isOpenedCover6 = tag.getBoolean("isOpenedCover6");
        this.isOpenedCover7 = tag.getBoolean("isOpenedCover7");
        this.isOpenedCover8 = tag.getBoolean("isOpenedCover8");
    }
}

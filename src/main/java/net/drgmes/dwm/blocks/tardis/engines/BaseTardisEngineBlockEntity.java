package net.drgmes.dwm.blocks.tardis.engines;

import net.drgmes.dwm.blocks.tardis.engines.containers.TardisEngineSystemsContainer;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.setup.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Objects;
import java.util.Optional;

public class BaseTardisEngineBlockEntity extends RandomizableContainerBlockEntity {
    public BaseTardisEngineBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.worldPosition).inflate(3, 4, 3);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public int getContainerSize() {
        return ITardisLevelData.SYSTEM_COMPONENTS_CONTAINER_SIZE;
    }

    @Override
    protected Component getDefaultName() {
        return this.getBlockState().getBlock().getName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new TardisEngineSystemsContainer(containerId, inventory, this);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        Optional<ITardisLevelData> tardis = Objects.requireNonNull(this.getLevel()).getCapability(ModCapabilities.TARDIS_DATA).resolve();
        return tardis.map(ITardisLevelData::getSystemComponents).orElse(NonNullList.create());
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemStacks) {
        Objects.requireNonNull(this.getLevel()).getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
            tardis.setSystemComponents(itemStacks);
        });
    }
}

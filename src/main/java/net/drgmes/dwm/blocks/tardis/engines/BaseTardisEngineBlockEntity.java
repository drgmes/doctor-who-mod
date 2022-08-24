package net.drgmes.dwm.blocks.tardis.engines;

import net.drgmes.dwm.blocks.tardis.engines.screens.handlers.TardisEngineSystemsScreenHandler;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.utils.base.inventory.IInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public abstract class BaseTardisEngineBlockEntity extends BlockEntity implements IInventory, NamedScreenHandlerFactory {
    public BaseTardisEngineBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public Text getDisplayName() {
        return this.getCachedState().getBlock().getName();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        if (!(this.getWorld() instanceof ServerWorld serverWorld)) return null;
        Optional<TardisStateManager> tardisHolder = TardisStateManager.get(serverWorld);
        return tardisHolder.isEmpty() ? null : tardisHolder.get().getSystemComponents();
    }

    @Override
    public ScreenHandler createMenu(int inventoryId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TardisEngineSystemsScreenHandler(inventoryId, playerInventory, this);
    }
}

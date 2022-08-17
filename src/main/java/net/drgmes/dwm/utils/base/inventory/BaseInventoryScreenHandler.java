package net.drgmes.dwm.utils.base.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public abstract class BaseInventoryScreenHandler extends ScreenHandler {
    protected final Inventory inventory;

    public BaseInventoryScreenHandler(ScreenHandlerType<?> menuType, int containerId, PlayerInventory playerInventory, Inventory inventory) {
        super(menuType, containerId);

        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack movableItemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasStack()) {
            ItemStack itemStack = slot.getStack();
            movableItemStack = itemStack.copy();

            if (index < this.inventory.size()) {
                if (!this.insertItem(itemStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.insertItem(itemStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
                slot.onTakeItem(player, movableItemStack);
            }
            else {
                slot.markDirty();
            }
        }

        return movableItemStack;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.inventory.onClose(player);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}

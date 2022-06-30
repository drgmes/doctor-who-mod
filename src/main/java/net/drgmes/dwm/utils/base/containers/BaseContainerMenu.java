package net.drgmes.dwm.utils.base.containers;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class BaseContainerMenu extends AbstractContainerMenu {
    public Container container;

    public BaseContainerMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, Container container) {
        super(menuType, containerId);

        this.container = container;
        container.startOpen(playerInventory.player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack movableItemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot.hasItem()) {
            ItemStack itemStack = slot.getItem();
            movableItemStack = itemStack.copy();

            if (slotIndex < this.container.getContainerSize()) {
                if (!this.moveItemStackTo(itemStack, this.container.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(itemStack, 0, this.container.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
                slot.onTake(player, movableItemStack);
            }
            else {
                slot.setChanged();
            }
        }

        return movableItemStack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }
}

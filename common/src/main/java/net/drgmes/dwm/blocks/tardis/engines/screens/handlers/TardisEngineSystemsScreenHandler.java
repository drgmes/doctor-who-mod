package net.drgmes.dwm.blocks.tardis.engines.screens.handlers;

import net.drgmes.dwm.blocks.tardis.engines.screens.handlers.slots.TardisEngineSystemSlot;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.setup.ModInventories;
import net.drgmes.dwm.utils.base.inventory.BaseInventoryScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.slot.Slot;

public class TardisEngineSystemsScreenHandler extends BaseInventoryScreenHandler {
    public TardisEngineSystemsScreenHandler(int inventoryId, PlayerInventory playerInventory) {
        this(inventoryId, playerInventory, new SimpleInventory(TardisStateManager.SYSTEM_COMPONENTS_CONTAINER_SIZE));
    }

    public TardisEngineSystemsScreenHandler(int inventoryId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModInventories.TARDIS_ENGINE.get(), inventoryId, playerInventory, inventory);

        // Systems inventory
        {
            final int startX = 17;
            final int startY = 28;
            for (int invRow = 0; invRow < 2; invRow++) {
                for (int invCol = 0; invCol < 7; invCol++) {
                    this.addSlot(new TardisEngineSystemSlot(inventory, invCol + invRow * 7, startX + invCol * 21, startY + invRow * 21));
                }
            }
        }

        // Player inventory
        {
            final int startX = 8;
            final int startY = 166;
            for (int playerInvRow = 0; playerInvRow < 3; playerInvRow++) {
                for (int playerInvCol = 0; playerInvCol < 9; playerInvCol++) {
                    this.addSlot(new Slot(playerInventory, playerInvCol + playerInvRow * 9 + 9, startX + playerInvCol * 18, startY - (4 - playerInvRow) * 18 - 10));
                }
            }
        }

        // Player hotbar
        {
            final int startX = 8;
            for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
                this.addSlot(new Slot(playerInventory, hotbarSlot, startX + hotbarSlot * 18, 142));
            }
        }
    }
}

package net.drgmes.dwm.blocks.tardis.engines.containers;

import net.drgmes.dwm.blocks.tardis.engines.containers.slots.TardisEngineSystemSlot;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.setup.ModContainers;
import net.drgmes.dwm.utils.base.containers.BaseContainerMenu;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class TardisEngineSystemsContainer extends BaseContainerMenu {
    public TardisEngineSystemsContainer(int i, Inventory playerInventory) {
        this(i, playerInventory, new SimpleContainer(ITardisLevelData.SYSTEM_COMPONENTS_CONTAINER_SIZE));
    }

    public TardisEngineSystemsContainer(int containerId, Inventory playerInventory, Container container) {
        super(ModContainers.TARDIS_ENGINE.get(), containerId, playerInventory, container);

        // Systems inventory
        {
            final int startX = 17;
            final int startY = 28;
            for (int invRow = 0; invRow < 2; invRow++) {
                for (int invCol = 0; invCol < 7; invCol++) {
                    this.addSlot(new TardisEngineSystemSlot(container, invCol + invRow * 7, startX + invCol * 21, startY + invRow * 21));
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

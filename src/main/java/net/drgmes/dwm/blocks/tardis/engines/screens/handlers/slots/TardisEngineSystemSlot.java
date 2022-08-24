package net.drgmes.dwm.blocks.tardis.engines.screens.handlers.slots;

import net.drgmes.dwm.blocks.tardis.engines.BaseTardisEngineBlockEntity;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.items.tardissystem.TardisSystemItem;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;

import java.util.Optional;

public class TardisEngineSystemSlot extends Slot {
    public TardisEngineSystemSlot(Inventory inventory, int slot, int x, int y) {
        super(inventory, slot, x, y);
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack itemStack) {
        super.onTakeItem(player, itemStack);

        if (itemStack.getItem() instanceof TardisSystemItem && this.inventory instanceof BaseTardisEngineBlockEntity tardisEngineBlockEntity && tardisEngineBlockEntity.getWorld() instanceof ServerWorld serverWorld) {
            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                ModSounds.playTardisComponentRemovedSound(tardis.getWorld(), tardis.getMainConsolePosition());
                tardis.updateConsoleTiles();
            });
        }
    }

    @Override
    public boolean canInsert(ItemStack itemStack) {
        if (this.inventory.count(itemStack.getItem()) > 0) return false;
        return itemStack.getItem() instanceof TardisSystemItem;
    }

    @Override
    public void setStack(ItemStack itemStack) {
        super.setStack(itemStack);

        if (itemStack.getItem() instanceof TardisSystemItem && this.inventory instanceof BaseTardisEngineBlockEntity tardisEngineBlockEntity && tardisEngineBlockEntity.getWorld() instanceof ServerWorld serverWorld) {
            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                ModSounds.playTardisComponentAddedSound(tardis.getWorld(), tardis.getMainConsolePosition());
                tardis.updateConsoleTiles();
            });
        }
    }

    @Override
    public boolean canTakePartial(PlayerEntity player) {
        ItemStack itemStack = this.getStack();
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof TardisSystemItem tardisSystemItem) {
            if (this.inventory instanceof BaseTardisEngineBlockEntity tardisEngineBlockEntity && tardisEngineBlockEntity.getWorld() instanceof ServerWorld serverWorld) {
                Optional<TardisStateManager> tardisHolder = TardisStateManager.get(serverWorld);
                if (tardisHolder.isPresent()) return !tardisHolder.get().getSystem(tardisSystemItem.getSystemType()).inProgress();
            }
        }

        return true;
    }
}

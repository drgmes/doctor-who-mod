package net.drgmes.dwm.blocks.tardis.engines.containers.slots;

import net.drgmes.dwm.blocks.tardis.engines.BaseTardisEngineBlockEntity;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.items.tardis.tardissystem.TardisSystemItem;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.Optional;

public class TardisEngineSystemSlot extends Slot {
    public TardisEngineSystemSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        if (this.container.countItem(itemStack.getItem()) > 0) return false;
        return itemStack.getItem() instanceof TardisSystemItem;
    }

    @Override
    public boolean mayPickup(Player player) {
        ItemStack itemStack = this.getItem();
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof TardisSystemItem tardisSystemItem) {
            if (this.container instanceof BaseTardisEngineBlockEntity tardisEngineBlockEntity) {
                Optional<ITardisLevelData> tardis = Objects.requireNonNull(tardisEngineBlockEntity.getLevel()).getCapability(ModCapabilities.TARDIS_DATA).resolve();
                if (tardis.isPresent()) return !tardis.get().getSystem(tardisSystemItem.getSystemType()).inProgress();
            }
        }

        return true;
    }

    @Override
    public void onTake(Player player, ItemStack itemStack) {
        super.onTake(player, itemStack);

        if (itemStack.getItem() instanceof TardisSystemItem && this.container instanceof BaseTardisEngineBlockEntity tardisEngineBlockEntity) {
            Objects.requireNonNull(tardisEngineBlockEntity.getLevel()).getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
                ModSounds.playTardisComponentRemovedSound(tardis.getLevel(), tardis.getCorePosition());
                tardis.updateConsoleTiles();
            });
        }
    }

    @Override
    public void set(ItemStack itemStack) {
        super.set(itemStack);

        if (itemStack.getItem() instanceof TardisSystemItem && this.container instanceof BaseTardisEngineBlockEntity tardisEngineBlockEntity) {
            Objects.requireNonNull(tardisEngineBlockEntity.getLevel()).getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
                ModSounds.playTardisComponentAddedSound(tardis.getLevel(), tardis.getCorePosition());
                tardis.updateConsoleTiles();
            });
        }
    }
}

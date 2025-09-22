package net.drgmes.dwm.items.tardis.systems;

import net.drgmes.dwm.common.tardis.systems.TardisBaseSystem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class TardisSystemItem extends Item {
    private final Class<? extends TardisBaseSystem> systemType;

    public TardisSystemItem(Settings props, Class<? extends TardisBaseSystem> systemType) {
        super(props);
        this.systemType = systemType;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, Item.TooltipContext context, List<Text> tooltips, TooltipType type) {
        tooltips.add(Text.translatable(this.getTranslationKey() + ".desc").formatted(Formatting.GRAY));
        super.appendTooltip(itemStack, context, tooltips, type);
    }

    public Class<? extends TardisBaseSystem> getSystemType() {
        return this.systemType;
    }
}

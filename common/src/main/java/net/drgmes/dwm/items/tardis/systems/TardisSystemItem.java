package net.drgmes.dwm.items.tardis.systems;

import net.drgmes.dwm.common.tardis.systems.ITardisSystem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class TardisSystemItem extends Item {
    private final Class<? extends ITardisSystem> systemType;

    public TardisSystemItem(Settings props, Class<? extends ITardisSystem> systemType) {
        super(props);
        this.systemType = systemType;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltips, TooltipContext context) {
        tooltips.add(Text.translatable(this.getTranslationKey() + ".desc").formatted(Formatting.GRAY));
        super.appendTooltip(itemStack, world, tooltips, context);
    }

    public Class<? extends ITardisSystem> getSystemType() {
        return this.systemType;
    }
}

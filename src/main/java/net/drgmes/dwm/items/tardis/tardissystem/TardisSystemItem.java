package net.drgmes.dwm.items.tardis.tardissystem;

import net.drgmes.dwm.common.tardis.systems.ITardisSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class TardisSystemItem extends Item {
    private final Class<? extends ITardisSystem> systemType;

    public TardisSystemItem(Properties props, Class<? extends ITardisSystem> systemType) {
        super(props);
        this.systemType = systemType;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(itemStack, level, list, tooltipFlag);
    }

    public Class<? extends ITardisSystem> getSystemType() {
        return this.systemType;
    }
}

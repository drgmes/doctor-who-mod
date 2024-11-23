package net.drgmes.dwm.items.sonicdevices;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class SonicScrewdriverItem extends Item implements ISonicDeviceItem {
    public SonicScrewdriverItem(Settings props) {
        super(props);
    }

    @Override
    public boolean canMine(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player) {
        return false;
    }

    @Override
    public float getMiningSpeed(ItemStack itemStack, BlockState blockState) {
        return 0;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        return this.useSonicDevice(world, player, EquipmentSlot.MAINHAND, false);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int slotIdx, boolean flag) {
        this.listenForSettingsKey(itemStack, world, entity);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, Item.TooltipContext context, List<Text> tooltips, TooltipType type) {
        this.fillTooltip(itemStack, tooltips);
        super.appendTooltip(itemStack, context, tooltips, type);
    }
}

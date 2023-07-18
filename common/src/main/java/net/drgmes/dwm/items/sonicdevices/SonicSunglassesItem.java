package net.drgmes.dwm.items.sonicdevices;

import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.network.server.SonicDeviceUsePacket;
import net.drgmes.dwm.setup.ModKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class SonicSunglassesItem extends Item implements ISonicDeviceItem, Equipment {
    public SonicSunglassesItem(Settings props) {
        super(props);
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public boolean damage(DamageSource source) {
        return super.damage(source) && !source.isIn(DamageTypeTags.IS_EXPLOSION);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return this.equipAndSwap(this, world, user, hand);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int slotIdx, boolean flag) {
        this.listenForSettingsKey(itemStack, world, entity);

        if (!world.isClient) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen != null || !(entity instanceof PlayerEntity player)) return;

        if (ModKeys.SONIC_SUNGLASSES_USAGE.isPressed()) {
            if (!player.getEquippedStack(EquipmentSlot.HEAD).equals(itemStack)) return;
            if (SonicDevice.checkItemStackIsSonicDevice(player.getEquippedStack(EquipmentSlot.MAINHAND))) return;
            if (SonicDevice.checkItemStackIsSonicDevice(player.getEquippedStack(EquipmentSlot.OFFHAND))) return;

            ActionResult result = this.useSonicDevice(player.getWorld(), player, EquipmentSlot.HEAD, false).getResult();
            if (!result.shouldSwingHand()) return;

            new SonicDeviceUsePacket(itemStack, EquipmentSlot.HEAD.getName(), false).sendToServer();
        }
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltips, TooltipContext context) {
        this.fillTooltip(itemStack, tooltips);
        super.appendTooltip(itemStack, world, tooltips, context);
    }
}

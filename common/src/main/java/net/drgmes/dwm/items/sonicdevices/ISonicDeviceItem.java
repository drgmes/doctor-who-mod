package net.drgmes.dwm.items.sonicdevices;

import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.items.sonicdevices.screens.SonicDeviceInterfaceMainScreen;
import net.drgmes.dwm.network.server.SonicDeviceUpdatePacket;
import net.drgmes.dwm.setup.ModKeys;
import net.drgmes.dwm.setup.ModSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public interface ISonicDeviceItem {
    @Environment(EnvType.CLIENT)
    default void listenForSettingsKey(ItemStack itemStack, World world, Entity entity) {
        if (!world.isClient) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen != null || !(entity instanceof PlayerEntity player)) return;

        if (ModKeys.SONIC_DEVICE_SETTINGS.isPressed()) {
            EquipmentSlot slot;

            if (player.getEquippedStack(EquipmentSlot.MAINHAND).equals(itemStack)) slot = EquipmentSlot.MAINHAND;
            else if (player.getEquippedStack(EquipmentSlot.OFFHAND).equals(itemStack)) slot = EquipmentSlot.OFFHAND;
            else if (player.getEquippedStack(EquipmentSlot.HEAD).equals(itemStack)) slot = EquipmentSlot.HEAD;
            else return;

            if (itemStack.getItem() instanceof SonicSunglassesItem) {
                if (SonicDevice.checkItemStackIsSonicDevice(player.getEquippedStack(EquipmentSlot.MAINHAND))) return;
                if (SonicDevice.checkItemStackIsSonicDevice(player.getEquippedStack(EquipmentSlot.OFFHAND))) return;
            }

            if (player.isSneaking()) {
                if (player.getItemCooldownManager().isCoolingDown(itemStack.getItem())) return;

                NbtCompound tag = SonicDevice.getData(itemStack);
                List<SonicDevice.EMode> modes = List.of(SonicDevice.EMode.values());
                SonicDevice.EMode mode = modes.get((SonicDevice.getInteractionMode(itemStack).ordinal() + 1) % modes.size());
                if (tag.contains("prevMode")) mode = SonicDevice.EMode.valueOf(tag.getString("prevMode"));

                SonicDevice.setInteractionMode(itemStack, mode);
                new SonicDeviceUpdatePacket(itemStack, slot.getName()).sendToServer();
                return;
            }

            mc.setScreen(new SonicDeviceInterfaceMainScreen(itemStack, slot.getName()));
        }
    }

    default void fillTooltip(ItemStack itemStack, List<Text> tooltips) {
        tooltips.add(Text.empty());

        MutableText mode = SonicDevice.getInteractionMode(itemStack).getTitle().copy();
        MutableText modeText = Text.translatable("title.dwm.sonic_device.mode", mode.formatted(Formatting.GOLD));
        tooltips.add(modeText.formatted(Formatting.GRAY));

        String tardisId = SonicDevice.getTardisId(itemStack);
        if (!tardisId.isEmpty()) {
            MutableText tardis = Text.literal(tardisId.substring(0, 8));
            MutableText tardisText = Text.translatable("title.dwm.tardis_id", tardis.formatted(Formatting.GOLD));
            tooltips.add(tardisText.formatted(Formatting.GRAY));
        }
    }

    default TypedActionResult<ItemStack> useSonicDevice(World world, PlayerEntity player, EquipmentSlot slot, boolean isAlternativeAction) {
        ActionResult result = SonicDevice.interact(world, player, slot, isAlternativeAction);

        if (result.shouldSwingHand()) {
            world.emitGameEvent(player, GameEvent.ITEM_INTERACT_FINISH, player.getBlockPos());

            if (!world.isClient) {
                if (slot == EquipmentSlot.HEAD) ModSounds.playSonicSunglassesMainSound(world, player.getBlockPos());
                else ModSounds.playSonicScrewdriverMainSound(world, player.getBlockPos());
            }
        }

        return new TypedActionResult<>(result, player.getEquippedStack(slot));
    }
}

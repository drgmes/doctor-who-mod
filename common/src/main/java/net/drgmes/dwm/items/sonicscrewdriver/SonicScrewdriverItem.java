package net.drgmes.dwm.items.sonicscrewdriver;

import net.drgmes.dwm.common.sonicscrewdriver.SonicScrewdriver;
import net.drgmes.dwm.items.sonicscrewdriver.screens.SonicScrewdriverInterfaceMainScreen;
import net.drgmes.dwm.network.server.SonicScrewdriverUpdatePacket;
import net.drgmes.dwm.setup.ModKeys;
import net.drgmes.dwm.setup.ModSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public class SonicScrewdriverItem extends Item {
    public SonicScrewdriverItem(Settings props) {
        super(props);
    }

    @Override
    public boolean damage(DamageSource source) {
        return super.damage(source) && !source.isIn(DamageTypeTags.IS_EXPLOSION);
    }

    @Override
    public boolean canMine(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player) {
        return false;
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack itemStack, BlockState blockState) {
        return 0;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        return this.useSonicScrewdriver(world, player, hand, false);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int slotIdx, boolean flag) {
        if (!world.isClient) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen != null || !(entity instanceof PlayerEntity player)) return;

        if (ModKeys.SONIC_SCREWDRIVER_SETTINGS.isPressed()) {
            ItemStack mainItemStack = player.getStackInHand(Hand.MAIN_HAND);
            ItemStack offItemStack = player.getStackInHand(Hand.OFF_HAND);

            if (!mainItemStack.equals(itemStack) && !offItemStack.equals(itemStack)) {
                return;
            }

            if (player.isSneaking()) {
                if (player.getItemCooldownManager().isCoolingDown(itemStack.getItem())) return;

                NbtCompound tag = SonicScrewdriver.getData(itemStack);
                List<SonicScrewdriver.EMode> modes = List.of(SonicScrewdriver.EMode.values());
                SonicScrewdriver.EMode mode = modes.get((SonicScrewdriver.getInteractionMode(itemStack).ordinal() + 1) % modes.size());
                if (tag.contains("prevMode")) mode = SonicScrewdriver.EMode.valueOf(tag.getString("prevMode"));

                SonicScrewdriver.setInteractionMode(itemStack, mode);
                new SonicScrewdriverUpdatePacket(itemStack, mainItemStack.equals(itemStack)).sendToServer();
                return;
            }

            mc.setScreen(new SonicScrewdriverInterfaceMainScreen(itemStack, mainItemStack.equals(itemStack)));
        }
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltips, TooltipContext context) {
        tooltips.add(Text.empty());

        MutableText mode = SonicScrewdriver.getInteractionMode(itemStack).getTitle().copy();
        MutableText modeText = Text.translatable("title.dwm.sonic_screwdriver.mode", mode.formatted(Formatting.GOLD));
        tooltips.add(modeText.formatted(Formatting.GRAY));

        String tardisId = SonicScrewdriver.getTardisId(itemStack);
        if (!tardisId.isEmpty()) {
            MutableText tardis = Text.literal(tardisId.substring(0, 8));
            MutableText tardisText = Text.translatable("title.dwm.tardis_id", tardis.formatted(Formatting.GOLD));
            tooltips.add(tardisText.formatted(Formatting.GRAY));
        }

        super.appendTooltip(itemStack, world, tooltips, context);
    }

    public TypedActionResult<ItemStack> useSonicScrewdriver(World world, PlayerEntity player, Hand hand, boolean isAlternativeAction) {
        ItemStack itemStack = player.getStackInHand(hand);
        ActionResult result = SonicScrewdriver.interact(world, player, hand, isAlternativeAction);

        if (result.shouldSwingHand()) {
            world.emitGameEvent(player, GameEvent.ITEM_INTERACT_FINISH, player.getBlockPos());
            if (!world.isClient) ModSounds.playSonicScrewdriverMainSound(world, player.getBlockPos());
        }

        return new TypedActionResult<>(result, itemStack);
    }
}

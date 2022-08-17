package net.drgmes.dwm.items.screwdriver;

import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.items.screwdriver.screens.ScrewdriverInterfaceMainScreen;
import net.drgmes.dwm.setup.ModKeys;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

public class ScrewdriverItem extends Item {
    public ScrewdriverItem(Item.Settings props) {
        super(props);
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
        return this.useScrewdriver(world, player, hand, false);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int slotIdx, boolean flag) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!entity.world.isClient || mc.currentScreen != null || !(entity instanceof PlayerEntity player)) return;

        if (ModKeys.SCREWDRIVER_SETTINGS.isPressed()) {
            if (player.getStackInHand(Hand.MAIN_HAND).equals(itemStack)) {
                mc.setScreen(new ScrewdriverInterfaceMainScreen(itemStack, true));
            }
            else if (player.getStackInHand(Hand.OFF_HAND).equals(itemStack)) {
                mc.setScreen(new ScrewdriverInterfaceMainScreen(itemStack, false));
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.empty());

        MutableText mode = Screwdriver.getInteractionMode(itemStack).getTitle().copy();
        MutableText modeText = Text.translatable("title.dwm.screwdriver.mode", mode.formatted(Formatting.GOLD));
        tooltip.add(modeText.formatted(Formatting.GRAY));

        String tardisId = Screwdriver.getTardisId(itemStack);
        if (!tardisId.isEmpty()) {
            MutableText tardis = Text.literal(tardisId.substring(0, 8));
            MutableText tardisText = Text.translatable("title.dwm.tardis_id", tardis.formatted(Formatting.GOLD));
            tooltip.add(tardisText.formatted(Formatting.GRAY));
        }

        super.appendTooltip(itemStack, world, tooltip, context);
    }

    public TypedActionResult<ItemStack> useScrewdriver(World world, PlayerEntity player, Hand hand, boolean isAlternativeAction) {
        ItemStack itemStack = player.getStackInHand(hand);
        ActionResult result = Screwdriver.interact(world, player, hand, isAlternativeAction);

        if (result.shouldSwingHand()) {
            world.emitGameEvent(player, GameEvent.ITEM_INTERACT_FINISH, player.getBlockPos());
            if (!world.isClient) ModSounds.playScrewdriverMainSound(world, player.getBlockPos());
        }

        return new TypedActionResult<>(result, itemStack);
    }
}

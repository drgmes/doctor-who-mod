package net.drgmes.dwm.items.screwdriver;

import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.items.screwdriver.screens.ScrewdriverInterfaceMainScreen;
import net.drgmes.dwm.network.server.ScrewdriverUpdatePacket;
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

public class ScrewdriverItem extends Item {
    public ScrewdriverItem(Settings props) {
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
        return this.useScrewdriver(world, player, hand, false);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int slotIdx, boolean flag) {
        if (!world.isClient) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen != null || !(entity instanceof PlayerEntity player)) return;

        if (ModKeys.SCREWDRIVER_SETTINGS.isPressed()) {
            ItemStack mainItemStack = player.getStackInHand(Hand.MAIN_HAND);
            ItemStack offItemStack = player.getStackInHand(Hand.OFF_HAND);

            if (!mainItemStack.equals(itemStack) && !offItemStack.equals(itemStack)) {
                return;
            }

            if (player.isSneaking()) {
                if (player.getItemCooldownManager().isCoolingDown(itemStack.getItem())) return;

                NbtCompound screwdriverData = Screwdriver.getData(itemStack);
                List<Screwdriver.EScrewdriverMode> modes = List.of(Screwdriver.EScrewdriverMode.values());
                Screwdriver.EScrewdriverMode mode = modes.get((Screwdriver.getInteractionMode(itemStack).ordinal() + 1) % modes.size());
                if (screwdriverData.contains("prevMode")) mode = Screwdriver.EScrewdriverMode.valueOf(screwdriverData.getString("prevMode"));

                Screwdriver.setInteractionMode(itemStack, mode);
                new ScrewdriverUpdatePacket(itemStack, mainItemStack.equals(itemStack)).sendToServer();
                return;
            }

            mc.setScreen(new ScrewdriverInterfaceMainScreen(itemStack, mainItemStack.equals(itemStack)));
        }
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltips, TooltipContext context) {
        tooltips.add(Text.empty());

        MutableText mode = Screwdriver.getInteractionMode(itemStack).getTitle().copy();
        MutableText modeText = Text.translatable("title.dwm.screwdriver.mode", mode.formatted(Formatting.GOLD));
        tooltips.add(modeText.formatted(Formatting.GRAY));

        String tardisId = Screwdriver.getTardisId(itemStack);
        if (!tardisId.isEmpty()) {
            MutableText tardis = Text.literal(tardisId.substring(0, 8));
            MutableText tardisText = Text.translatable("title.dwm.tardis_id", tardis.formatted(Formatting.GOLD));
            tooltips.add(tardisText.formatted(Formatting.GRAY));
        }

        super.appendTooltip(itemStack, world, tooltips, context);
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

package net.drgmes.dwm.setup;

import com.google.common.collect.ImmutableSet;
import dev.architectury.registry.level.entity.trade.SimpleTrade;
import dev.architectury.registry.level.entity.trade.TradeRegistry;
import net.drgmes.dwm.utils.builders.VillagerProfessionBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class ModVillagerProfessions {
    public static final List<VillagerProfessionBuilder> VILLAGER_PROFESSION_BUILDERS = new ArrayList<>();

    public static final VillagerProfessionBuilder SHOBOGAN_ENGINEER = new VillagerProfessionBuilder(
        "shobogan_engineer",
        1,
        1,
        SoundEvents.ENTITY_VILLAGER_WORK_ARMORER,
        () -> ImmutableSet.of(ModBlocks.ENGINEER_TABLE.getBlock()),
        (profession) -> {
            TradeRegistry.registerVillagerTrade(
                profession, 1,
                new SimpleTrade(new ItemStack(Items.EMERALD, 64), ItemStack.EMPTY, new ItemStack(ModItems.TARDIS_KEY_1.getItem()), 1, 10, 1),
                new SimpleTrade(new ItemStack(ModItems.TITANIUM_INGOT.getItem()), ItemStack.EMPTY, new ItemStack(Items.EMERALD, 2), 16, 2, 1)
            );

            TradeRegistry.registerVillagerTrade(
                profession, 2,
                new SimpleTrade(new ItemStack(Items.EMERALD, 12), ItemStack.EMPTY, new ItemStack(ModItems.REPAIR_KIT.getItem()), 1, 2, 1),
                new SimpleTrade(new ItemStack(Items.AMETHYST_SHARD), ItemStack.EMPTY, new ItemStack(Items.EMERALD, 4), 8, 4, 1)
            );

            TradeRegistry.registerVillagerTrade(
                profession, 3,
                new SimpleTrade(new ItemStack(Items.EMERALD, 64), ItemStack.EMPTY, new ItemStack(ModItems.SCREWDRIVER_11.getItem()), 1, 60, 1),
                new SimpleTrade(new ItemStack(Items.EMERALD, 64), ItemStack.EMPTY, new ItemStack(ModItems.SCREWDRIVER_12.getItem()), 1, 60, 1),
                new SimpleTrade(new ItemStack(Items.EMERALD, 64), ItemStack.EMPTY, new ItemStack(ModItems.SCREWDRIVER_13.getItem()), 1, 60, 1)
            );

            TradeRegistry.registerVillagerTrade(
                profession, 4,
                new SimpleTrade(new ItemStack(Items.EMERALD, 6), new ItemStack(Blocks.REDSTONE_BLOCK, 1), new ItemStack(ModBlocks.TARDIS_ROUNDEL.getBlockItem()), 16, 10, 1),
                new SimpleTrade(new ItemStack(Items.ECHO_SHARD), ItemStack.EMPTY, new ItemStack(Items.EMERALD, 12), 4, 10, 1)
            );
        }
    );

    public static void init() {
    }
}

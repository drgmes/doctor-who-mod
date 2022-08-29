package net.drgmes.dwm.setup;

import com.google.common.collect.ImmutableMap;
import net.drgmes.dwm.utils.builders.VillagerProfessionBuilder;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.village.TradeOffers;

import java.util.ArrayList;

public class ModVillagerProfessions {
    public static final ArrayList<VillagerProfessionBuilder> VILLAGER_PROFESSION_BUILDERS = new ArrayList<>();

    public static final VillagerProfessionBuilder ENGINEER = new VillagerProfessionBuilder("dwm.engineer", 1, 1, SoundEvents.ENTITY_VILLAGER_WORK_ARMORER, ModBlocks.ENGINEER_TABLE.getBlock());

    public static void init() {
    }

    public static void setup() {
        ENGINEER.setTradeOffers(ImmutableMap.of(
            1,
            new TradeOffers.Factory[]{
                new TradeOffers.SellItemFactory(ModItems.REPAIR_KIT.getItem(), 12, 1, 1, 10),
            },
            2,
            new TradeOffers.Factory[]{
                new TradeOffers.BuyForOneEmeraldFactory(Items.AMETHYST_SHARD, 1, 16, 2),
                new TradeOffers.SellItemFactory(ModItems.SCREWDRIVER_11.getItem(), 48, 1, 1, 60),
                new TradeOffers.SellItemFactory(ModItems.SCREWDRIVER_12.getItem(), 48, 1, 1, 60),
                new TradeOffers.SellItemFactory(ModItems.SCREWDRIVER_13.getItem(), 48, 1, 1, 60),
            },
            3,
            new TradeOffers.Factory[]{
                new TradeOffers.BuyForOneEmeraldFactory(Items.ECHO_SHARD, 1, 8, 4),
                new TradeOffers.SellItemFactory(ModItems.TARDIS_KEY_1.getItem(), 64, 1, 1, 60),
            }
        ));
    }
}

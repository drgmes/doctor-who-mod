package net.drgmes.dwm.utils.helpers;

import net.drgmes.dwm.DWM;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

public class RecipeHelper {
    public static Identifier getRecipeName(String name, String category) {
        return DWM.getIdentifier(name + "_" + category);
    }

    public static Identifier getConversionRecipeName(String name, String fromName) {
        return getRecipeName(name, "from_" + fromName);
    }

    public static Identifier getSmeltingRecipeName(String name) {
        return getConversionRecipeName(name, "smelting");
    }

    public static Identifier getSmeltingRecipeName(String name, String fromName) {
        return getConversionRecipeName(name, "smelting_" + fromName);
    }

    public static Identifier getBlastingRecipeName(String name) {
        return getConversionRecipeName(name, "blasting");
    }

    public static Identifier getBlastingRecipeName(String name, String fromName) {
        return getConversionRecipeName(name, "blasting_" + fromName);
    }

    public static Identifier getSmokingRecipeName(String name) {
        return getConversionRecipeName(name, "blasting");
    }

    public static Identifier getSmokingRecipeName(String name, String fromName) {
        return getConversionRecipeName(name, "blasting_" + fromName);
    }

    public static AdvancementCriterion<InventoryChangedCriterion.Conditions> conditionsFromTag(TagKey<Item> tag) {
        return conditionsFromItemPredicates(ItemPredicate.Builder.create().tag(tag).build());
    }

    public static AdvancementCriterion<InventoryChangedCriterion.Conditions> conditionsFromItem(ItemConvertible item) {
        return conditionsFromItemPredicates(ItemPredicate.Builder.create().items(new ItemConvertible[]{item}).build());
    }

    public static AdvancementCriterion<InventoryChangedCriterion.Conditions> conditionsFromItemPredicates(ItemPredicate... predicates) {
        return Criteria.INVENTORY_CHANGED.create(
            new InventoryChangedCriterion.Conditions(
                Optional.empty(),
                new InventoryChangedCriterion.Conditions.Slots(
                    NumberRange.IntRange.atLeast(10),
                    NumberRange.IntRange.ANY,
                    NumberRange.IntRange.ANY
                ),
                List.of(predicates)
            )
        );
    }
}

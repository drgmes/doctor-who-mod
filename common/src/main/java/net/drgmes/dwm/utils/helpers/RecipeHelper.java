package net.drgmes.dwm.utils.helpers;

import net.drgmes.dwm.DWM;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

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

    public static InventoryChangedCriterion.Conditions conditionsFromTag(TagKey<Item> tag) {
        return conditionsFromItemPredicates(ItemPredicate.Builder.create().tag(tag).build());
    }

    public static InventoryChangedCriterion.Conditions conditionsFromItem(ItemConvertible item) {
        return conditionsFromItemPredicates(ItemPredicate.Builder.create().items(new ItemConvertible[]{item}).build());
    }

    public static InventoryChangedCriterion.Conditions conditionsFromItemPredicates(ItemPredicate... predicates) {
        return new InventoryChangedCriterion.Conditions(EntityPredicate.Extended.EMPTY, NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, predicates);
    }
}

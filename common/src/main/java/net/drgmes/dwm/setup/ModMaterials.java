package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.minecraft.block.Block;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModMaterials {
    public static class ArmorMaterials {
        public static final RegistryEntry<net.minecraft.item.ArmorMaterial> TITANIUM = Registration.registerArmorMaterial("titanium", () -> {
            return new ArmorMaterial(
                Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                    map.put(ArmorItem.Type.BOOTS, 2);
                    map.put(ArmorItem.Type.LEGGINGS, 5);
                    map.put(ArmorItem.Type.CHESTPLATE, 4);
                    map.put(ArmorItem.Type.HELMET, 1);
                    map.put(ArmorItem.Type.BODY, 10);
                }),
                9,
                SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
                () -> Ingredient.ofItems(ModItems.TITANIUM_INGOT.getItem()),
                List.of(new ArmorMaterial.Layer(DWM.getIdentifier("titanium"))),
                0.0f,
                0.1f
            );
        });

        public static void init() {
        }
    }

    public enum ToolMaterial implements net.minecraft.item.ToolMaterial {
        TITANIUM(
            650,
            10,
            7.5f,
            2.5f,
            ModItems.TITANIUM_INGOT::getItem,
            BlockTags.INCORRECT_FOR_IRON_TOOL
        );

        private final int durability;
        private final int enchantability;
        private final float miningSpeedMultiplier;
        private final float attackDamage;
        private final Supplier<ItemConvertible> repairIngredientSupplier;
        private final TagKey<Block> inverseTag;

        ToolMaterial(int durability, int enchantability, float miningSpeedMultiplier, float attackDamage, Supplier<ItemConvertible> repairIngredientSupplier, TagKey<Block> inverseTag) {
            this.durability = durability;
            this.enchantability = enchantability;
            this.miningSpeedMultiplier = miningSpeedMultiplier;
            this.attackDamage = attackDamage;
            this.repairIngredientSupplier = repairIngredientSupplier;
            this.inverseTag = inverseTag;
        }

        @Override
        public int getDurability() {
            return this.durability;
        }

        @Override
        public int getEnchantability() {
            return this.enchantability;
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return this.miningSpeedMultiplier;
        }

        @Override
        public float getAttackDamage() {
            return this.attackDamage;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(this.repairIngredientSupplier.get());
        }

        @Override
        public TagKey<Block> getInverseTag() {
            return this.inverseTag;
        }
    }
}

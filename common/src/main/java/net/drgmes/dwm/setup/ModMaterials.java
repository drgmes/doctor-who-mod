package net.drgmes.dwm.setup;

import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.function.Supplier;

public class ModMaterials {
    public enum TOOL implements ToolMaterial {
        TITANIUM(
            MiningLevels.IRON,
            650,
            14,
            6.5f,
            2.5f,
            ModItems.TITANIUM_INGOT::getItem
        );

        private final int miningLevel;
        private final int durability;
        private final int enchantability;
        private final float miningSpeedMultiplier;
        private final float attackDamage;
        private final Supplier<ItemConvertible> repairIngredientSupplier;

        TOOL(int miningLevel, int durability, int enchantability, float miningSpeedMultiplier, float attackDamage, Supplier<ItemConvertible> repairIngredientSupplier) {
            this.miningLevel = miningLevel;
            this.durability = durability;
            this.enchantability = enchantability;
            this.miningSpeedMultiplier = miningSpeedMultiplier;
            this.attackDamage = attackDamage;
            this.repairIngredientSupplier = repairIngredientSupplier;
        }

        @Override
        public int getMiningLevel() {
            return this.miningLevel;
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
    }

    public enum ARMOR implements ArmorMaterial {
        TITANIUM(
            "titanium",
            25,
            9,
            0.0f,
            0.1f,
            new int[]{2, 5, 4, 1},
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            ModItems.TITANIUM_INGOT::getItem
        );

        private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};

        private final String name;
        private final int durabilityMultiplier;
        private final int enchantability;
        private final float toughness;
        private final float knockbackResistance;
        private final int[] protectionAmounts;
        private final SoundEvent equipSound;
        private final Supplier<ItemConvertible> repairIngredientSupplier;

        ARMOR(String name, int durabilityMultiplier, int enchantability, float toughness, float knockbackResistance, int[] protectionAmounts, SoundEvent equipSound, Supplier<ItemConvertible> repairIngredientSupplier) {
            this.name = name;
            this.durabilityMultiplier = durabilityMultiplier;
            this.enchantability = enchantability;
            this.toughness = toughness;
            this.knockbackResistance = knockbackResistance;
            this.protectionAmounts = protectionAmounts;
            this.equipSound = equipSound;
            this.repairIngredientSupplier = repairIngredientSupplier;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getDurability(ArmorItem.Type type) {
            return BASE_DURABILITY[type.ordinal()] * this.durabilityMultiplier;
        }

        @Override
        public int getEnchantability() {
            return this.enchantability;
        }

        @Override
        public float getToughness() {
            return this.toughness;
        }

        @Override
        public float getKnockbackResistance() {
            return this.knockbackResistance;
        }

        @Override
        public int getProtection(ArmorItem.Type type) {
            return this.protectionAmounts[type.ordinal()];
        }

        @Override
        public SoundEvent getEquipSound() {
            return this.equipSound;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(this.repairIngredientSupplier.get());
        }
    }
}

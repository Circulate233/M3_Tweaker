package com.circulation.m3t.crt.recipes;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;

import net.minecraft.item.ItemStack;

import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import project.studio.manametalmod.core.RecipesOreTable;

final class RecipeInputs {

    private static final int CRAFTING_SLOTS = 9;

    private RecipeInputs() {}

    static ItemStack[] itemGrid(IItemStack[] ingredients, int slots, int length) {
        checkLength(ingredients, slots);
        ItemStack[] result = new ItemStack[length];
        for (int i = 0; i < slots; i++) {
            if (ingredients[i] != null) {
                result[i] = requireItemStack(ingredients[i]);
            }
        }
        return result;
    }

    static RecipesOreTable craftingTable(IItemStack output, IIngredient[] ingredients) {
        checkLength(ingredients, CRAFTING_SLOTS);
        Object[] slots = new Object[CRAFTING_SLOTS + 1];
        for (int i = 0; i < CRAFTING_SLOTS; i++) {
            IIngredient ingredient = ingredients[i];
            if (ingredient instanceof IOreDictEntry ore) {
                slots[i] = ore.getName();
            } else if (ingredient instanceof IItemStack stack) {
                slots[i] = requireItemStack(stack);
            } else if (ingredient != null) {
                throw new IllegalArgumentException("Expected an item or ore dictionary entry in recipe slot " + i);
            }
        }
        slots[CRAFTING_SLOTS] = requireItemStack(output);
        return new RecipesOreTable(slots);
    }

    static ItemStack craftingTableOutput(RecipesOreTable recipe) {
        return recipe.items[CRAFTING_SLOTS];
    }

    private static void checkLength(Object[] ingredients, int minimum) {
        if (ingredients.length < minimum) {
            throw new IllegalArgumentException(
                "Expected at least " + minimum + " recipe slots, got " + ingredients.length);
        }
    }
}

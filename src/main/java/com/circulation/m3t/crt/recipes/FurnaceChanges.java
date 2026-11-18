package com.circulation.m3t.crt.recipes;

import java.util.Map;

import net.minecraft.item.ItemStack;

import com.circulation.m3t.reload.ItemRemovals;
import com.github.bsideup.jabel.Desugar;

import it.unimi.dsi.fastutil.objects.Reference2FloatLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;

final class FurnaceChanges {

    private final Reference2ObjectLinkedOpenHashMap<ItemStack, ItemStack> addedRecipes = new Reference2ObjectLinkedOpenHashMap<>();
    private final Reference2FloatLinkedOpenHashMap<ItemStack> addedExperience = new Reference2FloatLinkedOpenHashMap<>();
    private final ItemRemovals removedOutputs = new ItemRemovals();
    private Tables defaults;

    void add(ItemStack input, ItemStack output, float experience) {
        checkStack(input);
        checkStack(output);
        addedRecipes.put(input, output);
        addedExperience.put(output, experience);
    }

    void add(ItemStack[] inputs, ItemStack output, float experience) {
        checkStack(output);
        for (ItemStack input : inputs) {
            checkStack(input);
        }
        for (ItemStack input : inputs) {
            addedRecipes.put(input, output);
        }
        addedExperience.put(output, experience);
    }

    void remove(ItemStack output) {
        removedOutputs.add(output);
    }

    void clearChanges() {
        addedRecipes.clear();
        addedExperience.clear();
        removedOutputs.clear();
    }

    Tables apply(Map<ItemStack, ItemStack> recipes, Map<ItemStack, Float> experience) {
        if (addedExperience.isEmpty() && removedOutputs.isEmpty()) {
            Tables original = defaults;
            defaults = null;
            return original;
        }
        if (defaults == null) {
            defaults = new Tables(recipes, experience);
        }

        // ManaMetal returns the first matching entry, including entries with wildcard metadata.
        var result = new Reference2ObjectLinkedOpenHashMap<ItemStack, ItemStack>(
            defaults.recipes.size() + addedRecipes.size());
        var xp = new Reference2FloatLinkedOpenHashMap<ItemStack>(defaults.experience.size() + addedExperience.size());
        if (removedOutputs.isEmpty()) {
            result.putAll(defaults.recipes);
            xp.putAll(defaults.experience);
        } else {
            for (var entry : defaults.recipes.entrySet()) {
                if (!removedOutputs.contains(entry.getValue())) {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
            for (var entry : defaults.experience.entrySet()) {
                if (!removedOutputs.contains(entry.getKey())) {
                    xp.put(
                        entry.getKey(),
                        entry.getValue()
                            .floatValue());
                }
            }
        }
        result.putAll(addedRecipes);
        xp.putAll(addedExperience);
        return new Tables(result, xp);
    }

    private static void checkStack(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            throw new IllegalArgumentException("Expected a valid furnace recipe item");
        }
    }

    @Desugar
    record Tables(Map<ItemStack, ItemStack> recipes, Map<ItemStack, Float> experience) {}
}

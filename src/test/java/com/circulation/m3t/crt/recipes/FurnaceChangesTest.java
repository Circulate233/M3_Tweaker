package com.circulation.m3t.crt.recipes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.junit.Test;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2FloatLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;

public class FurnaceChangesTest {

    @Test
    public void appliesChangesInRecipeAndExperienceOrderAndRestoresTheOriginalTables() {
        Item material = new Item();
        ItemStack wildcardInput = new ItemStack(material, 1, OreDictionary.WILDCARD_VALUE);
        ItemStack exactInput = new ItemStack(material, 1, 2);
        ItemStack removedInput = new ItemStack(new Item());
        ItemStack wildcardOutput = new ItemStack(new Item());
        ItemStack removedOutput = new ItemStack(new Item());
        ItemStack scriptOutput = new ItemStack(new Item());
        ItemStack secondInput = new ItemStack(new Item());

        var recipes = new Reference2ObjectLinkedOpenHashMap<ItemStack, ItemStack>();
        recipes.put(wildcardInput, wildcardOutput);
        recipes.put(removedInput, removedOutput);
        var experience = new Reference2FloatLinkedOpenHashMap<ItemStack>();
        experience.put(removedOutput, 0.25f);
        experience.put(wildcardOutput, 0.5f);

        float scriptExperience = 1.5f;
        FurnaceChanges changes = new FurnaceChanges();
        changes.remove(removedOutput.copy());
        changes.add(new ItemStack[] { exactInput, secondInput }, scriptOutput, scriptExperience);
        FurnaceChanges.Tables result = changes.apply(recipes, experience);

        assertEquals(
            ObjectArrayList.of(wildcardInput, exactInput, secondInput),
            new ObjectArrayList<>(
                result.recipes()
                    .keySet()));
        assertSame(
            scriptOutput,
            result.recipes()
                .get(exactInput));
        assertSame(
            scriptOutput,
            result.recipes()
                .get(secondInput));
        assertEquals(
            ObjectArrayList.of(wildcardOutput, scriptOutput),
            new ObjectArrayList<>(
                result.experience()
                    .keySet()));
        assertEquals(
            experience.getFloat(wildcardOutput),
            result.experience()
                .get(wildcardOutput),
            0.0f);
        assertEquals(
            scriptExperience,
            result.experience()
                .get(scriptOutput),
            0.0f);

        changes.clearChanges();
        FurnaceChanges.Tables restored = changes.apply(result.recipes(), result.experience());
        assertSame(recipes, restored.recipes());
        assertSame(experience, restored.experience());
    }
}

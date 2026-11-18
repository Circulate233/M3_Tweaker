package com.circulation.m3t.crt.recipes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.junit.Test;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import project.studio.manametalmod.items.craftingRecipes.CastingData;

public class CastingHandlerTest {

    @Test
    public void matchesTheOutputTagAndRefreshesTheIndexAfterReload() {
        Item material = new Item();
        ItemStack alloy = output(material, "alloy");
        ItemStack plated = output(material, "plated");
        Object[] first = { 0, alloy };
        Object[] second = { 0, plated };

        List<Object[]> original = CastingData.getRecipes;
        List<Object[]> recipes = new ObjectArrayList<>();
        recipes.add(first);
        recipes.add(second);
        CastingData.getRecipes = recipes;
        CastingHandler handler = new CastingHandler();
        try {
            handler.postReload();
            assertEquals(recipes.indexOf(first), CastingHandler.findRecipe(alloy.copy()));
            assertEquals(recipes.indexOf(second), CastingHandler.findRecipe(plated.copy()));
            assertTrue(CastingHandler.findRecipe(output(material, "different")) < 0);

            handler.reload();
            recipes.remove(first);
            handler.postReload();
            assertTrue(CastingHandler.findRecipe(alloy) < 0);
            assertEquals(recipes.indexOf(second), CastingHandler.findRecipe(plated));
        } finally {
            CastingData.getRecipes = original;
            handler.postReload();
        }
    }

    private static ItemStack output(Item item, String finish) {
        ItemStack stack = new ItemStack(item);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("finish", finish);
        stack.setTagCompound(tag);
        return stack;
    }
}

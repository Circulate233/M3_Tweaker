package com.circulation.m3t.crt.recipes;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;

import net.minecraft.item.ItemStack;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.mixins.MMM.AccessorEternalAnvilRecipe;
import com.circulation.m3t.reload.ItemListChanges;
import com.circulation.m3t.reload.M3TCrtReload;

import minetweaker.api.item.IItemStack;
import project.studio.manametalmod.originmagic.EternalAnvilRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "EternalAnvil")
public class EternalAnvilHandler implements M3TCrtReload {

    private static final int RECIPE_SLOTS = 9;
    private static final ItemListChanges<EternalAnvilRecipe> CHANGES = new ItemListChanges<>(
        EternalAnvilRecipe::getResult);

    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack[] ingredients) {
        ItemStack result = RecipeInputs.positiveStack(output);
        addRecipe(result, ingredients, result.getUnlocalizedName() + ".name");
    }

    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack[] ingredients, String nameKey) {
        if (nameKey == null || nameKey.trim()
            .isEmpty()) {
            throw new IllegalArgumentException("Eternal anvil recipe name must not be empty");
        }
        addRecipe(RecipeInputs.positiveStack(output), ingredients, nameKey);
    }

    private static void addRecipe(ItemStack output, IItemStack[] ingredients, String nameKey) {
        if (ingredients == null || ingredients.length != RECIPE_SLOTS) {
            throw new IllegalArgumentException(
                "Eternal anvil recipes require exactly " + RECIPE_SLOTS + " ingredients");
        }
        RecipeInputs.checkOutputSlot(output);
        CHANGES.add(new EternalAnvilRecipe(nameKey, RecipeInputs.positiveStacks(ingredients), output));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        CHANGES.remove(requireItemStack(output));
    }

    @Override
    public void reload() {
        CHANGES.clearChanges();
    }

    @Override
    public void postReload() {
        CHANGES.applyTo(AccessorEternalAnvilRecipe.m3t$getRecipes());
    }
}

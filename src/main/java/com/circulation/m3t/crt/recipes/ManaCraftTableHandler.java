package com.circulation.m3t.crt.recipes;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;
import static project.studio.manametalmod.ManaMetalAPI.ManaCraftTableRecipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.reload.ItemListChanges;
import com.circulation.m3t.reload.M3TCrtReload;

import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import project.studio.manametalmod.core.RecipesOreTable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "ManaCraftTable")
public class ManaCraftTableHandler implements M3TCrtReload {

    private static final ItemListChanges<RecipesOreTable> CHANGES = new ItemListChanges<>(
        RecipeInputs::craftingTableOutput);

    @ZenMethod
    public static void addRecipe(IItemStack out, IIngredient[] input) {
        CHANGES.add(RecipeInputs.craftingTable(out, input));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        CHANGES.remove(requireItemStack(out));
    }

    @Override
    public void reload() {
        CHANGES.clearChanges();
    }

    @Override
    public void postReload() {
        CHANGES.applyTo(ManaCraftTableRecipes);
    }
}

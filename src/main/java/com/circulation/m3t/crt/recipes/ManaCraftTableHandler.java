package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.core.RecipesOreTable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

import static com.circulation.m3t.Util.Function.noHasItem;
import static project.studio.manametalmod.ManaMetalAPI.ManaCraftTableRecipes;

@ZenClass(M3TCrtAPI.CrtClass + "ManaCraftTable")
public class ManaCraftTableHandler implements M3TCrtReload {

    private static final List<RecipesOreTable> addManaCraftTableRecipes = new ReferenceArrayList<>();
    private static final List<ItemStack> removeManaCraftTableRecipes = new ReferenceArrayList<>();
    private static final List<RecipesOreTable> defManaCraftTableRecipes = new ReferenceArrayList<>();

    public void reload() {
        addManaCraftTableRecipes.clear();
        removeManaCraftTableRecipes.clear();
    }

    public void postReload() {
        if (defManaCraftTableRecipes.isEmpty()) {
            defManaCraftTableRecipes.addAll(ManaCraftTableRecipes);
        }

        List<RecipesOreTable> list = new ArrayList<>();
        defManaCraftTableRecipes.forEach(recipe -> {
            if (noHasItem(removeManaCraftTableRecipes, recipe.items[9])) {
                list.add(recipe);
            }
        });

        ManaCraftTableRecipes.clear();
        ManaCraftTableRecipes.addAll(list);
        ManaCraftTableRecipes.addAll(addManaCraftTableRecipes);
    }

    @ZenMethod
    public static void addRecipe(IItemStack out, IIngredient[] input) {
        Object[] objs = new Object[10];
        if (input.length < 9) return;
        for (int i = 0; i < 9; i++) {
            if (input[i] instanceof IOreDictEntry) {
                objs[i] = ((IOreDictEntry) input[i]).getName();
            } else if (input[i] instanceof IItemStack) {
                objs[i] = MineTweakerMC.getItemStack(input[i]);
            }
        }
        objs[9] = MineTweakerMC.getItemStack(out);
        addManaCraftTableRecipes.add(new RecipesOreTable(objs));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        removeManaCraftTableRecipes.add(MineTweakerMC.getItemStack(out));
    }
}

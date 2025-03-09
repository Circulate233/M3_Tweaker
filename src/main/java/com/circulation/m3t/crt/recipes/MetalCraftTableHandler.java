package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
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
import static project.studio.manametalmod.ManaMetalAPI.MetalCraftTableRecipes;

@ZenClass(M3TCrtAPI.CrtClass + "MetalCraftTable")
public class MetalCraftTableHandler implements M3TCrtReload {

    private static final List<RecipesOreTable> addMetalCraftTableRecipes = new ArrayList<>();
    private static final List<ItemStack> removeMetalCraftTableRecipes = new ArrayList<>();
    private static final List<RecipesOreTable> defMetalCraftTableRecipes = new ArrayList<>();

    public void reload(){
        addMetalCraftTableRecipes.clear();
        removeMetalCraftTableRecipes.clear();
    }

    public void postReload(){
        if (defMetalCraftTableRecipes.isEmpty()){defMetalCraftTableRecipes.addAll(MetalCraftTableRecipes);}

        List<RecipesOreTable> list = new ArrayList<>();
        defMetalCraftTableRecipes.forEach(recipe -> {
            if (noHasItem(removeMetalCraftTableRecipes, recipe.items[9])){
                list.add(recipe);
            }
        });

        MetalCraftTableRecipes.clear();
        MetalCraftTableRecipes.addAll(list);
        MetalCraftTableRecipes.addAll(addMetalCraftTableRecipes);
    }

    @ZenMethod
    public static void addRecipe(IItemStack out, IIngredient[] input) {
        Object[] objs = new Object[10];
        if (input.length < 9)return;
        for (int i = 0; i < 9; i++) {
            if (input[i] instanceof IOreDictEntry){
                objs[i] = ((IOreDictEntry) input[i]).getName();
            } else if (input[i] instanceof IItemStack){
                objs[i] = MineTweakerMC.getItemStack(input[i]);
            }
        }
        objs[9] = MineTweakerMC.getItemStack(out);
        addMetalCraftTableRecipes.add(new RecipesOreTable(objs));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        removeMetalCraftTableRecipes.add(MineTweakerMC.getItemStack(out));
    }
}

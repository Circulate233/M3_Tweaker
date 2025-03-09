package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.Lapuda.BlueSkyRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

import static com.circulation.m3t.Util.Function.noHasItem;
import static project.studio.manametalmod.ManaMetalAPI.BlueSkyRecipeList;

@ZenClass(M3TCrtAPI.CrtClass + "BlueSky")
public class BlueSkyHandler implements M3TCrtReload {

    private static final List<BlueSkyRecipe> addBlueSkyRecipeList = new ArrayList<>();
    private static final List<ItemStack> removeBlueSkyRecipeList = new ArrayList<>();
    private static final List<BlueSkyRecipe> defBlueSkyRecipeList = new ArrayList<>();

    public void reload(){
        addBlueSkyRecipeList.clear();
        removeBlueSkyRecipeList.clear();
    }

    public void postReload(){
        if (defBlueSkyRecipeList.isEmpty()){defBlueSkyRecipeList.addAll(BlueSkyRecipeList);}

        List<BlueSkyRecipe> list = new ArrayList<>();
        defBlueSkyRecipeList.forEach(recipe -> {
            if (noHasItem(removeBlueSkyRecipeList, recipe.out)){
                list.add(recipe);
            }
        });

        BlueSkyRecipeList.clear();
        BlueSkyRecipeList.addAll(list);
        BlueSkyRecipeList.addAll(addBlueSkyRecipeList);
    }

    @ZenMethod
    public static void addRecipe(IItemStack out, IItemStack[] input,int needXP) {
        addBlueSkyRecipeList.add(new BlueSkyRecipe(MineTweakerMC.getItemStack(out),needXP,MineTweakerMC.getItemStacks(input)));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        removeBlueSkyRecipeList.add(MineTweakerMC.getItemStack(out));
    }
}

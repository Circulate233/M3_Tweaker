package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.dark_magic.DarkItemRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

import static com.circulation.m3t.Util.Function.noHasItem;
import static project.studio.manametalmod.ManaMetalAPI.DarkItemRecipeList;

@ZenClass(M3TCrtAPI.CrtClass + "DarkItem")
public class DarkItemHandler implements M3TCrtReload {

    private static final List<DarkItemRecipe> addDarkItemRecipeList = new ReferenceArrayList<>();
    private static final List<ItemStack> removeDarkItemRecipeList = new ReferenceArrayList<>();
    private static final List<DarkItemRecipe> defDarkItemRecipeList = new ReferenceArrayList<>();

    public void reload(){
        addDarkItemRecipeList.clear();
        removeDarkItemRecipeList.clear();
    }

    public void postReload(){
        if (defDarkItemRecipeList.isEmpty()){defDarkItemRecipeList.addAll(DarkItemRecipeList);}

        List<DarkItemRecipe> list = new ReferenceArrayList<>();
        defDarkItemRecipeList.forEach(recipe -> {
            if (noHasItem(removeDarkItemRecipeList, recipe.out)){
                list.add(recipe);
            }
        });

        DarkItemRecipeList.clear();
        DarkItemRecipeList.addAll(list);
        DarkItemRecipeList.addAll(addDarkItemRecipeList);
    }

    @ZenMethod
    public static void addRecipe(IItemStack out, IItemStack[] input,int needXP) {
        addDarkItemRecipeList.add(new DarkItemRecipe(MineTweakerMC.getItemStack(out),needXP,MineTweakerMC.getItemStacks(input)));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        removeDarkItemRecipeList.add(MineTweakerMC.getItemStack(out));
    }
}

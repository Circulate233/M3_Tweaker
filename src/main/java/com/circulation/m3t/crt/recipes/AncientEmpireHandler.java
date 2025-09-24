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

import java.util.ArrayList;
import java.util.List;

import static com.circulation.m3t.Util.Function.noHasItem;
import static project.studio.manametalmod.ManaMetalAPI.AncientEmpireRecipeList;

@ZenClass(M3TCrtAPI.CrtClass + "AncientEmpire")
public class AncientEmpireHandler implements M3TCrtReload {

    private static final List<DarkItemRecipe> addAncientEmpireRecipeList = new ReferenceArrayList<>();
    private static final List<ItemStack> removeAncientEmpireRecipeList = new ReferenceArrayList<>();
    private static final List<DarkItemRecipe> defAncientEmpireRecipeList = new ReferenceArrayList<>();

    public void reload(){
        addAncientEmpireRecipeList.clear();
        removeAncientEmpireRecipeList.clear();
    }

    public void postReload(){
        if (defAncientEmpireRecipeList.isEmpty()){defAncientEmpireRecipeList.addAll(AncientEmpireRecipeList);}

        List<DarkItemRecipe> list = new ArrayList<>();
        defAncientEmpireRecipeList.forEach(recipe -> {
            if (noHasItem(removeAncientEmpireRecipeList, recipe.out)){
                list.add(recipe);
            }
        });

        AncientEmpireRecipeList.clear();
        AncientEmpireRecipeList.addAll(list);
        AncientEmpireRecipeList.addAll(addAncientEmpireRecipeList);
    }

    @ZenMethod
    public static void addRecipe(IItemStack out, IItemStack[] input,int needXP) {
        addAncientEmpireRecipeList.add(new DarkItemRecipe(MineTweakerMC.getItemStack(out),needXP,MineTweakerMC.getItemStacks(input)));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        removeAncientEmpireRecipeList.add(MineTweakerMC.getItemStack(out));
    }
}

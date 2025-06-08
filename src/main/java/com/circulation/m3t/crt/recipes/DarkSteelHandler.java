package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import com.circulation.m3t.mixins.MMM.AccessorDarkSteelRecipes;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.circulation.m3t.Util.Function.noHasItem;

@ZenClass(M3TCrtAPI.CrtClass + "DarkSteel")
public class DarkSteelHandler implements M3TCrtReload {

    private static final List<DarkSteel> addDarkSteelRecipeList = new ArrayList<>();
    private static final List<ItemStack> removeDarkSteelRecipeList = new ArrayList<>();
    private static final List<DarkSteel> defDarkSteelRecipeList = new ArrayList<>();

    public void reload(){
        addDarkSteelRecipeList.clear();
        removeDarkSteelRecipeList.clear();
    }

    public void postReload(){
        AccessorDarkSteelRecipes manaFurnace = ((AccessorDarkSteelRecipes)AccessorDarkSteelRecipes.getSmeltingBase());
        if (defDarkSteelRecipeList.isEmpty()){
            if (manaFurnace != null) {
                manaFurnace.getSmeltingList().forEach((input,output) -> {
                    Float exp = manaFurnace.getExperienceList().getOrDefault(output, 0.0f);
                    defDarkSteelRecipeList.add(new DarkSteel(input,output,exp));
                });
            }
        }

        var SmeltingList = new HashMap<>();
        var ExperienceList = new HashMap<>();

        defDarkSteelRecipeList.forEach(recipe -> {
            if (noHasItem(removeDarkSteelRecipeList, recipe.output)){
                SmeltingList.put(recipe.input,recipe.output);
                ExperienceList.put(recipe.output,recipe.exp);
            }
        });

        addDarkSteelRecipeList.forEach(recipe -> {
            SmeltingList.put(recipe.input,recipe.output);
            ExperienceList.put(recipe.output,recipe.exp);
        });

        if (manaFurnace != null) {
            manaFurnace.setExperienceList(ExperienceList);
        }
        if (manaFurnace != null) {
            manaFurnace.setSmeltingList(SmeltingList);
        }
    }

    @ZenMethod
    public static void addRecipe(IItemStack input,IItemStack output) {
        addRecipe(MineTweakerMC.getItemStack(input),MineTweakerMC.getItemStack(output), 0.0f);
    }

    @ZenMethod
    public static void addRecipe(IItemStack input,IItemStack output,Float exp) {
        addRecipe(MineTweakerMC.getItemStack(input),MineTweakerMC.getItemStack(output), exp);
    }

    public static void addRecipe(ItemStack input,ItemStack output,Float exp) {
        addDarkSteelRecipeList.add(new DarkSteel(input, output, exp));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        removeDarkSteelRecipeList.add(MineTweakerMC.getItemStack(out));
    }

    private static class DarkSteel{
        private final ItemStack input;
        private final ItemStack output;
        private final float exp;

        private DarkSteel(ItemStack input,ItemStack output,Float exp){
            this.input = input;
            this.output = output;
            this.exp = exp;
        }
    }
}

package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import com.circulation.m3t.mixins.MMM.AccessorIronWroughtFurnaceRecipes;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.circulation.m3t.Util.Function.noHasItem;

@ZenClass(M3TCrtAPI.CrtClass + "IronWroughtFurnace")
public class IronWroughtFurnaceHandler implements M3TCrtReload {

    private static final List<IronWroughtFurnace> addIronWroughtFurnaceRecipeList = new ArrayList<>();
    private static final List<ItemStack> removeIronWroughtFurnaceRecipeList = new ArrayList<>();
    private static final List<IronWroughtFurnace> defIronWroughtFurnaceRecipeList = new ArrayList<>();

    public void reload(){
        addIronWroughtFurnaceRecipeList.clear();
        removeIronWroughtFurnaceRecipeList.clear();
    }

    public void postReload(){
        AccessorIronWroughtFurnaceRecipes manaFurnace = ((AccessorIronWroughtFurnaceRecipes)AccessorIronWroughtFurnaceRecipes.getSmeltingBase());
        if (defIronWroughtFurnaceRecipeList.isEmpty()){
            manaFurnace.getSmeltingList().forEach((input,output) -> {
                Float exp = manaFurnace.getExperienceList().getOrDefault(output, 0.0f);
                defIronWroughtFurnaceRecipeList.add(new IronWroughtFurnace(input,output,exp));
            });
        }

        Map SmeltingList = new HashMap<>();
        Map ExperienceList = new HashMap();

        defIronWroughtFurnaceRecipeList.forEach(recipe -> {
            if (noHasItem(removeIronWroughtFurnaceRecipeList, recipe.output)){
                SmeltingList.put(recipe.input,recipe.output);
                ExperienceList.put(recipe.output,recipe.exp);
            }
        });

        addIronWroughtFurnaceRecipeList.forEach(recipe -> {
            SmeltingList.put(recipe.input,recipe.output);
            ExperienceList.put(recipe.output,recipe.exp);
        });

        manaFurnace.setExperienceList(ExperienceList);
        manaFurnace.setSmeltingList(SmeltingList);
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
        addIronWroughtFurnaceRecipeList.add(new IronWroughtFurnace(input, output, exp));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        removeIronWroughtFurnaceRecipeList.add(MineTweakerMC.getItemStack(out));
    }

    private static class IronWroughtFurnace{
        private ItemStack input;
        private ItemStack output;
        private float exp;

        private IronWroughtFurnace(ItemStack input,ItemStack output,Float exp){
            this.input = input;
            this.output = output;
            this.exp = exp;
        }
    }
}

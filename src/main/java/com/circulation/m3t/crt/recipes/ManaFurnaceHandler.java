package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import com.circulation.m3t.mixins.MMM.AccessorManaFurnaceRecipes;
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

@ZenClass(M3TCrtAPI.CrtClass + "ManaFurnace")
public class ManaFurnaceHandler implements M3TCrtReload {

    private static final List<ManaFurnace> addManaFurnaceRecipeList = new ArrayList<>();
    private static final List<ItemStack> removeManaFurnaceRecipeList = new ArrayList<>();
    private static final List<ManaFurnace> defManaFurnaceRecipeList = new ArrayList<>();

    public void reload(){
        addManaFurnaceRecipeList.clear();
        removeManaFurnaceRecipeList.clear();
    }

    public void postReload(){
        AccessorManaFurnaceRecipes manaFurnace = ((AccessorManaFurnaceRecipes)AccessorManaFurnaceRecipes.getSmeltingBase());
        if (defManaFurnaceRecipeList.isEmpty()){
            manaFurnace.getSmeltingList().forEach((input,output) -> {
                Float exp = manaFurnace.getExperienceList().getOrDefault(output, 0.0f);
                defManaFurnaceRecipeList.add(new ManaFurnace(input,output,exp));
            });
        }

        Map SmeltingList = new HashMap<>();
        Map ExperienceList = new HashMap();

        defManaFurnaceRecipeList.forEach(recipe -> {
            if (noHasItem(removeManaFurnaceRecipeList, recipe.output)){
                SmeltingList.put(recipe.input,recipe.output);
                ExperienceList.put(recipe.output,recipe.exp);
            }
        });

        addManaFurnaceRecipeList.forEach(recipe -> {
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
        addManaFurnaceRecipeList.add(new ManaFurnace(input, output, exp));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        removeManaFurnaceRecipeList.add(MineTweakerMC.getItemStack(out));
    }

    private static class ManaFurnace{
        private ItemStack input;
        private ItemStack output;
        private float exp;

        private ManaFurnace(ItemStack input,ItemStack output,Float exp){
            this.input = input;
            this.output = output;
            this.exp = exp;
        }
    }
}

package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import com.circulation.m3t.mixins.MMM.AccessorManaFurnaceRecipes;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.*;

import static com.circulation.m3t.Util.Function.noHasItem;

@ZenClass(M3TCrtAPI.CrtClass + "ManaFurnace")
public class ManaFurnaceHandler implements M3TCrtReload {

    private static final Map<ManaFurnace,List<ItemStack>> addManaFurnaceRecipeList = new HashMap<>();
    private static final List<ItemStack> removeManaFurnaceRecipeList = new ArrayList<>();
    private static final List<ManaFurnace> defManaFurnaceRecipeList = new ArrayList<>();

    public void reload(){
        addManaFurnaceRecipeList.clear();
        removeManaFurnaceRecipeList.clear();
    }

    public void postReload(){
        AccessorManaFurnaceRecipes manaFurnace = ((AccessorManaFurnaceRecipes)AccessorManaFurnaceRecipes.getSmeltingBase());
        if (defManaFurnaceRecipeList.isEmpty()){
            if (manaFurnace != null) {
                manaFurnace.getSmeltingList().forEach((input,output) -> {
                    Float exp = manaFurnace.getExperienceList().getOrDefault(output, 0.0f);
                    defManaFurnaceRecipeList.add(new ManaFurnace(input,output,exp));
                });
            }
        }

        var SmeltingList = new HashMap<>();
        var ExperienceList = new HashMap<>();

        defManaFurnaceRecipeList.forEach(recipe -> {
            if (noHasItem(removeManaFurnaceRecipeList, recipe.output)){
                SmeltingList.put(recipe.input,recipe.output);
                ExperienceList.put(recipe.output,recipe.exp);
            }
        });

        addManaFurnaceRecipeList.forEach((output,recipes) -> {
            recipes.forEach(recipe -> SmeltingList.put(recipe,output.output));
            ExperienceList.put(output.output,output.exp);
        });

        if (manaFurnace != null) {
            manaFurnace.setExperienceList(ExperienceList);
        }
        if (manaFurnace != null) {
            manaFurnace.setSmeltingList(SmeltingList);
        }
    }

    @ZenMethod
    public static void addRecipe(IIngredient input,IItemStack output) {
        addManaFurnaceRecipeList.put(new ManaFurnace(MineTweakerMC.getItemStack(output),0.0f),Arrays.asList(MineTweakerMC.getItemStacks(input.getItems())));
    }

    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output, Float exp) {
        addManaFurnaceRecipeList.put(new ManaFurnace(MineTweakerMC.getItemStack(output),exp),Arrays.asList(MineTweakerMC.getItemStacks(input.getItems())));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        removeManaFurnaceRecipeList.add(MineTweakerMC.getItemStack(out));
    }

    private static class ManaFurnace{
        private ItemStack input;
        private final ItemStack output;
        private final float exp;

        private ManaFurnace(ItemStack input,ItemStack output,Float exp){
            this.input = input;
            this.output = output;
            this.exp = exp;
        }

        private ManaFurnace(ItemStack output,Float exp){
            this.output = output;
            this.exp = exp;
        }
    }
}

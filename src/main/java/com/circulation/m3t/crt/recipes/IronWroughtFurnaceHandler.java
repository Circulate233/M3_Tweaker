package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import com.circulation.m3t.mixins.MMM.AccessorIronWroughtFurnaceRecipes;
import com.github.bsideup.jabel.Desugar;
import it.unimi.dsi.fastutil.objects.Reference2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

import static com.circulation.m3t.Util.Function.noHasItem;

@ZenClass(M3TCrtAPI.CrtClass + "IronWroughtFurnace")
public class IronWroughtFurnaceHandler implements M3TCrtReload {

    private static final List<IronWroughtFurnace> addIronWroughtFurnaceRecipeList = new ReferenceArrayList<>();
    private static final List<ItemStack> removeIronWroughtFurnaceRecipeList = new ReferenceArrayList<>();
    private static final List<IronWroughtFurnace> defIronWroughtFurnaceRecipeList = new ReferenceArrayList<>();

    public void reload() {
        addIronWroughtFurnaceRecipeList.clear();
        removeIronWroughtFurnaceRecipeList.clear();
    }

    public void postReload() {
        AccessorIronWroughtFurnaceRecipes manaFurnace = ((AccessorIronWroughtFurnaceRecipes) AccessorIronWroughtFurnaceRecipes.getSmeltingBase());
        if (defIronWroughtFurnaceRecipeList.isEmpty()) {
            if (manaFurnace != null) {
                manaFurnace.getSmeltingList().forEach((input, output) -> {
                    Float exp = manaFurnace.getExperienceList().getOrDefault(output, 0.0f);
                    defIronWroughtFurnaceRecipeList.add(new IronWroughtFurnace(input, output, exp));
                });
            }
        }

        var SmeltingList = new Reference2ObjectOpenHashMap<ItemStack, ItemStack>();
        var ExperienceList = new Reference2FloatOpenHashMap<ItemStack>();

        defIronWroughtFurnaceRecipeList.forEach(recipe -> {
            if (noHasItem(removeIronWroughtFurnaceRecipeList, recipe.output)) {
                SmeltingList.put(recipe.input, recipe.output);
                ExperienceList.put(recipe.output, recipe.exp);
            }
        });

        addIronWroughtFurnaceRecipeList.forEach(recipe -> {
            SmeltingList.put(recipe.input, recipe.output);
            ExperienceList.put(recipe.output, recipe.exp);
        });

        if (manaFurnace != null) {
            manaFurnace.setExperienceList(ExperienceList);
        }
        if (manaFurnace != null) {
            manaFurnace.setSmeltingList(SmeltingList);
        }
    }

    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        addRecipe(MineTweakerMC.getItemStack(input), MineTweakerMC.getItemStack(output), 0.0f);
    }

    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, Float exp) {
        addRecipe(MineTweakerMC.getItemStack(input), MineTweakerMC.getItemStack(output), exp);
    }

    public static void addRecipe(ItemStack input, ItemStack output, Float exp) {
        addIronWroughtFurnaceRecipeList.add(new IronWroughtFurnace(input, output, exp));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        removeIronWroughtFurnaceRecipeList.add(MineTweakerMC.getItemStack(out));
    }

    @Desugar
    private record IronWroughtFurnace(ItemStack input, ItemStack output, float exp) {
    }
}

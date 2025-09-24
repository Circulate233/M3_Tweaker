package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import com.circulation.m3t.mixins.MMM.AccessorDarkSteelRecipes;
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

@ZenClass(M3TCrtAPI.CrtClass + "DarkSteel")
public class DarkSteelHandler implements M3TCrtReload {

    private static final List<DarkSteel> addDarkSteelRecipeList = new ReferenceArrayList<>();
    private static final List<ItemStack> removeDarkSteelRecipeList = new ReferenceArrayList<>();
    private static final List<DarkSteel> defDarkSteelRecipeList = new ReferenceArrayList<>();

    public void reload() {
        addDarkSteelRecipeList.clear();
        removeDarkSteelRecipeList.clear();
    }

    public void postReload() {
        AccessorDarkSteelRecipes manaFurnace = ((AccessorDarkSteelRecipes) AccessorDarkSteelRecipes.getSmeltingBase());
        if (defDarkSteelRecipeList.isEmpty()) {
            if (manaFurnace != null) {
                manaFurnace.getSmeltingList().forEach((input, output) -> {
                    Float exp = manaFurnace.getExperienceList().getOrDefault(output, 0.0f);
                    defDarkSteelRecipeList.add(new DarkSteel(input, output, exp));
                });
            }
        }

        var SmeltingList = new Reference2ObjectOpenHashMap<ItemStack, ItemStack>();
        var ExperienceList = new Reference2FloatOpenHashMap<ItemStack>();

        defDarkSteelRecipeList.forEach(recipe -> {
            if (noHasItem(removeDarkSteelRecipeList, recipe.output)) {
                SmeltingList.put(recipe.input, recipe.output);
                ExperienceList.put(recipe.output, recipe.exp);
            }
        });

        addDarkSteelRecipeList.forEach(recipe -> {
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
        addDarkSteelRecipeList.add(new DarkSteel(input, output, exp));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        removeDarkSteelRecipeList.add(MineTweakerMC.getItemStack(out));
    }

    @Desugar
    private record DarkSteel(ItemStack input, ItemStack output, float exp) {

    }
}

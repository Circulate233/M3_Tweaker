package com.circulation.m3t.crt.recipes;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.mixins.mmm.AccessorManaFurnaceRecipes;
import com.circulation.m3t.reload.M3TCrtReload;

import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import project.studio.manametalmod.items.crafting.ManaFurnaceRecipes;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "ManaFurnace")
public class ManaFurnaceHandler implements M3TCrtReload {

    private static final FurnaceChanges CHANGES = new FurnaceChanges();

    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output) {
        addRecipe(input, output, 0.0f);
    }

    @ZenMethod
    public static void addRecipe(IIngredient input, IItemStack output, float exp) {
        CHANGES.add(MineTweakerMC.getItemStacks(input.getItems()), requireItemStack(output), exp);
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        CHANGES.remove(requireItemStack(out));
    }

    @Override
    public void reload() {
        CHANGES.clearChanges();
    }

    @Override
    public void postReload() {
        AccessorManaFurnaceRecipes furnace = (AccessorManaFurnaceRecipes) ManaFurnaceRecipes.smelting();
        FurnaceChanges.Tables tables = CHANGES.apply(furnace.m3t$getRecipes(), furnace.m3t$getExperience());
        if (tables != null) {
            furnace.m3t$setRecipes(tables.recipes());
            furnace.m3t$setExperience(tables.experience());
        }
    }
}

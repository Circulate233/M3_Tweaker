package com.circulation.m3t.crt.recipes;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;

import net.minecraft.item.ItemStack;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.mixins.mmm.AccessorDarkSteelRecipes;
import com.circulation.m3t.reload.M3TCrtReload;

import minetweaker.api.item.IItemStack;
import project.studio.manametalmod.dark_magic.DarkSteelRecipes;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "DarkSteel")
public class DarkSteelHandler implements M3TCrtReload {

    private static final FurnaceChanges CHANGES = new FurnaceChanges();

    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output) {
        addRecipe(input, output, 0.0f);
    }

    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, Float exp) {
        addRecipe(requireItemStack(input), requireItemStack(output), exp);
    }

    public static void addRecipe(ItemStack input, ItemStack output, Float exp) {
        CHANGES.add(input, output, exp);
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
        AccessorDarkSteelRecipes furnace = (AccessorDarkSteelRecipes) DarkSteelRecipes.smelting();
        FurnaceChanges.Tables tables = CHANGES.apply(furnace.m3t$getRecipes(), furnace.m3t$getExperience());
        if (tables != null) {
            furnace.m3t$setRecipes(tables.recipes());
            furnace.m3t$setExperience(tables.experience());
        }
    }
}

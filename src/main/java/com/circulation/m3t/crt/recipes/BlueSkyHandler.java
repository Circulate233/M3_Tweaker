package com.circulation.m3t.crt.recipes;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;
import static project.studio.manametalmod.ManaMetalAPI.BlueSkyRecipeList;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.reload.ItemListChanges;
import com.circulation.m3t.reload.M3TCrtReload;

import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import project.studio.manametalmod.Lapuda.BlueSkyRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "BlueSky")
public class BlueSkyHandler implements M3TCrtReload {

    private static final ItemListChanges<BlueSkyRecipe> CHANGES = new ItemListChanges<>(recipe -> recipe.out);

    @ZenMethod
    public static void addRecipe(IItemStack out, IItemStack[] input, int needXP) {
        CHANGES.add(new BlueSkyRecipe(requireItemStack(out), needXP, MineTweakerMC.getItemStacks(input)));
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
        CHANGES.applyTo(BlueSkyRecipeList);
    }
}

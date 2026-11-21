package com.circulation.m3t.crt.recipes;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.reload.ItemListChanges;
import com.circulation.m3t.reload.M3TCrtReload;

import minetweaker.api.item.IItemStack;
import project.studio.manametalmod.originmagic.OriginCrystalRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "OriginCrystalCore")
public class OriginCrystalCoreHandler implements M3TCrtReload {

    private static final ItemListChanges<OriginCrystalRecipe> CHANGES = new ItemListChanges<>(recipe -> recipe.output);

    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack[] ingredients, int needXP) {
        if (needXP < 0) {
            throw new IllegalArgumentException("Origin crystal recipe experience requirement must not be negative");
        }
        CHANGES.add(
            new OriginCrystalRecipe(
                RecipeInputs.positiveStack(output),
                needXP,
                RecipeInputs.positiveStacks(ingredients)));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        CHANGES.remove(requireItemStack(output));
    }

    @Override
    public void reload() {
        CHANGES.clearChanges();
    }

    @Override
    public void postReload() {
        CHANGES.applyTo(OriginCrystalRecipe.recipes);
    }
}

package com.circulation.m3t.crt.recipes;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;
import static project.studio.manametalmod.ManaMetalAPI.ManaMetalInjectionRecipeList;

import net.minecraft.item.ItemStack;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.reload.ItemListChanges;
import com.circulation.m3t.reload.M3TCrtReload;

import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "ManaMetalInjection")
public class ManaMetalInjectionHandler implements M3TCrtReload {

    private static final int INPUT_SLOTS = 8;
    private static final int OUTPUT_SLOT = INPUT_SLOTS + 1;
    private static final ItemListChanges<ItemStack[]> CHANGES = new ItemListChanges<>(recipe -> recipe[OUTPUT_SLOT]);

    @ZenMethod
    public static void addRecipe(IItemStack output, IItemStack mainInput, IItemStack[] input) {
        ItemStack[] recipe = RecipeInputs.itemGrid(input, INPUT_SLOTS, OUTPUT_SLOT + 1);
        recipe[INPUT_SLOTS] = requireItemStack(mainInput);
        recipe[OUTPUT_SLOT] = requireItemStack(output);
        CHANGES.add(recipe);
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
        CHANGES.applyTo(ManaMetalInjectionRecipeList);
    }
}

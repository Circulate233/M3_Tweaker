package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.reload.ItemListChanges;
import com.circulation.m3t.reload.M3TCrtReload;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.dark_magic.DarkFission;
import project.studio.manametalmod.dark_magic.TileEntityDarkFission;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;
import static project.studio.manametalmod.ManaMetalAPI.DarkFissionList;

@ZenClass(M3TCrtAPI.CrtClass + "DarkFission")
public class DarkFissionHandler implements M3TCrtReload {

    private static final ItemListChanges<DarkFission> CHANGES = new ItemListChanges<>(recipe -> recipe.imp);

    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack[] outputs) {
        ItemStack source = RecipeInputs.positiveStack(input);
        if (source.stackSize != 1) {
            throw new IllegalArgumentException("Dark fission consumes exactly one input per recipe");
        }
        int outputSlots = TileEntityDarkFission.out.length - 1;
        if (outputs == null || outputs.length == 0 || outputs.length > outputSlots) {
            throw new IllegalArgumentException("Dark fission recipes require 1 to " + outputSlots + " outputs");
        }
        ItemStack[] products = new ItemStack[outputs.length];
        for (int i = 0; i < outputs.length; i++) {
            ItemStack product = RecipeInputs.positiveStack(outputs[i]);
            RecipeInputs.checkOutputSlot(product);
            products[i] = product.copy();
        }
        CHANGES.add(new DarkFission(source.copy(), products));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        CHANGES.remove(requireItemStack(input));
    }

    @Override
    public void reload() {
        CHANGES.clearChanges();
    }

    @Override
    public void postReload() {
        CHANGES.applyTo(DarkFissionList);
    }
}

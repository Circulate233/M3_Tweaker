package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.reload.ItemIntChanges;
import com.circulation.m3t.reload.M3TCrtReload;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;
import static project.studio.manametalmod.ManaMetalAPI.PrimalForgeItemList;

@ZenClass(M3TCrtAPI.CrtClass + "PrimalForge")
public class PrimalForgeHandler implements M3TCrtReload {

    private static final ItemIntChanges CHANGES = new ItemIntChanges();

    @ZenMethod
    public static void addRecipe(IItemStack input, int points) {
        CHANGES.add(requireItemStack(input).getItem(), points);
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        CHANGES.remove(requireItemStack(input).getItem());
    }

    @Override
    public void reload() {
        CHANGES.clearChanges();
    }

    @Override
    public void postReload() {
        CHANGES.applyTo(PrimalForgeItemList);
    }
}

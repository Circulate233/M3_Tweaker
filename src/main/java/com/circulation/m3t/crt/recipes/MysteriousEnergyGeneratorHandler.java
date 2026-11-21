package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.reload.ItemIntChanges;
import com.circulation.m3t.reload.M3TCrtReload;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;
import static project.studio.manametalmod.ManaMetalAPI.MysteriousEnergyItemList;

@ZenClass(M3TCrtAPI.CrtClass + "MysteriousEnergyGenerator")
public class MysteriousEnergyGeneratorHandler implements M3TCrtReload {

    private static final ItemIntChanges CHANGES = new ItemIntChanges();

    @ZenMethod
    public static void addRecipe(IItemStack input, int energy) {
        CHANGES.add(requireItemStack(input).getItem(), energy);
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
        CHANGES.applyTo(MysteriousEnergyItemList);
    }
}

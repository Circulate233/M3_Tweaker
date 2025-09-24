package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

import static com.circulation.m3t.Util.Function.noHasItem;
import static project.studio.manametalmod.ManaMetalAPI.WandMakeRecipeList;

@ZenClass(M3TCrtAPI.CrtClass + "WandMake")
public class WandMakeHandler implements M3TCrtReload {

    private static final List<ItemStack[]> addWandMakeRecipeList = new ReferenceArrayList<>();
    private static final List<ItemStack> removeWandMakeRecipeList = new ReferenceArrayList<>();
    private static final List<ItemStack[]> defWandMakeRecipeList = new ReferenceArrayList<>();

    public void reload(){
        addWandMakeRecipeList.clear();
        removeWandMakeRecipeList.clear();
    }

    public void postReload(){
        if (defWandMakeRecipeList.isEmpty()){defWandMakeRecipeList.addAll(WandMakeRecipeList);}

        List<ItemStack[]> list = new ReferenceArrayList<>();
        defWandMakeRecipeList.forEach(recipe -> {
            if (noHasItem(removeWandMakeRecipeList, recipe[5])){
                list.add(recipe);
            }
        });

        WandMakeRecipeList.clear();
        WandMakeRecipeList.addAll(list);
        WandMakeRecipeList.addAll(addWandMakeRecipeList);
    }

    @ZenMethod
    public static void addRecipe(IItemStack output,IItemStack[] input) {
        ItemStack[] items = new ItemStack[6];
        if (input.length < 5)return;
        for (int i = 0; i < 5; i++) {
            items[i] = MineTweakerMC.getItemStack(input[i]);
        }
        items[5] = MineTweakerMC.getItemStack(output);
        addWandMakeRecipeList.add(items);
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        removeWandMakeRecipeList.add(MineTweakerMC.getItemStack(out));
    }
}

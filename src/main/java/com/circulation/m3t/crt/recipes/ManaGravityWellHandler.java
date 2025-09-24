package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.items.craftingRecipes.ManaGravityWellData;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

import static com.circulation.m3t.Util.Function.noHasItem;

@ZenClass(M3TCrtAPI.CrtClass + "ManaGravityWell")
public class ManaGravityWellHandler implements M3TCrtReload {

    private static final List<ItemStack[]> addManaGravityWellRecipeList = new ReferenceArrayList<>();
    private static final List<ItemStack> removeManaGravityWellRecipeList = new ReferenceArrayList<>();
    private static final List<ItemStack[]> defManaGravityWellRecipeList = new ReferenceArrayList<>();

    public void reload(){
        addManaGravityWellRecipeList.clear();
        removeManaGravityWellRecipeList.clear();
    }

    public void postReload(){
        if (defManaGravityWellRecipeList.isEmpty()){defManaGravityWellRecipeList.addAll(ManaGravityWellData.list);}

        List<ItemStack[]> list = new ReferenceArrayList<>();
        defManaGravityWellRecipeList.forEach(recipe -> {
            if (noHasItem(removeManaGravityWellRecipeList, recipe[12])){
                list.add(recipe);
            }
        });

        ManaGravityWellData.list.clear();
        ManaGravityWellData.list.addAll(list);
        ManaGravityWellData.list.addAll(addManaGravityWellRecipeList);
    }

    @ZenMethod
    public static void addRecipe(IItemStack output,IItemStack[] input) {
        ItemStack[] items = new ItemStack[13];
        if (input.length < 12)return;
        for (int i = 0; i < 12; i++) {
            items[i] = MineTweakerMC.getItemStack(input[i]);
        }
        items[12] = MineTweakerMC.getItemStack(output);
        addManaGravityWellRecipeList.add(items);
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        removeManaGravityWellRecipeList.add(MineTweakerMC.getItemStack(out));
    }
}

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
import static project.studio.manametalmod.ManaMetalAPI.ManaMetalInjectionRecipeList;

@ZenClass(M3TCrtAPI.CrtClass + "ManaMetalInjection")
public class ManaMetalInjectionHandler implements M3TCrtReload {

    private static final List<ItemStack[]> addManaMetalInjectionRecipeList = new ReferenceArrayList<>();
    private static final List<ItemStack> removeManaMetalInjectionRecipeList = new ReferenceArrayList<>();
    private static final List<ItemStack[]> defManaMetalInjectionRecipeList = new ReferenceArrayList<>();

    public void reload(){
        addManaMetalInjectionRecipeList.clear();
        removeManaMetalInjectionRecipeList.clear();
    }

    public void postReload(){
        if (defManaMetalInjectionRecipeList.isEmpty()){defManaMetalInjectionRecipeList.addAll(ManaMetalInjectionRecipeList);}

        List<ItemStack[]> list = new ReferenceArrayList<>();
        defManaMetalInjectionRecipeList.forEach(recipe -> {
            if (noHasItem(removeManaMetalInjectionRecipeList, recipe[9])){
                list.add(recipe);
            }
        });

        ManaMetalInjectionRecipeList.clear();
        ManaMetalInjectionRecipeList.addAll(list);
        ManaMetalInjectionRecipeList.addAll(addManaMetalInjectionRecipeList);
    }

    @ZenMethod
    public static void addRecipe(IItemStack output,IItemStack mainInput,IItemStack[] input) {
        ItemStack[] items = new ItemStack[10];
        if (input.length < 8)return;
        for (int i = 0; i < 8; i++) {
            items[i] = MineTweakerMC.getItemStack(input[i]);
        }
        items[8] = MineTweakerMC.getItemStack(mainInput);
        items[9] = MineTweakerMC.getItemStack(output);
        addManaMetalInjectionRecipeList.add(items);
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        removeManaMetalInjectionRecipeList.add(MineTweakerMC.getItemStack(out));
    }
}

package com.circulation.m3t.crt.recipes;

import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.items.craftingRecipes.CastingData;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

import static com.circulation.m3t.Util.Function.noHasItem;

@ZenClass("mods.m3t.Casting")
public class CastingHandler {

    public static final List<Object[]> M3TaddRecipes = new ArrayList<>();
    private static final List<ItemStack> M3TremoveRecipes = new ArrayList<>();
    private static final List<Object[]> CastingRecipes = new ArrayList<>();

    @ZenMethod
    public static void addRecipe(IItemStack out, int money, IItemStack[] imp1) {
        Object[] obj = new Object[2 + imp1.length];
        obj[0] = money;
        obj[1] = MineTweakerMC.getItemStack(out);
        int i = 2;
        for (IItemStack item : imp1){
            obj[i] = MineTweakerMC.getItemStack(item);
            i++;
        }
        M3TaddRecipes.add(obj);
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        M3TremoveRecipes.add(MineTweakerMC.getItemStack(out));
    }

    public static void reload(){
        M3TaddRecipes.clear();
        M3TremoveRecipes.clear();
    }

    public static void postReload(){
        if (CastingRecipes.isEmpty()){CastingRecipes.addAll(CastingData.getRecipes);}

        List<Object[]> list = new ArrayList<>();
        CastingRecipes.forEach(recipe -> {
            if (noHasItem(M3TremoveRecipes, (ItemStack) recipe[1])){
                list.add(recipe);
            }
        });

        list.addAll(M3TaddRecipes);
        CastingData.getRecipes.clear();
        CastingData.getRecipes.addAll(list);
    }

    private static void addCastingRecipes(ItemStack out, int money, ItemStack... imp1) {
        Object[] obj = new Object[2 + imp1.length];
        obj[0] = money;
        obj[1] = out;
        int i = 2;
        for (ItemStack item :imp1){
            obj[i] = item;
            i++;
        }
        M3TaddRecipes.add(obj);
    }

}

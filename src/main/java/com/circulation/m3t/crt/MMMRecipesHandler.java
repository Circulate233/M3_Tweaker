package com.circulation.m3t.crt;

import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import project.studio.manametalmod.food_collection.FoodCollectionCore;
import project.studio.manametalmod.magic.magicItem.MagicItemType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.m3t.MMMRecipes")
public class MMMRecipesHandler {

    @ZenMethod
    public static void addItemEffect(IItemStack itemStack, int typeID,float data,short maxUse) {
        FoodCollectionCore.addItemEffect(MineTweakerMC.getItemStack(itemStack), MagicItemType.getTypeFromID(typeID), data, maxUse);
    }

}

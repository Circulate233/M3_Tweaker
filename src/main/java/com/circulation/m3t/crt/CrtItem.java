package com.circulation.m3t.crt;

import cpw.mods.fml.common.registry.GameData;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.*;

@ZenClass("mods.m3t.Util")
public class CrtItem {

    public static Map<String,Item> itemMap = new HashMap<>();

    static {
        GameData.getItemRegistry().forEach(item -> itemMap.put(Item.itemRegistry.getNameForObject(item),(Item) item));
    }

    @ZenMethod
    public static IItemStack[] getModItem(String modId){
        return itemMap.keySet().parallelStream()
            .filter(itemName -> itemName.startsWith(modId))
            .map(name -> MineTweakerMC.getIItemStack(new ItemStack(itemMap.get(name))))
            .toArray(IItemStack[]::new);
    }

}

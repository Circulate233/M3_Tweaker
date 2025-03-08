package com.circulation.m3t.crt;

import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.Item;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.*;

import static project.studio.manametalmod.ManaMetalAPI.ItemValue;

@ZenClass("mods.m3t.Money")
public class MoneyHandler {

    private static final Map<Item,Integer> map = new HashMap<>();
    private static final Map<Item,Integer> defmap = new HashMap<>();

    @ZenMethod
    public static void setMoney(IItemStack item, int money) {
        map.put(MineTweakerMC.getItemStack(item).getItem(), money);
    }

    public static void setMoney(Item item,int money) {
        map.put(item, money);
    }

    public static void reload(){
        map.clear();
    }

    public static void postReload(){
        if (defmap.isEmpty()){defmap.putAll(ItemValue);}

        ItemValue.clear();
        ItemValue.putAll(map);
    }
}

package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.Item;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static project.studio.manametalmod.ManaMetalAPI.ItemValue;

@ZenClass(M3TCrtAPI.CrtClass + "Money")
public class MoneyHandler implements M3TCrtReload {

    private static final Reference2IntMap<Item> map = new Reference2IntOpenHashMap<>();
    private static final Reference2IntMap<Item> defmap = new Reference2IntOpenHashMap<>();

    @ZenMethod
    public static void setMoney(IItemStack item, int money) {
        map.put(MineTweakerMC.getItemStack(item).getItem(), money);
    }

    public static void setMoney(Item item,int money) {
        map.put(item, money);
    }

    public void reload(){
        map.clear();
    }

    public void postReload(){
        if (defmap.isEmpty()){defmap.putAll(ItemValue);}

        ItemValue.clear();
        ItemValue.putAll(map);
    }
}

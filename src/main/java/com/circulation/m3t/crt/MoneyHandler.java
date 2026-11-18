package com.circulation.m3t.crt;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;
import static project.studio.manametalmod.ManaMetalAPI.ItemValue;

import net.minecraft.item.Item;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.reload.M3TCrtReload;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "Money")
public class MoneyHandler implements M3TCrtReload {

    private static final Reference2IntMap<Item> prices = new Reference2IntOpenHashMap<>();

    @ZenMethod
    public static void setMoney(IItemStack item, int money) {
        setMoney(requireItemStack(item).getItem(), money);
    }

    public static void setMoney(Item item, int money) {
        prices.put(item, money);
    }

    @Override
    public void reload() {
        prices.clear();
    }

    @Override
    public void postReload() {
        ItemValue.clear();
        ItemValue.putAll(prices);
    }
}

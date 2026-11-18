package com.circulation.m3t.crt.store;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;
import static com.circulation.m3t.crt.CrtUtil.requireItemStacks;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.reload.M3TCrtReload;
import com.circulation.m3t.store.NpcStock;

import minetweaker.api.item.IItemStack;
import project.studio.manametalmod.core.Icommodity;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "NPCStore")
public class NPCStoreHandler implements M3TCrtReload {

    @ZenMethod
    public static void addItem(int typeID, IItemStack item, int money) {
        NpcStock.addItem(typeID, new Icommodity(requireItemStack(item), money));
    }

    @ZenMethod
    public static void addItems(int typeID, IItemStack[] items, int[] money) {
        NpcStock.addItem(typeID, StoreItems.priced(items, money));
    }

    @ZenMethod
    public static void removeItems(int typeID, IItemStack[] items) {
        NpcStock.removeItem(typeID, requireItemStacks(items));
    }

    @ZenMethod
    public static void removeItem(int typeID, IItemStack stack) {
        NpcStock.removeItem(typeID, requireItemStack(stack));
    }

    @Override
    public void postReload() {
        NpcStock.sortAdditions();
    }

    @Override
    public void reload() {
        NpcStock.clearChanges();
    }
}

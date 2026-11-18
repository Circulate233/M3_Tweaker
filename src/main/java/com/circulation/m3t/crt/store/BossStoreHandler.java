package com.circulation.m3t.crt.store;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;
import static com.circulation.m3t.crt.CrtUtil.requireItemStacks;

import net.minecraft.item.ItemStack;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.reload.ItemListChanges;
import com.circulation.m3t.reload.M3TCrtReload;

import minetweaker.api.item.IItemStack;
import project.studio.manametalmod.core.Icommodity;
import project.studio.manametalmod.rpg.BossStore;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "BossStore")
public class BossStoreHandler implements M3TCrtReload {

    private static final ItemListChanges<Icommodity> LEVEL_ONE = new ItemListChanges<>(Icommodity::getItem);
    private static final ItemListChanges<Icommodity> LEVEL_TWO = new ItemListChanges<>(Icommodity::getItem);

    @ZenMethod
    public static void addItem(IItemStack item, int money, boolean isLv2) {
        ItemStack stack = requireItemStack(item);
        if (!isLv2) {
            LEVEL_ONE.add(new Icommodity(stack, money));
        }
        LEVEL_TWO.add(new Icommodity(stack, money));
    }

    @ZenMethod
    public static void addItems(IItemStack[] items, int[] money, boolean isLv2) {
        Icommodity[] additions = StoreItems.priced(items, money);
        if (!isLv2) {
            LEVEL_ONE.addAll(additions);
        }
        LEVEL_TWO.addAll(additions);
    }

    @ZenMethod
    public static void removeItems(IItemStack[] items, boolean isLv2) {
        for (ItemStack item : requireItemStacks(items)) {
            remove(item, isLv2);
        }
    }

    @ZenMethod
    public static void removeItem(IItemStack item, boolean isLv2) {
        remove(requireItemStack(item), isLv2);
    }

    private static void remove(ItemStack item, boolean isLv2) {
        if (!isLv2) {
            LEVEL_ONE.remove(item);
        }
        LEVEL_TWO.remove(item);
    }

    @Override
    public void postReload() {
        LEVEL_ONE.applyTo(BossStore.Items1);
        LEVEL_TWO.applyTo(BossStore.Items2);
    }

    @Override
    public void reload() {
        LEVEL_ONE.clearChanges();
        LEVEL_TWO.clearChanges();
    }
}

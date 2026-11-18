package com.circulation.m3t.crt.store;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;

import minetweaker.api.item.IItemStack;
import project.studio.manametalmod.core.Icommodity;

final class StoreItems {

    private StoreItems() {}

    static Icommodity[] priced(IItemStack[] items, int[] prices) {
        if (prices.length < items.length) {
            throw new IllegalArgumentException("Each store item requires a price");
        }
        Icommodity[] entries = new Icommodity[items.length];
        for (int i = 0; i < items.length; i++) {
            entries[i] = new Icommodity(requireItemStack(items[i]), prices[i]);
        }
        return entries;
    }
}

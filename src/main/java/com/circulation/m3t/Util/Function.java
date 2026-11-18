package com.circulation.m3t.Util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.registry.GameRegistry;

public final class Function {

    private Function() {}

    public static String getText(String str) {
        return StatCollector.translateToLocal(str);
    }

    public static ItemStack getItemStack(String itemID) {
        if (itemID != null) {
            String[] parts = itemID.split(":");
            if (parts.length >= 2) {
                String modId = parts[0];
                String itemName = parts[1];
                Item item = GameRegistry.findItem(modId, itemName);
                if (item == null) return null;
                int meta = (parts.length > 2) ? Integer.parseInt(parts[2]) : 0;
                int count = (parts.length > 3) ? Integer.parseInt(parts[3]) : 1;
                return new ItemStack(item, count, meta);
            }
        }
        return null;
    }

}

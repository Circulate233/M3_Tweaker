//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.circulation.m3t.Util;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class Function {
    public Function() {
    }

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
                return new ItemStack(item,count,meta);
            }
        }
        return null;
    }

    public static boolean noHasItem(List<ItemStack> set, ItemStack item){
        for (ItemStack item1 : set) {
            if (item1 == null || item == null) return true;
            if (item1.getItem() == item.getItem() && item1.getItemDamage() == item.getItemDamage()){
                return false;
            }
        }
        return true;
    }

}

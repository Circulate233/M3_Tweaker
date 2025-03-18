//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.circulation.m3t.Util;

import com.circulation.m3t.crt.events.BaubleEvent;
import com.circulation.m3t.crt.events.BaublePostEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import minetweaker.util.IEventHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

import static com.circulation.m3t.crt.events.M3TEventAPI.*;

public class Function {

    public static boolean isClient = FMLCommonHandler.instance().getEffectiveSide().isClient();
    public static boolean isServer = !isClient;

    public static String getText(String str) {
        return StatCollector.translateToLocal(str);
    }

    public static void onBaubleWearEvent(IEventHandler<BaubleEvent> handler) {
        defWear.add(handler);
    }

    public static void onBaubleDisrobeEvent(IEventHandler<BaubleEvent> handler) {
        defDisrobe.add(handler);
    }

    public static void onBaubleWearPostEvent(IEventHandler<BaublePostEvent> handler) {
        defWearPost.add(handler);
    }

    public static void onBaubleDisrobePostEvent(IEventHandler<BaublePostEvent> handler) {
        defDisrobePost.add(handler);
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
        if (item == null) return false;
        for (ItemStack item1 : set) {
            if (item1 != null && item1.getItem() == item.getItem() && item1.getItemDamage() == item.getItemDamage()){
                return false;
            }
        }
        return true;
    }

}

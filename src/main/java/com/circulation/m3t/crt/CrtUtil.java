package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.Function;
import cpw.mods.fml.common.registry.GameData;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.MMM;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.*;

@ZenClass(M3TCrtAPI.CrtClass + "Util")
public class CrtUtil {

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

    @ZenMethod
    public static String createString(Object... objects){
        StringBuilder s = new StringBuilder();
        for (Object object : objects) {
            s.append(object);
        }
        return s.toString();
    }

    @ZenMethod
    public static Boolean isServer(){
        return Function.isServer;
    }

}

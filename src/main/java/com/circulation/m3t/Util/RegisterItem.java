package com.circulation.m3t.Util;

import com.circulation.m3t.hander.BaublesRegisterHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class RegisterItem {

    public static void register(){
        BaublesRegisterHandler.register();
    }

    private static void registerItem(Item item){GameRegistry.registerItem(item,item.getUnlocalizedName());}
}

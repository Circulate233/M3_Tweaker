package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.hander.M3TBaublesSuitHandler;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.player.IPlayer;
import net.minecraft.item.Item;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "BaublesSuit")
public class CrtBaublesSuitHandler extends M3TBaublesSuitHandler {

    @ZenMethod
    public static int getSuitQuantity(String suitName, IPlayer player){
        return getSuitQuantity(suitName, MineTweakerMC.getPlayer(player));
    }

    @ZenMethod
    public static boolean hasSuit(String name){
        return map.containsKey(name);
    }

    @ZenMethod
    public static CrtSuitHandler createSuit(IItemStack item){
        return new CrtSuitHandler(Item.itemRegistry.getNameForObject(MineTweakerMC.getItemStack(item).getItem()));
    }

    @ZenMethod
    public static CrtSuitHandler createSuit(String name){
        return new CrtSuitHandler(name);
    }

    @ZenMethod
    public static CrtTagSuitHandler createTagSuit(String name){
        return new CrtTagSuitHandler(name);
    }

    @ZenMethod
    public static CrtScatteredSuitHandler createScatteredSuit(String name){
        return new CrtScatteredSuitHandler(name);
    }
}

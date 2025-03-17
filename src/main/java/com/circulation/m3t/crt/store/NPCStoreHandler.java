package com.circulation.m3t.crt.store;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import com.circulation.m3t.hander.NPCHandler;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import project.studio.manametalmod.core.Icommodity;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenClass(M3TCrtAPI.CrtClass + "NPCStore")
public class NPCStoreHandler implements M3TCrtReload {

    @ZenMethod
    public static void addItem(int typeID,IItemStack item,int Money){
        NPCHandler.addItem(typeID,new Icommodity(MineTweakerMC.getItemStack(item),Money));
    }

    @ZenMethod
    public static void addItems(int typeID,IItemStack[] items,int[] Money){
        List<Icommodity> list = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            list.add(new Icommodity(MineTweakerMC.getItemStack(items[i]),Money[i]));
        }
        NPCHandler.addItem(typeID,list.toArray(new Icommodity[0]));
    }

    @ZenMethod
    public static void removeItems(int typeID, IItemStack[] items){
        NPCHandler.removeItem(typeID,MineTweakerMC.getItemStacks(items));
    }

    @ZenMethod
    public static void removeItem(int typeID, IItemStack stack){
        NPCHandler.removeItem(typeID,MineTweakerMC.getItemStack(stack));
    }

    @Override
    public void postReload() {

    }

    @Override
    public void reload() {
        NPCHandler.addMap.clear();
        NPCHandler.removeMap.clear();
    }



}

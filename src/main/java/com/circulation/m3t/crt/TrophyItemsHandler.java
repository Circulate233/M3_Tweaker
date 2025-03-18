package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ZenClass(M3TCrtAPI.CrtClass + "TrophyItem")
public class TrophyItemsHandler implements M3TCrtReload {

    private static Random rand = new Random();
    public static final List<TrophyItem> addTrophyItems = new ArrayList<>();
    public static final List<TrophyItem> addTrophyItemsLv2 = new ArrayList<>();


    @Override
    public void postReload() {
    }

    @Override
    public void reload() {
        addTrophyItems.clear();
        addTrophyItemsLv2.clear();
    }

    @ZenMethod
    public static void addItem(IItemStack iitem, int weight,boolean isLv2) {
        addItem(iitem,weight,iitem.getAmount(),iitem.getAmount(),isLv2);
    }

    @ZenMethod
    public static void addItem(IItemStack iitem, int weight,int min,int max,boolean isLv2) {
        ItemStack item = MineTweakerMC.getItemStack(iitem);
        if (item == null)return;
        if (isLv2){
            for (int i = 0; i < weight; i++) {
                addTrophyItemsLv2.add(new TrophyItem(item,min,max));
            }
        } else {
            for (int i = 0; i < weight; i++) {
                addTrophyItems.add(new TrophyItem(item,min,max));
            }
        }
    }

    public static class TrophyItem {
        public ItemStack output;
        public int min;
        public int max;
        public boolean chance;

        public TrophyItem(ItemStack output,int min,int max) {
            this.output = output;
            this.min = min;
            this.max = max;
            this.chance = min < max;
        }
    }

}

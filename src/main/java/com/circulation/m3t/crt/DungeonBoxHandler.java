package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.core.WeightedItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.circulation.m3t.Util.Function.noHasItem;
import static project.studio.manametalmod.ManaMetalAPI.*;

@ZenClass(M3TCrtAPI.CrtClass + "DungeonBox")
public class DungeonBoxHandler implements M3TCrtReload {

    @Override
    public void postReload() {
        for (DungeonBox value : DungeonBox.values()) {
            value.init();
            value.reloadLoot();
        }
    }

    @Override
    public void reload() {
        for (DungeonBox value : DungeonBox.values()) {
            value.clear();
        }
    }

    /**
     * 从对应等级的奖池中删去物品
     * @param iitem 物品,支持meta
     * @param weight 权重，但是我也不知道他是怎么判断的（？）
     * @param min 最少堆叠数
     * @param max 最大堆叠数，需要大于min，否则不工作
     * @param ordinal 宝箱序号
     */
    @ZenMethod
    public static void addItem(IItemStack iitem, int weight, int min, int max, int ordinal) {
        ItemStack item = MineTweakerMC.getItemStack(iitem);
        if (item == null || min > max)return;
        DungeonBox.getInstance(ordinal).addItems.add(new WeightedItemStack(item,min,max - min,weight,item.getItemDamage()));
    }

    /**
     * 向对应等级的奖池中加入物品
     * @param iitem 物品,支持meta
     * @param weight 权重，但是我也不知道他是怎么判断的（？）
     * @param min 最少堆叠数
     * @param max 最大堆叠数，需要大于min，否则不工作
     * @param name 宝箱名称
     */
    @ZenMethod
    public static void addItem(IItemStack iitem, int weight, int min, int max,String name) {
        ItemStack item = MineTweakerMC.getItemStack(iitem);
        if (item == null || min > max)return;
        var i = DungeonBox.getInstance(name);
        if (i == null)return;
        i.addItems.add(new WeightedItemStack(item,min,max - min,weight,item.getItemDamage()));
    }

    /**
     * 从对应等级的奖池中删去物品
     * @param iitem 物品,支持meta
     * @param ordinal 宝箱序号
     */
    @ZenMethod
    public static void removaItem(IItemStack iitem,int ordinal){
        ItemStack item = MineTweakerMC.getItemStack(iitem);
        if (item == null)return;
        DungeonBox.getInstance(ordinal).removeItems.add(item);
    }

    /**
     * 从对应等级的奖池中删去物品
     * @param iitem 物品,支持meta
     * @param name 宝箱名称
     */
    @ZenMethod
    public static void removaItem(IItemStack iitem,String name){
        ItemStack item = MineTweakerMC.getItemStack(iitem);
        if (item == null)return;
        var i = DungeonBox.getInstance(name.toUpperCase());
        if (i == null)return;
        i.removeItems.add(item);
    }

    private enum DungeonBox{
        WOOD(DungeonItemsWood),
        WEAPON(DungeonItemsWeapon),
        QUARTZ(DungeonItemsQuartz),
        HEAVY(DungeonItemsHeavy),
        STURDY(DungeonItemsSturdy),
        PRECIOUST1(DungeonItemsPreciousT1),
        PRECIOUST2(DungeonItemsPreciousT2),
        PRECIOUST3(DungeonItemsPreciousT3);

        private final List<WeightedItemStack> addItems = new ArrayList<>();
        private final Set<ItemStack> removeItems = new HashSet<>();
        private final List<WeightedItemStack> def = new ArrayList<>();
        private final List<WeightedItemStack> Loot;
        private boolean init;

        DungeonBox(List<WeightedItemStack> list){
            this.Loot = list;
            this.init = false;
        }

        private static DungeonBox getInstance(String name){
            return DungeonBox.valueOf(name);
        }

        private static DungeonBox getInstance(int i){
            return DungeonBox.values()[i < 0 ? 0 : i >= DungeonBox.values().length ? DungeonBox.values().length - 1 : i];
        }

        private void clear(){
            this.addItems.clear();
            this.removeItems.clear();
        }

        private void reloadLoot(){
            this.Loot.clear();
            this.def.stream()
                .filter(recipe -> noHasItem(this.removeItems, recipe.item))
                .forEach(this.Loot::add);
            this.Loot.addAll(this.addItems);
        }

        private void init(){
            if (!init){
                if (this.def.isEmpty()) {
                    this.def.addAll(this.Loot);
                    this.init = true;
                }
            }
        }
    }
}

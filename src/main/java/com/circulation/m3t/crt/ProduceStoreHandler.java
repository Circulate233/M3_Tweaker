package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.ManaMetalAPI;
import project.studio.manametalmod.core.Icommodity;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.*;

import static com.circulation.m3t.Util.Function.noHasItem;

@ZenClass(M3TCrtAPI.CrtClass + "ProduceStore")
public class ProduceStoreHandler implements M3TCrtReload {

    private static final Map<ProduceStore, Set<ItemStack>> removeMap = new HashMap<>();
    private static final Map<ProduceStore, List<Icommodity>> addMap = new HashMap<>();
    private static final Map<ProduceStore, List<Icommodity>> defMap = new HashMap<>();

    @ZenMethod
    public static void addStoreItem(String type,IItemStack items,int money) {
        addStoreItem(ProduceStore.valueOf(type),new Icommodity(MineTweakerMC.getItemStack(items),money));
    }

    @ZenMethod
    public static void addStoreItems(String type,IItemStack[] items,int[] money) {
        List<Icommodity> list = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            list.add(new Icommodity(MineTweakerMC.getItemStack(items[i]),money[i]));
        }
        addStoreItem(ProduceStore.valueOf(type),list.toArray(new Icommodity[0]));
    }

    public static void addStoreItem(ProduceStore type, Icommodity... items){
        final List<Icommodity> icommoditys = new ArrayList<>(Arrays.asList(items));
        if (addMap.containsKey(type)){
            final List<Icommodity> list = addMap.get(type);
            list.addAll(icommoditys);
            addMap.put(type,list);
        } else {
            addMap.put(type,icommoditys);
        }
    }

    @ZenMethod
    public static void removeStoreItem(String type,IItemStack items) {
        removeStoreItem(ProduceStore.valueOf(type),MineTweakerMC.getItemStack(items));
    }

    @ZenMethod
    public static void removeStoreItems(String type,IItemStack[] items) {
        removeStoreItem(ProduceStore.valueOf(type),MineTweakerMC.getItemStacks(items));
    }

    public static void removeStoreItem(ProduceStore type, ItemStack... items){
        final Set<ItemStack> items1 = new HashSet<>(Arrays.asList(items));
        if (removeMap.containsKey(type)){
            final Set<ItemStack> set = removeMap.get(type);
            set.addAll(items1);
            removeMap.put(type,set);
        } else {
            removeMap.put(type,items1);
        }
    }

    public void reload(){
        addMap.clear();
        removeMap.clear();
    }

    public void postReload(){
        for (ProduceStore value : ProduceStore.values()) {
            if (defMap.get(value) == null){
                final List<Icommodity> list = new ArrayList<>(value.getStore());
                defMap.put(value,list);
            }
            List<Icommodity> list = new ArrayList<>();
            if (removeMap.containsKey(value)) {
                defMap.get(value).forEach(icommodity -> {
                    if (noHasItem(Arrays.asList(removeMap.get(value).toArray(new ItemStack[0])),icommodity.getItem())){
                        list.add(icommodity);
                    }
                });
            } else {
                list.addAll(defMap.get(value));
            }
            if (addMap.containsKey(value)){
                list.addAll(addMap.get(value));
            }
            final List<Icommodity> store = value.getStore();
            store.clear();
            store.addAll(list);
        }
    }

    public enum ProduceStore {
        Mining,
        Farmer,
        Fishing,
        Beekeeping,
        Dragon,
        GemCraft,
        Casting,
        Cooking,
        Tailor,
        Brewing;

        public List<Icommodity> getStore() {
            switch (this) {
                case Mining:
                    return ManaMetalAPI.Mine_Store;
                case Farmer:
                    return ManaMetalAPI.Farm_Store;
                case Fishing:
                    return ManaMetalAPI.Fish_Store;
                case Beekeeping:
                    return ManaMetalAPI.Beekeeping_Store;
                case Dragon:
                    return ManaMetalAPI.Dragon_Store;
                case GemCraft:
                    return ManaMetalAPI.Gem_Store;
                case Casting:
                    return ManaMetalAPI.Forge_Store;
                case Cooking:
                    return ManaMetalAPI.Cooking_Store;
                case Tailor:
                    return ManaMetalAPI.Textile_Store;
                case Brewing:
                    return ManaMetalAPI.brewing_Store;
            }
            return java.util.Collections.emptyList();
        }

        public static List<Icommodity> getStore(String name){
            return ProduceStore.valueOf(name).getStore();
        }
    }
}

package com.circulation.m3t.crt.store;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.ManaMetalAPI;
import project.studio.manametalmod.core.Icommodity;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.circulation.m3t.Util.Function.noHasItem;

@ZenClass(M3TCrtAPI.CrtClass + "ProduceStore")
public class ProduceStoreHandler implements M3TCrtReload {

    private static final Map<ProduceStore, Set<ItemStack>> removeMap = new HashMap<>();
    private static final Map<ProduceStore, List<Icommodity>> addMap = new HashMap<>();
    private static final Map<ProduceStore, List<Icommodity>> defMap = new HashMap<>();

    @ZenMethod
    public static void addStoreItem(String type,IItemStack items,int money) {
        addStoreItem(ProduceStore.valueOf(type.toUpperCase()),new Icommodity(MineTweakerMC.getItemStack(items),money));
    }

    @ZenMethod
    public static void addStoreItems(String type,IItemStack[] items,int[] money) {
        List<Icommodity> list = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            list.add(new Icommodity(MineTweakerMC.getItemStack(items[i]),money[i]));
        }
        addStoreItem(ProduceStore.valueOf(type.toUpperCase()),list.toArray(new Icommodity[0]));
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
        removeStoreItem(ProduceStore.valueOf(type.toUpperCase()),MineTweakerMC.getItemStack(items));
    }

    @ZenMethod
    public static void removeStoreItems(String type,IItemStack[] items) {
        removeStoreItem(ProduceStore.valueOf(type.toUpperCase()),MineTweakerMC.getItemStacks(items));
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
                    if (noHasItem(removeMap.get(value),icommodity.getItem())){
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
        MINING,
        FARMER,
        FISHING,
        BEEKEEPING,
        DRAGON,
        GEMCRAFT,
        CASTING,
        COOKING,
        TAILOR,
        BREWING;

        public List<Icommodity> getStore() {
            return switch (this) {
                case MINING -> ManaMetalAPI.Mine_Store;
                case FARMER -> ManaMetalAPI.Farm_Store;
                case FISHING -> ManaMetalAPI.Fish_Store;
                case BEEKEEPING -> ManaMetalAPI.Beekeeping_Store;
                case DRAGON -> ManaMetalAPI.Dragon_Store;
                case GEMCRAFT -> ManaMetalAPI.Gem_Store;
                case CASTING -> ManaMetalAPI.Forge_Store;
                case COOKING -> ManaMetalAPI.Cooking_Store;
                case TAILOR -> ManaMetalAPI.Textile_Store;
                case BREWING -> ManaMetalAPI.brewing_Store;
            };
        }

        public static List<Icommodity> getStore(String name){
            return ProduceStore.valueOf(name.toUpperCase()).getStore();
        }
    }
}

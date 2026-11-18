package com.circulation.m3t.crt.store;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;
import static com.circulation.m3t.crt.CrtUtil.requireItemStacks;

import java.util.List;
import java.util.Locale;

import net.minecraft.item.ItemStack;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.reload.ItemListChanges;
import com.circulation.m3t.reload.M3TCrtReload;

import minetweaker.api.item.IItemStack;
import project.studio.manametalmod.ManaMetalAPI;
import project.studio.manametalmod.core.Icommodity;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "ProduceStore")
public class ProduceStoreHandler implements M3TCrtReload {

    private static final ProduceStore[] STORES = ProduceStore.values();

    @ZenMethod
    public static void addStoreItem(String type, IItemStack item, int money) {
        ProduceStore.fromName(type).changes.add(new Icommodity(requireItemStack(item), money));
    }

    @ZenMethod
    public static void addStoreItems(String type, IItemStack[] items, int[] money) {
        addStoreItem(ProduceStore.fromName(type), StoreItems.priced(items, money));
    }

    public static void addStoreItem(ProduceStore type, Icommodity... items) {
        type.changes.addAll(items);
    }

    @ZenMethod
    public static void removeStoreItem(String type, IItemStack item) {
        ProduceStore.fromName(type).changes.remove(requireItemStack(item));
    }

    @ZenMethod
    public static void removeStoreItems(String type, IItemStack[] items) {
        removeStoreItem(ProduceStore.fromName(type), requireItemStacks(items));
    }

    public static void removeStoreItem(ProduceStore type, ItemStack... items) {
        for (ItemStack item : items) {
            type.changes.remove(item);
        }
    }

    @Override
    public void reload() {
        for (ProduceStore type : STORES) {
            type.changes.clearChanges();
        }
    }

    @Override
    public void postReload() {
        for (ProduceStore type : STORES) {
            type.changes.applyTo(type.getStore());
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

        private final ItemListChanges<Icommodity> changes = new ItemListChanges<>(Icommodity::getItem);

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

        public static List<Icommodity> getStore(String name) {
            return fromName(name).getStore();
        }

        private static ProduceStore fromName(String name) {
            return valueOf(name.toUpperCase(Locale.ROOT));
        }
    }
}

package com.circulation.m3t.store;

import static com.circulation.m3t.Util.Function.getItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.circulation.m3t.M3Tweaker;
import com.circulation.m3t.reload.ItemRemovals;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import project.studio.manametalmod.core.Icommodity;
import project.studio.manametalmod.npc.NpcStoreType;

public final class NpcStock {

    private static final NpcStoreType[] STORE_TYPES = NpcStoreType.values();
    private static final Comparator<Icommodity> BY_ITEM_ID = Comparator.comparingInt(
        commodity -> Item.getIdFromItem(
            commodity.getItem()
                .getItem()));
    private static final Reference2ObjectOpenHashMap<NpcStoreType, Changes> BY_STORE = new Reference2ObjectOpenHashMap<>();

    private NpcStock() {}

    public static void removeItem(NpcStoreType type, ItemStack... items) {
        if (items.length == 0) return;
        ItemRemovals removals = changes(type).removals;
        for (ItemStack stack : items) {
            removals.add(stack);
        }
    }

    public static void removeItem(NpcStoreType type, Item... items) {
        if (items.length == 0) return;
        ItemRemovals removals = changes(type).removals;
        for (Item item : items) {
            removals.add(new ItemStack(item));
        }
    }

    public static void removeItem(NpcStoreType type, String... items) {
        for (String id : items) {
            try {
                ItemStack stack = parseItem(id);
                changes(type).removals.add(stack);
            } catch (IllegalArgumentException e) {
                M3Tweaker.logger.warn("Invalid NPC removal '{}': {}", id, e.getMessage());
            }
        }
    }

    public static void removeItem(int typeID, ItemStack... items) {
        removeItem(storeType(typeID), items);
    }

    public static void removeItem(int typeID, Item... items) {
        removeItem(storeType(typeID), items);
    }

    public static void removeItem(int typeID, String... items) {
        removeItem(storeType(typeID), items);
    }

    public static void addItem(NpcStoreType type, Icommodity... items) {
        if (items.length == 0) return;
        ObjectArrayList<Icommodity> additions = changes(type).additions;
        additions.ensureCapacity(additions.size() + items.length);
        for (Icommodity commodity : items) {
            requireItem(commodity.getItem());
            additions.add(commodity);
        }
    }

    public static void addItem(NpcStoreType type, Object[][] entries) {
        for (int i = 0; i < entries.length; i++) {
            try {
                Icommodity commodity = parseCommodity(entries[i]);
                changes(type).additions.add(commodity);
            } catch (IllegalArgumentException e) {
                M3Tweaker.logger.warn("Invalid NPC commodity at index {}: {}", i, e.getMessage());
            }
        }
    }

    public static void addItem(NpcStoreType type, String... entries) {
        for (String entry : entries) {
            try {
                Icommodity commodity = parseCommodity(entry);
                changes(type).additions.add(commodity);
            } catch (IllegalArgumentException e) {
                M3Tweaker.logger.warn("Invalid NPC commodity '{}': {}", entry, e.getMessage());
            }
        }
    }

    public static void addItem(int typeID, Icommodity... items) {
        addItem(storeType(typeID), items);
    }

    public static void addItem(int typeID, Object[][] entries) {
        addItem(storeType(typeID), entries);
    }

    public static void addItem(int typeID, String... entries) {
        addItem(storeType(typeID), entries);
    }

    public static void clearChanges() {
        BY_STORE.clear();
    }

    public static void sortAdditions() {
        for (Changes changes : BY_STORE.values()) {
            changes.additions.sort(BY_ITEM_ID);
        }
    }

    public static List<Icommodity> apply(NpcStoreType type, List<Icommodity> original) {
        Changes changes = BY_STORE.get(type);
        if (changes == null) return original;

        List<Icommodity> stock = new ObjectArrayList<>(original.size() + changes.additions.size());
        if (changes.removals.isEmpty()) {
            stock.addAll(original);
        } else {
            for (Icommodity commodity : original) {
                ItemStack item = commodity == null ? null : commodity.getItem();
                if (item == null || item.getItem() == null) {
                    M3Tweaker.logger.warn("Cannot match an NPC item removal against an empty commodity in {}", type);
                } else if (!changes.removals.contains(item)) {
                    stock.add(commodity);
                }
            }
        }
        stock.addAll(changes.additions);
        return stock;
    }

    private static NpcStoreType storeType(int id) {
        if (id < 0 || id >= STORE_TYPES.length) {
            throw new IllegalArgumentException("Unknown NPC store ID " + id);
        }
        return STORE_TYPES[id];
    }

    private static Changes changes(NpcStoreType type) {
        return BY_STORE.computeIfAbsent(Objects.requireNonNull(type, "NPC store type"), ignored -> new Changes());
    }

    private static Icommodity parseCommodity(Object[] fields) {
        if (fields == null || fields.length < 2 || !(fields[1] instanceof Integer price)) {
            throw new IllegalArgumentException("Expected an item and an integer price");
        }
        ItemStack item;
        if (fields[0] instanceof String id) {
            item = parseItem(id);
        } else if (fields[0] instanceof ItemStack stack) {
            item = requireItem(stack);
        } else {
            throw new IllegalArgumentException("Expected an item ID or stack");
        }
        if (fields.length < 3) return new Icommodity(item, price);
        if (!(fields[2] instanceof Boolean limit)) {
            throw new IllegalArgumentException("Expected a boolean limit flag");
        }
        if (fields.length < 4) return new Icommodity(item, price, limit);
        if (!(fields[3] instanceof Integer maxCount)) {
            throw new IllegalArgumentException("Expected an integer maximum count");
        }
        return new Icommodity(item, price, limit, maxCount);
    }

    private static Icommodity parseCommodity(String entry) {
        if (entry == null) throw new IllegalArgumentException("Expected a commodity string");
        String[] fields = entry.split(";");
        if (fields.length < 2) throw new IllegalArgumentException("Expected item;price");

        ItemStack item = parseItem(fields[0]);
        int price = Integer.parseInt(fields[1]);
        // The string format reads a limit flag with four fields and a maximum count with five.
        if (fields.length < 4) return new Icommodity(item, price);
        boolean limit = Integer.parseInt(fields[2]) != 1;
        if (fields.length < 5) return new Icommodity(item, price, limit);
        return new Icommodity(item, price, limit, Integer.parseInt(fields[3]));
    }

    private static ItemStack parseItem(String id) {
        ItemStack stack = getItemStack(id);
        if (stack == null) throw new IllegalArgumentException("Unknown item ID " + id);
        return stack;
    }

    private static ItemStack requireItem(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            throw new IllegalArgumentException("Expected a valid item stack");
        }
        return stack;
    }

    private static final class Changes {

        private final ObjectArrayList<Icommodity> additions = new ObjectArrayList<>();
        private final ItemRemovals removals = new ItemRemovals();
    }
}

package com.circulation.m3t.reload;

import java.util.Map;

import net.minecraft.item.Item;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMaps;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;

public final class ItemIntChanges {

    private final Reference2IntOpenHashMap<Item> additions = new Reference2IntOpenHashMap<>(1);
    private final ReferenceOpenHashSet<Item> removals = new ReferenceOpenHashSet<>(1);
    private final Reference2ObjectOpenHashMap<Item, Integer> replaced = new Reference2ObjectOpenHashMap<>(1);

    public void add(Item item, int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Machine recipe value must be positive");
        }
        additions.put(item, value);
    }

    public void remove(Item item) {
        removals.add(item);
    }

    public void clearChanges() {
        additions.clear();
        removals.clear();
    }

    public void applyTo(Map<Item, Integer> target) {
        for (Reference2ObjectMap.Entry<Item, Integer> entry : Reference2ObjectMaps.fastIterable(replaced)) {
            if (entry.getValue() == null) {
                target.remove(entry.getKey());
            } else {
                target.put(entry.getKey(), entry.getValue());
            }
        }
        replaced.clear();
        for (Item item : removals) {
            Integer previous = target.remove(item);
            if (previous != null) {
                replaced.put(item, previous);
            }
        }
        for (Reference2IntMap.Entry<Item> entry : Reference2IntMaps.fastIterable(additions)) {
            Item item = entry.getKey();
            Integer previous = target.put(item, entry.getIntValue());
            replaced.putIfAbsent(item, previous);
        }
    }
}

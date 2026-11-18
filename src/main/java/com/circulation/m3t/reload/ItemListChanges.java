package com.circulation.m3t.reload;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import net.minecraft.item.ItemStack;

import com.circulation.m3t.M3Tweaker;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public final class ItemListChanges<T> {

    private final Function<T, ItemStack> itemOf;
    private final ObjectArrayList<T> additions = new ObjectArrayList<>();
    private final ItemRemovals removals = new ItemRemovals();
    private List<T> defaults;

    public ItemListChanges(Function<T, ItemStack> itemOf) {
        this.itemOf = itemOf;
    }

    public void add(T entry) {
        additions.add(entry);
    }

    public void addAll(T[] entries) {
        additions.ensureCapacity(additions.size() + entries.length);
        Collections.addAll(additions, entries);
    }

    public void remove(ItemStack item) {
        removals.add(item);
    }

    public void clearChanges() {
        additions.clear();
        removals.clear();
    }

    public void applyTo(List<T> target) {
        if (additions.isEmpty() && removals.isEmpty()) {
            if (defaults != null) {
                target.clear();
                target.addAll(defaults);
                defaults = null;
            }
            return;
        }
        if (defaults == null) {
            defaults = new ObjectArrayList<>(target);
        }
        target.clear();
        if (removals.isEmpty()) {
            target.addAll(defaults);
        } else {
            for (T entry : defaults) {
                ItemStack stack = entry == null ? null : itemOf.apply(entry);
                if (stack == null || stack.getItem() == null) {
                    M3Tweaker.logger.warn("Cannot match an item removal against an entry without an item: {}", entry);
                } else if (!removals.contains(stack)) {
                    target.add(entry);
                }
            }
        }
        target.addAll(additions);
    }
}

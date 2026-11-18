package com.circulation.m3t.reload;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

public final class ItemRemovals {

    private Reference2ObjectOpenHashMap<Item, ObjectList<ItemStack>> byItem;

    public void add(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            throw new IllegalArgumentException("Expected a valid item to remove");
        }
        if (byItem == null) {
            byItem = new Reference2ObjectOpenHashMap<>(1);
        }
        ObjectList<ItemStack> variants = byItem.computeIfAbsent(stack.getItem(), ignored -> new ObjectArrayList<>(1));
        if (!matches(variants, stack)) {
            variants.add(stack);
        }
    }

    public boolean contains(ItemStack stack) {
        ObjectList<ItemStack> variants = byItem == null ? null : byItem.get(stack.getItem());
        return variants != null && matches(variants, stack);
    }

    private static boolean matches(ObjectList<ItemStack> variants, ItemStack stack) {
        for (int i = 0, size = variants.size(); i < size; i++) {
            if (variants.get(i)
                .isItemEqual(stack)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return byItem == null;
    }

    public void clear() {
        byItem = null;
    }
}

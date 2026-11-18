package com.circulation.m3t.reload;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.junit.Test;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class ItemListChangesTest {

    @Test
    public void removesMatchingDefaultsAndAppendsScriptEntriesInOrder() {
        Item item = new Item() {

            @Override
            public int getDamage(ItemStack stack) {
                return 0;
            }
        };
        ItemStack before = new ItemStack(new Item());
        ItemStack removed = new ItemStack(item, 1, 3);
        ItemStack anotherVariant = new ItemStack(item, 1, 4);
        ItemStack after = new ItemStack(new Item());
        ItemStack replacement = removed.copy();
        ItemStack addition = new ItemStack(new Item());
        List<ItemStack> defaults = ObjectArrayList.of(before, removed, anotherVariant, after);
        List<ItemStack> target = new ObjectArrayList<>(defaults);
        ItemListChanges<ItemStack> changes = new ItemListChanges<>(stack -> stack);

        ItemStack removal = removed.copy();
        removal.stackSize += 2;
        removal.setTagCompound(new NBTTagCompound());
        removal.getTagCompound()
            .setString("label", "not part of matching");
        changes.remove(removal);
        changes.addAll(new ItemStack[] { replacement, addition });
        changes.applyTo(target);
        assertEquals(ObjectArrayList.of(before, anotherVariant, after, replacement, addition), target);

        changes.clearChanges();
        changes.applyTo(target);
        assertEquals(defaults, target);
    }

    @Test
    public void scriptsDoNotBecomeDefaultsWhenTheOriginalListIsEmpty() {
        List<ItemStack> target = new ObjectArrayList<>();
        ItemListChanges<ItemStack> changes = new ItemListChanges<>(stack -> stack);
        ItemStack first = new ItemStack(new Item());
        ItemStack second = new ItemStack(new Item());

        changes.add(first);
        changes.applyTo(target);
        assertEquals(ObjectArrayList.of(first), target);

        changes.clearChanges();
        changes.add(second);
        changes.applyTo(target);
        assertEquals(ObjectArrayList.of(second), target);

        changes.clearChanges();
        changes.applyTo(target);
        assertTrue(target.isEmpty());
    }
}

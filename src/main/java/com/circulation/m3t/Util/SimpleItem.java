package com.circulation.m3t.Util;

import com.github.bsideup.jabel.Desugar;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Desugar
public record SimpleItem(Item item, int meta, NBTTagCompound nbt) {

    private static final NBTTagCompound NullNbt = new NBTTagCompound() {
        @Override
        public boolean equals(Object nbt) {
            return nbt == this;
        }

        @Override
        public int hashCode() {
            return Integer.MIN_VALUE;
        }
    };

    public SimpleItem(ItemStack stack) {
        this(stack.getItem(), stack.getItemDamage(), stack.getTagCompound());
    }

    public static final SimpleItem empty = new SimpleItem(null, 0, null);
    private static final Reference2ObjectMap<Item, Int2ObjectMap<Map<NBTTagCompound, SimpleItem>>> chane = Reference2ObjectMaps.synchronize(new Reference2ObjectOpenHashMap<>());

    public static SimpleItem getInstance(ItemStack stack) {
        if (stack == null) return empty;
        if (stack.getItem() == null) return empty;
        final var m = chane.computeIfAbsent(stack.getItem(), i -> Int2ObjectMaps.synchronize(new Int2ObjectOpenHashMap<>()))
            .computeIfAbsent(stack.getItemDamage(), i -> new ConcurrentHashMap<>());
        var nbt = stack.getTagCompound();
        if (nbt == null) nbt = NullNbt;
        var o = m.get(nbt);
        if (o == null) {
            o = new SimpleItem(stack);
            m.put(nbt, o);
        }
        return o;
    }

    public static SimpleItem getNoNBTInstance(ItemStack stack) {
        if (stack == null) return empty;
        if (stack.getItem() == null) return empty;
        final var m = chane.computeIfAbsent(stack.getItem(), i -> Int2ObjectMaps.synchronize(new Int2ObjectOpenHashMap<>()))
            .computeIfAbsent(stack.getItemDamage(), i -> new ConcurrentHashMap<>());
        var nbt = NullNbt;
        var o = m.get(NullNbt);
        if (o == null) {
            o = new SimpleItem(stack);
            m.put(NullNbt, o);
        }
        return o;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SimpleItem that = (SimpleItem) o;
        return meta == that.meta && Objects.equals(item, that.item) && Objects.equals(nbt, that.nbt);
    }

    @Override
    public int hashCode() {
        int result = item.hashCode();
        result = 31 * result + meta;
        result = 31 * result + (nbt != null ? nbt.hashCode() : 0);
        return result;
    }

}

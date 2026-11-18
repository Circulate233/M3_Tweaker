package com.circulation.m3t.crt;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;
import static project.studio.manametalmod.ManaMetalAPI.DungeonItemsHeavy;
import static project.studio.manametalmod.ManaMetalAPI.DungeonItemsPreciousT1;
import static project.studio.manametalmod.ManaMetalAPI.DungeonItemsPreciousT2;
import static project.studio.manametalmod.ManaMetalAPI.DungeonItemsPreciousT3;
import static project.studio.manametalmod.ManaMetalAPI.DungeonItemsQuartz;
import static project.studio.manametalmod.ManaMetalAPI.DungeonItemsSturdy;
import static project.studio.manametalmod.ManaMetalAPI.DungeonItemsWeapon;
import static project.studio.manametalmod.ManaMetalAPI.DungeonItemsWood;

import java.util.List;
import java.util.Locale;

import net.minecraft.item.ItemStack;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.reload.ItemListChanges;
import com.circulation.m3t.reload.M3TCrtReload;

import minetweaker.api.item.IItemStack;
import project.studio.manametalmod.core.WeightedItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "DungeonBox")
public class DungeonBoxHandler implements M3TCrtReload {

    private static final DungeonBox[] BOXES = DungeonBox.values();

    @ZenMethod
    public static void addItem(IItemStack iitem, int weight, int min, int max, int ordinal) {
        getBox(ordinal).changes.add(createLoot(iitem, weight, min, max));
    }

    @ZenMethod
    public static void addItem(IItemStack iitem, int weight, int min, int max, String name) {
        DungeonBox.valueOf(name).changes.add(createLoot(iitem, weight, min, max));
    }

    @ZenMethod
    public static void removaItem(IItemStack iitem, int ordinal) {
        getBox(ordinal).changes.remove(requireItemStack(iitem));
    }

    @ZenMethod
    public static void removaItem(IItemStack iitem, String name) {
        DungeonBox.valueOf(name.toUpperCase(Locale.ROOT)).changes.remove(requireItemStack(iitem));
    }

    private static WeightedItemStack createLoot(IItemStack input, int weight, int min, int max) {
        if (min > max) throw new IllegalArgumentException("Minimum loot count must not exceed maximum loot count");
        ItemStack item = requireItemStack(input);
        return new WeightedItemStack(item, min, max - min, weight, item.getItemDamage());
    }

    private static DungeonBox getBox(int ordinal) {
        return BOXES[Math.min(Math.max(ordinal, 0), BOXES.length - 1)];
    }

    @Override
    public void reload() {
        for (DungeonBox box : BOXES) {
            box.changes.clearChanges();
        }
    }

    @Override
    public void postReload() {
        for (DungeonBox box : BOXES) {
            box.changes.applyTo(box.loot);
        }
    }

    private enum DungeonBox {

        WOOD(DungeonItemsWood),
        WEAPON(DungeonItemsWeapon),
        QUARTZ(DungeonItemsQuartz),
        HEAVY(DungeonItemsHeavy),
        STURDY(DungeonItemsSturdy),
        PRECIOUST1(DungeonItemsPreciousT1),
        PRECIOUST2(DungeonItemsPreciousT2),
        PRECIOUST3(DungeonItemsPreciousT3);

        private final List<WeightedItemStack> loot;
        private final ItemListChanges<WeightedItemStack> changes = new ItemListChanges<>(entry -> entry.item);

        DungeonBox(List<WeightedItemStack> loot) {
            this.loot = loot;
        }
    }
}

package com.circulation.m3t.crt;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;
import static project.studio.manametalmod.ManaMetalAPI.GeneralLootT1;
import static project.studio.manametalmod.ManaMetalAPI.GeneralLootT2;
import static project.studio.manametalmod.ManaMetalAPI.GeneralLootT3;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.reload.ItemListChanges;
import com.circulation.m3t.reload.M3TCrtReload;

import minetweaker.api.item.IItemStack;
import project.studio.manametalmod.core.WeightedItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "TrophyItem")
public class TrophyItemsHandler implements M3TCrtReload {

    private static final TrophyItems[] TIERS = TrophyItems.values();

    @ZenMethod
    public static void addItem(IItemStack iitem, int weight, int min, int max, int level) {
        if (weight < 0 || min < 0 || min > max) {
            throw new IllegalArgumentException("Expected non-negative loot weight and count, with min <= max");
        }
        ItemStack item = requireItemStack(iitem);
        getTier(level).changes.add(new WeightedItemStack(item, min, max - min, weight, item.getItemDamage()));
    }

    @ZenMethod
    public static void removaItem(IItemStack iitem, int level) {
        getTier(level).changes.remove(requireItemStack(iitem));
    }

    private static TrophyItems getTier(int level) {
        return TIERS[Math.min(Math.max(level, 1), TIERS.length) - 1];
    }

    @Override
    public void reload() {
        for (TrophyItems tier : TIERS) {
            tier.changes.clearChanges();
        }
    }

    @Override
    public void postReload() {
        for (TrophyItems tier : TIERS) {
            tier.changes.applyTo(tier.loot);
        }
    }

    private enum TrophyItems {

        LV1(GeneralLootT1),
        LV2(GeneralLootT2),
        LV3(GeneralLootT3);

        private final List<WeightedItemStack> loot;
        private final ItemListChanges<WeightedItemStack> changes = new ItemListChanges<>(entry -> entry.item);

        TrophyItems(List<WeightedItemStack> loot) {
            this.loot = loot;
        }
    }
}

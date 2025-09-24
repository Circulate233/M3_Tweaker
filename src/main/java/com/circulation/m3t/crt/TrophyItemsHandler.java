package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.core.WeightedItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;
import java.util.Set;

import static com.circulation.m3t.Util.Function.noHasItem;
import static project.studio.manametalmod.ManaMetalAPI.GeneralLootT1;
import static project.studio.manametalmod.ManaMetalAPI.GeneralLootT2;
import static project.studio.manametalmod.ManaMetalAPI.GeneralLootT3;

@ZenClass(M3TCrtAPI.CrtClass + "TrophyItem")
public class TrophyItemsHandler implements M3TCrtReload {

    @Override
    public void postReload() {
        for (TrophyItems value : TrophyItems.values()) {
            value.init();
            value.reloadLoot();
        }
    }

    @Override
    public void reload() {
        for (TrophyItems value : TrophyItems.values()) {
            value.clear();
        }
    }

    /**
     * 向对应等级的奖池中加入物品
     *
     * @param iitem  物品,支持meta
     * @param weight 权重，但是我也不知道他是怎么判断的（？）
     * @param min    最少堆叠数
     * @param max    最大堆叠数，需要大于min，否则不工作
     * @param level  宝箱等级
     */
    @ZenMethod
    public static void addItem(IItemStack iitem, int weight, int min, int max, int level) {
        ItemStack item = MineTweakerMC.getItemStack(iitem);
        if (item == null || weight < 0 || min < 0 || min > max) return;
        TrophyItems.getInstance(level).addItems.add(new WeightedItemStack(item, min, max - min, weight, item.getItemDamage()));
    }

    /**
     * 从对应等级的奖池中删去物品
     *
     * @param iitem 物品,支持meta
     * @param level 宝箱等级
     */
    @ZenMethod
    public static void removaItem(IItemStack iitem, int level) {
        ItemStack item = MineTweakerMC.getItemStack(iitem);
        if (item == null) return;
        TrophyItems.getInstance(level).removeItems.add(item);
    }

    private enum TrophyItems {
        LV1(GeneralLootT1),
        LV2(GeneralLootT2),
        LV3(GeneralLootT3);

        private final List<WeightedItemStack> addItems = new ReferenceArrayList<>();
        private final Set<ItemStack> removeItems = new ReferenceOpenHashSet<>();
        private final List<WeightedItemStack> def = new ReferenceArrayList<>();
        private final List<WeightedItemStack> Loot;
        private boolean init;

        TrophyItems(List<WeightedItemStack> list) {
            this.Loot = list;
            this.init = false;
        }

        private static TrophyItems getInstance(int i) {
            return TrophyItems.values()[i < 1 ? 0 : i > 3 ? 2 : i - 1];
        }

        private void clear() {
            this.addItems.clear();
            this.removeItems.clear();
        }

        private void reloadLoot() {
            this.Loot.clear();
            this.def.stream()
                .filter(recipe -> noHasItem(this.removeItems, recipe.item))
                .forEach(this.Loot::add);
            this.Loot.addAll(this.addItems);
        }

        private void init() {
            if (!init) {
                if (this.def.isEmpty()) {
                    this.def.addAll(this.Loot);
                    this.init = true;
                }
            }
        }
    }

}

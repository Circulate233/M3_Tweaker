package com.circulation.m3t.crt.store;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.core.Icommodity;
import project.studio.manametalmod.rpg.BossStore;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.*;

import static com.circulation.m3t.Util.Function.noHasItem;

@ZenClass(M3TCrtAPI.CrtClass + "BossStore")
public class BossStoreHandler implements M3TCrtReload {

    private static final List<Icommodity> addBossStore = new ArrayList<>();
    private static final Set<ItemStack> removeBossStore = new HashSet<>();

    private static final List<Icommodity> addBossStoreLV2 = new ArrayList<>();
    private static final Set<ItemStack> removeBossStoreLV2 = new HashSet<>();

    protected static List<Icommodity> def = new ArrayList<>();
    protected static List<Icommodity> defLV2 = new ArrayList<>();

    @ZenMethod
    public static void addItem(IItemStack item, int Money, boolean isLv2){
        if (isLv2) {
            addBossStoreLV2.add(new Icommodity(MineTweakerMC.getItemStack(item), Money));
        } else {
            addBossStore.add(new Icommodity(MineTweakerMC.getItemStack(item), Money));
            addBossStoreLV2.add(new Icommodity(MineTweakerMC.getItemStack(item), Money));
        }
    }

    @ZenMethod
    public static void addItems(IItemStack[] items,int[] Money, boolean isLv2){
        List<Icommodity> list = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            list.add(new Icommodity(MineTweakerMC.getItemStack(items[i]),Money[i]));
        }
        if (isLv2) {
            addBossStoreLV2.addAll(list);
        } else {
            addBossStore.addAll(list);
            addBossStoreLV2.addAll(list);
        }
    }

    @ZenMethod
    public static void removeItems(IItemStack[] items, boolean isLv2){
        if (isLv2) {
            removeBossStoreLV2.addAll(Arrays.asList(MineTweakerMC.getItemStacks(items)));
        } else {
            removeBossStore.addAll(Arrays.asList(MineTweakerMC.getItemStacks(items)));
            removeBossStoreLV2.addAll(Arrays.asList(MineTweakerMC.getItemStacks(items)));
        }
    }

    @ZenMethod
    public static void removeItem(IItemStack item, boolean isLv2){
        if (isLv2) {
            removeBossStoreLV2.add(MineTweakerMC.getItemStack(item));
        } else {
            removeBossStore.add(MineTweakerMC.getItemStack(item));
            removeBossStoreLV2.add(MineTweakerMC.getItemStack(item));
        }
    }

    @Override
    public void postReload() {
        if (def.isEmpty()) def.addAll(BossStore.Items1);
        if (defLV2.isEmpty()) defLV2.addAll(BossStore.Items2);

        List<Icommodity> list = new ArrayList<>();
        def.forEach(item -> {
            if (noHasItem(Arrays.asList(removeBossStore.toArray(new ItemStack[0])), item.getItem())) {
                list.add(item);
            }
        });
        list.addAll(addBossStore);
        BossStore.Items1.clear();
        BossStore.Items1.addAll(list);

        List<Icommodity> listLV2 = new ArrayList<>();
        defLV2.forEach(item -> {
            if (noHasItem(Arrays.asList(removeBossStoreLV2.toArray(new ItemStack[0])), item.getItem())) {
                listLV2.add(item);
            }
        });
        listLV2.addAll(addBossStoreLV2);
        BossStore.Items2.clear();
        BossStore.Items2.addAll(listLV2);
    }

    @Override
    public void reload() {
        addBossStore.clear();
        addBossStoreLV2.clear();
        removeBossStore.clear();
        removeBossStoreLV2.clear();
    }
}

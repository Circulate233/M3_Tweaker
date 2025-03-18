package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.ManaMetalAPI;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.*;

import static com.circulation.m3t.Util.Function.getItemStack;
import static com.circulation.m3t.Util.Function.noHasItem;

@ZenClass(M3TCrtAPI.CrtClass + "DungeonBox")
public class DungeonBoxHandler implements M3TCrtReload {

    private static List<ItemStack> addDungeonBoxs = new ArrayList<>();
    private static Set<ItemStack> removeDungeonBoxs = new HashSet<>();

    private static List<ItemStack> addDungeonBoxsLV2 = new ArrayList<>();
    private static Set<ItemStack> removeDungeonBoxsLV2 = new HashSet<>();

    protected static List<ItemStack> def;
    protected static List<ItemStack> defLV2;

    public static boolean BoxYes = true;
    public static boolean initialization = true;
    public static boolean clearYes = false;
    public static boolean clearYesLV2 = false;

    public static void addDungeonBox(ItemStack... item) {
        if (false){
            addDungeonBoxsLV2.addAll(Arrays.asList(item));
        } else {
            addDungeonBoxs.addAll(Arrays.asList(item));
        }
        if (!BoxYes) {BoxYes = true;}
    }

    public static void removeDungeonBox(ItemStack... item) {
        if (false){
            removeDungeonBoxsLV2.addAll(Arrays.asList(item));
        } else {
            removeDungeonBoxs.addAll(Arrays.asList(item));
        }
        if (!BoxYes) {BoxYes = true;}
    }

    private static boolean isClearYes(){
        if (false){
            if (clearYesLV2){
                clearYesLV2 = false;
                return false;
            }
        } else {
            if (clearYes){
                clearYes = false;
                return false;
            }
        }
        return true;
    }

    @ZenMethod
    public static void clear() {
        if (false){
            clearYesLV2 = true;
        } else {
            clearYes = true;
        }
        if (!BoxYes) {BoxYes = true;}
    }

    @ZenMethod
    public static void addDungeonBoxs(IItemStack[] item) {
        addDungeonBox(MineTweakerMC.getItemStacks(item));
    }

    @ZenMethod
    public static void removeDungeonBoxs(IItemStack[] item) {
        removeDungeonBox(MineTweakerMC.getItemStacks(item));
    }

    @ZenMethod
    public static void addDungeonBox(IItemStack item) {
        addDungeonBox(MineTweakerMC.getItemStack(item));
    }

    @ZenMethod
    public static void removeDungeonBox(IItemStack item) {
        removeDungeonBox(MineTweakerMC.getItemStack(item));
    }

    public static void addDungeonBox(String item) {
        addDungeonBox(getItemStack(item));
    }

    public static void removeDungeonBox(String item) {
        removeDungeonBox(getItemStack(item));
    }

    public static void registerDungeonBox() {
        if (initialization) {
            def = ManaMetalAPI.DungeonItems;
            defLV2 = ManaMetalAPI.DungeonItemsLV2;
            initialization = false;
        }

        List<ItemStack> list = new ArrayList<>();
        if (isClearYes()) {
            def.forEach(item -> {
                if (noHasItem(Arrays.asList(removeDungeonBoxs.toArray(new ItemStack[0])), item)) {
                    list.add(item);
                }
            });
        }
        list.addAll(addDungeonBoxs);
        ManaMetalAPI.DungeonItems.clear();
        ManaMetalAPI.DungeonItems.addAll(list);

//        List<ItemStack> listLV2 = new ArrayList<>();
//        if (isClearYes(true)) {
//            defLV2.forEach(item -> {
//                if (noHasItem(Arrays.asList(removeDungeonBoxsLV2.toArray(new ItemStack[0])), item)) {
//                    listLV2.add(item);
//                }
//            });
//        }
//        listLV2.addAll(addDungeonBoxsLV2);
//        ManaMetalAPI.DungeonItemsLV2.clear();
//        ManaMetalAPI.DungeonItemsLV2.addAll(listLV2);
//        addDungeonBoxsLV2 = new ArrayList<>();
//        removeDungeonBoxsLV2 = new HashSet<>();
    }

    @Override
    public void postReload() {

    }

    @Override
    public void reload() {
        addDungeonBoxs.clear();
        removeDungeonBoxs.clear();
        BoxYes = true;
    }
}

package com.circulation.m3t.hander;

import com.circulation.m3t.M3Tweaker;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.core.Icommodity;
import project.studio.manametalmod.npc.NpcStoreType;

import java.util.*;

import static com.circulation.m3t.Util.Function.*;

public class NPCHandler {

    public static Map<NpcStoreType, Set<ItemStack>> removeMap = new HashMap<>();
    public static Map<NpcStoreType, Set<Icommodity>> addMap = new HashMap<>();

    public static void removeItem(NpcStoreType type, ItemStack... items){
        final Set<ItemStack> items1 = new HashSet<>(Arrays.asList(items));
        if (removeMap.containsKey(type)){
            final Set<ItemStack> set = removeMap.get(type);
            set.addAll(items1);
            removeMap.put(type,set);
        } else {
            removeMap.put(type,items1);
        }
    }

    public static void removeItem(NpcStoreType type,Item... items){
        final Set<ItemStack> items1 = new HashSet<>();
        Arrays.asList(items).forEach(item -> items1.add(new ItemStack(item)));
        removeItem(type,items1.toArray(new ItemStack[0]));
    }

    public static void removeItem(NpcStoreType type,String... items){
        final Set<ItemStack> items1 = new HashSet<>();
        Arrays.asList(items).forEach(itemid -> {
            final ItemStack item = getItemStack(itemid);
            if (item == null) {
                M3Tweaker.logger.info("{} not a correct item ID", itemid);
                return;
            }
            final ItemStack item1 = item.splitStack(1);
            items1.add(item1);
        });
        if (!items1.isEmpty()) {
            removeItem(type, items1.toArray(new ItemStack[0]));
        }
    }

    public static void removeItem(int typeID,Item... items){
        NpcStoreType type = NpcStoreType.getTypeFromID(typeID);
        final Set<ItemStack> items1 = new HashSet<>();
        Arrays.asList(items).forEach(item -> items1.add(new ItemStack(item)));
        removeItem(type,items1.toArray(new ItemStack[0]));
    }

    public static void removeItem(int typeID,ItemStack... items){
        NpcStoreType type = NpcStoreType.getTypeFromID(typeID);
        final Set<ItemStack> items1 = new HashSet<>(Arrays.asList(items));
        removeItem(type,items1.toArray(new ItemStack[0]));
    }

    public static void removeItem(int typeID,String... items){
        NpcStoreType type = NpcStoreType.getTypeFromID(typeID);
        final Set<ItemStack> items1 = new HashSet<>();
        Arrays.asList(items).forEach(itemid -> {
            final ItemStack item = getItemStack(itemid);
            if (item == null) {
                com.circulation.m3t.M3Tweaker.logger.info("{} not a correct item ID", itemid);
                return;
            }
            final ItemStack item1 = item.splitStack(1);
            items1.add(item);
        });
        if (!items1.isEmpty()) {
            removeItem(type, items1.toArray(new ItemStack[0]));
        }
    }

    public static void addItem(NpcStoreType type, Icommodity... items){
        final Set<Icommodity> icommoditys = new HashSet<>(Arrays.asList(items));
        if (addMap.containsKey(type)){
            final Set<Icommodity> list = addMap.get(type);
            list.addAll(icommoditys);
            addMap.put(type,list);
        } else {
            addMap.put(type,icommoditys);
        }
    }

    public static void addItem(NpcStoreType type, Object[][] icommodityss) {
        final Set<Icommodity> icommoditys = new HashSet<>();
        Arrays.asList(icommodityss).forEach(icommodityO -> {
            Icommodity icommodity = new Icommodity();
            if (icommodityO[0] instanceof String) {
                icommodity.setItem(getItemStack((String) icommodityO[0]));
            }
            if (icommodityO[0] instanceof ItemStack) {
                icommodity.setItem((ItemStack) icommodityO[0]);
            }
            icommodity.setPrice((int) icommodityO[1]);
            if (icommodityO.length > 2) {
                icommodity.setLimit((boolean) icommodityO[2]);
                if (icommodityO.length > 3) {
                    icommodity.setMaxCount((int) icommodityO[3]);
                }
            }
            icommoditys.add(icommodity);
        });
        addItem(type, icommoditys.toArray(new Icommodity[0]));
    }

    public static void addItem(NpcStoreType type, String... icommodityss){
        final Set<Icommodity> icommoditys = new HashSet<>();
        Arrays.asList(icommodityss).forEach(icommodityS -> {
            Icommodity icommodity = new Icommodity();
            String[] parts = icommodityS.split(";");
            if (getItemStack(parts[0]) == null) {
                com.circulation.m3t.M3Tweaker.logger.info("{} not a correct item ID", parts[0]);
                return;
            }
            icommodity.setItem(getItemStack(parts[0]));
            icommodity.setPrice(Integer.parseInt(parts[1]));
            if (parts.length > 3){
                icommodity.setLimit(Integer.parseInt(parts[2]) != 1);
                if (parts.length > 4) {
                    icommodity.setMaxCount(Integer.parseInt(parts[3]));
                }
            }
            icommoditys.add(icommodity);
        });
        addItem(type,icommoditys.toArray(new Icommodity[0]));
    }

    public static void addItem(int typeID, Icommodity... items){
        NpcStoreType type = NpcStoreType.getTypeFromID(typeID);
        final Set<Icommodity> icommoditys = new HashSet<>(Arrays.asList(items));
        if (addMap.containsKey(type)){
            final Set<Icommodity> list = addMap.get(type);
            list.addAll(icommoditys);
            addMap.put(type,list);
        } else {
            addMap.put(type,icommoditys);
        }
    }

    public static void addItem(int typeID, Object[][] icommodityss) {
        NpcStoreType type = NpcStoreType.getTypeFromID(typeID);
        final Set<Icommodity> icommoditys = new HashSet<>();
        Arrays.asList(icommodityss).forEach(icommodityO -> {
            Icommodity icommodity = new Icommodity();
            if (icommodityO[0] instanceof String) {
                icommodity.setItem(getItemStack((String) icommodityO[0]));
            }
            if (icommodityO[0] instanceof ItemStack) {
                icommodity.setItem((ItemStack) icommodityO[0]);
            }
            icommodity.setPrice((int) icommodityO[1]);
            if (icommodityO.length > 2) {
                icommodity.setLimit((boolean) icommodityO[2]);
                if (icommodityO.length > 3) {
                    icommodity.setMaxCount((int) icommodityO[3]);
                }
            }
            icommoditys.add(icommodity);
        });
        addItem(type, icommoditys.toArray(new Icommodity[0]));
    }

    public static void addItem(int typeID, String... icommodityss){
        NpcStoreType type = NpcStoreType.getTypeFromID(typeID);
        final Set<Icommodity> icommoditys = new HashSet<>();
        Arrays.asList(icommodityss).forEach(icommodityS -> {
            Icommodity icommodity = new Icommodity();
            String[] parts = icommodityS.split(";");
            if (getItemStack(parts[0]) == null) {
                com.circulation.m3t.M3Tweaker.logger.info("'{}' not a correct item ID", parts[0]);
                return;
            }
            icommodity.setItem(getItemStack(parts[0]));
            icommodity.setPrice(Integer.parseInt(parts[1]));
            if (parts.length > 3){
                icommodity.setLimit(Integer.parseInt(parts[2]) != 1);
                if (parts.length > 4) {
                    icommodity.setMaxCount(Integer.parseInt(parts[3]));
                }
            }
            icommoditys.add(icommodity);
        });
        addItem(type,icommoditys.toArray(new Icommodity[0]));
    }
}

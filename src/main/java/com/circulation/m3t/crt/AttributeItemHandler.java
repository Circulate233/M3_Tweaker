package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.food_collection.FoodCollectionItem;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;
import project.studio.manametalmod.magic.magicItem.MagicItemType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static project.studio.manametalmod.ManaMetalAPI.FoodCollectionItemList;
import static project.studio.manametalmod.ManaMetalAPI.HotPotFoodList;

@ZenClass(M3TCrtAPI.CrtClass + "AttributeItem")
public class AttributeItemHandler {

    private static final List<FoodCollectionItem> addFoodList = new ArrayList<>();
    private static final List<FoodCollectionItem> defFoodList = new ArrayList<>();
//    private static final List<AttributesItem> addAttributesItemList = new ArrayList<>();
//    private static final List<AttributesItem> defAttributesItemList = new ArrayList<>();
    private static final Map<Item, IMagicEffect> addHotPotFoodList = new HashMap<>();
    private static final Map<Item, IMagicEffect> defHotPotFoodList = new HashMap<>();

    public static void reload(){
        addFoodList.clear();
        addHotPotFoodList.clear();
//        addAttributesItemList.clear();
    }

    public static void postReload(){
        if (defFoodList.isEmpty()){defFoodList.addAll(FoodCollectionItemList);}
//        if (defAttributesItemList.isEmpty()){defAttributesItemList.addAll(AttributesItemList);}
        if (defHotPotFoodList.isEmpty()){defHotPotFoodList.putAll(HotPotFoodList);}

        FoodCollectionItemList.clear();
        FoodCollectionItemList.addAll(defFoodList);
        FoodCollectionItemList.addAll(addFoodList);
//        AttributesItemList.clear();
//        AttributesItemList.addAll(defAttributesItemList);
//        AttributesItemList.addAll(addAttributesItemList);
        HotPotFoodList.clear();
        HotPotFoodList.putAll(defHotPotFoodList);
        HotPotFoodList.putAll(addHotPotFoodList);
    }

    @ZenMethod
    public static void addFoodEffect(IItemStack itemStack, int[] typeID, float[] data, short maxUse) {
        ItemStack item = MineTweakerMC.getItemStack(itemStack);
        FoodCollectionItem food = new FoodCollectionItem(Item.itemRegistry.getNameForObject(item.getItem()), (short)item.getItemDamage(), maxUse);
        IMagicEffect[] MES = new IMagicEffect[typeID.length];
        for (int i = 0; i < typeID.length; i++) {
            MES[i] = new IMagicEffect(MagicItemType.getTypeFromID(typeID[i]), data[i]);
        }
        food.effect = MES;
        addFoodList.add(food);
    }

//    @ZenMethod
//    public static void addAttributesItemEffect(IItemStack itemStack, int[] typeID, float[] data, short maxUse) {
//        IMagicEffect[] MES = new IMagicEffect[typeID.length];
//        for (int i = 0; i < typeID.length; i++) {
//            MES[i] = new IMagicEffect(MagicItemType.getTypeFromID(typeID[i]), data[i]);
//        }
//        addAttributesItemList.add(new AttributesItem(AttributesItemType.LifeCrystal, MineTweakerMC.getItemStack(itemStack), maxUse,MES));
//    }

    /*
     * 不支持nbt，meta
     */
    @ZenMethod
    public static void addHotPotItemEffect(IItemStack itemStack, int typeID,float data) {
        addHotPotFoodList.put(MineTweakerMC.getItemStack(itemStack).getItem(), new IMagicEffect(MagicItemType.getTypeFromID(typeID), data));
    }

}

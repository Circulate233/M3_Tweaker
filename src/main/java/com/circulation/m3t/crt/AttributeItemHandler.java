package com.circulation.m3t.crt;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;
import static project.studio.manametalmod.ManaMetalAPI.FoodCollectionItemList;
import static project.studio.manametalmod.ManaMetalAPI.HotPotFoodList;

import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.item.MagicEffects;
import com.circulation.m3t.reload.M3TCrtReload;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import minetweaker.api.item.IItemStack;
import project.studio.manametalmod.food_collection.FoodCollectionItem;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "AttributeItem")
public class AttributeItemHandler implements M3TCrtReload {

    private static final List<FoodCollectionItem> FOODS = new ObjectArrayList<>();
    private static final Map<Item, IMagicEffect> HOT_POT_FOODS = new Reference2ObjectOpenHashMap<>();
    private List<FoodCollectionItem> defaultFoods;
    private Map<Item, IMagicEffect> defaultHotPotFoods;

    @ZenMethod
    public static void addFoodEffect(IItemStack itemStack, int[] typeID, float[] data, short maxUse) {
        ItemStack item = requireItemStack(itemStack);
        FoodCollectionItem food = new FoodCollectionItem(
            Item.itemRegistry.getNameForObject(item.getItem()),
            (short) item.getItemDamage(),
            maxUse);
        food.effect = MagicEffects.createAll(typeID, data);
        FOODS.add(food);
    }

    @ZenMethod
    public static void addHotPotItemEffect(IItemStack itemStack, int typeID, float data) {
        HOT_POT_FOODS.put(requireItemStack(itemStack).getItem(), MagicEffects.create(typeID, data));
    }

    @Override
    public void reload() {
        FOODS.clear();
        HOT_POT_FOODS.clear();
    }

    @Override
    public void postReload() {
        if (defaultFoods != null || !FOODS.isEmpty()) {
            if (defaultFoods == null) {
                defaultFoods = new ObjectArrayList<>(FoodCollectionItemList);
            }
            FoodCollectionItemList.clear();
            FoodCollectionItemList.addAll(defaultFoods);
            FoodCollectionItemList.addAll(FOODS);
            if (FOODS.isEmpty()) defaultFoods = null;
        }

        if (defaultHotPotFoods != null || !HOT_POT_FOODS.isEmpty()) {
            if (defaultHotPotFoods == null) {
                defaultHotPotFoods = new Reference2ObjectOpenHashMap<>(HotPotFoodList);
            }
            HotPotFoodList.clear();
            HotPotFoodList.putAll(defaultHotPotFoods);
            HotPotFoodList.putAll(HOT_POT_FOODS);
            if (HOT_POT_FOODS.isEmpty()) defaultHotPotFoods = null;
        }
    }
}

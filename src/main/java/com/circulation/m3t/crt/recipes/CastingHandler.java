package com.circulation.m3t.crt.recipes;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import project.studio.manametalmod.items.craftingRecipes.CastingData;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.circulation.m3t.Util.Function.noHasItem;

@ZenClass(M3TCrtAPI.CrtClass + "Casting")
public class CastingHandler implements M3TCrtReload {

    public static final List<Object[]> M3TaddRecipes = new ReferenceArrayList<>();
    private static final List<ItemStack> M3TremoveRecipes = new ReferenceArrayList<>();
    private static final List<Object[]> CastingRecipes = new ReferenceArrayList<>();
    public static final Reference2IntMap<ItemKey> mapCastingRecipes = new Reference2IntOpenHashMap<>();

    @ZenMethod
    public static void addRecipe(IItemStack out, int money, IItemStack[] imp1) {
        Object[] obj = new Object[2 + imp1.length];
        obj[0] = money;
        obj[1] = MineTweakerMC.getItemStack(out);
        int i = 2;
        for (IItemStack item : imp1){
            obj[i] = MineTweakerMC.getItemStack(item);
            i++;
        }
        M3TaddRecipes.add(obj);
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        M3TremoveRecipes.add(MineTweakerMC.getItemStack(out));
    }

    public void reload(){
        M3TaddRecipes.clear();
        M3TremoveRecipes.clear();
    }

    public void postReload(){
        if (CastingRecipes.isEmpty()){CastingRecipes.addAll(CastingData.getRecipes);}

        List<Object[]> list = new ArrayList<>();
        CastingRecipes.forEach(recipe -> {
            if (noHasItem(M3TremoveRecipes, (ItemStack) recipe[1])){
                list.add(recipe);
            }
        });

        list.addAll(M3TaddRecipes);
        CastingData.getRecipes.clear();
        CastingData.getRecipes.addAll(list);

        mapCastingRecipes.clear();
        for (int i = 0; i < CastingData.getRecipes.size(); i++) {
            Object[] item = CastingData.getItems(i);
            mapCastingRecipes.put(ItemKey.getItemKey((ItemStack)item[1]),i);
        }
    }

    private static void addCastingRecipes(ItemStack out, int money, ItemStack... imp1) {
        Object[] obj = new Object[2 + imp1.length];
        obj[0] = money;
        obj[1] = out;
        int i = 2;
        for (ItemStack item :imp1){
            obj[i] = item;
            i++;
        }
        M3TaddRecipes.add(obj);
    }

    public static class ItemKey{
        public Item item;
        public int meta;
        public NBTTagCompound nbt;
        public static final NBTTagCompound emNbt = new NBTTagCompound();

        private static final Map<Item, Map<Integer,Map<NBTTagCompound,ItemKey>>> keyPool = new ConcurrentHashMap<>();

        private ItemKey(ItemStack itemStack){
            this.item = itemStack.getItem();
            this.meta = itemStack.getItemDamage();
            this.nbt = itemStack.getTagCompound();
        }

        public static ItemKey getItemKey(ItemStack item){
            return keyPool.computeIfAbsent(item.getItem(), k -> new ConcurrentHashMap<>())
                .computeIfAbsent(item.getItemDamage(), m -> new ConcurrentHashMap<>())
                .computeIfAbsent(item.getTagCompound() == null ? emNbt:item.getTagCompound(), m -> new ItemKey(item));
        }
    }

}

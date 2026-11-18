package com.circulation.m3t.crt.recipes;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;

import java.util.List;
import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.reload.ItemListChanges;
import com.circulation.m3t.reload.M3TCrtReload;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import project.studio.manametalmod.items.craftingRecipes.CastingData;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "Casting")
public class CastingHandler implements M3TCrtReload {

    private static final int COST_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int INPUT_START = 2;
    private static final ItemListChanges<Object[]> CHANGES = new ItemListChanges<>(
        recipe -> (ItemStack) recipe[OUTPUT_SLOT]);
    private static volatile Object2IntMap<ItemKey> recipeIndex = new Object2IntOpenHashMap<>(0);

    @ZenMethod
    public static void addRecipe(IItemStack out, int money, IItemStack[] imp1) {
        Object[] recipe = new Object[INPUT_START + imp1.length];
        recipe[COST_SLOT] = money;
        recipe[OUTPUT_SLOT] = requireItemStack(out);
        for (int i = 0; i < imp1.length; i++) {
            recipe[INPUT_START + i] = MineTweakerMC.getItemStack(imp1[i]);
        }
        CHANGES.add(recipe);
    }

    @ZenMethod
    public static void removeRecipe(IItemStack out) {
        CHANGES.remove(requireItemStack(out));
    }

    @Override
    public void reload() {
        CHANGES.clearChanges();
    }

    @Override
    public void postReload() {
        List<Object[]> recipes = CastingData.getRecipes;
        CHANGES.applyTo(recipes);

        Object2IntMap<ItemKey> index = new Object2IntOpenHashMap<>(recipes.size());
        for (int i = 0; i < recipes.size(); i++) {
            ItemStack output = (ItemStack) recipes.get(i)[OUTPUT_SLOT];
            index.put(new ItemKey(output, true), i);
        }
        recipeIndex = index;
    }

    public static int findRecipe(ItemStack output) {
        return output == null || output.getItem() == null ? -1
            : recipeIndex.getOrDefault(new ItemKey(output, false), -1);
    }

    private static final class ItemKey {

        private final Item item;
        private final int meta;
        private final NBTTagCompound nbt;

        private ItemKey(ItemStack stack, boolean copyTag) {
            item = stack.getItem();
            meta = stack.getItemDamage();
            NBTTagCompound tag = stack.getTagCompound();
            nbt = tag == null || tag.hasNoTags() ? null : copyTag ? (NBTTagCompound) tag.copy() : tag;
        }

        @Override
        public boolean equals(Object other) {
            return this == other
                || other instanceof ItemKey key && item == key.item && meta == key.meta && Objects.equals(nbt, key.nbt);
        }

        @Override
        public int hashCode() {
            return 31 * (31 * System.identityHashCode(item) + meta) + Objects.hashCode(nbt);
        }
    }

}

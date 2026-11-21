package com.circulation.m3t.crt;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.Function;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameData;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "Util")
public class CrtUtil {

    public static ItemStack requireItemStack(IItemStack item) {
        ItemStack stack = MineTweakerMC.getItemStack(item);
        if (stack == null || stack.getItem() == null) {
            throw new IllegalArgumentException("Expected a valid item stack");
        }
        return stack;
    }

    public static ItemStack[] requireItemStacks(IItemStack[] items) {
        ItemStack[] stacks = new ItemStack[items.length];
        for (int i = 0; i < items.length; i++) {
            stacks[i] = requireItemStack(items[i]);
        }
        return stacks;
    }

    @ZenMethod
    public static IItemStack[] getModItem(String modId) {
        ObjectArrayList<IItemStack> result = new ObjectArrayList<>();
        for (Object entry : GameData.getItemRegistry()) {
            Item item = (Item) entry;
            String name = Item.itemRegistry.getNameForObject(item);
            if (name != null && name.startsWith(modId)) {
                result.add(MineTweakerMC.getIItemStack(new ItemStack(item)));
            }
        }
        return result.toArray(new IItemStack[0]);
    }

    @ZenMethod
    public static String I18n(String key) {
        return Function.getText(key);
    }

    @ZenMethod
    public static String createString(Object... objects) {
        StringBuilder s = new StringBuilder();
        for (Object object : objects) {
            s.append(object);
        }
        return s.toString();
    }

    @ZenMethod
    public static Boolean isServer() {
        return FMLCommonHandler.instance()
            .getEffectiveSide()
            .isServer();
    }

}

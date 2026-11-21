package com.circulation.m3t.crt.recipes;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;
import static project.studio.manametalmod.ManaMetalAPI.MirrorPoolCopyableItems;

import net.minecraft.item.ItemStack;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.reload.ItemListChanges;
import com.circulation.m3t.reload.M3TCrtReload;

import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "MirrorPool")
public class MirrorPoolHandler implements M3TCrtReload {

    private static final ItemListChanges<ItemStack> CHANGES = new ItemListChanges<>(template -> template);

    @ZenMethod
    public static void addRecipe(IItemStack template) {
        CHANGES.add(
            RecipeInputs.positiveStack(template)
                .copy());
    }

    @ZenMethod
    public static void removeRecipe(IItemStack template) {
        CHANGES.remove(requireItemStack(template));
    }

    @Override
    public void reload() {
        CHANGES.clearChanges();
    }

    @Override
    public void postReload() {
        CHANGES.applyTo(MirrorPoolCopyableItems);
    }
}

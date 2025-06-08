package com.circulation.m3t.mixins.MMM;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import project.studio.manametalmod.items.crafting.IronWroughtFurnaceRecipes;

import java.util.Map;

@Mixin(value = IronWroughtFurnaceRecipes.class,remap = false)
public interface AccessorIronWroughtFurnaceRecipes {

    @Accessor("smeltingBase")
    static IronWroughtFurnaceRecipes getSmeltingBase() {
        return null;
    }

    @Accessor("smeltingList")
    Map<ItemStack,ItemStack> getSmeltingList();

    @Accessor("experienceList")
    Map<ItemStack,Float> getExperienceList();

    @Accessor("smeltingList")
    void setSmeltingList(Map<?,?> map);

    @Accessor("experienceList")
    void setExperienceList(Map<?,?> map);
}

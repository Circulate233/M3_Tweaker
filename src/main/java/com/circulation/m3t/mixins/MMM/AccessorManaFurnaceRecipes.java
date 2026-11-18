package com.circulation.m3t.mixins.MMM;

import java.util.Map;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import project.studio.manametalmod.items.crafting.ManaFurnaceRecipes;

@Mixin(value = ManaFurnaceRecipes.class, remap = false)
public interface AccessorManaFurnaceRecipes {

    @Accessor("smeltingList")
    Map<ItemStack, ItemStack> m3t$getRecipes();

    @Accessor("experienceList")
    Map<ItemStack, Float> m3t$getExperience();

    @Accessor("smeltingList")
    void m3t$setRecipes(Map<ItemStack, ItemStack> recipes);

    @Accessor("experienceList")
    void m3t$setExperience(Map<ItemStack, Float> experience);
}

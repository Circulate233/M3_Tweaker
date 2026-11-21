package com.circulation.m3t.mixins.MMM;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import project.studio.manametalmod.originmagic.EternalAnvilRecipe;

import java.util.List;

@Mixin(value = EternalAnvilRecipe.class, remap = false)
public interface AccessorEternalAnvilRecipe {

    @Accessor("RECIPES")
    static List<EternalAnvilRecipe> m3t$getRecipes() {
        throw new AssertionError("Eternal anvil recipe accessor was not applied");
    }
}

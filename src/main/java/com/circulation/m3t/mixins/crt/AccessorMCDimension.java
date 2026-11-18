package com.circulation.m3t.mixins.crt;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import minetweaker.mc1710.world.MCDimension;

@Mixin(value = MCDimension.class, remap = false)
public interface AccessorMCDimension {

    @Accessor
    World getWorld();
}

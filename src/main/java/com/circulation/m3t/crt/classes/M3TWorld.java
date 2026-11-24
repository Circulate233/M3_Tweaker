package com.circulation.m3t.crt.classes;

import minetweaker.api.world.IDimension;
import minetweaker.mc1710.world.MCDimension;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenGetter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

@ZenExpansion("minetweaker.world.IDimension")
public class M3TWorld {

    private static MethodHandle world;

    @ZenGetter("dim")
    public static int getDim(IDimension dimension) {
        return getWorld(dimension).provider.dimensionId;
    }

    @ZenGetter("remote")
    public static boolean isRemote(IDimension dimension) {
        return getWorld(dimension).isRemote;
    }

    private static World getWorld(IDimension dimension) {
        if (world == null) {
            try {
                world = MethodHandles.lookup().findGetter(MCDimension.class,"world", World.class);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return (World) world.invokeExact((MCDimension) dimension);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}

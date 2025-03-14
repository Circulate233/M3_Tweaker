package com.circulation.m3t.crt.classes;

import com.circulation.m3t.mixins.crt.AccessorMCDimension;
import minetweaker.api.world.IDimension;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenExpansion("minetweaker.world.IDimension")
public class M3TWorld {

    @ZenGetter("dim")
    public static int getDim(IDimension dimension){
        return ((AccessorMCDimension)dimension).getWorld().provider.dimensionId;
    }

    @ZenGetter("remote")
    public static boolean isRemote(IDimension dimension){
        return ((AccessorMCDimension)dimension).getWorld().isRemote;
    }
}

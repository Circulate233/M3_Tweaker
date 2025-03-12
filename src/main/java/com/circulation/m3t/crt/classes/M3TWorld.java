package com.circulation.m3t.crt.classes;

import com.circulation.m3t.mixins.crt.AccessorMCDimension;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.player.IPlayer;
import minetweaker.api.world.IDimension;
import minetweaker.mc1710.world.MCDimension;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenExpansion("minetweaker.world.IDimension")
public class M3TWorld {

    @ZenGetter("dim")
    public static int getDim(IDimension dimension){
        return ((AccessorMCDimension)dimension).getWorld().provider.dimensionId;
    }

}

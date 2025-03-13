package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.hander.M3TBaublesSuitHandler;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.player.IPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import project.studio.manametalmod.inventory.ContainerManaItem;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;
import project.studio.manametalmod.magic.magicItem.MagicItemType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.*;

@ZenClass(M3TCrtAPI.CrtClass + "BaublesSuit")
public class CrtBaublesSuitHandler extends M3TBaublesSuitHandler {

    @ZenMethod
    public static int getSuitQuantity(String suitName, IPlayer player){
        return getSuitQuantity(suitName, MineTweakerMC.getPlayer(player));
    }

    @ZenMethod
    public static boolean hasSuit(String name){
        return map.containsKey(name);
    }

    @ZenMethod
    public static void registerBaublesSuit(IItemStack item, int quantity, String tooltip, int[] typeID, float[] data) {
        List<IMagicEffect> effects = new ArrayList<>();
        for (int i = 0; i < typeID.length; i++) {
            effects.add(new IMagicEffect(MagicItemType.getTypeFromID(typeID[i]),data[i]));
        }
        registerBaublesSuit(MineTweakerMC.getItemStack(item).getItem(),quantity,tooltip,effects);
    }
}

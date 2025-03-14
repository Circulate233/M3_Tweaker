package com.circulation.m3t.crt.classes;

import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.player.IPlayer;
import minetweaker.api.util.Position3f;
import minetweaker.api.world.IDimension;
import minetweaker.mc1710.world.MCDimension;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.entity.nbt.NbtBaubles;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenExpansion("minetweaker.player.IPlayer")
public class M3TPlayer {

    @ZenMethod
    public static IItemStack getBauble(IPlayer player, int slot){
        NbtBaubles baubles = MMM.getEntityNBT(MineTweakerMC.getPlayer(player)).item;
        ItemStack item = baubles.getStackInSlot(slot);
        return MineTweakerMC.getIItemStack(baubles.getStackInSlot(slot));
    }

    @ZenGetter("world")
    public static IDimension getWorld(IPlayer player){
        if (player == null)return null;
        return new MCDimension(MineTweakerMC.getPlayer(player).worldObj);
    }

    @ZenGetter("pos")
    public static Position3f getPos(IPlayer iplayer){
        EntityPlayer player = MineTweakerMC.getPlayer(iplayer);
        if (player == null)return null;
        return new Position3f((float) player.posX, (float) player.posY, (float) player.posZ);
    }

}

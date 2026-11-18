package com.circulation.m3t.crt.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.circulation.m3t.M3TCrtAPI;

import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.player.IPlayer;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenClass(M3TCrtAPI.CrtClass + "event.BaublePostEvent")
public class BaublePostEvent {

    private final EntityPlayer player;
    private final ItemStack item;

    public BaublePostEvent(EntityPlayer player, ItemStack item) {
        this.player = player;
        this.item = item;
    }

    @ZenGetter("player")
    public IPlayer getPlayer() {
        return MineTweakerMC.getIPlayer(player);
    }

    @ZenGetter("item")
    public IItemStack getItem() {
        return MineTweakerMC.getIItemStack(item);
    }
}

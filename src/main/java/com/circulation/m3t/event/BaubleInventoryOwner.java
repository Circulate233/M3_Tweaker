package com.circulation.m3t.event;

import net.minecraft.entity.player.EntityPlayer;

/** Exposes the player bound to a ManaMetal accessory inventory through the mixin. */
public interface BaubleInventoryOwner {

    EntityPlayer m3t$getOwner();
}

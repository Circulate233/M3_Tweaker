package com.circulation.m3t.suit;

import static com.circulation.m3t.suit.M3TBaublesSuitHandler.nbtName;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;

import com.circulation.m3t.network.UpdateBauble;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public final class SuitPlayerEvents {

    @SubscribeEvent
    public void onPlayerClone(Clone event) {
        NBTTagCompound original = event.original.getEntityData();
        if (event.entityPlayer instanceof EntityPlayerMP && original.hasKey(nbtName, 10)) {
            event.entityPlayer.getEntityData()
                .setTag(
                    nbtName,
                    original.getCompoundTag(nbtName)
                        .copy());
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (event.player instanceof EntityPlayerMP player) {
            UpdateBauble.sendTo(
                player,
                player.getEntityData()
                    .getCompoundTag(nbtName));
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP player && player.getEntityData()
            .hasKey(nbtName, 10)) {
            UpdateBauble.sendTo(
                player,
                player.getEntityData()
                    .getCompoundTag(nbtName));
        }
    }
}

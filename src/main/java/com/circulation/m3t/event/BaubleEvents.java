package com.circulation.m3t.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public final class BaubleEvents {

    private BaubleEvents() {}

    public static boolean isWearCanceled(EntityPlayer player, ItemStack item) {
        return shouldPost(player, item) && MinecraftForge.EVENT_BUS.post(new BaubleEquipmentEvent.Wear(player, item));
    }

    public static boolean isDisrobeCanceled(EntityPlayer player, ItemStack item) {
        return shouldPost(player, item)
            && MinecraftForge.EVENT_BUS.post(new BaubleEquipmentEvent.Disrobe(player, item));
    }

    public static void postWear(EntityPlayer player, ItemStack item) {
        if (shouldPost(player, item)) {
            MinecraftForge.EVENT_BUS.post(new BaubleEquipmentEvent.WearPost(player, item));
        }
    }

    public static void postDisrobe(EntityPlayer player, ItemStack item) {
        if (shouldPost(player, item)) {
            MinecraftForge.EVENT_BUS.post(new BaubleEquipmentEvent.DisrobePost(player, item));
        }
    }

    private static boolean shouldPost(EntityPlayer player, ItemStack item) {
        return player != null && player.worldObj != null
            && !player.worldObj.isRemote
            && item != null
            && item.getItem() != null;
    }
}

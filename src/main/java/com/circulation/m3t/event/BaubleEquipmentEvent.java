package com.circulation.m3t.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

/** ManaMetal accessory events, posted on MinecraftForge.EVENT_BUS on the logical server. */
public class BaubleEquipmentEvent extends Event {

    public final EntityPlayer player;
    public final ItemStack item;

    protected BaubleEquipmentEvent(EntityPlayer player, ItemStack item) {
        this.player = player;
        this.item = item;
    }

    @Cancelable
    public static final class Wear extends BaubleEquipmentEvent {

        public Wear(EntityPlayer player, ItemStack item) {
            super(player, item);
        }
    }

    @Cancelable
    public static final class Disrobe extends BaubleEquipmentEvent {

        public Disrobe(EntityPlayer player, ItemStack item) {
            super(player, item);
        }
    }

    public static final class WearPost extends BaubleEquipmentEvent {

        public WearPost(EntityPlayer player, ItemStack item) {
            super(player, item);
        }
    }

    public static final class DisrobePost extends BaubleEquipmentEvent {

        public DisrobePost(EntityPlayer player, ItemStack item) {
            super(player, item);
        }
    }
}

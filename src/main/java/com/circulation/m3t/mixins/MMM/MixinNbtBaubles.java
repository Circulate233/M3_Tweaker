package com.circulation.m3t.mixins.MMM;

import com.circulation.m3t.M3Tweaker;
import com.circulation.m3t.crt.events.BaublePostEvent;
import com.circulation.m3t.crt.events.M3TEventAPI;
import minetweaker.api.util.Position3f;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.core.EntityNBTBaseM3;
import project.studio.manametalmod.entity.nbt.NbtBaubles;

@Mixin(value = NbtBaubles.class,remap = false)
public abstract class MixinNbtBaubles extends EntityNBTBaseM3 implements IInventory {

    @Inject(method = "wearItem",at = @At("HEAD"))
    public void wearItemMixin(ItemStack item, boolean sound, CallbackInfo ci) {
        if (MMM.isServer() && this.entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) this.entity;
            BaublePostEvent event = new BaublePostEvent(player,item);
            M3TEventAPI.publishAllWearPost(event);
        }
    }

    @Inject(method = "disrobeItem",at = @At("HEAD"))
    public void disrobeItemMixin(ItemStack item, CallbackInfo ci) {
        if (MMM.isServer() && this.entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) this.entity;
            BaublePostEvent event = new BaublePostEvent(player,item);
            M3TEventAPI.publishAllDisrobePost(event);
        }
    }
}

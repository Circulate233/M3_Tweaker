package com.circulation.m3t.mixins.MMM;

import static com.circulation.m3t.suit.M3TBaublesSuitHandler.nbtName;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import project.studio.manametalmod.event.EventFx;

@Mixin(value = EventFx.class, remap = false)
public class MixinEventFx {

    @Inject(method = "resetPlayerAattribute", at = @At(value = "HEAD"))
    private static void clearPlayerSuitNbt(EntityPlayer player, boolean equipmentVerification, CallbackInfo ci) {
        NBTTagCompound entityData = player.getEntityData();
        if (entityData.hasKey(nbtName)) {
            entityData.removeTag(nbtName);
        }
    }

}

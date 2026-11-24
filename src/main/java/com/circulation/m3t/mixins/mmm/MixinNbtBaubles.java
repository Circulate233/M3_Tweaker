package com.circulation.m3t.mixins.mmm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.circulation.m3t.event.BaubleEvents;
import com.circulation.m3t.event.BaubleInventoryOwner;

import project.studio.manametalmod.core.EntityNBTBaseM3;
import project.studio.manametalmod.entity.nbt.NbtBaubles;

@Mixin(value = NbtBaubles.class, remap = false)
public abstract class MixinNbtBaubles extends EntityNBTBaseM3 implements BaubleInventoryOwner {

    @Override
    public EntityPlayer m3t$getOwner() {
        return entity instanceof EntityPlayer p ? p : null;
    }

    @Inject(method = "wearItem", at = @At("RETURN"), require = 1)
    private void m3t$afterEquip(ItemStack item, boolean sound, CallbackInfo ci) {
        if (GetSide().isServer()) {
            BaubleEvents.postWear(m3t$getOwner(), item);
        }
    }

    @Inject(method = "disrobeItem", at = @At("RETURN"), require = 1)
    private void m3t$afterUnequip(ItemStack item, CallbackInfo ci) {
        if (GetSide().isServer()) {
            BaubleEvents.postDisrobe(m3t$getOwner(), item);
        }
    }
}

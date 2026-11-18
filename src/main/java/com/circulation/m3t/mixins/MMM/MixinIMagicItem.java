package com.circulation.m3t.mixins.MMM;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.circulation.m3t.event.BaubleEvents;
import com.circulation.m3t.event.BaubleInventoryOwner;

import project.studio.manametalmod.api.weapon.IMagicItem;
import project.studio.manametalmod.entity.nbt.ManaMetalModRoot;

@Mixin(value = IMagicItem.class, remap = false)
public abstract class MixinIMagicItem {

    @Inject(
        method = "tryPushItem",
        at = @At(
            value = "INVOKE",
            target = "Lproject/studio/manametalmod/entity/nbt/NbtBaubles;setInventorySlotContents(ILnet/minecraft/item/ItemStack;)V",
            remap = true),
        cancellable = true,
        require = 1)
    private void m3t$beforeEquip(int slot, ManaMetalModRoot root, ItemStack item, World world,
        CallbackInfoReturnable<Boolean> cir) {
        if (BaubleEvents.isWearCanceled(((BaubleInventoryOwner) root.item).m3t$getOwner(), item)) {
            cir.setReturnValue(false);
        }
    }

}

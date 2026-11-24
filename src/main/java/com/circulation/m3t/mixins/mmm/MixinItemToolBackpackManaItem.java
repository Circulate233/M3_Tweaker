package com.circulation.m3t.mixins.mmm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.circulation.m3t.event.BaubleEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import project.studio.manametalmod.api.IFishIngRod;
import project.studio.manametalmod.api.weapon.IMagicItem;
import project.studio.manametalmod.archeology.IAntiquities;
import project.studio.manametalmod.entity.nbt.ManaMetalModRoot;
import project.studio.manametalmod.items.ItemToolBackpackManaItem;

@Mixin(value = ItemToolBackpackManaItem.class, remap = false)
public abstract class MixinItemToolBackpackManaItem {

    @Inject(method = "canWear", at = @At("RETURN"), cancellable = true, require = 1)
    private void m3t$checkEquip(ItemStack stack, EntityPlayer player, World world, ManaMetalModRoot root,
        CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && stack != null) {
            Item item = stack.getItem();
            if ((item instanceof IMagicItem || item instanceof IFishIngRod || item instanceof IAntiquities)
                && BaubleEvents.isWearCanceled(player, stack)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(
        method = "wear",
        at = @At(
            value = "INVOKE",
            target = "Lproject/studio/manametalmod/MMM;cloneItemStack([Lnet/minecraft/item/ItemStack;)[Lnet/minecraft/item/ItemStack;",
            ordinal = 0),
        cancellable = true,
        require = 1)
    private void m3t$checkCompleteSwap(ItemStack backpack, World world, EntityPlayer player, CallbackInfo ci,
        @Local(name = "root") ManaMetalModRoot root) {
        ItemStack[] equipped = root.item.items;
        for (int slot = 0; slot < equipped.length; slot++) {
            // ManaMetal excludes slot 41 from backpack swaps.
            if (slot != 41 && BaubleEvents.isDisrobeCanceled(player, equipped[slot])) {
                ci.cancel();
                return;
            }
        }
    }

    // The second canWear call is the per-slot check in partial mode, before rewriting the stored item.
    @WrapOperation(
        method = "wear",
        at = @At(
            value = "INVOKE",
            target = "Lproject/studio/manametalmod/items/ItemToolBackpackManaItem;canWear(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lproject/studio/manametalmod/entity/nbt/ManaMetalModRoot;)Z",
            ordinal = 1),
        require = 1)
    private boolean m3t$checkPartialSwap(ItemToolBackpackManaItem backpack, ItemStack stack, EntityPlayer player,
        World world, ManaMetalModRoot root, Operation<Boolean> original, @Local(name = "slotindex") byte slot) {
        return original.call(backpack, stack, player, world, root)
            && !BaubleEvents.isDisrobeCanceled(player, root.item.getStackInSlot(slot));
    }
}

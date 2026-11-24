package com.circulation.m3t.mixins.mmm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.circulation.m3t.event.BaubleEvents;

import project.studio.manametalmod.inventory.ContainerManaItem;

@Mixin(ContainerManaItem.SlotMana.class)
public abstract class MixinSlotMana extends Slot {

    @Shadow(remap = false)
    EntityPlayer player;

    protected MixinSlotMana(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Inject(method = "isItemValid", at = @At("RETURN"), cancellable = true, require = 1)
    private void m3t$checkEquip(ItemStack item, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && BaubleEvents.isWearCanceled(player, item)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canTakeStack", at = @At("RETURN"), cancellable = true, require = 1)
    private void m3t$checkUnequip(EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && BaubleEvents.isDisrobeCanceled(player, getStack())) {
            cir.setReturnValue(false);
        }
    }

}

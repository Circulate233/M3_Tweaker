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
import com.llamalad7.mixinextras.sugar.Local;

import project.studio.manametalmod.api.IFishIngRod;
import project.studio.manametalmod.api.weapon.IMagicItem;
import project.studio.manametalmod.archeology.IAntiquities;
import project.studio.manametalmod.entity.nbt.ManaMetalModRoot;
import project.studio.manametalmod.inventory.EquipmentSwapPlan;
import project.studio.manametalmod.items.ItemToolBackpackManaItem;

@Mixin(value = ItemToolBackpackManaItem.class, remap = false)
public abstract class MixinItemToolBackpackManaItem809 {

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
            target = "Lproject/studio/manametalmod/inventory/EquipmentSwapPlan;writeTo(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/nbt/NBTTagCompound;"),
        cancellable = true,
        require = 1)
    private void m3t$checkPlannedSwap(ItemStack backpack, World world, EntityPlayer player, CallbackInfo ci,
        @Local(name = "root") ManaMetalModRoot root, @Local(name = "plan") EquipmentSwapPlan plan) {
        // Validate before the plan is serialized or either inventory is mutated.
        boolean complete = backpack.getTagCompound()
            .getBoolean("tagComplete");
        ItemStack[] equipped = root.item.items;

        for (int slot = 0; slot < plan.accessoryChanges.length; slot++) {
            if (!plan.accessoryChanges[slot]) {
                continue;
            }

            ItemStack incoming = plan.accessories[slot];
            ItemStack outgoing = equipped[slot];
            if ((incoming == null || !BaubleEvents.isWearCanceled(player, incoming))
                && (outgoing == null || !BaubleEvents.isDisrobeCanceled(player, outgoing))) {
                continue;
            }

            if (complete) {
                ci.cancel();
                return;
            }

            // In partial mode, keep this slot untouched and leave its incoming item in the backpack.
            plan.accessoryChanges[slot] = false;
            plan.accessories[slot] = ItemStack.copyItemStack(outgoing);
            plan.storedAccessories[slot] = ItemStack.copyItemStack(incoming);
        }
    }
}

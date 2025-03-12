package com.circulation.m3t.mixins.MMM;

import com.circulation.m3t.crt.events.BaubleEvent;
import com.circulation.m3t.crt.events.M3TEventAPI;
import minetweaker.api.util.Position3f;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import project.studio.manametalmod.inventory.ContainerManaItem;

@Mixin(ContainerManaItem.SlotMana.class)
public abstract class MixinSlotMana extends Slot {

    @Shadow(remap = false)
    EntityPlayer player;

    public MixinSlotMana(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }

    @Inject(method = "isItemValid",at = @At("RETURN"), cancellable = true)
    public void isItemValidMixin(ItemStack item, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            BaubleEvent event = new BaubleEvent(player, this.getStack());
            M3TEventAPI.publishAllWear(event);
            cir.setReturnValue(!event.isCancel());
        }
    }

}

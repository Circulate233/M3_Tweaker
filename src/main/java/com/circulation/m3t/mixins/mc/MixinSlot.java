package com.circulation.m3t.mixins.mc;

import com.circulation.m3t.crt.events.BaubleEvent;
import com.circulation.m3t.crt.events.M3TEventAPI;
import minetweaker.api.util.Position3f;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.circulation.m3t.Util.Function.slotMana;

@Mixin(value = Slot.class)
public abstract class MixinSlot {

    @Shadow
    public abstract ItemStack getStack();

    @Inject(method = "canTakeStack",at = @At("RETURN"), cancellable = true)
    public void canTakeStackMixin(EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
        if (this.getClass().getName().equals(slotMana)) {
            BaubleEvent event = new BaubleEvent(player, this.getStack(),new Position3f((float) player.posX, (float) player.posY, (float) player.posZ), player.worldObj);
            M3TEventAPI.publishAllDisrobe(event);
            cir.setReturnValue(!event.isCancel());
        }
    }
}

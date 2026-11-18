package com.circulation.m3t.mixins.mc;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.circulation.m3t.suit.M3TBaubleTagSuitHandler;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    public void getDisplayName(CallbackInfoReturnable<String> cir) {
        String tagName = M3TBaubleTagSuitHandler.getNameTag((ItemStack) (Object) this);
        if (tagName != null && !tagName.isEmpty()) {
            cir.setReturnValue(tagName + cir.getReturnValue());
        }
    }

}

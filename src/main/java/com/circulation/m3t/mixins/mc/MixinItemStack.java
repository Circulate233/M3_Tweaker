package com.circulation.m3t.mixins.mc;

import com.circulation.m3t.hander.M3TBaubleTagSuitHandler;
import com.circulation.m3t.hander.M3TBaublesSuitHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.circulation.m3t.hander.M3TBaublesSuitHandler.nbtName;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

    @Inject(method = "getDisplayName",at = @At("RETURN"), cancellable = true)
    public void getDisplayName(CallbackInfoReturnable<String> cir) {
        if (this.hasTagCompound() && this.getTagCompound().hasKey(nbtName)) {
            String SuitName = this.getTagCompound().getString(nbtName);
            if (M3TBaublesSuitHandler.hasSuit(SuitName) && M3TBaubleTagSuitHandler.Tags.containsKey(SuitName)) {
                cir.setReturnValue(M3TBaubleTagSuitHandler.Tags.get(SuitName) + cir.getReturnValue());
            }
        }
    }

    @Shadow
    public abstract NBTTagCompound getTagCompound();

    @Shadow
    public abstract boolean hasTagCompound();

}

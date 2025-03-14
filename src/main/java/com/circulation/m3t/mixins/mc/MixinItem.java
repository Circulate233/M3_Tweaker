package com.circulation.m3t.mixins.mc;

import com.circulation.m3t.hander.M3TBaubleTagSuitHandler;
import com.circulation.m3t.hander.M3TBaublesSuitHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.circulation.m3t.hander.M3TBaublesSuitHandler.nbtName;

@Mixin(Item.class)
public class MixinItem {

    @Inject(method = "getItemStackDisplayName",at = @At("RETURN"), cancellable = true)
    public void getItemStackDisplayName(ItemStack item, CallbackInfoReturnable<String> cir) {
        if (item.hasTagCompound() && item.getTagCompound().hasKey(nbtName)) {
            String SuitName = item.getTagCompound().getString(nbtName);
            if (M3TBaublesSuitHandler.hasSuit(SuitName)) {
                cir.setReturnValue(M3TBaubleTagSuitHandler.Tags.get(SuitName) + cir.getReturnValue());
            }
        }
    }

}

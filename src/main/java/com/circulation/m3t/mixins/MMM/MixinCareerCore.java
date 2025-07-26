package com.circulation.m3t.mixins.MMM;

import com.circulation.m3t.crt.CareerModifierHandler;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.core.CareerCore;
import project.studio.manametalmod.entity.nbt.ManaMetalModRoot;

@Mixin(value = CareerCore.class, remap = false)
public class MixinCareerCore {

    @Inject(method = "setPlayerCarrerModifier", at = @At("RETURN"))
    private static void onSetCareerModifier(EntityPlayer player, CareerCore type,
                                            boolean isRemove, CallbackInfo ci) {
        if (!isRemove) {
            ManaMetalModRoot root = project.studio.manametalmod.MMM.getEntityNBT(player);
            CareerModifierHandler.applyCareerModifiers(player, root);
        }
    }

    // 修正方法名和签名！
    @Inject(
        method = "addPoitnBase(Lnet/minecraft/entity/player/EntityPlayer;Lproject/studio/manametalmod/entity/nbt/ManaMetalModRoot;Lproject/studio/manametalmod/core/CareerCore$CareerPoint;IZ)V",
        at = @At("HEAD")
    )
    private static void onPointEffect(EntityPlayer player, ManaMetalModRoot root,
                                      CareerCore.CareerPoint type, int count,
                                      boolean changePoint, CallbackInfo ci) {
        if (changePoint && count > 0) {
            CareerModifierHandler.modifyPointEffects(player, root, type, count);
        }
    }
}

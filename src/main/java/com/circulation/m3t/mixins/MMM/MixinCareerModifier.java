package com.circulation.m3t.mixins.MMM;

import com.circulation.m3t.crt.CareerModifierHandler;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.entity.nbt.ManaMetalModRoot;
import project.studio.manametalmod.entity.nbt.NbtCareer;

@Mixin(value = NbtCareer.class, remap = false)
public abstract class MixinCareerModifier {

    @Shadow
    public ManaMetalModRoot obj;

    @Shadow
    public abstract EntityPlayer getPlayer();

    // 在更新时应用修改
    @Inject(method = "Update", at = @At("HEAD"))
    private void applyCareerModifiers(CallbackInfo ci) {
        EntityPlayer player = this.getPlayer();
        if (player != null && this.obj != null) {
            if (player.worldObj.getTotalWorldTime() % 20 == 0) {
                CareerModifierHandler.applyCareerModifiers(player, this.obj);
            }
        }
    }
}

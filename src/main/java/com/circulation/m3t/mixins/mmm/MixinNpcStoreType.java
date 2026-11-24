package com.circulation.m3t.mixins.mmm;

import java.util.List;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.circulation.m3t.store.NpcStock;

import project.studio.manametalmod.core.Icommodity;
import project.studio.manametalmod.npc.NpcStoreType;

@Mixin(value = NpcStoreType.class, remap = false)
public class MixinNpcStoreType {

    @Inject(method = "getItems", at = @At("RETURN"), cancellable = true)
    private static void getItemsMixin(NpcStoreType type, Random rand, CallbackInfoReturnable<List<Icommodity>> cir) {
        List<Icommodity> original = cir.getReturnValue();
        List<Icommodity> stock = NpcStock.apply(type, original);
        if (stock != original) {
            cir.setReturnValue(stock);
        }
    }

}

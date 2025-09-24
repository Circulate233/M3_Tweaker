package com.circulation.m3t.mixins.MMM;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import project.studio.manametalmod.core.Icommodity;
import project.studio.manametalmod.npc.NpcStoreType;

import java.util.List;
import java.util.Random;

import static com.circulation.m3t.Util.Function.noHasItem;
import static com.circulation.m3t.hander.NPCHandler.addMap;
import static com.circulation.m3t.hander.NPCHandler.removeMap;

@Mixin(value = NpcStoreType.class,remap = false)
public class MixinNpcStoreType {

    @Inject(method = "getItems", at = @At("RETURN"), cancellable = true)
    private static void getItemsMixin(NpcStoreType type, Random rand, CallbackInfoReturnable<List<Icommodity>> cir) {
        List<Icommodity> list = new ObjectArrayList<>();

        if (removeMap.containsKey(type)) {
            cir.getReturnValue().forEach(icommodity -> {
                final ItemStack item = icommodity.getItem();
                if (noHasItem(new ObjectArrayList<>(removeMap.get(type)), item)) {
                    list.add(icommodity);
                }
            });
        } else {
            list.addAll(cir.getReturnValue());
        }

        if (addMap.containsKey(type)) {
            list.addAll(addMap.get(type));
        }

        cir.setReturnValue(list);
    }

}

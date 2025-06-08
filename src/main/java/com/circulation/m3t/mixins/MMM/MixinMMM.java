package com.circulation.m3t.mixins.MMM;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import project.studio.manametalmod.MMM;

import java.util.Random;
import java.util.Set;

import static com.circulation.m3t.crt.TrophyItemsHandler.*;

@Mixin(value = MMM.class,remap = false)
public abstract class MixinMMM {

    @Final
    @Shadow
    public static Random rand;

    @Inject(method = "getTrophyItems()Lnet/minecraft/item/ItemStack;",at = @At("HEAD"), cancellable = true)
    private static void getTrophyItemsMixin(CallbackInfoReturnable<ItemStack> cir) {
        if (!addTrophyItems.isEmpty()) {
            int random = rand.nextInt(82 + addTrophyItems.size());
            if (random - 82 >= 0) {
                TrophyItem item = addTrophyItems.get(random - 82);
                ItemStack out = item.output.copy();
                if (item.chance) {
                    out.stackSize = (rand.nextInt(item.max - item.min) + item.max);
                } else {
                    out.stackSize = item.max;
                }
                cir.setReturnValue(out);
            }
        }
    }

    @Inject(method = "getTrophyItemsLV2",at = @At("HEAD"), cancellable = true)
    private static void getTrophyItemsLV2(CallbackInfoReturnable<ItemStack> cir) {
        if (!addTrophyItems.isEmpty()) {
            int random = rand.nextInt(47 + addTrophyItemsLv2.size());
            if (random - 47 >= 0) {
                TrophyItem item = addTrophyItemsLv2.get(random - 47);
                ItemStack out = item.output.copy();
                if (item.chance) {
                    out.stackSize = (rand.nextInt(item.max - item.min) + item.max);
                } else {
                    out.stackSize = item.max;
                }
                cir.setReturnValue(out);
            }
        }
    }

    /**
     * @author circulation
     * @reason 测试性的更改...希望没事
     */
    @Overwrite
    public static final boolean isNBTTagCompoundEqual(NBTTagCompound nbt1, NBTTagCompound nbt2) {
        if (nbt2 == null) return true;
        else if (nbt1 != null){
            return m3Tweaker$containsAll(nbt1,nbt2);
        }
        return false;
    }

    @Unique
    private static boolean m3Tweaker$containsAll(NBTTagCompound ownedNbt, NBTTagCompound needNbt) {
        Set<?> keys = needNbt.func_150296_c();
        for (Object keyO : keys) {
            String key = (String) keyO;
            if (!ownedNbt.hasKey(key)) {
                return false;
            }

            NBTBase needValue = needNbt.getTag(key);
            NBTBase otherValue = ownedNbt.getTag(key);

            if (!m3Tweaker$deepContains(needValue, otherValue)) {
                return false;
            }
        }
        return true;
    }

    @Unique
    private static boolean m3Tweaker$deepContains(NBTBase need, NBTBase other) {
        if (need.getId() != other.getId()) {
            return false;
        }

        if (need.getId() == 10) {
            return m3Tweaker$containsAll((NBTTagCompound) other ,(NBTTagCompound) need);
        } else {
            return need.equals(other);
        }
    }

}

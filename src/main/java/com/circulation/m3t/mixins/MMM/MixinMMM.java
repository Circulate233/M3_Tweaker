package com.circulation.m3t.mixins.MMM;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.*;
import project.studio.manametalmod.MMM;

import java.util.Set;

@Mixin(value = MMM.class,remap = false)
public abstract class MixinMMM {

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

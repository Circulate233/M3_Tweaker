package com.circulation.m3t.mixins.MMM;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import project.studio.manametalmod.optool.MagicItemOP;

@Mixin(value = MagicItemOP.class,remap = false)
public class MixinMagicItemOP {

    /**
     * @author circulation
     * @reason 允许M3自定义饰品使用nbt定义需求的等级
     */
    @Overwrite
    public int getNeedLV(ItemStack item, EntityPlayer player) {
        if (item.hasTagCompound()) {
            if (item.getTagCompound().hasKey("level")) {
                return Math.max(item.getTagCompound().getInteger("level"), 1);
            }
        }
        return 1;
    }

}

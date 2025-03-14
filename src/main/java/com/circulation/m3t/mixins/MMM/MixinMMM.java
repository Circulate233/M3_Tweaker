package com.circulation.m3t.mixins.MMM;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.itemAndBlockCraft.ItemCraft3;

import java.util.Map;

import static com.circulation.m3t.crt.TrophyItemsHandler.*;

@Mixin(value = MMM.class,remap = false)
public class MixinMMM {

    /**
     * @author circulation
     * @reason 修改方法以支持自定义修改
     */
    @Overwrite
    public static ItemStack getTrophyItems() {
        int random = MMM.rand.nextInt(totalWeight + 1);

        if (TrophyItems.isEmpty()){
            for (Map.Entry<TrophyItem, Integer> out : defTrophyItems.entrySet()) {
                random -= out.getValue();
                if (random < 0) {
                    return out.getKey().getOutput();
                }
            }
        } else {
            for (Map.Entry<TrophyItem, Integer> out : TrophyItems.entrySet()) {
                random -= out.getValue();
                if (random < 0) {
                    return out.getKey().getOutput();
                }
            }
        }

        return new ItemStack(ItemCraft3.candy1, 1);
    }

    /**
     * @author circulation
     * @reason 修改方法以支持自定义修改
     */
    @Overwrite
    public static ItemStack getTrophyItemsLV2() {
        int random = MMM.rand.nextInt(totalWeightLv2 + 1);

        if (TrophyItemsLv2.isEmpty()){
            for (Map.Entry<TrophyItem, Integer> out : defTrophyItemsLv2.entrySet()) {
                random -= out.getValue();
                if (random < 0) {
                    return out.getKey().getOutput();
                }
            }
        } else {
            for (Map.Entry<TrophyItem, Integer> out : TrophyItemsLv2.entrySet()) {
                random -= out.getValue();
                if (random < 0) {
                    return out.getKey().getOutput();
                }
            }
        }

        return EnchantmentHelper.addRandomEnchantment(MMM.rand, new ItemStack(Items.book, 1, 0), 50);
    }

}

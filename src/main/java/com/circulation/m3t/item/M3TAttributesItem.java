package com.circulation.m3t.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.core.AttributesItem;
import project.studio.manametalmod.entity.nbt.ManaMetalModRoot;
import project.studio.manametalmod.items.ItemAttributesItemBase;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;

import java.util.List;

public class M3TAttributesItem extends ItemAttributesItemBase {

    private static class C{

    }

    public M3TAttributesItem(String name) {
        super(name,null, "random.burp");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List list, boolean p_77624_4_) {
        AttributesItem data = AttributesItem.get(this.type);
        if (data != null) {
            ManaMetalModRoot root = MMM.getEntityNBT(player);
            if (root != null) {
                list.add(MMM.getTranslateText("AttributesItemType.useitem.lore"));
                list.add(EnumChatFormatting.LIGHT_PURPLE + MMM.getTranslateText("AttributesItemType.maxextents.lore") + root.carrer.AttributesItemUseCount[data.type.ordinal()] + " / " + data.maxUse);
                IMagicEffect.addInformation(item, player, list, this.getItemEffect(data));
            }
        }

    }

}

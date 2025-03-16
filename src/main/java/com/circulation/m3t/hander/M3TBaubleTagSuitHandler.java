package com.circulation.m3t.hander;

import com.circulation.m3t.Util.Function;
import com.circulation.m3t.network.UpdateBauble;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.entity.nbt.NbtBaubles;
import project.studio.manametalmod.inventory.ContainerManaItem;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;

import java.util.*;

import static com.circulation.m3t.M3Tweaker.network;

public class M3TBaubleTagSuitHandler extends M3TBaublesSuitHandler {

    public static Map<String,String> Tags = new HashMap<>();

    public static void reload() {
        Tags.clear();
        map.clear();
        effmap.clear();
    }

    public static void registerEvents() {
        Function.onBaubleWearPostEvent(event -> {
            EntityPlayer player = event.player;
            if (player.worldObj.isRemote)return;
            NBTTagCompound playerNbt = player.getEntityData();
            if (event.item.hasTagCompound() && event.item.getTagCompound().hasKey(nbtName)) {
                String suitName = event.item.getTagCompound().getString(nbtName);
                if (map.containsKey(suitName)) {
                    int newE = 0;
                    if (!player.getEntityData().hasKey(nbtName)) {
                        playerNbt.setTag(nbtName, new NBTTagCompound());
                        playerNbt.getCompoundTag(nbtName).setInteger(suitName, 0);
                    } else {
                        NBTTagCompound suits = playerNbt.getCompoundTag(nbtName);
                        if (suits.hasKey(suitName)) {
                            effmap.get(suitName).get(getSuitQuantity(suitName, player)).effects.isEmpty();
                            NbtBaubles.setEffect(effmap.get(suitName).get(getSuitQuantity(suitName, player)).effects, MMM.getEntityNBT(player), new ItemStack(Items.apple), true, player);//删除旧套装属性
                            suits.setInteger(suitName, suits.getInteger(suitName) + 1);//更新套装状态
                            newE = suits.getInteger(suitName);
                        } else {
                            suits.setInteger(suitName, 1);
                        }
                    }
                    network.sendTo(new UpdateBauble(playerNbt.getCompoundTag(nbtName)), (EntityPlayerMP) player);//同步客户端防止显示问题
                    NbtBaubles.setEffect(effmap.get(suitName).get(newE).effects, MMM.getEntityNBT(player), new ItemStack(Items.apple), false, player);//应用新属性
                }
            }
        });

        //几乎同上
        Function.onBaubleDisrobePostEvent(event -> {
            EntityPlayer player = event.player;
            if (player.worldObj.isRemote)return;
            NBTTagCompound playerNbt = player.getEntityData();
            if (event.item.hasTagCompound() && event.item.getTagCompound().hasKey(nbtName)) {
                String suitName = event.item.getTagCompound().getString(nbtName);
                int newE = 0;
                if (map.containsKey(suitName)) {
                    if (!player.getEntityData().hasKey(nbtName)) {
                        playerNbt.setTag(nbtName, new NBTTagCompound());
                        playerNbt.getCompoundTag(nbtName).setInteger(suitName, 0);
                    } else {
                        NBTTagCompound suits = playerNbt.getCompoundTag(nbtName);
                        if (suits.hasKey(suitName)) {
                            effmap.get(suitName).get(getSuitQuantity(suitName, player)).effects.isEmpty();
                            NbtBaubles.setEffect(effmap.get(suitName).get(getSuitQuantity(suitName, player)).effects, MMM.getEntityNBT(player), new ItemStack(Items.apple), true, player);
                            suits.setInteger(suitName, Math.max(0, suits.getInteger(suitName) - 1));
                            newE = suits.getInteger(suitName);
                        } else {
                            suits.setInteger(suitName, 0);
                        }
                    }
                    network.sendTo(new UpdateBauble(playerNbt.getCompoundTag(nbtName)), (EntityPlayerMP) player);
                    NbtBaubles.setEffect(effmap.get(suitName).get(newE).effects, MMM.getEntityNBT(player), new ItemStack(Items.apple), false, player);//应用新属性
                }
            }
        });
    }

    public static class TagBaublesSuit extends BaublesSuit{
        public String TagName;

        public TagBaublesSuit(String TagName, String tooltip, List<IMagicEffect> effects) {
            super(tooltip,effects);
            this.TagName = TagName;
        }

    }

    public static class TagSuitHandler {
        protected String suitName;
        protected String tagName;
        protected Map<Integer, BaublesSuit> map = new LinkedHashMap<>();
        protected Map<Integer, BaublesSuit> effmap = new LinkedHashMap<>();

        public TagSuitHandler(String suitName) {
            this.suitName = suitName;
            for (int i = 0; i < ContainerManaItem.slots.length + 1; i++) {
                effmap.put(i,new TagBaublesSuit(tagName,tagName, Collections.emptyList()));
            }
            this.tagName = suitName;
        }

        public static TagSuitHandler create(String name) {
            return new TagSuitHandler(name);
        }

        public TagSuitHandler setTagName(String name){
            this.tagName = name;
            return this;
        }

        public TagSuitHandler addSuit(int quantity, String tooltip, List<IMagicEffect> effects) {
            Map<Integer, BaublesSuit> mapp = new HashMap<>();
            TagBaublesSuit suit = new TagBaublesSuit(tagName,tooltip,effects);
            this.map.put(quantity,suit);
            for (int i = quantity; i < ContainerManaItem.slots.length + 1; i++) {
                this.effmap.put(i,suit);
            }
            return this;
        }

        public void register(){
            M3TBaubleTagSuitHandler.Tags.put(this.suitName,this.tagName);
            M3TBaublesSuitHandler.map.put(this.suitName,map);
            M3TBaublesSuitHandler.effmap.put(this.suitName,effmap);
        }
    }
}

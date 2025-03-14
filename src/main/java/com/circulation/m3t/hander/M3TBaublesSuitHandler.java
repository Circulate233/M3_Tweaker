package com.circulation.m3t.hander;

import com.circulation.m3t.Util.Function;
import com.circulation.m3t.network.UpdateBauble;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.entity.nbt.NbtBaubles;
import project.studio.manametalmod.inventory.ContainerManaItem;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.circulation.m3t.M3Tweaker.network;

public class M3TBaublesSuitHandler {

    public static Map<String,Map<Integer,BaublesSuit>> map = new HashMap<>();
    public static Map<String,Map<Integer,BaublesSuit>> effmap = new HashMap<>();
    public static final String nbtName = "Suit";

    public static void registerEvents() {
        Function.onBaubleWearPostEvent(event -> {
            EntityPlayer player = event.player;
            if (player.worldObj.isRemote)return;
            NBTTagCompound playerNbt = player.getEntityData();
            String suitName = Item.itemRegistry.getNameForObject(event.item.getItem());
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
        });

        //几乎同上
        Function.onBaubleDisrobePostEvent(event -> {
            EntityPlayer player = event.player;
            if (player.worldObj.isRemote)return;
            NBTTagCompound playerNbt = player.getEntityData();
            String suitName = Item.itemRegistry.getNameForObject(event.item.getItem());
            if (map.containsKey(suitName)) {
                int newE = 0;
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
                NbtBaubles.setEffect(effmap.get(suitName).get(newE).effects, MMM.getEntityNBT(player), new ItemStack(Items.apple), false, player);
            }
        });
    }

    public static boolean hasSuit(String name){
        return map.containsKey(name);
    }

    public static Map<Integer,BaublesSuit> getSuit(String name){
        return map.get(name);
    }

    public static int getSuitQuantity(String suitName,EntityPlayer player){
        NBTTagCompound playerNbt = player.getEntityData();
        if (player.getEntityData().hasKey(nbtName)) {
            return playerNbt.getCompoundTag(nbtName).getInteger(suitName);
        } else {
            return 0;
        }
    }

    public static class BaublesSuit{
        public String tooltip;
        public List<IMagicEffect> effects;

        public BaublesSuit(String tooltip, List<IMagicEffect> effects) {
            this.tooltip = tooltip;
            this.effects = effects;
        }
    }

    public static class SuitHandler {
        protected String suitName;
        protected Map<Integer, BaublesSuit> map = new HashMap<>();
        protected Map<Integer, BaublesSuit> effmap = new HashMap<>();

        public SuitHandler(String suitName) {
            this.suitName = suitName;
            for (int i = 0; i < ContainerManaItem.slots.length + 1; i++) {
                effmap.put(i,new BaublesSuit(suitName, Collections.emptyList()));
            }
        }

        public static SuitHandler create(Item item) {
            return new SuitHandler(Item.itemRegistry.getNameForObject(item));
        }

        public static SuitHandler create(String name) {
            return new SuitHandler(name);
        }

        public SuitHandler addSuit(int quantity, String tooltip, List<IMagicEffect> effects) {
            Map<Integer, BaublesSuit> mapp = new HashMap<>();
            BaublesSuit suit = new BaublesSuit(tooltip,effects);
            this.map.put(quantity,suit);
            for (int i = quantity; i < ContainerManaItem.slots.length + 1; i++) {
                this.effmap.put(i,suit);
            }
            return this;
        }

        public void register(){
            M3TBaublesSuitHandler.map.put(this.suitName,map);
            M3TBaublesSuitHandler.effmap.put(this.suitName,effmap);
        }
    }
}

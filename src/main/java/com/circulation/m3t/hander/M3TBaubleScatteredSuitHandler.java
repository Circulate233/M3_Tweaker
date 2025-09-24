package com.circulation.m3t.hander;

import com.circulation.m3t.Util.Function;
import com.circulation.m3t.network.UpdateBauble;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.circulation.m3t.M3Tweaker.network;

public class M3TBaubleScatteredSuitHandler extends M3TBaublesSuitHandler {

    public static final Map<Scattered, List<String>> Scattereds = new Reference2ObjectOpenHashMap<>();

    public static void reload() {
        Scattereds.clear();
    }

    public static void registerEvents() {
        Function.onBaubleWearPostEvent(event -> {
            EntityPlayer player = event.player;
            if (player.worldObj.isRemote) return;
            NBTTagCompound playerNbt = player.getEntityData();
            if (Scattereds.containsKey(Scattered.getScattered(event.item))) {
                for (String suitName : Scattereds.get(Scattered.getScattered(event.item))) {
                    int newE = 1;
                    if (!player.getEntityData().hasKey(nbtName)) {
                        playerNbt.setTag(nbtName, new NBTTagCompound());
                        playerNbt.getCompoundTag(nbtName).setInteger(suitName, 1);
                    } else {
                        NBTTagCompound suits = playerNbt.getCompoundTag(nbtName);
                        if (suits.hasKey(suitName)) {
                            NbtBaubles.setEffect(effmap.get(suitName).get(getSuitQuantity(suitName, player)).effects, MMM.getEntityNBT(player), new ItemStack(Items.apple), true, player);//删除旧套装属性
                            suits.setInteger(suitName, suits.getInteger(suitName) + 1);//更新套装状态
                            newE = suits.getInteger(suitName);
                        } else {
                            suits.setInteger(suitName, 1);
                        }
                    }
                    NbtBaubles.setEffect(effmap.get(suitName).get(newE).effects, MMM.getEntityNBT(player), new ItemStack(Items.apple), false, player);//应用新属性
                }
                network.sendTo(new UpdateBauble(playerNbt.getCompoundTag(nbtName)), (EntityPlayerMP) player);//同步客户端防止显示问题
            }
        });

        Function.onBaubleDisrobePostEvent(event -> {
            EntityPlayer player = event.player;
            if (player.worldObj.isRemote) return;
            NBTTagCompound playerNbt = player.getEntityData();
            if (Scattereds.containsKey(Scattered.getScattered(event.item))) {
                for (String suitName : Scattereds.get(Scattered.getScattered(event.item))) {
                    int newE = 0;
                    if (player.getEntityData().hasKey(nbtName)) {
                        NBTTagCompound suits = playerNbt.getCompoundTag(nbtName);
                        if (suits.hasKey(suitName)) {
                            NbtBaubles.setEffect(effmap.get(suitName).get(getSuitQuantity(suitName, player)).effects, MMM.getEntityNBT(player), new ItemStack(Items.apple), true, player);
                            final var level = suits.getInteger(suitName) - 1;
                            if (level > 0) {
                                suits.setInteger(suitName, level);
                                newE = suits.getInteger(suitName);
                            } else {
                                suits.removeTag(suitName);
                            }
                        }
                    }
                    NbtBaubles.setEffect(effmap.get(suitName).get(newE).effects, MMM.getEntityNBT(player), new ItemStack(Items.apple), false, player);
                }
                network.sendTo(new UpdateBauble(playerNbt.getCompoundTag(nbtName)), (EntityPlayerMP) player);//同步客户端防止显示问题
            }
        });
    }

    public static class ScatteredSuitHandler {
        private final String suitName;
        private final List<Scattered> items = new ArrayList<>();
        private final Int2ObjectMap<BaublesSuit> map = new Int2ObjectLinkedOpenHashMap<>();
        private final Int2ObjectMap<BaublesSuit> effmap = new Int2ObjectLinkedOpenHashMap<>();

        protected ScatteredSuitHandler(String suitName) {
            this.suitName = suitName;
            for (int i = 0; i < ContainerManaItem.slots.length + 1; i++) {
                effmap.put(i, new BaublesSuit(suitName, Collections.emptyList()));
            }
        }

        public static ScatteredSuitHandler create(String name) {
            return new ScatteredSuitHandler(name);
        }

        public ScatteredSuitHandler addItem(ItemStack item) {
            this.items.add(Scattered.getScattered(item));
            return this;
        }

        public ScatteredSuitHandler addItems(ItemStack... item) {
            for (ItemStack itemStack : item) {
                this.items.add(Scattered.getScattered(itemStack));
            }
            return this;
        }

        public ScatteredSuitHandler addSuit(int quantity, String tooltip, List<IMagicEffect> effects) {
            BaublesSuit suit = new BaublesSuit(tooltip, effects);
            this.map.put(quantity, suit);
            for (int i = quantity; i < ContainerManaItem.slots.length + 1; i++) {
                this.effmap.put(i, suit);
            }
            return this;
        }

        public void register() {
            items.forEach(item -> {
                if (Scattereds.containsKey(item)) {
                    List<String> list = Scattereds.get(item);
                    list.add(this.suitName);
                    Scattereds.put(item, list);
                } else {
                    Scattereds.put(item, new ObjectArrayList<>(Collections.singletonList(this.suitName)));
                }
            });
            M3TBaublesSuitHandler.map.put(this.suitName, map);
            M3TBaublesSuitHandler.effmap.put(this.suitName, effmap);
        }
    }

    public static class Scattered {
        public Item item;
        public int meta;

        private static final Map<Item, Int2ReferenceMap<Scattered>> keyPool = new Reference2ObjectOpenHashMap<>();

        private Scattered(ItemStack itemStack) {
            this.item = itemStack.getItem();
            this.meta = itemStack.getItemDamage();
        }

        public static Scattered getScattered(ItemStack item) {
            return keyPool.computeIfAbsent(item.getItem(), k -> new Int2ReferenceArrayMap<>())
                .computeIfAbsent(item.getItemDamage(), m -> new Scattered(item));
        }
    }
}

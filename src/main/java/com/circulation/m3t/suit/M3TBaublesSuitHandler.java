package com.circulation.m3t.suit;

import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;

public class M3TBaublesSuitHandler {

    public static final String nbtName = "Suit";
    private static final Map<String, Int2ObjectSortedMap<BaublesSuit>> SUITS = new Object2ObjectOpenHashMap<>();

    public static void reload() {
        SUITS.clear();
    }

    public static boolean isEmpty() {
        return SUITS.isEmpty();
    }

    public static boolean hasSuit(String name) {
        return SUITS.containsKey(name);
    }

    public static Int2ObjectSortedMap<BaublesSuit> getSuit(String name) {
        return SUITS.get(name);
    }

    public static int getSuitQuantity(String suitName, EntityPlayer player) {
        if (player == null) {
            return 0;
        }
        NBTTagCompound data = player.getEntityData();
        return data.hasKey(nbtName, 10) ? data.getCompoundTag(nbtName)
            .getInteger(suitName) : 0;
    }

    static BaublesSuit getActiveTier(Int2ObjectSortedMap<BaublesSuit> tiers, int quantity) {
        if (quantity <= 0 || tiers.isEmpty() || tiers.firstIntKey() > quantity) {
            return null;
        }
        BaublesSuit active = null;
        for (var it = Int2ObjectSortedMaps.fastIterator(tiers); it.hasNext();) {
            var tier = it.next();
            if (tier.getIntKey() > quantity) {
                break;
            }
            active = tier.getValue();
        }
        return active;
    }

    public static class BaublesSuit {

        public final String tooltip;
        public final List<IMagicEffect> effects;

        public BaublesSuit(String tooltip, List<IMagicEffect> effects) {
            this.tooltip = tooltip;
            this.effects = effects.isEmpty() ? ObjectImmutableList.of() : new ObjectImmutableList<>(effects);
        }
    }

    public static class SuitHandler {

        protected final String suitName;
        private final Int2ObjectSortedMap<BaublesSuit> tiers = new Int2ObjectAVLTreeMap<>();

        protected SuitHandler(String suitName) {
            this.suitName = suitName;
        }

        public static SuitHandler create(Item item) {
            return create(Item.itemRegistry.getNameForObject(item));
        }

        public static SuitHandler create(String name) {
            return new SuitHandler(name);
        }

        public SuitHandler addSuit(int quantity, String tooltip, List<IMagicEffect> effects) {
            tiers.put(quantity, new BaublesSuit(tooltip, effects));
            return this;
        }

        public void register() {
            SUITS.put(suitName, Int2ObjectSortedMaps.unmodifiable(new Int2ObjectAVLTreeMap<>(tiers)));
        }
    }
}

package com.circulation.m3t.suit;

import static com.circulation.m3t.suit.M3TBaublesSuitHandler.nbtName;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;

public final class M3TBaubleTagSuitHandler {

    private static final Map<String, String> TAG_NAMES = new Object2ObjectOpenHashMap<>();

    private M3TBaubleTagSuitHandler() {}

    public static void reload() {
        TAG_NAMES.clear();
    }

    public static String getSuitName(ItemStack item) {
        NBTTagCompound tag = item.getTagCompound();
        if (!TAG_NAMES.isEmpty() && tag != null && tag.hasKey(nbtName, 8)) {
            String name = tag.getString(nbtName);
            if (TAG_NAMES.containsKey(name)) {
                return name;
            }
        }
        return null;
    }

    public static String getNameTag(ItemStack item) {
        String suitName = getSuitName(item);
        return suitName != null ? TAG_NAMES.get(suitName) : null;
    }

    public static class TagSuitHandler extends M3TBaublesSuitHandler.SuitHandler {

        private String tagName = " ";

        protected TagSuitHandler(String suitName) {
            super(suitName);
        }

        public static TagSuitHandler create(String name) {
            return new TagSuitHandler(name);
        }

        public TagSuitHandler setTagName(String name) {
            tagName = name;
            return this;
        }

        @Override
        public TagSuitHandler addSuit(int quantity, String tooltip, List<IMagicEffect> effects) {
            super.addSuit(quantity, tooltip, effects);
            return this;
        }

        @Override
        public void register() {
            super.register();
            TAG_NAMES.put(suitName, tagName);
        }
    }
}

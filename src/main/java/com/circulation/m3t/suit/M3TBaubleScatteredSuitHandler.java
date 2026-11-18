package com.circulation.m3t.suit;

import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;

public final class M3TBaubleScatteredSuitHandler {

    private static final Map<Item, Int2ObjectMap<ObjectList<String>>> MEMBERS = new Reference2ObjectOpenHashMap<>();

    private M3TBaubleScatteredSuitHandler() {}

    public static void reload() {
        MEMBERS.clear();
    }

    public static ObjectList<String> getSuits(ItemStack stack) {
        Int2ObjectMap<ObjectList<String>> variants = MEMBERS.get(stack.getItem());
        if (variants == null) {
            return ObjectLists.emptyList();
        }
        ObjectList<String> suits = variants.get(stack.getItemDamage());
        return suits != null ? suits : ObjectLists.emptyList();
    }

    public static class ScatteredSuitHandler extends M3TBaublesSuitHandler.SuitHandler {

        private final List<Member> members = new ObjectArrayList<>();

        protected ScatteredSuitHandler(String suitName) {
            super(suitName);
        }

        public static ScatteredSuitHandler create(String name) {
            return new ScatteredSuitHandler(name);
        }

        public ScatteredSuitHandler addItem(ItemStack item) {
            members.add(new Member(item.getItem(), item.getItemDamage()));
            return this;
        }

        public ScatteredSuitHandler addItems(ItemStack... items) {
            for (ItemStack item : items) {
                addItem(item);
            }
            return this;
        }

        @Override
        public ScatteredSuitHandler addSuit(int quantity, String tooltip, List<IMagicEffect> effects) {
            super.addSuit(quantity, tooltip, effects);
            return this;
        }

        @Override
        public void register() {
            super.register();
            for (Member member : members) {
                Int2ObjectMap<ObjectList<String>> variants = MEMBERS
                    .computeIfAbsent(member.item, ignored -> new Int2ObjectOpenHashMap<>(2));
                ObjectList<String> suits = variants.computeIfAbsent(member.meta, ignored -> new ObjectArrayList<>(1));
                if (!suits.contains(suitName)) {
                    suits.add(suitName);
                }
            }
        }
    }

    private static final class Member {

        private final Item item;
        private final int meta;

        private Member(Item item, int meta) {
            this.item = item;
            this.meta = meta;
        }
    }
}

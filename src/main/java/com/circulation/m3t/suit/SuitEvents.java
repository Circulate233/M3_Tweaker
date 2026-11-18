package com.circulation.m3t.suit;

import static com.circulation.m3t.suit.M3TBaublesSuitHandler.nbtName;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.circulation.m3t.event.BaubleEquipmentEvent;
import com.circulation.m3t.network.UpdateBauble;
import com.circulation.m3t.suit.M3TBaublesSuitHandler.BaublesSuit;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.entity.nbt.ManaMetalModRoot;
import project.studio.manametalmod.entity.nbt.NbtBaubles;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;

public final class SuitEvents {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onWear(BaubleEquipmentEvent.WearPost event) {
        update(event.player, event.item, 1);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onDisrobe(BaubleEquipmentEvent.DisrobePost event) {
        update(event.player, event.item, -1);
    }

    private static void update(EntityPlayer player, ItemStack stack, int delta) {
        if (M3TBaublesSuitHandler.isEmpty()) {
            return;
        }
        String registryName = Item.itemRegistry.getNameForObject(stack.getItem());
        Int2ObjectSortedMap<BaublesSuit> regularSuit = M3TBaublesSuitHandler.getSuit(registryName);
        String tagSuit = M3TBaubleTagSuitHandler.getSuitName(stack);
        ObjectList<String> scatteredSuits = M3TBaubleScatteredSuitHandler.getSuits(stack);
        if (regularSuit == null && tagSuit == null && scatteredSuits.isEmpty()) {
            return;
        }

        NBTTagCompound entityData = player.getEntityData();
        boolean hasSuits = entityData.hasKey(nbtName, 10);
        if (!hasSuits && delta < 0) {
            return;
        }
        NBTTagCompound quantities = entityData.getCompoundTag(nbtName);
        boolean changed = updateSuit(player, quantities, registryName, regularSuit, delta);
        if (tagSuit != null && !tagSuit.equals(registryName)) {
            changed |= updateSuit(player, quantities, tagSuit, M3TBaublesSuitHandler.getSuit(tagSuit), delta);
        }
        for (int i = 0, size = scatteredSuits.size(); i < size; i++) {
            String suit = scatteredSuits.get(i);
            if (!suit.equals(registryName) && !suit.equals(tagSuit)) {
                changed |= updateSuit(player, quantities, suit, M3TBaublesSuitHandler.getSuit(suit), delta);
            }
        }
        if (changed) {
            if (!hasSuits) {
                entityData.setTag(nbtName, quantities);
            }
            if (player instanceof EntityPlayerMP serverPlayer) {
                UpdateBauble.sendTo(serverPlayer, quantities);
            }
        }
    }

    private static boolean updateSuit(EntityPlayer player, NBTTagCompound quantities, String name,
        Int2ObjectSortedMap<BaublesSuit> tiers, int delta) {
        if (tiers == null) {
            return false;
        }
        int before = quantities.getInteger(name);
        int after = Math.max(0, before + delta);
        if (before == after) {
            return false;
        }

        BaublesSuit oldTier = M3TBaublesSuitHandler.getActiveTier(tiers, before);
        BaublesSuit newTier = M3TBaublesSuitHandler.getActiveTier(tiers, after);
        if (oldTier != newTier) {
            ManaMetalModRoot root = MMM.getEntityNBT(player);
            applyTier(player, root, oldTier, -1);
            applyTier(player, root, newTier, 1);
        }
        if (after == 0) {
            quantities.removeTag(name);
        } else {
            quantities.setInteger(name, after);
        }
        return true;
    }

    private static void applyTier(EntityPlayer player, ManaMetalModRoot root, BaublesSuit tier, int sign) {
        if (tier == null || root == null) {
            return;
        }
        // Apply just the suit effects; the accessory itself already supplies its own fly-time modifier.
        for (int i = 0, size = tier.effects.size(); i < size; i++) {
            IMagicEffect effect = tier.effects.get(i);
            NbtBaubles.apply(effect.getType(), root, sign * effect.getValue(), player);
        }
    }
}

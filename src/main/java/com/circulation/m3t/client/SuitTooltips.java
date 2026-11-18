package com.circulation.m3t.client;

import static com.circulation.m3t.Util.Function.getText;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import org.lwjgl.input.Keyboard;

import com.circulation.m3t.M3TConfig;
import com.circulation.m3t.suit.M3TBaubleScatteredSuitHandler;
import com.circulation.m3t.suit.M3TBaubleTagSuitHandler;
import com.circulation.m3t.suit.M3TBaublesSuitHandler;
import com.circulation.m3t.suit.M3TBaublesSuitHandler.BaublesSuit;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMaps;
import it.unimi.dsi.fastutil.objects.ObjectList;
import project.studio.manametalmod.api.weapon.IMagicItem;
import project.studio.manametalmod.archeology.ItemAntiquitiesBase;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;
import project.studio.manametalmod.magic.magicItem.MagicItemPearl;

public final class SuitTooltips {

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (M3TBaublesSuitHandler.isEmpty()) {
            return;
        }
        ItemStack stack = event.itemStack;
        Item item = stack.getItem();
        if (!(item instanceof IMagicItem || item instanceof MagicItemPearl || item instanceof ItemAntiquitiesBase)) {
            return;
        }
        String registryName = Item.itemRegistry.getNameForObject(item);
        Int2ObjectSortedMap<BaublesSuit> regularSuit = M3TBaublesSuitHandler.getSuit(registryName);
        String tagSuit = M3TBaubleTagSuitHandler.getSuitName(stack);
        ObjectList<String> scattered = M3TBaubleScatteredSuitHandler.getSuits(stack);
        if (regularSuit == null && tagSuit == null && scattered.isEmpty()) {
            return;
        }

        boolean keyboard = Keyboard.isCreated();
        boolean shift = keyboard
            && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT));
        boolean alt = keyboard && (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU));
        boolean showAll = M3TConfig.showAllSuitEffects || (shift && alt);
        boolean compact = !showAll && (shift || alt);

        List<String> lines = event.toolTip;
        EntityPlayer player = event.entityPlayer;
        lines.add(getText("bauble.tooltip"));
        appendSuit(lines, player, registryName, regularSuit, showAll, compact);
        if (tagSuit != null && !tagSuit.equals(registryName)) {
            appendSuit(lines, player, tagSuit, M3TBaublesSuitHandler.getSuit(tagSuit), showAll, compact);
        }
        for (int i = 0, size = scattered.size(); i < size; i++) {
            String name = scattered.get(i);
            if (!name.equals(registryName) && !name.equals(tagSuit)) {
                appendSuit(lines, player, name, M3TBaublesSuitHandler.getSuit(name), showAll, compact);
            }
        }
        if (!showAll && !compact) {
            lines.add(getText("bauble.key"));
        }
        lines.add(getText("bauble.endtip"));
    }

    private static void appendSuit(List<String> lines, EntityPlayer player, String name,
        Int2ObjectSortedMap<BaublesSuit> tiers, boolean showAll, boolean compact) {
        if (tiers == null || tiers.isEmpty()) {
            return;
        }
        int quantity = M3TBaublesSuitHandler.getSuitQuantity(name, player);
        int currentLevel = 0;
        BaublesSuit current = null;
        for (var it = Int2ObjectSortedMaps.fastIterator(tiers); it.hasNext();) {
            var tier = it.next();
            int level = tier.getIntKey();
            if (showAll) {
                appendTier(lines, tier.getValue(), level, quantity, true);
            } else if (level <= quantity) {
                currentLevel = level;
                current = tier.getValue();
            } else {
                if (current != null) {
                    appendTier(lines, current, currentLevel, quantity, !compact);
                    current = null;
                }
                appendTier(lines, tier.getValue(), level, quantity, !compact);
                if (!compact) {
                    break;
                }
            }
        }
        if (current != null) {
            appendTier(lines, current, currentLevel, quantity, !compact);
        }
    }

    private static void appendTier(List<String> lines, BaublesSuit tier, int level, int quantity, boolean showEffects) {
        boolean unlocked = quantity >= level;
        EnumChatFormatting color = unlocked ? EnumChatFormatting.GOLD : EnumChatFormatting.GRAY;
        lines.add(color + tier.tooltip + color + "(" + Math.min(level, quantity) + "/" + level + ")");
        if (showEffects) {
            for (int i = 0, size = tier.effects.size(); i < size; i++) {
                IMagicEffect effect = tier.effects.get(i);
                float value = effect.getValue();
                EnumChatFormatting effectColor = !unlocked ? EnumChatFormatting.GRAY
                    : value < 0.0f ? EnumChatFormatting.RED : EnumChatFormatting.AQUA;
                IMagicEffect.applyInfo(lines, effect.getType(), effectColor, value, value < 0.0f ? "" : "+");
            }
        }
    }
}

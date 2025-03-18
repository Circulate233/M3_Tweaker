package com.circulation.m3t.Util;

import com.circulation.m3t.M3TConfig;
import com.circulation.m3t.hander.M3TBaublesSuitHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Unique;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;

import java.util.List;
import java.util.Map;

public class SuitTooltips {

    public static void handleSuitInfo(ItemStack item, EntityPlayer player, List<String> list, String suitName) {
        if (!M3TBaublesSuitHandler.hasSuit(suitName)) return;

        final Map<Integer, M3TBaublesSuitHandler.BaublesSuit> suits = M3TBaublesSuitHandler.getSuit(suitName);
        final int quantity = M3TBaublesSuitHandler.getSuitQuantity(suitName, player);

        if (M3TConfig.showAllSuitEffects || (isShift() && isAlt())) {
            suits.forEach((lv, suit) -> addSuitEntry(item, player, list, lv, suit, quantity, true));
        } else if (!isShift() && !isAlt()) {
            handleDefaultMode(item, player, list, suits, quantity);
        } else {
            handleOtherKeyMode(item, player, list, suits, quantity);
        }
    }

    public static void handleDefaultMode(ItemStack item, EntityPlayer player, List<String> list,
                                             Map<Integer, M3TBaublesSuitHandler.BaublesSuit> suits, int quantity) {
        Map.Entry<Integer, M3TBaublesSuitHandler.BaublesSuit> currentSuit = null;
        Map.Entry<Integer, M3TBaublesSuitHandler.BaublesSuit> nextSuit = null;

        for (Map.Entry<Integer, M3TBaublesSuitHandler.BaublesSuit> entry : suits.entrySet()) {
            int lv = entry.getKey();
            if (quantity >= lv) {
                currentSuit = entry;
            } else {
                nextSuit = entry;
                break;
            }
        }

        if (currentSuit != null) {
            addSuitEntry(item, player, list, currentSuit.getKey(), currentSuit.getValue(), quantity, true);
        }
        if (nextSuit != null) {
            addSuitEntry(item, player, list, nextSuit.getKey(), nextSuit.getValue(), quantity, true);
        }
    }

    public static void handleOtherKeyMode(ItemStack item, EntityPlayer player, List<String> list,
                                              Map<Integer, M3TBaublesSuitHandler.BaublesSuit> suits, int quantity) {
        Map.Entry<Integer, M3TBaublesSuitHandler.BaublesSuit> lastValidSuit = null;
        for (Map.Entry<Integer, M3TBaublesSuitHandler.BaublesSuit> entry : suits.entrySet()) {
            int lv = entry.getKey();
            if (quantity >= lv) {
                lastValidSuit = entry;
            } else {
                if (lastValidSuit != null) {
                    addSuitEntry(item, player, list, lastValidSuit.getKey(), lastValidSuit.getValue(), quantity, false);
                    lastValidSuit = null;
                }
                addSuitEntry(item, player, list, lv, entry.getValue(), quantity, false);
            }
        }
        if (lastValidSuit != null) {
            addSuitEntry(item, player, list, lastValidSuit.getKey(), lastValidSuit.getValue(), quantity, false);
        }
    }

    public static void addSuitEntry(ItemStack item, EntityPlayer player, List<String> list,
                                        Integer lv, M3TBaublesSuitHandler.BaublesSuit suit,
                                        int quantity, boolean showEffects) {
        boolean isUnlocked = quantity >= lv;
        EnumChatFormatting color = isUnlocked ? EnumChatFormatting.GOLD : EnumChatFormatting.GRAY;
        String progress = isUnlocked ? "(" + lv + "/" + lv + ")" : "(" + quantity + "/" + lv + ")";
        list.add(color + suit.tooltip + color + progress);

        if (showEffects) {
            addInformation(item, player, list, suit.effects, !isUnlocked);
        }
    }

    public static boolean isShift(){
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public static boolean isAlt(){
        return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
    }

    public static void addInformation(ItemStack item, EntityPlayer player, List ListInfo, List<IMagicEffect> effects, boolean gray) {
        if (effects != null && !effects.isEmpty()) {
            for (IMagicEffect ime : effects) {
                EnumChatFormatting color = EnumChatFormatting.AQUA;
                float value = ime.getValue();
                String Modified = "+";
                if (gray) {
                    color = EnumChatFormatting.GRAY;
                } else if (value < 0.0F) {
                    color = EnumChatFormatting.RED;
                }
                if (value < 0.0F) {
                    Modified = "";
                }

                IMagicEffect.applyInfo(ListInfo, ime.getType(), color, value, Modified);
            }
        }

    }
}

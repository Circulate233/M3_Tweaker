package com.circulation.m3t.mixins.MMM;

import com.circulation.m3t.M3TConfig;
import com.circulation.m3t.Util.Function;
import com.circulation.m3t.crt.events.BaubleEvent;
import com.circulation.m3t.crt.events.M3TEventAPI;
import com.circulation.m3t.hander.M3TBaubleScatteredSuitHandler;
import com.circulation.m3t.hander.M3TBaubleTagSuitHandler;
import com.circulation.m3t.hander.M3TBaublesSuitHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import project.studio.manametalmod.api.IQualityItem;
import project.studio.manametalmod.api.ISpecialItem;
import project.studio.manametalmod.api.weapon.IMagicItem;
import project.studio.manametalmod.entity.nbt.ManaMetalModRoot;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.circulation.m3t.hander.M3TBaubleScatteredSuitHandler.Scattereds;
import static com.circulation.m3t.hander.M3TBaublesSuitHandler.nbtName;

@Mixin(IMagicItem.class)
public abstract class MixinIMagicItem extends Item implements ISpecialItem, IQualityItem {

    @Unique
    private EntityPlayer m3Tweaker$player;

    @Inject(method = "onItemRightClick",at = @At("HEAD"))
    public void onItemRightClick(ItemStack item, World world, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        this.m3Tweaker$player = player;
    }

    @Inject(method = "tryPushItem",at = @At(value = "INVOKE", target = "Lproject/studio/manametalmod/entity/nbt/NbtBaubles;setInventorySlotContents(ILnet/minecraft/item/ItemStack;)V",shift = At.Shift.BEFORE), cancellable = true)
    public void tryPushItem(int slot, ManaMetalModRoot root, ItemStack item, World world, CallbackInfoReturnable<Boolean> cir) {
        if (this.m3Tweaker$player == null)return;
        BaubleEvent event = new BaubleEvent(this.m3Tweaker$player, item);
        M3TEventAPI.publishAllWear(event);
        if (event.isCancel()){
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "addInformation",at = @At("TAIL"))
    public void addInformation(ItemStack item, EntityPlayer player, List finallist, boolean booleans, CallbackInfo ci) {
        String registrySuitName = Item.itemRegistry.getNameForObject(this);
        boolean hasSuit = false;
        List list = new ArrayList();
        if (M3TBaublesSuitHandler.hasSuit(registrySuitName)) {
            m3Tweaker$handleSuitInfo(item, player, list, registrySuitName);
            hasSuit = true;
        }

        String nbtSuit = null;
        if (item.hasTagCompound() && item.getTagCompound().hasKey(nbtName)) {
            String tagSuitName = item.getTagCompound().getString(nbtName);
            if (M3TBaubleTagSuitHandler.Tags.containsKey(tagSuitName)){
                m3Tweaker$handleSuitInfo(item, player, list, tagSuitName);
                nbtSuit = tagSuitName;
                hasSuit = true;
            }
        }

        if (Scattereds.containsKey(M3TBaubleScatteredSuitHandler.Scattered.getScattered(item))) {
            final String finalNbtSuit = nbtSuit;
            Scattereds.get(M3TBaubleScatteredSuitHandler.Scattered.getScattered(item))
                .stream().filter(suitName -> finalNbtSuit == null || !finalNbtSuit.equals(suitName))
                .forEach(suitName -> m3Tweaker$handleSuitInfo(item, player, list, suitName));
            hasSuit = true;
        }

        if (!M3TConfig.showAllSuitEffects && hasSuit && !m3Tweaker$isShift() && !m3Tweaker$isAlt()) {
            list.add(Function.getText("bauble.key"));
        }

        if (hasSuit){
            finallist.add(Function.getText("bauble.tooltip"));
            finallist.addAll(list);
            finallist.add(Function.getText("bauble.endtip"));
        }
    }

    @Unique
    private void m3Tweaker$handleSuitInfo(ItemStack item, EntityPlayer player, List<String> list, String suitName) {
        if (!M3TBaublesSuitHandler.hasSuit(suitName)) return;

        final Map<Integer, M3TBaublesSuitHandler.BaublesSuit> suits = M3TBaublesSuitHandler.getSuit(suitName);
        final int quantity = M3TBaublesSuitHandler.getSuitQuantity(suitName, player);

        if (M3TConfig.showAllSuitEffects || (m3Tweaker$isShift() && m3Tweaker$isAlt())) {
            suits.forEach((lv, suit) -> m3Tweaker$addSuitEntry(item, player, list, lv, suit, quantity, true));
        } else if (!m3Tweaker$isShift() && !m3Tweaker$isAlt()) {
            m3Tweaker$handleDefaultMode(item, player, list, suits, quantity);
        } else {
            m3Tweaker$handleOtherKeyMode(item, player, list, suits, quantity);
        }
    }

    @Unique
    private void m3Tweaker$handleDefaultMode(ItemStack item, EntityPlayer player, List<String> list,
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
            m3Tweaker$addSuitEntry(item, player, list, currentSuit.getKey(), currentSuit.getValue(), quantity, true);
        }
        if (nextSuit != null) {
            m3Tweaker$addSuitEntry(item, player, list, nextSuit.getKey(), nextSuit.getValue(), quantity, true);
        }
    }

    @Unique
    private void m3Tweaker$handleOtherKeyMode(ItemStack item, EntityPlayer player, List<String> list,
                                              Map<Integer, M3TBaublesSuitHandler.BaublesSuit> suits, int quantity) {
        Map.Entry<Integer, M3TBaublesSuitHandler.BaublesSuit> lastValidSuit = null;
        for (Map.Entry<Integer, M3TBaublesSuitHandler.BaublesSuit> entry : suits.entrySet()) {
            int lv = entry.getKey();
            if (quantity >= lv) {
                lastValidSuit = entry;
            } else {
                if (lastValidSuit != null) {
                    m3Tweaker$addSuitEntry(item, player, list, lastValidSuit.getKey(), lastValidSuit.getValue(), quantity, false);
                    lastValidSuit = null;
                }
                m3Tweaker$addSuitEntry(item, player, list, lv, entry.getValue(), quantity, false);
            }
        }
        if (lastValidSuit != null) {
            m3Tweaker$addSuitEntry(item, player, list, lastValidSuit.getKey(), lastValidSuit.getValue(), quantity, false);
        }
    }

    @Unique
    private void m3Tweaker$addSuitEntry(ItemStack item, EntityPlayer player, List<String> list,
                                        Integer lv, M3TBaublesSuitHandler.BaublesSuit suit,
                                        int quantity, boolean showEffects) {
        boolean isUnlocked = quantity >= lv;
        EnumChatFormatting color = isUnlocked ? EnumChatFormatting.GOLD : EnumChatFormatting.GRAY;
        String progress = isUnlocked ? "(" + lv + "/" + lv + ")" : "(" + quantity + "/" + lv + ")";
        list.add(color + suit.tooltip + color + progress);

        if (showEffects) {
            m3Tweaker$addInformation(item, player, list, suit.effects, !isUnlocked);
        }
    }

    @Unique
    private static boolean m3Tweaker$isShift(){
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    @Unique
    private static boolean m3Tweaker$isAlt(){
        return Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU);
    }

    @Unique
    private static void m3Tweaker$addInformation(ItemStack item, EntityPlayer player, List ListInfo, List<IMagicEffect> effects, boolean gray) {
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

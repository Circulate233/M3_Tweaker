package com.circulation.m3t.mixins.MMM;

import com.circulation.m3t.crt.events.BaubleEvent;
import com.circulation.m3t.crt.events.M3TEventAPI;
import com.circulation.m3t.hander.M3TBaublesSuitHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

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
    public void addInformation(ItemStack item, EntityPlayer player, List list, boolean booleans, CallbackInfo ci) {
        String suitName = Item.itemRegistry.getNameForObject(this);
        if (M3TBaublesSuitHandler.hasSuit(suitName)) {
            M3TBaublesSuitHandler.getSuit(suitName).forEach((lv,suit) -> {
                int HasQuantity = M3TBaublesSuitHandler.getSuitQuantity(suitName,player);
                list.add((HasQuantity >= lv ? EnumChatFormatting.GOLD : EnumChatFormatting.GRAY) + suit.tooltip + "(" + Math.min(HasQuantity,lv) + "/" + lv + ")");
                m3Tweaker$addInformation(item, player, list, suit.effects,HasQuantity < lv);
            });
        }
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
                } else {
                    if (value < 0.0F) {
                        color = EnumChatFormatting.RED;
                        Modified = "-";
                    }
                }

                IMagicEffect.applyInfo(ListInfo, ime.getType(), color, value, Modified);
            }
        }

    }
}

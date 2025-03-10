package com.circulation.m3t.mixins.MMM;

import com.circulation.m3t.crt.events.BaubleEvent;
import com.circulation.m3t.crt.events.M3TEventAPI;
import minetweaker.api.util.Position3f;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import project.studio.manametalmod.api.weapon.IMagicItem;
import project.studio.manametalmod.entity.nbt.ManaMetalModRoot;

@Mixin(IMagicItem.class)
public class MixinIMagicItem {

    @Unique
    private EntityPlayer m3Tweaker$player;

    @Inject(method = "onItemRightClick",at = @At("HEAD"))
    public void onItemRightClick(ItemStack item, World world, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir) {
        this.m3Tweaker$player = player;
    }

    @Inject(method = "tryPushItem",at = @At(value = "INVOKE", target = "Lproject/studio/manametalmod/entity/nbt/NbtBaubles;setInventorySlotContents(ILnet/minecraft/item/ItemStack;)V",shift = At.Shift.BEFORE), cancellable = true)
    public void tryPushItem(int slot, ManaMetalModRoot root, ItemStack item, World world, CallbackInfoReturnable<Boolean> cir) {
        if (this.m3Tweaker$player == null)return;
        BaubleEvent event = new BaubleEvent(this.m3Tweaker$player, item,new Position3f((float) this.m3Tweaker$player.posX, (float) this.m3Tweaker$player.posY, (float) this.m3Tweaker$player.posZ),this.m3Tweaker$player.worldObj);
        M3TEventAPI.publishAllWear(event);
        if (event.isCancel()){
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}

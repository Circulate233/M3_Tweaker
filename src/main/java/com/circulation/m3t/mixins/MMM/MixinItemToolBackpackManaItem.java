package com.circulation.m3t.mixins.MMM;

import com.circulation.m3t.crt.events.BaubleEvent;
import com.circulation.m3t.crt.events.M3TEventAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import project.studio.manametalmod.api.weapon.IMagicItem;
import project.studio.manametalmod.entity.nbt.ManaMetalModRoot;
import project.studio.manametalmod.items.ItemToolBackpackManaItem;

import java.util.HashSet;
import java.util.Set;

@Mixin(value = ItemToolBackpackManaItem.class,remap = false)
public abstract class MixinItemToolBackpackManaItem {

    @Unique
    public Set<Integer> m3Tweaker$sss = new HashSet<>();
    @Unique
    public NBTTagList m3Tweaker$nbttaglist;
    @Unique
    public byte m3Tweaker$slotIndex;

    @Inject(method = "canWear",at = @At("RETURN"),cancellable = true)
    public void canWearMixin(ItemStack item, EntityPlayer player, World world, ManaMetalModRoot root, CallbackInfoReturnable<Boolean> cir) {
        if (item.getItem() instanceof IMagicItem && cir.getReturnValue()) {
            BaubleEvent event = new BaubleEvent(player,item);
            M3TEventAPI.publishAllWear(event);
            cir.setReturnValue(!event.isCancel());
        }
    }

    @Redirect(method = "wear",at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;getTagList(Ljava/lang/String;I)Lnet/minecraft/nbt/NBTTagList;",ordinal = 0,remap = true))
    public NBTTagList wearMixin(NBTTagCompound instance, String exceptionalism, int i) {
        this.m3Tweaker$nbttaglist = instance.getTagList(exceptionalism, i);
        return this.m3Tweaker$nbttaglist;
    }

    @Redirect(method = "wear",at = @At(value = "INVOKE", target = "Lproject/studio/manametalmod/items/ItemToolBackpackManaItem;disrobeItems(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lproject/studio/manametalmod/entity/nbt/ManaMetalModRoot;I)V",ordinal = 0))
    public void wearMixin0(ItemToolBackpackManaItem instance, ItemStack item, World world, EntityPlayer player, ManaMetalModRoot root, int s) {
        BaubleEvent event = new BaubleEvent(player,item);
        M3TEventAPI.publishAllDisrobe(event);
        if (event.isCancel()) {
            m3Tweaker$sss.add(s);
        } else {
            instance.disrobeItems(root.item.items[s], world, player, root, s);
        }
    }

    @Redirect(method = "wear",at = @At(value = "INVOKE", target = "Lproject/studio/manametalmod/items/ItemToolBackpackManaItem;wearitems(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lproject/studio/manametalmod/entity/nbt/ManaMetalModRoot;I)V",ordinal = 0))
    public void wearMixin1(ItemToolBackpackManaItem instance, ItemStack item, World world, EntityPlayer player, ManaMetalModRoot root, int s) {
        if (!m3Tweaker$sss.contains(s)) {
            this.wearitems(item, world, player, root, s);
        }
    }

    @Redirect(method = "wear",at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagList;appendTag(Lnet/minecraft/nbt/NBTBase;)V",ordinal = 0,remap = true))
    public void wearMixin2(NBTTagList instance, NBTBase nbt) {
        NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbt;
        if (!m3Tweaker$sss.contains(nbttagcompound1.getInteger("Slot"))) {
            instance.appendTag(nbttagcompound1);
        } else {
            int s = nbttagcompound1.getInteger("Slot");
            NBTTagCompound tagtemp = this.m3Tweaker$nbttaglist.getCompoundTagAt(s);
            m3Tweaker$sss.remove(s);
            instance.appendTag(tagtemp);
        }
    }

    @Redirect(method = "wear",at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;loadItemStackFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/item/ItemStack;",remap = true,ordinal = 3))
    public ItemStack wearMixin3(NBTTagCompound nbtTagCompound){
        this.m3Tweaker$slotIndex = nbtTagCompound.getByte("Slot");
        return ItemStack.loadItemStackFromNBT(nbtTagCompound);
    }

    @Redirect(method = "wear",at = @At(value = "INVOKE", target = "Lproject/studio/manametalmod/items/ItemToolBackpackManaItem;canWear(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lproject/studio/manametalmod/entity/nbt/ManaMetalModRoot;)Z",ordinal = 1))
    public boolean wearMixin3(ItemToolBackpackManaItem instance, ItemStack item, EntityPlayer player, World world, ManaMetalModRoot root) {
        final boolean canWear = instance.canWear(item, player, world, root);
        if (canWear){
            if (root.item.getStackInSlot(this.m3Tweaker$slotIndex) != null){
                BaubleEvent event = new BaubleEvent(player,root.item.getStackInSlot(this.m3Tweaker$slotIndex));
                M3TEventAPI.publishAllDisrobe(event);
                return !event.isCancel();
            }
        }
        return false;
    }

    @Shadow
    public abstract void wearitems(ItemStack item, World world, EntityPlayer player, ManaMetalModRoot root, int slotindex);
}

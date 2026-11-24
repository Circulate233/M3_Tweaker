package com.circulation.m3t.mixins.mmm;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.circulation.m3t.M3Tweaker;
import com.circulation.m3t.crt.recipes.CastingHandler;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import project.studio.manametalmod.items.craftingRecipes.CastingData;
import project.studio.manametalmod.network.MessageCastingTable;

@Mixin(value = MessageCastingTable.class, remap = false)
public class MixinMessageCastingTable {

    @Shadow
    private int ID;
    @Shadow
    private int itemNum;

    @Inject(method = "fromBytes", at = @At("TAIL"), require = 1)
    private void m3t$readOutput(ByteBuf buf, CallbackInfo ci) {
        if (this.ID == 0) {
            ItemStack output = ByteBufUtils.readItemStack(buf);
            this.itemNum = CastingHandler.findRecipe(output);
            if (this.itemNum < 0) {
                M3Tweaker.logger.warn("Rejected a casting selection with no matching recipe for {}", output);
            }
        }
    }

    @Inject(method = "toBytes", at = @At("TAIL"), require = 1)
    private void m3t$writeOutput(ByteBuf buf, CallbackInfo ci) {
        if (this.ID == 0) {
            ByteBufUtils.writeItemStack(buf, (ItemStack) CastingData.getItems(this.itemNum)[1]);
        }
    }

    @Inject(
        method = "onMessage(Lproject/studio/manametalmod/network/MessageCastingTable;Lcpw/mods/fml/common/network/simpleimpl/MessageContext;)Lcpw/mods/fml/common/network/simpleimpl/IMessage;",
        at = @At("HEAD"),
        cancellable = true,
        require = 1)
    private void m3t$rejectMissingRecipe(MessageCastingTable message, MessageContext context,
        CallbackInfoReturnable<IMessage> cir) {
        MixinMessageCastingTable decoded = (MixinMessageCastingTable) (Object) message;
        if (decoded.ID == 0 && decoded.itemNum < 0) {
            cir.setReturnValue(null);
        }
    }
}

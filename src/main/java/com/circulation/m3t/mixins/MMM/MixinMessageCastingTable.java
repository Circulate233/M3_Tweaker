package com.circulation.m3t.mixins.MMM;

import com.circulation.m3t.crt.recipes.CastingHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import project.studio.manametalmod.items.craftingRecipes.CastingData;
import project.studio.manametalmod.network.MessageCastingTable;

@Mixin(value = MessageCastingTable.class,remap = false)
public class MixinMessageCastingTable {

    @Shadow
    private int ID;
    @Shadow
    private int itemNum;

    @Unique
    private ItemStack m3Tweaker$output;

    @Inject(method = "fromBytes", at = @At("TAIL"))
    public void fromBytes(ByteBuf buf, CallbackInfo ci) {
        if (this.ID == 0) {
            this.m3Tweaker$output = ByteBufUtils.readItemStack(buf);
            CastingHandler.ItemKey key = CastingHandler.ItemKey.getItemKey(this.m3Tweaker$output);
            this.itemNum = CastingHandler.mapCastingRecipes.getInt(key);
        }
    }

    @Inject(method = "toBytes", at = @At("TAIL"))
    public void toBytes(ByteBuf buf, CallbackInfo ci) {
        if (this.ID == 0) {
            this.m3Tweaker$output = (ItemStack) CastingData.getItems(this.itemNum)[1];
            ByteBufUtils.writeItemStack(buf, this.m3Tweaker$output);
        }
    }
}

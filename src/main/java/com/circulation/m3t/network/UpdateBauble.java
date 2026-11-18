package com.circulation.m3t.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import com.circulation.m3t.M3Tweaker;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class UpdateBauble implements IMessage {

    private NBTTagCompound nbt;

    public UpdateBauble() {}

    private UpdateBauble(NBTTagCompound nbt) {
        this.nbt = (NBTTagCompound) nbt.copy();
    }

    public static void sendTo(EntityPlayerMP player, NBTTagCompound suits) {
        if (player.playerNetServerHandler != null) {
            M3Tweaker.network.sendTo(new UpdateBauble(suits), player);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.nbt = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.nbt);
    }

    public static class Handler implements IMessageHandler<UpdateBauble, IMessage> {

        @Override
        public IMessage onMessage(UpdateBauble message, MessageContext ctx) {
            if (ctx.side.isClient() && message.nbt != null) {
                M3Tweaker.proxy.receiveSuitUpdate(message.nbt);
            }
            return null;
        }
    }
}

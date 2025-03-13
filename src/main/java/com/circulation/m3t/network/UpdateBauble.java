package com.circulation.m3t.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import static com.circulation.m3t.hander.M3TBaublesSuitHandler.nbtName;

public class UpdateBauble implements IMessage {

    private NBTTagCompound nbt;

    public UpdateBauble(){}

    public UpdateBauble(NBTTagCompound nbt){
        this.nbt = nbt;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.nbt = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf,this.nbt);
    }

    public static class Handler implements IMessageHandler<UpdateBauble, IMessage> {
        @Override
        public IMessage onMessage(UpdateBauble message, MessageContext ctx) {
            if (Minecraft.getMinecraft() != null) {
                Minecraft.getMinecraft().thePlayer.getEntityData().setTag(nbtName, message.nbt);
            } else {
                ctx.getServerHandler().playerEntity.getEntityData().setTag(nbtName, message.nbt);
            }
            return null;
        }
    }
}

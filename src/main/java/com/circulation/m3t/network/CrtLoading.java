package com.circulation.m3t.network;

import com.circulation.m3t.Util.RegisterRecipe;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class CrtLoading implements IMessage {

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<CrtLoading, IMessage> {

        @Override
        public IMessage onMessage(CrtLoading message, MessageContext ctx) {
            RegisterRecipe.M3Recipe();
            return null;
        }
    }
}

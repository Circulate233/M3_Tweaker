package com.circulation.m3t.hander;

import com.circulation.m3t.item.M3TBaublesBasic;
import com.circulation.m3t.network.UpdateBauble;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

import static com.circulation.m3t.M3Tweaker.network;
import static com.circulation.m3t.hander.M3TBaublesSuitHandler.nbtName;

public class BaublesRegisterHandler {

    public static BaublesRegisterHandler INSTANCE = new BaublesRegisterHandler();

    private BaublesRegisterHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        if (event.entityPlayer instanceof EntityPlayerMP) {
            NBTTagCompound playerNbt = event.original.getEntityData().getCompoundTag(nbtName);
            event.entityPlayer.getEntityData().setTag(nbtName, playerNbt);
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            NBTTagCompound playerNbt = event.player.getEntityData().getCompoundTag(nbtName);
            network.sendTo(new UpdateBauble(playerNbt), (EntityPlayerMP) event.player);//同步客户端防止显示问题
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        NBTTagCompound playerNbt = player.getEntityData();
        if (playerNbt.hasKey(nbtName)) {
            if (!player.worldObj.isRemote) {
                network.sendTo(new UpdateBauble(playerNbt.getCompoundTag(nbtName)), (EntityPlayerMP) player);
            } else {
                network.sendToServer(new UpdateBauble(playerNbt.getCompoundTag(nbtName)));
            }
        }
    }

    public static void register() {
        M3TBaublesBasic.registerAllBaubles();
    }

}

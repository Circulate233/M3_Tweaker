package com.circulation.m3t.hander;

import com.circulation.m3t.item.M3TBaublesBasic;
import com.circulation.m3t.network.UpdateBauble;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

import java.util.List;
import java.util.Map;

import static com.circulation.m3t.M3Tweaker.network;
import static com.circulation.m3t.hander.M3TBaublesSuitHandler.nbtName;

public class BaublesRegisterHandler {

    public static BaublesRegisterHandler INSTANCE = new BaublesRegisterHandler();

    private BaublesRegisterHandler(){

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

package com.circulation.m3t.proxy;

import static com.circulation.m3t.suit.M3TBaublesSuitHandler.nbtName;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

import com.circulation.m3t.client.SuitTooltips;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new SuitTooltips());
    }

    @Override
    public void receiveSuitUpdate(NBTTagCompound suits) {
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.func_152344_a(() -> {
            EntityPlayer player = minecraft.thePlayer;
            if (player != null) {
                player.getEntityData()
                    .setTag(nbtName, suits);
            }
        });
    }

}

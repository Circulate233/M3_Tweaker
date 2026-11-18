package com.circulation.m3t.proxy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

import com.circulation.m3t.M3TConfig;
import com.circulation.m3t.item.CustomBaubles;
import com.circulation.m3t.item.M3TBaublesBasic;
import com.circulation.m3t.suit.SuitEvents;
import com.circulation.m3t.suit.SuitPlayerEvents;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        CustomBaubles.register(M3TConfig.readConfig());
        SuitPlayerEvents playerEvents = new SuitPlayerEvents();
        MinecraftForge.EVENT_BUS.register(playerEvents);
        FMLCommonHandler.instance()
            .bus()
            .register(playerEvents);
    }

    public void init(FMLInitializationEvent event) {
        M3TBaublesBasic.registerAllBaubles();
        MinecraftForge.EVENT_BUS.register(new SuitEvents());
    }

    public void receiveSuitUpdate(NBTTagCompound suits) {}

}

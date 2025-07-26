package com.circulation.m3t.proxy;

import com.circulation.m3t.M3TConfig;
import com.circulation.m3t.Util.RegisterItem;
import com.circulation.m3t.hander.*;
import com.circulation.m3t.item.CustomBaubles;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        M3TConfig.readConfig();
        CustomBaubles.register(M3TConfig.baubles);
        FMLCommonHandler.instance().bus().register(BaublesRegisterHandler.INSTANCE);
    }

    public void init(FMLInitializationEvent event) {
        RegisterItem.register();
        ReloadHandler.register();
        M3TBaublesSuitHandler.registerEvents();
        M3TBaubleTagSuitHandler.registerEvents();
        M3TBaubleScatteredSuitHandler.registerEvents();
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

}

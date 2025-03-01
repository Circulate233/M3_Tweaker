package com.circulation.m3t.proxy;

import com.circulation.m3t.Util.RegisterItem;
import com.circulation.m3t.Util.RegisterRecipe;
import com.circulation.m3t.crt.CustomBaubles;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event) {
        RegisterItem.register();
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {
        RegisterRecipe.register();
        CustomBaubles.register();
    }
}

package com.circulation.m3t;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.circulation.m3t.command.CommandM3T;
import com.circulation.m3t.network.UpdateBauble;
import com.circulation.m3t.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(
    modid = M3Tweaker.MOD_ID,
    name = "M3 Tweaker",
    version = Tags.VERSION,
    acceptedMinecraftVersions = "[1.7.10]",
    dependencies = "required-after:manametalmod@[8.0.2,);" + "required-after:MineTweaker3;required-after:gtnhlib;")
public class M3Tweaker {

    public static final String MOD_ID = "m3t";

    @Mod.Instance(MOD_ID)
    public static M3Tweaker instance;

    public static final String CLIENT_PROXY = "com.circulation.m3t.proxy.ClientProxy";
    public static final String COMMON_PROXY = "com.circulation.m3t.proxy.CommonProxy";
    public static final CreativeTabs CreativeTab = new CreativeTabs(MOD_ID) {

        @Override
        public Item getTabIconItem() {
            return new project.studio.manametalmod.items.ItemBagBingo3();
        }

    };

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;

    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        network.registerMessage(UpdateBauble.Handler.class, UpdateBauble.class, 0, Side.CLIENT);
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        M3TCrtAPI.register();
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(CommandM3T.INSTANCE);
    }

}

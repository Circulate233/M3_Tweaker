package com.circulation.m3t;

import com.circulation.m3t.command.CommandM3T;
import com.circulation.m3t.network.UpdateBauble;
import com.circulation.m3t.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod(
        modid = M3Tweaker.MOD_ID, name = "M3 Tweaker", version = M3Tweaker.VERSION, acceptedMinecraftVersions = "[1.7.10]",
        dependencies = "required-after:manametalmod@[7.3.7,);" +
                       "required-after:MineTweaker3;"
)
public class M3Tweaker {

    public static final String MOD_ID = "m3t";
    public static final String VERSION = "0.7.0";

    @Mod.Instance(MOD_ID)
    public static M3Tweaker instance = new M3Tweaker();

    public static final String CLIENT_PROXY = "com.circulation.m3t.proxy.ClientProxy";
    public static final String COMMON_PROXY = "com.circulation.m3t.proxy.CommonProxy";
    public static final CreativeTabs CreativeTab = new CreativeTabs(MOD_ID){

        @Override
        public Item getTabIconItem() {
            return new project.studio.manametalmod.items.ItemBagBingo3();
        }

    };

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;

    public static Logger logger = LogManager.getLogger(MOD_ID);

    public static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        int start = 0;

        network.registerMessage(UpdateBauble.Handler.class, UpdateBauble.class, start++, Side.CLIENT);
        network.registerMessage(UpdateBauble.Handler.class, UpdateBauble.class, start++, Side.SERVER);
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        try {
            M3TCrtAPI.register();
        } catch (IOException ignored){

        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event){
        event.registerServerCommand(CommandM3T.INSTANCE);
    }

}

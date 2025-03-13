package com.circulation.m3t;

import com.circulation.m3t.command.CommandM3T;
import com.circulation.m3t.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rfg.examplemod.Tags;

import java.io.IOException;

@Mod(
        modid = "m3t", name = "M3 Tweaker", version = Tags.VERSION, acceptedMinecraftVersions = "[1.7.10]",
        dependencies = "required-after:manametalmod@[7.3.0,);" +
                       "required-after:MineTweaker3;"
)
public class M3Tweaker {

    public static final String MOD_ID = "m3t";

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
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        int start = 0;

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        proxy.init(event);
        M3TCrtAPI.register();
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

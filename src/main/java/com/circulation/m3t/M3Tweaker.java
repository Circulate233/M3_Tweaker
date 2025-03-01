package com.circulation.m3t;

import com.circulation.m3t.Util.RegisterRecipe;
import com.circulation.m3t.command.CommandM3T;
import com.circulation.m3t.item.M3EBaublesBasic;
import com.circulation.m3t.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = "m3t",
        name = "M3 Tweaker",
        version = "1.7.10-0.1",
        acceptedMinecraftVersions = "[1.7.10]",
        dependencies = "required-after:manametalmod@[7.3.0,)"
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
            return M3EBaublesBasic.getBauble("Core");
        }

    };

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;

    public static Logger logger = LogManager.getLogger("m3t");

    public static SimpleNetworkWrapper network;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
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
        RegisterRecipe.M3Recipe();
    }

}

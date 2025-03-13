package com.circulation.m3t.proxy;

import com.circulation.m3t.Util.RegisterItem;
import com.circulation.m3t.hander.BaublesRegisterHandler;
import com.circulation.m3t.hander.M3TBaublesSuitHandler;
import com.circulation.m3t.hander.ReloadHandler;
import com.circulation.m3t.item.CustomBaubles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CommonProxy {

    static List<CustomBaubles.Baubles> baubles = new ArrayList<>();

    public void preInit(FMLPreInitializationEvent event) throws IOException {
        readConfig();
        CustomBaubles.register(baubles);
        FMLCommonHandler.instance().bus().register(BaublesRegisterHandler.INSTANCE);
    }

    public void init(FMLInitializationEvent event) {
        RegisterItem.register();
        ReloadHandler.register();
        M3TBaublesSuitHandler.registerEvents();
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    private static void readConfig() throws IOException {
        Path config = new File(Loader.instance().getConfigDir(), "M3T").toPath();
        Path baubles = config.resolve("M3TBaubles.json");

        Files.createDirectories(config);

        Gson baublesGson = (new GsonBuilder()).disableHtmlEscaping().setPrettyPrinting().create();

        if (Files.exists(baubles)) {
            CommonProxy.baubles.addAll(baublesGson.fromJson(new String(Files.readAllBytes(baubles)), (new TypeToken<List<CustomBaubles.Baubles>>() {}).getType()));
        } else {
            Map<Integer,Float> map = new HashMap<>();
            map.put(10,50.0f);
            map.put(7,50.0f);
            map.put(3,50.0f);
            CommonProxy.baubles.add(new CustomBaubles.Baubles("测试小道具！","W我是超级测试王","minecraft:diamond:0", (short) 10,14,10,999,map));
            CommonProxy.baubles.add(new CustomBaubles.Baubles("amuck","item.ddd","item.aaa", "def:m3t:amuck", (short) 27,7,10,784,map));
            Files.write(baubles, baublesGson.toJson(CommonProxy.baubles).getBytes());
        }
    }
}

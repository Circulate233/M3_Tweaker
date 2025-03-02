package com.circulation.m3t.proxy;

import com.circulation.m3t.Util.RegisterItem;
import com.circulation.m3t.Util.RegisterRecipe;
import com.circulation.m3t.hander.BaublesRegisterHandler;
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
import java.util.*;

public class CommonProxy {

    static List<CustomBaubles.Baubles> baubles = new ArrayList<>();

    public void preInit(FMLPreInitializationEvent event) throws IOException {
        FMLCommonHandler.instance().bus().register(BaublesRegisterHandler.INSTANCE);
        readConfig();
        CustomBaubles.register(baubles);

    }

    public void init(FMLInitializationEvent event) {
        RegisterItem.register();
    }

    public void postInit(FMLPostInitializationEvent event) {
        RegisterRecipe.register();
    }

    private static void readConfig() throws IOException {
        File configFile = new File(Loader.instance().getConfigDir(), "M3TBaubles.json");
        Gson gson = (new GsonBuilder()).disableHtmlEscaping().setPrettyPrinting().create();
        if (configFile.isFile()) {
            baubles.addAll(gson.fromJson(new String(Files.readAllBytes(configFile.toPath())), (new TypeToken<List<CustomBaubles.Baubles>>() {}).getType()));
        } else {
            configFile.createNewFile();
            Map<Integer,Float> map = new HashMap<>();
            map.put(10,50.0f);
            map.put(7,50.0f);
            map.put(3,50.0f);
            baubles.add(new CustomBaubles.Baubles("测试小道具！","W我是超级测试王","minecraft:diamond", (short) 10,10,map));
            baubles.add(new CustomBaubles.Baubles("item.ddd","item.aaa","def", (short) 27,10,map));
            Files.write(configFile.toPath(), gson.toJson(baubles).getBytes());
        }
    }
}

package com.circulation.m3t.proxy;

import com.circulation.m3t.Util.RegisterItem;
import com.circulation.m3t.Util.RegisterRecipe;
import com.circulation.m3t.crt.CustomBaubles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;
import project.studio.manametalmod.magic.magicItem.MagicItemType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class CommonProxy {

    static List<CustomBaubles.Baubles> baubles = new ArrayList<>();

    public void preInit(FMLPreInitializationEvent event) throws IOException {
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
            baubles.add(new CustomBaubles.Baubles((short) 1, 1, Collections.singletonList(new IMagicEffect(MagicItemType.attackSpeed, 100.0f))));
            Files.write(configFile.toPath(), gson.toJson(baubles).getBytes());
        }
    }
}

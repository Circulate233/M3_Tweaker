package com.circulation.m3t;

import com.circulation.m3t.item.CustomBaubles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cpw.mods.fml.common.Loader;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class M3TConfig {

    public static List<CustomBaubles.Baubles> baubles = new ArrayList<>();
    public static boolean randomTag;
    public static boolean showAllSuitEffects;

    static final File configFile = new File(Loader.instance().getConfigDir(), "M3T");
    static final Configuration config;

    static {
        try {
            Files.createDirectories(configFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        config = new Configuration(new File(configFile, "M3T.cfg"));
    }

    public static void readConfig() throws IOException {
        readCfg();
        readBaubles();
    }

    public static void readBaubles() throws IOException {
        Path baubles = configFile.toPath().resolve("M3TBaubles.json");

        Gson baublesGson = (new GsonBuilder()).disableHtmlEscaping().setPrettyPrinting().create();

        if (Files.exists(baubles)) {
            M3TConfig.baubles.addAll(baublesGson.fromJson(new String(Files.readAllBytes(baubles)), (new TypeToken<List<CustomBaubles.Baubles>>() {}).getType()));
        } else {
            Map<Integer,Float> map = new HashMap<>();
            map.put(10,50.0f);
            map.put(7,50.0f);
            map.put(3,50.0f);
            M3TConfig.baubles.add(new CustomBaubles.Baubles("测试小道具！","W我是超级测试王","minecraft:diamond:0", (short) 10,14,10,999,map));
            M3TConfig.baubles.add(new CustomBaubles.Baubles("amuck","item.ddd","item.aaa", "def:m3t:amuck", (short) 27,7,10,784,map));
            Files.write(baubles, baublesGson.toJson(M3TConfig.baubles).getBytes());
        }

    }

    public static void readCfg() {
        config.load();

        randomTag = config.getBoolean("randomTag",Configuration.CATEGORY_GENERAL,false,"是否为每个饰品添加一个随机tag？");
        showAllSuitEffects = config.getBoolean("showAllSuitEffects",Configuration.CATEGORY_GENERAL,false,"是否默认直接显示全部的套装效果");

        if (config.hasChanged()) {
            config.save();
        }
    }
}

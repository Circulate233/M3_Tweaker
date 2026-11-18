package com.circulation.m3t;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import net.minecraftforge.common.config.Configuration;

import com.circulation.m3t.item.CustomBaubles.Baubles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import cpw.mods.fml.common.Loader;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;

public class M3TConfig {

    private static final Baubles[] NO_BAUBLES = new Baubles[0];
    private static final Gson BAUBLES_GSON = new GsonBuilder().disableHtmlEscaping()
        .setPrettyPrinting()
        .create();
    public static boolean randomTag;
    public static boolean showAllSuitEffects;

    static final File configFile = new File(
        Loader.instance()
            .getConfigDir(),
        "M3T");
    static final Configuration config;

    static {
        try {
            Files.createDirectories(configFile.toPath());
        } catch (IOException e) {
            throw new IllegalStateException("Could not create the M3 Tweaker config folder: " + configFile, e);
        }
        config = new Configuration(new File(configFile, "M3T.cfg"));
    }

    public static Baubles[] readConfig() {
        readCfg();
        return readBaubles();
    }

    public static Baubles[] readBaubles() {
        Path file = configFile.toPath()
            .resolve("M3TBaubles.json");
        if (Files.exists(file)) {
            try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                Baubles[] entries = BAUBLES_GSON.fromJson(reader, Baubles[].class);
                if (entries != null) return entries;
                M3Tweaker.logger.error("Expected an array of bauble definitions in {}", file);
                return NO_BAUBLES;
            } catch (IOException | JsonParseException e) {
                M3Tweaker.logger.error("Could not read bauble definitions from {}", file, e);
                return NO_BAUBLES;
            }
        }

        Int2FloatMap effects = new Int2FloatOpenHashMap(3);
        effects.put(10, 50.0f);
        effects.put(7, 50.0f);
        effects.put(3, 50.0f);
        Baubles[] defaults = {
            new Baubles("测试小道具！", "W我是超级测试王", "minecraft:diamond:0", (short) 10, 14, 10, 999, effects),
            new Baubles("amuck", "item.ddd", "item.aaa", "def:m3t:amuck", (short) 27, 7, 10, 784, effects) };
        try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            BAUBLES_GSON.toJson(defaults, writer);
        } catch (IOException | JsonParseException e) {
            M3Tweaker.logger.error("Could not write default bauble definitions to {}", file, e);
        }
        return defaults;
    }

    public static void readCfg() {
        config.load();

        randomTag = config.getBoolean("randomTag", Configuration.CATEGORY_GENERAL, false, "是否为每件饰品添加随机 NBT 标签");
        showAllSuitEffects = config
            .getBoolean("showAllSuitEffects", Configuration.CATEGORY_GENERAL, false, "是否默认显示全部套装效果");

        if (config.hasChanged()) {
            config.save();
        }
    }
}

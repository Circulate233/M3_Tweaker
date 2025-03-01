package com.circulation.m3t.mixins;

import cpw.mods.fml.common.Loader;
import io.github.tox1cozz.mixinbooterlegacy.ILateMixinLoader;
import io.github.tox1cozz.mixinbooterlegacy.LateMixin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.BooleanSupplier;

@SuppressWarnings({"unused", "SameParameterValue"})
@LateMixin
public class M3TLateMixinLoader implements ILateMixinLoader {

    public static final Logger LOG = LogManager.getLogger("M3E");
    public static final String LOG_PREFIX = "[M3E]" + ' ';
    private static final Map<String, BooleanSupplier> MIXIN_CONFIGS = new LinkedHashMap<>();

    static {
        addMixinCFG("mixins.m3t.json");
    }

    @Override
    public List<String> getMixinConfigs() {
        return new ArrayList<>(MIXIN_CONFIGS.keySet());
    }

    @Override
    public boolean shouldMixinConfigQueue(final String mixinConfig) {
        BooleanSupplier supplier = MIXIN_CONFIGS.get(mixinConfig);
        if (supplier == null) {
            LOG.warn(LOG_PREFIX + "Mixin config {} is not found in config map! It will never be loaded.", mixinConfig);
            return false;
        }
        return supplier.getAsBoolean();
    }

    private static boolean modLoaded(final String modID) {
        return Loader.isModLoaded(modID);
    }

    private static void addModdedMixinCFG(final String mixinConfig, final String modID) {
        MIXIN_CONFIGS.put(mixinConfig, () -> modLoaded(modID));
    }

    private static void addModdedMixinCFG(final String mixinConfig, final String modID, final String... modIDs) {
        MIXIN_CONFIGS.put(mixinConfig, () -> modLoaded(modID) && Arrays.stream(modIDs).allMatch(Loader::isModLoaded));
    }

    private static void addMixinCFG(final String mixinConfig) {
        MIXIN_CONFIGS.put(mixinConfig, () -> true);
    }

    private static void addMixinCFG(final String mixinConfig, final BooleanSupplier conditions) {
        MIXIN_CONFIGS.put(mixinConfig, conditions);
    }
}

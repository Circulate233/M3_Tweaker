package com.circulation.m3t.mixins;

import java.util.List;

import io.github.tox1cozz.mixinbooterlegacy.ILateMixinLoader;
import io.github.tox1cozz.mixinbooterlegacy.LateMixin;
import it.unimi.dsi.fastutil.objects.ObjectLists;

@LateMixin
public class M3TLateMixinLoader implements ILateMixinLoader {

    private static final String CONFIG = "mixins.m3t.json";

    @Override
    public List<String> getMixinConfigs() {
        return ObjectLists.singleton(CONFIG);
    }

    @Override
    public boolean shouldMixinConfigQueue(String mixinConfig) {
        return CONFIG.equals(mixinConfig);
    }
}

package com.circulation.m3t.item;

import project.studio.manametalmod.magic.magicItem.IMagicEffect;
import project.studio.manametalmod.magic.magicItem.MagicItemType;

public final class MagicEffects {

    private static final MagicItemType[] TYPES = MagicItemType.values();

    private MagicEffects() {}

    public static IMagicEffect create(int type, float value) {
        if (type < 0 || type >= TYPES.length) {
            throw new IllegalArgumentException("Unknown ManaMetal effect ID " + type);
        }
        return new IMagicEffect(TYPES[type], value);
    }

    public static IMagicEffect[] createAll(int[] types, float[] values) {
        if (values.length < types.length) {
            throw new IllegalArgumentException("Each ManaMetal effect requires a value");
        }
        IMagicEffect[] effects = new IMagicEffect[types.length];
        for (int i = 0; i < types.length; i++) {
            effects[i] = create(types[i], values[i]);
        }
        return effects;
    }
}

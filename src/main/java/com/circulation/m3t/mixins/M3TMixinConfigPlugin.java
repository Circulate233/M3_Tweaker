package com.circulation.m3t.mixins;

import java.util.List;
import java.util.Set;

import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

/** Selects the backpack mixin matching MMM's inline (pre-8.0.9) or planned (8.0.9+) swap implementation. */
public final class M3TMixinConfigPlugin implements IMixinConfigPlugin {

    private static final String LEGACY_BACKPACK_MIXIN = "com.circulation.m3t.mixins.mmm.MixinItemToolBackpackManaItem";
    private static final String MODERN_BACKPACK_MIXIN = "com.circulation.m3t.mixins.mmm.MixinItemToolBackpackManaItem809";
    private static final String EQUIPMENT_SWAP_PLAN_CLASS = "project/studio/manametalmod/inventory/EquipmentSwapPlan.class";

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (LEGACY_BACKPACK_MIXIN.equals(mixinClassName)) {
            return !hasEquipmentSwapPlan();
        }
        if (MODERN_BACKPACK_MIXIN.equals(mixinClassName)) {
            return hasEquipmentSwapPlan();
        }
        return true;
    }

    private boolean hasEquipmentSwapPlan() {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader != null && classLoader.getResource(EQUIPMENT_SWAP_PLAN_CLASS) != null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}

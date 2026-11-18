package com.circulation.m3t.item;

import java.util.List;
import java.util.Map;

import com.circulation.m3t.M3Tweaker;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import project.studio.manametalmod.core.ManaItemType;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;
import project.studio.manametalmod.magic.magicItem.MagicItemType;

public class CustomBaubles {

    public static void register(Baubles[] definitions) {
        for (int index = 0; index < definitions.length; index++) {
            Baubles bauble = definitions[index];
            if (bauble == null) {
                M3Tweaker.logger.error("Missing bauble definition at index {}", index);
                continue;
            }
            String id = bauble.id == null ? ManaItemType.getTypeFromID(bauble.type)
                .name() : bauble.id;
            String name = bauble.name == null ? " " : bauble.name;
            String tooltip = bauble.tooltip == null ? " " : bauble.tooltip;
            String icon = bauble.icon == null ? "minecraft:apple" : bauble.icon;

            List<IMagicEffect> effects = toEffects(bauble.effects);
            M3TBaublesBasic.registerEffect(
                id,
                name,
                icon,
                tooltip,
                bauble.type,
                Math.max(bauble.quality - 1, 0),
                bauble.level,
                bauble.money,
                effects);
        }
    }

    private static List<IMagicEffect> toEffects(Map<Integer, Float> definitions) {
        if (definitions == null || definitions.isEmpty()) {
            return ObjectLists.emptyList();
        }
        List<IMagicEffect> effects = new ObjectArrayList<>(definitions.size());
        definitions.forEach((key, value) -> effects.add(new IMagicEffect(MagicItemType.getTypeFromID(key), value)));
        return effects;
    }

    public static class Baubles {

        private final String id;
        private final String name;
        private final String tooltip;
        private final String icon;
        private final short type;
        private final int level;
        private final int quality;
        private final long money;
        private final Map<Integer, Float> effects;

        public Baubles(String id, String Name, String tooltip, String Icon, Short Type, int quality, int Level,
            long money, Int2FloatMap Effects) {
            this.id = id;
            this.name = Name;
            this.tooltip = tooltip;
            this.icon = Icon;
            this.type = Type;
            this.level = Level;
            this.money = money;
            this.quality = quality;
            this.effects = Effects;
        }

        public Baubles(String Name, String tooltip, String Icon, Short Type, int quality, int Level, long money,
            Int2FloatMap Effects) {
            this.id = ManaItemType.getTypeFromID(Type)
                .name();
            this.name = Name;
            this.tooltip = tooltip;
            this.icon = Icon;
            this.type = Type;
            this.level = Level;
            this.money = money;
            this.quality = quality;
            this.effects = Effects;
        }
    }
}

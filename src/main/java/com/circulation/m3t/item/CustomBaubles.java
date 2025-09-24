package com.circulation.m3t.item;

import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import project.studio.manametalmod.core.ManaItemType;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;
import project.studio.manametalmod.magic.magicItem.MagicItemType;

import java.util.List;

public class CustomBaubles {

    public static void register(List<Baubles> list) {
        list.forEach(bauble -> {
            final List<IMagicEffect> listM = new ObjectArrayList<>();

            String ID = bauble.id == null ? ManaItemType.getTypeFromID(bauble.type).name() : bauble.id;
            String Name = bauble.name == null ? " " : bauble.name;
            String Tooltip = bauble.tooltip == null ? " " : bauble.tooltip;
            String Icon = bauble.icon == null ? "minecraft:apple" : bauble.icon;

            bauble.effects.forEach((key, value) -> listM.add(new IMagicEffect(MagicItemType.getTypeFromID(key), value)));
            M3TBaublesBasic.registerEffect(ID, Name, Icon, Tooltip, bauble.type, Math.max(bauble.quality - 1, 0), bauble.level, bauble.money, listM);
        });
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
        private final Int2FloatMap effects;

        public Baubles(String id, String Name, String tooltip, String Icon, Short Type, int quality, int Level, long money, Int2FloatMap Effects) {
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

        public Baubles(String Name, String tooltip, String Icon, Short Type, int quality, int Level, long money, Int2FloatMap Effects) {
            this.id = ManaItemType.getTypeFromID(Type).name();
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

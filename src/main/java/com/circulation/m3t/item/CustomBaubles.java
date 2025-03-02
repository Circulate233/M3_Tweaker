package com.circulation.m3t.item;

import project.studio.manametalmod.magic.magicItem.IMagicEffect;
import project.studio.manametalmod.magic.magicItem.MagicItemType;
import stanhebben.zenscript.annotations.ZenClass;

import java.util.*;

@ZenClass("mods.m3t.Baubles")
public class CustomBaubles {

    public static void register(List<Baubles> list){
        list.forEach(bauble -> {
            final List<IMagicEffect> listM = new ArrayList<>();
            bauble.effects.forEach((key, value) -> listM.add(new IMagicEffect(MagicItemType.getTypeFromID(key),value)));
            M3EBaublesBasic.registerEffect(bauble.name, bauble.icon,bauble.tooltip, bauble.type, bauble.level, listM);
        });
    }

    public static class Baubles{
        private final String name;
        private final String tooltip;
        private final String icon;
        private final short type;
        private final int level;
        private final Map<Integer, Float> effects;

        public Baubles(String Name,String tooltip,String Icon, Short Type,int Level,Map<Integer, Float> Effects){
            this.name = Name;
            this.tooltip = tooltip;
            this.icon = Icon;
            this.type = Type;
            this.level = Level;
            this.effects = Effects;
        }
    }
}

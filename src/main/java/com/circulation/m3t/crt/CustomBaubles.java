package com.circulation.m3t.crt;

import com.circulation.m3t.item.M3EBaublesBasic;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;
import stanhebben.zenscript.annotations.ZenClass;

import java.util.*;

@ZenClass("mods.m3t.Baubles")
public class CustomBaubles {

    public static void register(List<Baubles> list){
        list.forEach(bauble -> M3EBaublesBasic.registerEffect(bauble.type,bauble.level,bauble.effects));
    }

    public static class Baubles{
        private final short type;
        private final int level;
        private final List<IMagicEffect> effects;

        public Baubles(Short Type,int Level,List<IMagicEffect> Effects){
            this.type = Type;
            this.level = Level;
            this.effects = Effects;
        }
    }
}

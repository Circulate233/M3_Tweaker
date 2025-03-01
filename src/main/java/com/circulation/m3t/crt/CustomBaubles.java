package com.circulation.m3t.crt;

import com.circulation.m3t.item.M3EBaublesBasic;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;
import project.studio.manametalmod.magic.magicItem.MagicItemType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.*;

@ZenClass("mods.m3t.Baubles")
public class CustomBaubles {

    @ZenMethod
    public static void registerBauble(short type, int level, int[] effectNames,float[] effectValues){
        List<IMagicEffect> list = new ArrayList<>();
        for (int i = 0; i < effectValues.length; i++) {
            list.add(new IMagicEffect(MagicItemType.getTypeFromID(effectNames[i]),effectValues[i]));
        }
        Baubles.register(type,level,list);
    }

    public static void register(){
        Baubles.list.forEach(bauble -> M3EBaublesBasic.registerEffect(bauble.type,bauble.level,bauble.effects));
    }

    private static class Baubles{
        private final short type;
        private final int level;
        private final List<IMagicEffect> effects;
        private static final List<Baubles> list = new ArrayList<>();

        private Baubles(Short Type,int Level,List<IMagicEffect> Effects){
            this.type = Type;
            this.level = Level;
            this.effects = Effects;
        }

        private static void register(Short Type,int Level,List<IMagicEffect> Effects){
            list.add(new Baubles(Type,Level,Effects));
        }
    }
}

package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.hander.M3TBaubleTagSuitHandler;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;
import project.studio.manametalmod.magic.magicItem.MagicItemType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenClass(M3TCrtAPI.CrtClass + "TagSuitHandler")
public class CrtTagSuitHandler extends M3TBaubleTagSuitHandler.TagSuitHandler {

    public CrtTagSuitHandler(String suitName) {
        super(suitName);
    }

    @ZenMethod
    public CrtTagSuitHandler setNameTag(String name){
        super.setTagName(name);
        return this;
    }

    @ZenMethod
    public CrtTagSuitHandler addSuit(int quantity, String tooltip,int[] typeID, float[] data) {
        List<IMagicEffect> MES = new ArrayList<>();
        for (int i = 0; i < typeID.length; i++) {
            MES.add(new IMagicEffect(MagicItemType.getTypeFromID(typeID[i]), data[i]));
        }
        this.addSuit(quantity, tooltip, MES);
        return this;
    }

    @ZenMethod
    @Override
    public void register(){
        super.register();
    }
}

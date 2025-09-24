package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.hander.M3TBaublesSuitHandler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;
import project.studio.manametalmod.magic.magicItem.MagicItemType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ZenClass(M3TCrtAPI.CrtClass + "SuitHandler")
public class CrtSuitHandler extends M3TBaublesSuitHandler.SuitHandler {

    public CrtSuitHandler(String suitName) {
        super(suitName);
    }

    @ZenMethod
    public CrtSuitHandler addSuit(int quantity, String tooltip,int[] typeID, float[] data) {
        List<IMagicEffect> MES = new ObjectArrayList<>();
        for (int i = 0; i < typeID.length; i++) {
            MES.add(new IMagicEffect(MagicItemType.getTypeFromID(typeID[i]), data[i]));
        }
        this.addSuit(quantity, tooltip, MES);
        return this;
    }

    @ZenMethod
    public void register(){
        super.register();
    }

}

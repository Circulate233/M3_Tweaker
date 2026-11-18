package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.item.MagicEffects;
import com.circulation.m3t.suit.M3TBaublesSuitHandler;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "SuitHandler")
public class CrtSuitHandler extends M3TBaublesSuitHandler.SuitHandler {

    public CrtSuitHandler(String suitName) {
        super(suitName);
    }

    @ZenMethod
    public CrtSuitHandler addSuit(int quantity, String tooltip, int[] typeID, float[] data) {
        super.addSuit(quantity, tooltip, ObjectArrayList.wrap(MagicEffects.createAll(typeID, data)));
        return this;
    }

    @ZenMethod
    @Override
    public void register() {
        super.register();
    }

}

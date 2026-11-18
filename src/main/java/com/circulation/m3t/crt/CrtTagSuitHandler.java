package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.item.MagicEffects;
import com.circulation.m3t.suit.M3TBaubleTagSuitHandler;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "TagSuitHandler")
public class CrtTagSuitHandler extends M3TBaubleTagSuitHandler.TagSuitHandler {

    public CrtTagSuitHandler(String suitName) {
        super(suitName);
    }

    @ZenMethod
    public CrtTagSuitHandler setNameTag(String name) {
        super.setTagName(name);
        return this;
    }

    @ZenMethod
    public CrtTagSuitHandler addSuit(int quantity, String tooltip, int[] typeID, float[] data) {
        super.addSuit(quantity, tooltip, ObjectArrayList.wrap(MagicEffects.createAll(typeID, data)));
        return this;
    }

    @ZenMethod
    @Override
    public void register() {
        super.register();
    }
}

package com.circulation.m3t.crt;

import static com.circulation.m3t.crt.CrtUtil.requireItemStack;
import static com.circulation.m3t.crt.CrtUtil.requireItemStacks;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.item.MagicEffects;
import com.circulation.m3t.suit.M3TBaubleScatteredSuitHandler;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "ScatteredSuitHandler")
public class CrtScatteredSuitHandler extends M3TBaubleScatteredSuitHandler.ScatteredSuitHandler {

    public CrtScatteredSuitHandler(String suitName) {
        super(suitName);
    }

    @ZenMethod
    public CrtScatteredSuitHandler addItem(IItemStack item) {
        super.addItem(requireItemStack(item));
        return this;
    }

    @ZenMethod
    public CrtScatteredSuitHandler addItems(IItemStack... item) {
        super.addItems(requireItemStacks(item));
        return this;
    }

    @ZenMethod
    public CrtScatteredSuitHandler addSuit(int quantity, String tooltip, int[] typeID, float[] data) {
        super.addSuit(quantity, tooltip, ObjectArrayList.wrap(MagicEffects.createAll(typeID, data)));
        return this;
    }

    @ZenMethod
    @Override
    public void register() {
        super.register();
    }
}

package com.circulation.m3t.crt;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.hander.M3TBaubleScatteredSuitHandler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import project.studio.manametalmod.magic.magicItem.IMagicEffect;
import project.studio.manametalmod.magic.magicItem.MagicItemType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ZenClass(M3TCrtAPI.CrtClass + "ScatteredSuitHandler")
public class CrtScatteredSuitHandler extends M3TBaubleScatteredSuitHandler.ScatteredSuitHandler {

    public CrtScatteredSuitHandler(String suitName) {
        super(suitName);
    }

    @ZenMethod
    public CrtScatteredSuitHandler addItem(IItemStack item){
        super.addItem(MineTweakerMC.getItemStack(item));
        return this;
    }

    @ZenMethod
    public CrtScatteredSuitHandler addItems(IItemStack... item){
        super.addItems(MineTweakerMC.getItemStacks(item));
        return this;
    }

    @ZenMethod
    public CrtScatteredSuitHandler addSuit(int quantity, String tooltip, int[] typeID, float[] data) {
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

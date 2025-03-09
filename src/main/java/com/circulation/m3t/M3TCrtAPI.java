package com.circulation.m3t;

import com.circulation.m3t.crt.*;
import com.circulation.m3t.crt.recipes.*;
import minetweaker.MineTweakerAPI;

import java.util.ArrayList;
import java.util.List;

public class M3TCrtAPI {

    public static final String CrtClass = "mods.m3t.";

    static void register(){
        List<Class> list = new ArrayList<>();

        list.add(DungeonBoxHandler.class);
        list.add(NPCStoreHandler.class);
        list.add(ProduceStoreHandler.class);
        list.add(CrtItem.class);
        list.add(AttributeItemHandler.class);
        list.add(MoneyHandler.class);
        list.add(ManaGravityWellHandler.class);
        list.add(ManaMetalInjectionHandler.class);

        list.add(CastingHandler.class);

        list.forEach(MineTweakerAPI::registerClass);
    }

}

package com.circulation.m3t;

import com.circulation.m3t.crt.*;
import com.circulation.m3t.crt.recipes.CastingHandler;
import com.circulation.m3t.item.CustomBaubles;
import minetweaker.MineTweakerAPI;

import java.util.ArrayList;
import java.util.List;

public class M3TCrtAPI {

    static void register(){
        List<Class> list = new ArrayList<>();

        list.add(DungeonBoxHandler.class);
        list.add(NPCStoreHandler.class);
        list.add(MMMRecipesHandler.class);
        list.add(ProduceStoreHandler.class);
        list.add(CrtItem.class);
        list.add(CustomBaubles.class);
        list.add(AttributeItemHandler.class);

        list.add(CastingHandler.class);

        list.forEach(MineTweakerAPI::registerClass);
    }

}

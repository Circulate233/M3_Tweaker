package com.circulation.m3t.Util;

import com.circulation.m3t.crt.ProduceStoreHandler;
import com.circulation.m3t.crt.recipes.CastingHandler;
import com.circulation.m3t.hander.MoneyHandler;
import com.circulation.m3t.hander.NPCHandler;

public class RegisterRecipe {

    public static boolean complete = false;

    public static void register() {
        MoneyHandler.registerAllMoney();
    }

    public static void M3Recipe(){
        if (complete)return;
        CastingHandler.register();
        ProduceStoreHandler.register();
        NPCHandler.register();
        complete = true;
    }
}

package com.circulation.m3t.Util;

import com.circulation.m3t.crt.ProduceStoreHandler;
import com.circulation.m3t.crt.recipes.CastingHandler;
import com.circulation.m3t.hander.MoneyHandler;
import com.circulation.m3t.hander.NPCHandler;

public class RegisterRecipe {

    public static void register() {
        MoneyHandler.registerAllMoney();
    }

    public static void M3TRecipe(){
        CastingHandler.reload();
        ProduceStoreHandler.reload();
        NPCHandler.reload();
    }

    public static void M3TPostRecipe(){
        CastingHandler.postReload();
        ProduceStoreHandler.postReload();
    }
}

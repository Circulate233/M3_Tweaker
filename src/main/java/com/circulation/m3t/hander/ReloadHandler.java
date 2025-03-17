package com.circulation.m3t.hander;

import com.circulation.m3t.Util.M3TCrtReload;
import minetweaker.MineTweakerImplementationAPI;

import java.util.ArrayList;
import java.util.List;

public class ReloadHandler {

    public static List<M3TCrtReload> reloads = new ArrayList<>();

    public static void M3TRecipe(){
        reloads.forEach(M3TCrtReload::reload);
        M3TBaubleTagSuitHandler.reload();
        M3TBaubleScatteredSuitHandler.reload();
        M3TBaublesSuitHandler.reload();
    }

    public static void M3TPostRecipe(){
        reloads.forEach(M3TCrtReload::postReload);
    }

    public static void register() {
        MineTweakerImplementationAPI.onReloadEvent(event -> M3TRecipe());
        MineTweakerImplementationAPI.onPostReload(event -> M3TPostRecipe());
    }
}

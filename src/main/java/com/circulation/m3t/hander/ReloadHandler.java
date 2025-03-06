package com.circulation.m3t.hander;

import com.circulation.m3t.Util.RegisterRecipe;
import minetweaker.MineTweakerImplementationAPI;

public class ReloadHandler {

    public static void register() {
        MineTweakerImplementationAPI.onReloadEvent(event -> RegisterRecipe.M3TRecipe());
        MineTweakerImplementationAPI.onPostReload(event -> RegisterRecipe.M3TPostRecipe());
    }
}

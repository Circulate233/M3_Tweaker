package com.circulation.m3t.reload;

import java.util.List;

import com.circulation.m3t.suit.M3TBaubleScatteredSuitHandler;
import com.circulation.m3t.suit.M3TBaubleTagSuitHandler;
import com.circulation.m3t.suit.M3TBaublesSuitHandler;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import minetweaker.MineTweakerImplementationAPI;

public final class ReloadHandler {

    private static final List<M3TCrtReload> RELOAD_HANDLERS = new ObjectArrayList<>();

    private ReloadHandler() {}

    public static void add(M3TCrtReload handler) {
        RELOAD_HANDLERS.add(handler);
    }

    private static void beforeScripts() {
        RELOAD_HANDLERS.forEach(M3TCrtReload::reload);
        M3TBaubleTagSuitHandler.reload();
        M3TBaubleScatteredSuitHandler.reload();
        M3TBaublesSuitHandler.reload();
    }

    private static void afterScripts() {
        RELOAD_HANDLERS.forEach(M3TCrtReload::postReload);
    }

    public static void register() {
        MineTweakerImplementationAPI.onReloadEvent(event -> beforeScripts());
        MineTweakerImplementationAPI.onPostReload(event -> afterScripts());
    }
}

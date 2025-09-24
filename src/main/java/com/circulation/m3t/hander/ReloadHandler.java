package com.circulation.m3t.hander;

import com.circulation.m3t.Util.M3TCrtReload;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import minetweaker.MineTweakerImplementationAPI;

import java.util.List;

public class ReloadHandler {

    public static List<M3TCrtReload> reloads = new ObjectArrayList<>();

    public static void M3TReload() {
        reloads.forEach(M3TCrtReload::reload);
        M3TBaubleTagSuitHandler.reload();
        M3TBaubleScatteredSuitHandler.reload();
        M3TBaublesSuitHandler.reload();
    }

    public static void M3TPostReload() {
        reloads.forEach(M3TCrtReload::postReload);
    }

    public static void register() {
        MineTweakerImplementationAPI.onReloadEvent(event -> M3TReload());
        MineTweakerImplementationAPI.onPostReload(event -> M3TPostReload());
    }
}

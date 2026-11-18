package com.circulation.m3t;

import net.minecraftforge.common.MinecraftForge;

import com.circulation.m3t.crt.AttributeItemHandler;
import com.circulation.m3t.crt.CrtBaublesSuitHandler;
import com.circulation.m3t.crt.CrtScatteredSuitHandler;
import com.circulation.m3t.crt.CrtSuitHandler;
import com.circulation.m3t.crt.CrtTagSuitHandler;
import com.circulation.m3t.crt.CrtUtil;
import com.circulation.m3t.crt.DungeonBoxHandler;
import com.circulation.m3t.crt.M3TRandom;
import com.circulation.m3t.crt.MoneyHandler;
import com.circulation.m3t.crt.TrophyItemsHandler;
import com.circulation.m3t.crt.classes.M3TPlayer;
import com.circulation.m3t.crt.classes.M3TWorld;
import com.circulation.m3t.crt.events.BaubleEvent;
import com.circulation.m3t.crt.events.BaublePostEvent;
import com.circulation.m3t.crt.events.CancelEvent;
import com.circulation.m3t.crt.events.M3TEventAPI;
import com.circulation.m3t.crt.recipes.AncientEmpireHandler;
import com.circulation.m3t.crt.recipes.BlueSkyHandler;
import com.circulation.m3t.crt.recipes.CastingHandler;
import com.circulation.m3t.crt.recipes.DarkItemHandler;
import com.circulation.m3t.crt.recipes.DarkSteelHandler;
import com.circulation.m3t.crt.recipes.IronWroughtFurnaceHandler;
import com.circulation.m3t.crt.recipes.ManaCraftTableHandler;
import com.circulation.m3t.crt.recipes.ManaFurnaceHandler;
import com.circulation.m3t.crt.recipes.ManaGravityWellHandler;
import com.circulation.m3t.crt.recipes.ManaMetalInjectionHandler;
import com.circulation.m3t.crt.recipes.MetalCraftTableHandler;
import com.circulation.m3t.crt.recipes.WandMakeHandler;
import com.circulation.m3t.crt.store.BossStoreHandler;
import com.circulation.m3t.crt.store.NPCStoreHandler;
import com.circulation.m3t.crt.store.ProduceStoreHandler;
import com.circulation.m3t.reload.M3TCrtReload;
import com.circulation.m3t.reload.ReloadHandler;

import minetweaker.MineTweakerAPI;

public final class M3TCrtAPI {

    public static final String CrtClass = "mods.m3t.";

    private M3TCrtAPI() {}

    static void register() {
        registerClasses(
            M3TPlayer.class,
            M3TWorld.class,
            CrtUtil.class,
            M3TRandom.class,
            CancelEvent.class,
            BaublePostEvent.class,
            BaubleEvent.class,
            CrtSuitHandler.class,
            CrtTagSuitHandler.class,
            CrtScatteredSuitHandler.class,
            CrtBaublesSuitHandler.class);

        M3TEventAPI events = new M3TEventAPI();
        registerHandlers(
            events,
            new AttributeItemHandler(),
            new DungeonBoxHandler(),
            new MoneyHandler(),
            new TrophyItemsHandler(),
            new AncientEmpireHandler(),
            new BlueSkyHandler(),
            new CastingHandler(),
            new DarkItemHandler(),
            new DarkSteelHandler(),
            new IronWroughtFurnaceHandler(),
            new ManaCraftTableHandler(),
            new ManaFurnaceHandler(),
            new ManaGravityWellHandler(),
            new ManaMetalInjectionHandler(),
            new MetalCraftTableHandler(),
            new WandMakeHandler(),
            new BossStoreHandler(),
            new NPCStoreHandler(),
            new ProduceStoreHandler());
        MinecraftForge.EVENT_BUS.register(events);
        ReloadHandler.register();
    }

    private static void registerClasses(Class<?>... classes) {
        for (Class<?> type : classes) {
            MineTweakerAPI.registerClass(type);
        }
    }

    private static void registerHandlers(M3TCrtReload... handlers) {
        for (M3TCrtReload handler : handlers) {
            MineTweakerAPI.registerClass(handler.getClass());
            ReloadHandler.add(handler);
        }
    }
}

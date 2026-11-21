package com.circulation.m3t.crt.events;

import com.circulation.m3t.event.BaubleEquipmentEvent;
import com.circulation.m3t.test.ForgeTestRuntime;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import org.junit.Test;
import stanhebben.zenscript.ZenModule;
import stanhebben.zenscript.impl.GenericCompileEnvironment;
import stanhebben.zenscript.impl.GenericErrorLogger;
import stanhebben.zenscript.impl.GenericRegistry;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BaubleEventTest {

    @Test
    public void forgeRoutesEachEventAndZenScriptVetoSurvivesTheBridge() throws Exception {
        ForgeTestRuntime.run(Scenario.class, "verify");
    }

    public static class Scenario {

        public static void verify() throws Exception {
            EventBus bus = new EventBus();
            M3TEventAPI bridge = new M3TEventAPI();
            bus.register(bridge);
            ForgeVeto veto = new ForgeVeto();
            bus.register(veto);

            GenericCompileEnvironment environment = new GenericCompileEnvironment();
            GenericRegistry registry = new GenericRegistry(environment, new GenericErrorLogger(System.err));
            environment.setRegistry(registry);
            registry.registerNativeClass(CancelEvent.class);
            registry.registerNativeClass(BaublePostEvent.class);
            registry.registerNativeClass(BaubleEvent.class);
            registry.registerNativeClass(M3TEventAPI.class);
            registry.registerGlobal(
                "mods",
                registry.getRoot()
                    .get("mods"));
            ZenModule script = ZenModule.compileScriptString("""
                import mods.m3t.event.BaubleEvent;
                mods.m3t.Events.onBaubleWearEvent(function(event as BaubleEvent) {
                    if (!event.isCancel()) { event.cancel(); }
                });
                """, "baubleevents.zs", environment, Scenario.class.getClassLoader());
            script.getMain()
                .run();

            AtomicInteger wear = new AtomicInteger();
            AtomicInteger disrobe = new AtomicInteger();
            AtomicInteger wearPost = new AtomicInteger();
            AtomicInteger disrobePost = new AtomicInteger();
            M3TEventAPI.onBaubleWearEvent(event -> {
                assertTrue(event.isCancel());
                wear.incrementAndGet();
            });
            M3TEventAPI.onBaubleDisrobeEvent(event -> {
                assertTrue(event.isCancel());
                disrobe.incrementAndGet();
            });
            M3TEventAPI.onBaubleWearPostEvent(event -> wearPost.incrementAndGet());
            M3TEventAPI.onBaubleDisrobePostEvent(event -> disrobePost.incrementAndGet());

            assertTrue(bus.post(new BaubleEquipmentEvent.Wear(null, null)));
            assertTrue(bus.post(new BaubleEquipmentEvent.Disrobe(null, null)));
            assertFalse(bus.post(new BaubleEquipmentEvent.WearPost(null, null)));
            assertFalse(bus.post(new BaubleEquipmentEvent.DisrobePost(null, null)));
            assertEquals(1, wear.get());
            assertEquals(1, disrobe.get());
            assertEquals(1, wearPost.get());
            assertEquals(1, disrobePost.get());

            bridge.reload();
            bus.unregister(veto);
            assertFalse(bus.post(new BaubleEquipmentEvent.Wear(null, null)));
            assertFalse(bus.post(new BaubleEquipmentEvent.Disrobe(null, null)));
            assertFalse(bus.post(new BaubleEquipmentEvent.WearPost(null, null)));
            assertFalse(bus.post(new BaubleEquipmentEvent.DisrobePost(null, null)));
            assertEquals(1, wear.get());
            assertEquals(1, disrobe.get());
            assertEquals(1, wearPost.get());
            assertEquals(1, disrobePost.get());
        }
    }

    public static class ForgeVeto {

        @SubscribeEvent(priority = EventPriority.HIGH)
        public void onDisrobe(BaubleEquipmentEvent.Disrobe event) {
            event.setCanceled(true);
        }
    }
}

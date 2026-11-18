package com.circulation.m3t.crt.events;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.event.BaubleEquipmentEvent;
import com.circulation.m3t.reload.M3TCrtReload;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import minetweaker.util.IEventHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "Events")
public class M3TEventAPI implements M3TCrtReload {

    private static final ScriptHandlers<BaubleEvent> WEAR = new ScriptHandlers<>();
    private static final ScriptHandlers<BaubleEvent> DISROBE = new ScriptHandlers<>();
    private static final ScriptHandlers<BaublePostEvent> WEAR_POST = new ScriptHandlers<>();
    private static final ScriptHandlers<BaublePostEvent> DISROBE_POST = new ScriptHandlers<>();

    @ZenMethod
    public static void onBaubleWearEvent(IEventHandler<BaubleEvent> handler) {
        WEAR.add(handler);
    }

    @ZenMethod
    public static void onBaubleDisrobeEvent(IEventHandler<BaubleEvent> handler) {
        DISROBE.add(handler);
    }

    @ZenMethod
    public static void onBaubleWearPostEvent(IEventHandler<BaublePostEvent> handler) {
        WEAR_POST.add(handler);
    }

    @ZenMethod
    public static void onBaubleDisrobePostEvent(IEventHandler<BaublePostEvent> handler) {
        DISROBE_POST.add(handler);
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onWear(BaubleEquipmentEvent.Wear event) {
        forwardPre(WEAR, event);
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onDisrobe(BaubleEquipmentEvent.Disrobe event) {
        forwardPre(DISROBE, event);
    }

    @SubscribeEvent
    public void onWearPost(BaubleEquipmentEvent.WearPost event) {
        forwardPost(WEAR_POST, event);
    }

    @SubscribeEvent
    public void onDisrobePost(BaubleEquipmentEvent.DisrobePost event) {
        forwardPost(DISROBE_POST, event);
    }

    private static void forwardPre(ScriptHandlers<BaubleEvent> handlers, BaubleEquipmentEvent event) {
        ObjectArrayList<IEventHandler<BaubleEvent>> snapshot = handlers.snapshot;
        if (!snapshot.isEmpty()) {
            dispatch(snapshot, new BaubleEvent(event));
        }
    }

    private static void forwardPost(ScriptHandlers<BaublePostEvent> handlers, BaubleEquipmentEvent event) {
        ObjectArrayList<IEventHandler<BaublePostEvent>> snapshot = handlers.snapshot;
        if (!snapshot.isEmpty()) {
            dispatch(snapshot, new BaublePostEvent(event.player, event.item));
        }
    }

    private static <T> void dispatch(ObjectArrayList<IEventHandler<T>> handlers, T event) {
        for (int i = 0, size = handlers.size(); i < size; i++) {
            handlers.get(i)
                .handle(event);
        }
    }

    @Override
    public void reload() {
        WEAR.clear();
        DISROBE.clear();
        WEAR_POST.clear();
        DISROBE_POST.clear();
    }

    private static final class ScriptHandlers<T> {

        // Only registration changes the list; dispatch never holds a lock while executing a script.
        private volatile ObjectArrayList<IEventHandler<T>> snapshot = new ObjectArrayList<>(0);

        private synchronized void add(IEventHandler<T> handler) {
            ObjectArrayList<IEventHandler<T>> updated = new ObjectArrayList<>(snapshot.size() + 1);
            updated.addAll(snapshot);
            updated.add(handler);
            snapshot = updated;
        }

        private synchronized void clear() {
            if (!snapshot.isEmpty()) {
                snapshot = new ObjectArrayList<>(0);
            }
        }
    }
}

package com.circulation.m3t.crt.events;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.Util.M3TCrtReload;
import minetweaker.util.EventList;
import minetweaker.util.IEventHandler;
import project.studio.manametalmod.MMM;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "Events")
public class M3TEventAPI implements M3TCrtReload {

    private static final EventList<BaubleEvent> Wear = new EventList();
    private static final EventList<BaubleEvent> Disrobe = new EventList();
    public static final EventList<BaubleEvent> defWear = new EventList();
    public static final EventList<BaubleEvent> defDisrobe = new EventList();
    private static final EventList<BaublePostEvent> WearPost = new EventList();
    private static final EventList<BaublePostEvent> DisrobePost = new EventList();
    public static final EventList<BaublePostEvent> defWearPost = new EventList();
    public static final EventList<BaublePostEvent> defDisrobePost = new EventList();

    @ZenMethod
    public static void onBaubleWearEvent(IEventHandler<BaubleEvent> handler) {
        Wear.add(handler);
    }

    @ZenMethod
    public static void onBaubleDisrobeEvent(IEventHandler<BaubleEvent> handler) {
        Disrobe.add(handler);
    }

    @ZenMethod
    public static void onBaubleWearPostEvent(IEventHandler<BaublePostEvent> handler) {
        WearPost.add(handler);
    }

    @ZenMethod
    public static void onBaubleDisrobePostEvent(IEventHandler<BaublePostEvent> handler) {
        DisrobePost.add(handler);
    }

    public static void publishAllDisrobePost(BaublePostEvent event){
        DisrobePost.publish(event);
        defDisrobePost.publish(event);
    }

    public static void publishAllWearPost(BaublePostEvent event){
        WearPost.publish(event);
        defWearPost.publish(event);
    }

    public static void publishAllDisrobe(BaubleEvent event){
        if (MMM.isCLIENT())return;
        Disrobe.publish(event);
        defDisrobe.publish(event);
    }

    public static void publishAllWear(BaubleEvent event){
        if (MMM.isCLIENT())return;
        Wear.publish(event);
        defWear.publish(event);
    }

    @Override
    public void reload() {
        Wear.clear();
        Disrobe.clear();
        WearPost.clear();
        DisrobePost.clear();
    }

    @Override
    public void postReload() {

    }
}

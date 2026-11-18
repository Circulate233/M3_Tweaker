package com.circulation.m3t.crt.events;

import com.circulation.m3t.M3TCrtAPI;
import com.circulation.m3t.event.BaubleEquipmentEvent;

import stanhebben.zenscript.annotations.ZenClass;

@ZenClass(M3TCrtAPI.CrtClass + "event.BaubleEvent")
public class BaubleEvent extends BaublePostEvent implements CancelEvent {

    private final BaubleEquipmentEvent forgeEvent;

    BaubleEvent(BaubleEquipmentEvent event) {
        super(event.player, event.item);
        this.forgeEvent = event;
    }

    @Override
    public void cancel() {
        forgeEvent.setCanceled(true);
    }

    @Override
    public boolean isCancel() {
        return forgeEvent.isCanceled();
    }
}

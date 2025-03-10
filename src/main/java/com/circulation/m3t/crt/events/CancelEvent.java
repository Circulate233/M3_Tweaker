package com.circulation.m3t.crt.events;

import com.circulation.m3t.M3TCrtAPI;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "event.CancelEvent")
public interface CancelEvent {

    @ZenMethod
    void cancel();

    @ZenMethod
    boolean isCancel();
}

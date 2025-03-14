package com.circulation.m3t.crt.events;

import com.circulation.m3t.M3TCrtAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass(M3TCrtAPI.CrtClass + "event.BaubleEvent")
public class BaubleEvent extends BaublePostEvent implements CancelEvent {

    private boolean Cancel;

    public BaubleEvent(EntityPlayer player, ItemStack item) {
        super(player, item);
        this.Cancel = false;
    }

    @Override
    public void cancel() {
        this.Cancel = true;
    }

    @Override
    public boolean isCancel() {
        return this.Cancel;
    }
}

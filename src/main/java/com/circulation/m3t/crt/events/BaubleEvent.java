package com.circulation.m3t.crt.events;

import com.circulation.m3t.M3TCrtAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.player.IPlayer;
import minetweaker.api.util.Position3f;
import minetweaker.api.world.IDimension;
import minetweaker.mc1710.world.MCDimension;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

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

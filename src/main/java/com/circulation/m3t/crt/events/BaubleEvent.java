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
public class BaubleEvent implements CancelEvent {

    public EntityPlayer player;
    public ItemStack item;
    public Position3f pos;
    public MCDimension world;
    private boolean Cancel;

    public BaubleEvent(EntityPlayer player, ItemStack item, Position3f pos, World world) {
        this.player = player;
        this.item = item;
        this.pos = pos;
        this.Cancel = false;
        this.world = new MCDimension(world);
    }

    @ZenGetter("player")
    public IPlayer getPlayer(){
        return MineTweakerMC.getIPlayer(player);
    }

    @ZenGetter("world")
    public IDimension getWorld(){
        return this.world;
    }

    @ZenGetter("pos")
    public Position3f getPos(){
        return this.pos;
    }

    @ZenGetter("item")
    public IItemStack getItem(){
        return MineTweakerMC.getIItemStack(item);
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

package com.circulation.m3t.crt.events;

import com.circulation.m3t.M3TCrtAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(M3TCrtAPI.CrtClass + "CareerEvent")
public class CareerEvent implements CancelEvent {

    protected final EntityPlayer player;
    protected final int careerID;
    protected boolean canceled = false;

    public CareerEvent(EntityPlayer player, int careerID) {
        this.player = player;
        this.careerID = careerID;
    }

    @Override
    @ZenMethod
    public void cancel() {
        this.canceled = true;
    }

    @Override
    @ZenMethod
    public boolean isCancel() {
        return this.canceled;
    }

    @ZenGetter("player")
    public IPlayer getPlayer() {
        return MineTweakerMC.getIPlayer(player);
    }

    @ZenGetter("careerID")
    public int getCareerID() {
        return careerID;
    }

    @ZenClass(M3TCrtAPI.CrtClass + "CareerLevelUpEvent")
    public static class LevelUpEvent extends CareerEvent {
        private final int newLevel;

        public LevelUpEvent(EntityPlayer player, int careerID, int newLevel) {
            super(player, careerID);
            this.newLevel = newLevel;
        }

        @ZenGetter("level")
        public int getLevel() {
            return newLevel;
        }
    }

    @ZenClass(M3TCrtAPI.CrtClass + "CareerChangeEvent")
    public static class ChangeEvent extends CareerEvent {
        private final int oldCareerID;

        public ChangeEvent(EntityPlayer player, int oldCareerID, int newCareerID) {
            super(player, newCareerID);
            this.oldCareerID = oldCareerID;
        }

        @ZenGetter("oldCareerID")
        public int getOldCareerID() {
            return oldCareerID;
        }
    }

    @ZenClass(M3TCrtAPI.CrtClass + "CareerPointAllocateEvent")
    public static class PointAllocateEvent extends CareerEvent {
        private final String pointType;
        private final int amount;

        public PointAllocateEvent(EntityPlayer player, int careerID, String pointType, int amount) {
            super(player, careerID);
            this.pointType = pointType;
            this.amount = amount;
        }

        @ZenGetter("pointType")
        public String getPointType() {
            return pointType;
        }

        @ZenGetter("amount")
        public int getAmount() {
            return amount;
        }
    }
}

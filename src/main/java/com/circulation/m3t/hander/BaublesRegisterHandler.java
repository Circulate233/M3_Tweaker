package com.circulation.m3t.hander;

import com.circulation.m3t.item.M3EBaublesBasic;
import com.circulation.m3t.network.CrtLoading;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;

import static com.circulation.m3t.M3Tweaker.network;

public class BaublesRegisterHandler {

    public static BaublesRegisterHandler INSTANCE = new BaublesRegisterHandler();

    private BaublesRegisterHandler(){

    }

    public static final short Necklace = 0;
    public static final short Belt = 1;
    public static final short Earring = 2;
    public static final short Cloak = 3;
    public static final short Ring = 4;
    public static final short Shoulder = 5;
    public static final short Wrist = 6;
    public static final short Gloves = 7;
    public static final short Medal = 8;
    public static final short Bracelet = 9;
    public static final short Anklet = 10;
    public static final short Hairpin = 11;
    public static final short Scarf = 12;
    public static final short Tattoo = 13;
    public static final short Sock = 14;
    public static final short Glasses = 15;
    public static final short Brooch = 16;
    public static final short Special = 17;
    public static final short KitchenKnife = 18;
    public static final short CookingKnife = 19;
    public static final short Brewing = 20;
    public static final short Mirror = 21;
    public static final short Forging = 22;
    public static final short Needle = 23;
    public static final short Pearl = 24;
    public static final short FishingRod = 25;
    public static final short Pet_items = 26;
    public static final short Core = 27;
    public static final short Antiquities = 28;

    public static void register() {
        M3EBaublesBasic.registerAllBaubles();
    }

    @SubscribeEvent
    public void PlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event){
        network.sendTo(new CrtLoading(), (EntityPlayerMP) event.player);
    }

}

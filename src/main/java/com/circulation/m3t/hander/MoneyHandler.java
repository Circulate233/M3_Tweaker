package com.circulation.m3t.hander;

import net.minecraft.item.Item;
import project.studio.manametalmod.ManaMetalAPI;

import java.util.*;

public class MoneyHandler {

    private static final Map<Item,Integer> map = new HashMap<>();

    public static void setMoney(Item item,int money) {
        map.put(item, money);
    }

    public static void registerAllMoney() {
        map.forEach(ManaMetalAPI::setItemValue);
    }
}

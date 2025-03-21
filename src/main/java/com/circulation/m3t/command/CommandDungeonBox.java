package com.circulation.m3t.command;

import com.circulation.m3t.crt.DungeonBoxHandler;
import minetweaker.MineTweakerAPI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;
import project.studio.manametalmod.ManaMetalAPI;

import java.util.List;

import static com.circulation.m3t.Util.Function.getText;

public class CommandDungeonBox extends DungeonBoxHandler {

    public static void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 2){
            switch (args[1]){
                case "dungeonBoxList":
                    if (ManaMetalAPI.DungeonItems != null){
                        ManaMetalAPI.DungeonItems.forEach(itemStack -> MineTweakerAPI.logInfo(Item.itemRegistry.getNameForObject(itemStack.getItem()) + ":" + itemStack.getItemDamage() + "   amount:" + itemStack.stackSize));
                    } else {
                        sender.addChatMessage(new ChatComponentText(getText("info.m3t.command.dungeonBox1")));
                    }
                    return;
            }
        }
        sender.addChatMessage(new ChatComponentText(getText("info.m3t.command.false")));
    }

    public static List DungeonBoxTab(String[] args) {
        if (args.length == 2) {
            return CommandBase.getListOfStringsMatchingLastWord(args,
                "dungeonBoxList"
            );
        }
        return null;
    }

}

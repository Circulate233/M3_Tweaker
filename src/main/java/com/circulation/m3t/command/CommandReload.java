package com.circulation.m3t.command;

import com.circulation.m3t.Util.Function;
import com.circulation.m3t.Util.RegisterRecipe;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import static com.circulation.m3t.Util.Function.getText;

public class CommandReload {

    public static void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 1){
            RegisterRecipe.complete = false;
            RegisterRecipe.M3Recipe();

            sender.addChatMessage(new ChatComponentText(Function.getText("info.m3t.command.reload")));
            return;
        }
        sender.addChatMessage(new ChatComponentText(getText("info.m3t.command.false")));
    }

}

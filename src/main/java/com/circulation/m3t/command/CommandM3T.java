package com.circulation.m3t.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class CommandM3T extends CommandBase {

    public static CommandM3T INSTANCE = new CommandM3T();

    private CommandM3T(){}

    @Override
    public String getCommandName() {
        return "m3t";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "info.m3t.command.help";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0){
            switch (args[0]){
                case "help":
                    sendHelp(sender);
                    return;
                case "Bauble":
                    CommandBauble.processCommand(sender,args);
                    return;
            }
        }
        sender.addChatMessage(new ChatComponentTranslation("info.m3t.command.help"));
    }

    @Override
    public List<?> addTabCompletionOptions(ICommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args,
                "help",
                "dungeonBox",
                "Bauble"
            );
        }

        if (args.length == 2) {
            switch (args[0]){
                case "Bauble":
                    return CommandBauble.BaubleTab(args);
            }
        }

        return completions;
    }

    public void sendHelp(ICommandSender sender){
        if (sender instanceof EntityPlayer) {
            String[] help = new String[]{
                "/m3t DungeonBox {}",
                "/m3t Bauble {}"
            };
            for (String s : help) {
                sender.addChatMessage(new ChatComponentText(s));
            }
        }
    }
}

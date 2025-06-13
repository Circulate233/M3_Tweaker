package com.circulation.m3t.command;

import com.circulation.m3t.Util.Function;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import project.studio.manametalmod.MMM;
import project.studio.manametalmod.inventory.ContainerManaItem;

import java.util.List;

import static project.studio.manametalmod.optool.OpToolCore.magicitemOP;

public class CommandBauble {

    public static void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 2){
            switch (args[1]){
                case "debug":
                    for(int s = 0; s < ContainerManaItem.slots.length; ++s) {
                        if (s != 41 && ContainerManaItem.slots[s] != null) {
                            ItemStack item = new ItemStack(magicitemOP,1,s);
                            new NBTTagCompound();
                            NBTTagCompound nbt;
                            NBTTagCompound nbt1 = new NBTTagCompound();
                            nbt1.setString("Name","§6Number " + s + Function.getText("manaItemType." + ContainerManaItem.slots[s]));
                            nbt = new NBTTagCompound();
                            nbt.setTag("display",nbt1);
                            item.setTagCompound(nbt);
                            MMM.getEntityNBT((EntityPlayer) sender).item.setInventorySlotContents(s,item);
                        }
                    }
                    return;
            }
        }
        sender.addChatMessage(new ChatComponentTranslation("info.m3t.command.false"));
    }

    public static List<?> BaubleTab(String[] args) {
        if (args.length == 2) {
            return CommandBase.getListOfStringsMatchingLastWord(args,
                "debug"
            );
        }
        return null;
    }


}

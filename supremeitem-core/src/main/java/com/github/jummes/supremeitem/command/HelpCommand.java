package com.github.jummes.supremeitem.command;

import com.github.jummes.libs.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class HelpCommand extends AbstractCommand {

    public HelpCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @Override
    protected void execute() {
        sender.sendMessage("        &cSupreme&6Item &cHelp\n" +
                "&2/si help &7Show help message.\n" +
                "&2/si list &7Show the items GUI.\n" +
                "&2/si get [name] &7Get the item with the given name.\n" +
                "&2/si skill &7Open the skills GUI.");
    }

    @Override
    protected boolean isOnlyPlayer() {
        return false;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("supremeitem.admin.help");
    }
}

package com.github.jummes.supremeitem.command;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.item.Item;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class ItemGetCommand extends AbstractCommand {

    public ItemGetCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @Override
    protected void execute() {
        if (arguments.length < 1) {
            return;
        }

        Item item = SupremeItem.getInstance().getItemManager().getItemByName(arguments[0]);
        if (item == null) {
            sender.sendMessage(Libs.getLocale().get("messages.command.item-not-found"));
            return;
        }
        ((Player) sender).getInventory().addItem(item.getUsableItem());
        sender.sendMessage(Libs.getLocale().get("messages.command.item-received"));
    }

    @Override
    protected boolean isOnlyPlayer() {
        return true;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("supremeitem.item.get");
    }
}

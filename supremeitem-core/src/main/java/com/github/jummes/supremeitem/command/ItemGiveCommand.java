package com.github.jummes.supremeitem.command;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class ItemGiveCommand extends AbstractCommand {
    public ItemGiveCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @Override
    protected void execute() {
        if (arguments.length < 2) {
            return;
        }

        Player p = Bukkit.getPlayer(arguments[0]);
        if (p == null) {
            sender.sendMessage(Libs.getLocale().get("messages.command.player-not-found"));
            return;
        }

        Item item = SupremeItem.getInstance().getItemManager().getByName(arguments[1]);
        if (item == null) {
            sender.sendMessage(Libs.getLocale().get("messages.command.item-not-found"));
            return;
        }

        p.getInventory().addItem(item.getUsableItem());
        sender.sendMessage(Libs.getLocale().get("messages.command.player-item-received").replace("$player", p.getName()));
    }

    @Override
    protected boolean isOnlyPlayer() {
        return false;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("supremeitem.item.give");
    }
}

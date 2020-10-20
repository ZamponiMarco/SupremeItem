package com.github.jummes.supremeitem.command;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.libs.gui.model.ModelCollectionInventoryHolder;
import com.github.jummes.supremeitem.SupremeItem;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class PlaceholderListCommand extends AbstractCommand {
    public PlaceholderListCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @SneakyThrows
    @Override
    protected void execute() {
        Player p = (Player) sender;
        p.openInventory(new ModelCollectionInventoryHolder<>(SupremeItem.getInstance(),
                SupremeItem.getInstance().getSavedPlaceholderManager(), "placeholders").getInventory());
    }

    @Override
    protected boolean isOnlyPlayer() {
        return true;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("supremeitem.placeholder.list");
    }
}

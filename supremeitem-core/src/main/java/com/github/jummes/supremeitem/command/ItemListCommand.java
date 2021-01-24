package com.github.jummes.supremeitem.command;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.gui.ItemCollectionInventoryHolder;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class ItemListCommand extends AbstractCommand {

    public ItemListCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @SneakyThrows
    @Override
    protected void execute() {
        Player p = (Player) sender;
        p.openInventory(new ItemCollectionInventoryHolder(SupremeItem.getInstance(), null,
                new ModelPath<>(SupremeItem.getInstance().getItemManager(), null), SupremeItem.getInstance().
                getItemManager().getClass().getDeclaredField("items"), 1, o -> true).getInventory());
    }

    @Override
    protected boolean isOnlyPlayer() {
        return true;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("supremeitem.item.list");
    }
}

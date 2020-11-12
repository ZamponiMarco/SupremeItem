package com.github.jummes.supremeitem.command;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.database.NamedModel;
import com.github.jummes.supremeitem.item.Item;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import java.util.stream.Collectors;

public class TestCommand extends AbstractCommand {
    public TestCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @Override
    protected void execute() {
        Item item = SupremeItem.getInstance().getItemManager().getByName(arguments[0]);
        System.out.println(item.getUsedSkills().stream().map(NamedModel::getName).collect(Collectors.joining()));
    }

    @Override
    protected boolean isOnlyPlayer() {
        return false;
    }

    @Override
    protected Permission getPermission() {
        return null;
    }
}

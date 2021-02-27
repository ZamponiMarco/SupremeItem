package com.github.jummes.supremeitem.command;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.supremeitem.SupremeItem;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupCommand extends AbstractCommand {
    public BackupCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @Override
    protected void execute() {
        File pluginFolder = SupremeItem.getInstance().getDataFolder();

        File backupFolder = new File(pluginFolder, "backup");
        if (!backupFolder.exists()) {
            backupFolder.mkdir();
        }

        if (backupFile(pluginFolder, backupFolder, "item") &&
                backupFile(pluginFolder, backupFolder, "savedskill") &&
                backupFile(pluginFolder, backupFolder, "savedplaceholder"))
            sender.sendMessage(Libs.getLocale().get("messages.command.backup-success"));
        else
            sender.sendMessage(Libs.getLocale().get("messages.command.backup-failure"));
    }

    private boolean backupFile(File pluginFolder, File backupFolder, String fileName) {
        File file = new File(pluginFolder, fileName + ".yml");

        if (file.exists()) {
            return FileUtil.copy(file, new File(backupFolder, new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss_a").
                    format(new Date()) + "-" + fileName + ".yml"));
        }
        return false;
    }

    @Override
    protected boolean isOnlyPlayer() {
        return false;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("supremeitem.admin.backup");
    }
}

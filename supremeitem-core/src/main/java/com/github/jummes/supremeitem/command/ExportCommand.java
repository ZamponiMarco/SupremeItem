package com.github.jummes.supremeitem.command;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.github.jummes.supremeitem.util.CompressUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.nio.charset.Charset;
import java.util.Base64;

public class ExportCommand extends AbstractCommand {
    public ExportCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @Override
    protected void execute() {
        Player player = (Player) sender;

        if (!Bukkit.getServer().getOnlineMode()) {
            return;
        }

        if (arguments.length < 1) {
            return;
        }

        String name = arguments[0];
        Item item = SupremeItem.getInstance().getItemManager().getByName(name);

        if (item == null) {
            return;
        }

        JsonArray skillsArray = new JsonArray();
        item.getUsedSkills().stream().map(skill -> parseSkill(skill, player.getName())).
                forEach(skill -> skillsArray.add(skill));

        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);
        obj.addProperty("item", new String(Base64.getEncoder().encode(CompressUtils.
                compress(item.toSerializedString().getBytes())), Charset.defaultCharset()));
        obj.addProperty("owner", player.getName());
        obj.add("skills", skillsArray);

        System.out.println(obj.toString());
    }

    private JsonElement parseSkill(SavedSkill skill, String owner) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", skill.getName());
        obj.addProperty("owner", owner);
        obj.addProperty("skill", new String(Base64.getEncoder().encode(CompressUtils.
                compress(skill.toSerializedString().getBytes())), Charset.defaultCharset()));
        return obj;
    }

    @Override
    protected boolean isOnlyPlayer() {
        return true;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("supremeitem.item.export");
    }
}

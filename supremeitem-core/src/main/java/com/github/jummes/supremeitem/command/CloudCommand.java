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

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CloudCommand extends AbstractCommand {
    public CloudCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @Override
    protected void execute() {

        if (arguments.length < 1) {
            return;
        }

        String argument = arguments[0];

        switch (argument) {
            case "import":
            case "export":
                exportItem();
        }
    }

    @Override
    protected boolean isOnlyPlayer() {
        return true;
    }

    @Override
    protected Permission getPermission() {
        return null;
    }

    private void exportItem() {
        Bukkit.getScheduler().runTaskAsynchronously(SupremeItem.getInstance(), () -> {
            Player player = (Player) sender;

            if (!Bukkit.getServer().getOnlineMode()) {
                return;
            }

            if (arguments.length < 2) {
                return;
            }

            String name = arguments[1];
            Item item = SupremeItem.getInstance().getItemManager().getByName(name);

            if (item == null) {
                return;
            }

            JsonObject obj = getItemJson(player, item);

            byte[] out = obj.toString().getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            URL url;
            try {
                url = new URL("http://localhost:3000/items");
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection) con;
                http.setFixedLengthStreamingMode(length);
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.setDoOutput(true);
                http.connect();
                try (OutputStream os = http.getOutputStream()) {
                    os.write(out);
                }
                http.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private JsonObject getItemJson(Player player, Item item) {
        JsonArray skillsArray = new JsonArray();
        item.getUsedSavedSkills().stream().map(skill -> parseSkill(skill, player.getName())).
                forEach(skillsArray::add);

        JsonObject obj = new JsonObject();
        obj.addProperty("name", item.getName());
        obj.addProperty("item", new String(Base64.getEncoder().encode(CompressUtils.
                compress(item.toSerializedString().getBytes())), Charset.defaultCharset()));
        obj.addProperty("owner", player.getName());
        obj.add("skills", skillsArray);
        return obj;
    }

    private JsonElement parseSkill(SavedSkill skill, String owner) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", skill.getName());
        obj.addProperty("owner", owner);
        obj.addProperty("skill", new String(Base64.getEncoder().encode(CompressUtils.
                compress(skill.toSerializedString().getBytes())), Charset.defaultCharset()));
        return obj;
    }
}

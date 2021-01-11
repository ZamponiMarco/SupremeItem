package com.github.jummes.supremeitem.command;

import com.github.jummes.libs.command.AbstractCommand;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.database.NamedModel;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import com.github.jummes.supremeitem.util.CompressUtils;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.io.*;
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
                importItem();
                break;
            case "export":
                exportItem();
                break;
            case "list":
                listItems();
        }
    }

    private void listItems() {
        Bukkit.getScheduler().runTaskAsynchronously(SupremeItem.getInstance(), () -> {
            Player player = (Player) sender;

            if (!Bukkit.getServer().getOnlineMode()) {
                return;
            }

            String id = player.getUniqueId().toString();

            URL url;
            try {
                url = new URL("http://localhost:3000/items/" + id + "?keys=true");
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection) con;
                http.setRequestMethod("GET");
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.setDoInput(true);
                http.connect();
                try (InputStream is = http.getInputStream()) {
                    Reader reader = new InputStreamReader(is);
                    Gson gson = new GsonBuilder().create();
                    final TypeAdapter<JsonArray> jsonObjectTypeAdapter = gson.getAdapter(JsonArray.class);
                    JsonReader jsonReader = gson.newJsonReader(reader);
                    final JsonArray incomingJsonObject = jsonObjectTypeAdapter.read(jsonReader);
                    incomingJsonObject.forEach(elm -> {
                        player.sendMessage(elm.getAsJsonObject().get("name").getAsString());
                    });
                    jsonReader.close();
                }
                http.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected boolean isOnlyPlayer() {
        return true;
    }

    @Override
    protected Permission getPermission() {
        return null;
    }

    private void importItem() {
        Bukkit.getScheduler().runTaskAsynchronously(SupremeItem.getInstance(), () -> {
            Player player = (Player) sender;

            if (!Bukkit.getServer().getOnlineMode()) {
                return;
            }

            if (arguments.length < 3) {
                return;
            }

            String playerName = arguments[1];
            String itemName = arguments[2];
            String playerId;

            URL url;
            try {
                String uuid = getPlayerUUID(playerName);

                url = new URL("http://localhost:3000/items/" + uuid + "/" + itemName);
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection) con;
                http.setRequestMethod("GET");
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.setDoInput(true);
                http.connect();
                try (InputStream is = http.getInputStream()) {
                    Reader reader = new InputStreamReader(is);
                    Gson gson = new GsonBuilder().create();
                    final TypeAdapter<JsonObject> jsonObjectTypeAdapter = gson.getAdapter(JsonObject.class);
                    JsonReader jsonReader = gson.newJsonReader(reader);
                    final JsonObject incomingJsonObject = jsonObjectTypeAdapter.read(jsonReader);
                    // JsonReader does not consume entire input stream and can read necessary JSON token only
                    // And this is what we generate
                    incomingJsonObject.getAsJsonArray("skills").forEach(elm ->
                            SupremeItem.getInstance().getSavedSkillManager().addSkill((SavedSkill) NamedModel.
                                    fromSerializedString(new String(CompressUtils.decompress(Base64.getDecoder().
                                            decode(elm.getAsJsonObject().get("skill").getAsString())), Charset.defaultCharset()))));
                    SupremeItem.getInstance().getItemManager().addItem((Item) NamedModel.fromSerializedString(
                            new String(CompressUtils.decompress(Base64.getDecoder().decode(incomingJsonObject.
                                    get("item").getAsString())), Charset.defaultCharset())));
                    jsonReader.close();
                }
                http.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private String getPlayerUUID(String playerName) throws IOException {
        String playerId;
        URL url1 = new URL("https://api.mojang.com/profiles/minecraft");
        URLConnection con2 = url1.openConnection();
        HttpURLConnection http2 = (HttpURLConnection) con2;
        http2.setRequestMethod("POST");
        http2.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http2.setDoOutput(true);
        String load = "[\"" + playerName + "\"]";
        byte[] out = load.getBytes(StandardCharsets.UTF_8);
        http2.connect();
        try (OutputStream os = http2.getOutputStream()) {
            os.write(out);
        }
        try (InputStream is2 = http2.getInputStream()) {
            Reader reader2 = new InputStreamReader(is2);
            Gson gson2 = new GsonBuilder().create();
            final TypeAdapter<JsonObject> jsonObjectTypeAdapter = gson2.getAdapter(JsonObject.class);
            JsonReader jsonReader = gson2.newJsonReader(reader2);
            jsonReader.beginArray();
            final JsonObject incomingJsonObject = jsonObjectTypeAdapter.read(jsonReader);
            playerId = incomingJsonObject.get("id").getAsString();
            jsonReader.endArray();
            jsonReader.close();
        }
        http2.disconnect();

        String uuid = String.format("%s-%s-%s-%s-%s", playerId.substring(0, 8), playerId.substring(8, 12),
                playerId.substring(12, 16), playerId.substring(16, 20), playerId.substring(20));
        return uuid;
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
        item.getUsedSavedSkills().stream().map(skill -> parseSkill(skill, player.getUniqueId().toString())).
                forEach(skillsArray::add);

        JsonObject obj = new JsonObject();
        obj.addProperty("name", item.getName());
        obj.addProperty("item", new String(Base64.getEncoder().encode(CompressUtils.
                compress(item.toSerializedString().getBytes())), Charset.defaultCharset()));
        obj.addProperty("owner", player.getUniqueId().toString());
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

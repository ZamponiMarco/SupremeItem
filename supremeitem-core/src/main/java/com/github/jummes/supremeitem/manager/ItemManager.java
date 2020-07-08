package com.github.jummes.supremeitem.manager;

import com.github.jummes.libs.model.ModelManager;
import com.github.jummes.supremeitem.item.Item;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public class ItemManager extends ModelManager<Item> {

    public List<Item> items;

    public ItemManager(Class<Item> classObject, String databaseType, JavaPlugin plugin) {
        super(classObject, databaseType, plugin);
        items = database.loadObjects();
    }

    public Item getById(UUID id) {
        return items.stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
    }
}

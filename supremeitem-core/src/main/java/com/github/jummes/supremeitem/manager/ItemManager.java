package com.github.jummes.supremeitem.manager;

import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.ModelManager;
import com.github.jummes.supremeitem.item.AbstractItem;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.item.ItemFolder;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class ItemManager extends ModelManager<AbstractItem> {

    private List<AbstractItem> items;

    public ItemManager(Class<AbstractItem> classObject, String databaseType, JavaPlugin plugin) {
        super(classObject, databaseType, plugin, ImmutableMap.of("name", "item"));
        items = database.loadObjects();
    }

    public static boolean isSupremeItem(ItemStack i) {
        return !Libs.getWrapper().getTagItem(i, "supreme-item").equals("");
    }

    public Item getItemById(UUID id) {
        return items.stream().map(abstractItem -> abstractItem.getById(id)).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public Item getItemByName(String name) {
        return items.stream().map(abstractItem -> abstractItem.getByName(name)).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public ItemFolder getFolderByName(String name) {
        return (ItemFolder) items.stream().filter(abstractItem -> abstractItem instanceof ItemFolder &&
                abstractItem.getName().equals(name)).findFirst().orElse(null);
    }

    public AbstractItem getAbstractItemByName(String name) {
        Item item = getItemByName(name);
        if (item != null) {
            return item;
        }
        return getFolderByName(name);
    }

    public void addItem(AbstractItem item) {
        items.add(item);
        saveModel(item);
    }
}

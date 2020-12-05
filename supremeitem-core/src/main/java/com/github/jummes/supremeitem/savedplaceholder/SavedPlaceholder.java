package com.github.jummes.supremeitem.savedplaceholder;

import com.github.jummes.libs.annotation.CustomClickable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelObjectInventoryHolder;
import com.github.jummes.libs.gui.model.RemoveConfirmationInventoryHolder;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.database.NamedModel;
import com.github.jummes.supremeitem.placeholder.Placeholder;
import com.github.jummes.supremeitem.placeholder.numeric.entity.MaxHealthPlaceholder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Map;

@Getter
@CustomClickable(customCollectionClickConsumer = "getCustomClickConsumer")
public class SavedPlaceholder extends NamedModel {

    private static final String PLACEHOLDER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzcyMzcwNGE5ZDU5MTBiOWNkNTA1ZGM5OWM3NzliZjUwMzc5Y2I4NDc0NWNjNzE5ZTlmNzg0ZGQ4YyJ9fX0=";

    private static int counter = 1;

    @Serializable(headTexture = PLACEHOLDER_HEAD, description = "gui.saved-placeholder.placeholder")
    private Placeholder placeholder;
    @Serializable
    private ItemStackWrapper item;

    public SavedPlaceholder() {
        this(nextAvailableName(), new MaxHealthPlaceholder(), new ItemStackWrapper(), true);
    }

    public SavedPlaceholder(String name, Placeholder placeholder, ItemStackWrapper item) {
        this(name, placeholder, item, true);
        counter++;
    }

    protected SavedPlaceholder(String name, Placeholder placeholder, ItemStackWrapper item, boolean increasedCounter) {
        super(name);
        this.placeholder = placeholder;
        this.item = item;
    }

    private static String nextAvailableName() {
        String name;
        do {
            name = "placeholder" + counter;
            counter++;
        } while (SupremeItem.getInstance().getSavedPlaceholderManager().getByName(name) != null);
        return name;
    }

    public static SavedPlaceholder deserialize(Map<String, Object> map) {
        String name = (String) map.get("name");
        Placeholder placeholder = (Placeholder) map.get("placeholder");
        ItemStackWrapper item = (ItemStackWrapper) map.getOrDefault("item", new ItemStackWrapper());
        return new SavedPlaceholder(name, placeholder, item);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(item.getWrapped(), "&6&lName: &c" + name,
                Libs.getLocale().getList("gui.saved-placeholder.description"));
    }


    public void getCustomClickConsumer(JavaPlugin plugin, PluginInventoryHolder parent,
                                       ModelPath<? extends Model> path, Field field, InventoryClickEvent e) {
        if (e.getClick().equals(ClickType.LEFT)) {
            path.addModel(this);
            if (!e.getCursor().getType().equals(Material.AIR)) {
                ItemStack newItem = e.getCursor().clone();
                this.item = new ItemStackWrapper(newItem, true);
                path.saveModel();
                e.getWhoClicked().getInventory().addItem(newItem);
                e.getCursor().setAmount(0);
                path.popModel();
                e.getWhoClicked().openInventory(parent.getInventory());
                return;
            }
            e.getWhoClicked().openInventory(new ModelObjectInventoryHolder(plugin, parent, path).getInventory());
        } else if (e.getClick().equals(ClickType.RIGHT)) {
            e.getWhoClicked().openInventory(new RemoveConfirmationInventoryHolder(plugin, parent, path, this,
                    field).getInventory());
        }
    }

}

package com.github.jummes.supremeitem.gui;

import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelCollectionInventoryHolder;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public abstract class SelectableCollectionInventoryHolder<S extends Model> extends ModelCollectionInventoryHolder<S> {

    protected static final String SELECTED_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQ3OGNjMzkxYWZmYjgwYjJiMzVlYjczNjRmZjc2MmQzODQyNGMwN2U3MjRiOTkzOTZkZWU5MjFmYmJjOWNmIn19fQ==";

    protected final List<S> selected;

    public SelectableCollectionInventoryHolder(JavaPlugin plugin, PluginInventoryHolder parent, ModelPath<S> path,
                                               Field field, int page, Predicate<S> filter) {
        super(plugin, parent, path, field, page, filter);
        this.selected = new ArrayList<>();
    }

    @Override
    protected void initializeInventory() {
        try {
            List<S> models = getModels();
            List<S> toList = getPageModels(models);

            int maxPage = (int) Math.ceil((models.size() > 0 ? models.size() : 1) / (double) MODELS_NUMBER);

            this.inventory = Bukkit.createInventory(this, 54,
                    MessageUtils.color("&c&l" + field.getName() + " &6&l(&c" + page + "&6&l/&c" + maxPage + "&6&l)"));

            toList.forEach(model -> registerClickConsumer(toList.indexOf(model), selected.contains(model) ?
                    getGlintedItem(model) : model.getGUIItem(), e -> executeClickConsumer(model, e)));

            placeCollectionOnlyItems(maxPage);
            registerClickConsumer(53, getBackItem(), getBackConsumer());
            fillInventoryWith(Material.GRAY_STAINED_GLASS_PANE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract ItemStack getGlintedItem(S model);

    protected void selectModel(S model, InventoryClickEvent e) {
        if (!selected.contains(model)) {
            selected.add(model);
        } else {
            selected.remove(model);
        }
        e.getWhoClicked().openInventory(getInventory());
    }

    protected void selectAllActions(InventoryClickEvent e, Collection<S> models) {
        if (selected.isEmpty()) {
            selected.addAll(models);
        } else {
            unselectAllActions();
        }
        e.getWhoClicked().openInventory(getInventory());
    }

    protected void unselectAllActions() {
        selected.clear();
    }

}

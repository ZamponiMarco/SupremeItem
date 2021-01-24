package com.github.jummes.supremeitem.gui;

import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelObjectInventoryHolder;
import com.github.jummes.libs.gui.model.RemoveConfirmationInventoryHolder;
import com.github.jummes.libs.gui.model.create.ModelCreateInventoryHolder;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.item.AbstractItem;
import com.github.jummes.supremeitem.item.Item;
import com.github.jummes.supremeitem.item.ItemFolder;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ItemCollectionInventoryHolder extends SelectableCollectionInventoryHolder<AbstractItem> {

    public ItemCollectionInventoryHolder(JavaPlugin plugin, PluginInventoryHolder parent, ModelPath<AbstractItem> path,
                                         Field field, int page, Predicate<AbstractItem> filter) {
        super(plugin, parent, path, field, page, filter);
    }

    public ItemStack getGlintedItem(AbstractItem item) {
        List<String> lore = Libs.getLocale().getList("gui.item.description");
        lore.set(0, MessageUtils.color("&6&lApply actions &eto all selected items:"));
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(SELECTED_HEAD), "&6&lName: &c" + item.getName(), lore);
    }

    @Override
    protected Consumer<InventoryClickEvent> getAddConsumer() {
        return e -> e.getWhoClicked().openInventory(new ModelCreateInventoryHolder(plugin, this, path, field,
                Item.class, true).getInventory());
    }

    @SneakyThrows
    @Override
    protected void defaultClickConsumer(AbstractItem model, InventoryClickEvent e) {
        Collection<AbstractItem> items = ((Collection<AbstractItem>) FieldUtils.readField(field,
                path.getLast() != null ? path.getLast() : path.getModelManager(), true));
        if (e.getClick().equals(ClickType.LEFT)) {
            openItemGUI(model, e);
        } else if (e.getClick().equals(ClickType.RIGHT)) {
            deleteActions(model, e);
        } else if (e.getClick().equals(ClickType.MIDDLE)) {
            getUsableItem(model, e);
        } else if (e.getClick().equals(ClickType.NUMBER_KEY) && e.getHotbarButton() == 8) {
            if (model instanceof ItemFolder && !selected.contains(model)) {
                unwrapItems((ItemFolder) model, items, e);
            } else {
                wrapItems(model, e, items);
            }
        } else if (e.getClick().equals(ClickType.DROP)) {
            selectModel(model, e);
        } else if (e.getClick().equals(ClickType.CONTROL_DROP)) {
            selectAllActions(e, items);
        } else {
            unselectAllActions();
        }
    }

    private void wrapItems(AbstractItem model, InventoryClickEvent e, Collection<AbstractItem> items) {
        List<AbstractItem> toRemove = selected.contains(model) ? selected : Lists.newArrayList(model);
        ItemFolder batch = new ItemFolder();
        batch.getItems().addAll(toRemove);
        toRemove.forEach(item -> {
            path.addModel(item);
            path.deleteModel();
            path.popModel();
        });
        items.removeAll(toRemove);
        items.add(batch);
        path.addModel(batch);
        path.saveModel();
        path.popModel();
        unselectAllActions();
        e.getWhoClicked().openInventory(getInventory());
    }

    private void unwrapItems(ItemFolder model, Collection<AbstractItem> items, InventoryClickEvent e) {
        model.getItems().forEach(item -> {
            items.add(item);
            path.addModel(item);
            path.saveModel();
            path.popModel();
        });
        path.addModel(model);
        path.deleteModel();
        path.popModel();
        items.remove(model);
        unselectAllActions();
        e.getWhoClicked().openInventory(getInventory());
    }


    private void getUsableItem(AbstractItem model, InventoryClickEvent e) {
        if (model instanceof Item) {
            e.getWhoClicked().getInventory().addItem(((Item) model).getUsableItem());
        }
    }

    private void openItemGUI(AbstractItem model, InventoryClickEvent e) {
        path.addModel(model);
        e.getWhoClicked().openInventory(new ModelObjectInventoryHolder(plugin, this, path).getInventory());
    }

    private void deleteActions(AbstractItem model, InventoryClickEvent e) {
        if (selected.contains(model)) {
            e.getWhoClicked().openInventory(new RemoveConfirmationInventoryHolder(plugin, this, path,
                    new ArrayList<>(selected), field).getInventory());
        } else {
            e.getWhoClicked().openInventory(new RemoveConfirmationInventoryHolder(plugin, this, path, model,
                    field).getInventory());
        }
    }
}

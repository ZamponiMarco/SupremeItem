package com.github.jummes.supremeitem.item;

import com.github.jummes.libs.annotation.CustomClickable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelObjectInventoryHolder;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.skill.Skill;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.*;

@AllArgsConstructor
@Getter
@Setter
@CustomClickable(customCollectionClickConsumer = "defaultClickConsumer")
public class Item implements Model {

    private static final String CUSTOM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFkY2Y4NWRlYTg0NDFmN2FmMjg3ZmU3ZTAyMTFjNzRmYzY5YzI5MjNlZDQ5YTE2ZjZkZDFiOWU4MWEyNDlkMyJ9fX0=";

    @Serializable(stringValue = true)
    private UUID id;
    @Serializable(headTexture = CUSTOM_HEAD, description = "gui.item.name")
    private String name;
    @Serializable(headTexture = CUSTOM_HEAD, description = "gui.item.item")
    private ItemStackWrapper item;
    @Serializable(headTexture = CUSTOM_HEAD, description = "gui.item.skill-set")
    private Set<Skill> skillSet;
    @Serializable(headTexture = CUSTOM_HEAD, description = "gui.item.consumable")
    private boolean consumable;

    public Item() {
        this(UUID.randomUUID(), RandomStringUtils.randomAlphabetic(6), new ItemStackWrapper(), Sets.newHashSet(), false);
    }

    public static Item deserialize(Map<String, Object> map) {
        UUID id = UUID.fromString((String) map.get("id"));
        String name = (String) map.get("name");
        ItemStackWrapper item = (ItemStackWrapper) map.get("item");
        Set<Skill> skillSet = new HashSet<Skill>((List<Skill>) map.get("skillSet"));
        boolean consumable = (boolean) map.getOrDefault("consumable", false);
        return new Item(id, name, item, skillSet, consumable);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("==", getClass().getName());
        map.put("id", id.toString());
        map.put("name", name);
        map.put("item", item);
        map.put("skillSet", new ArrayList<>(skillSet));
        map.put("consumable", consumable);
        return map;
    }

    public ItemStack getUsableItem() {
        ItemStack item = this.item.getWrapped().clone();
        return Libs.getWrapper().addTagToItem(item, "supreme-item", getId().toString());
    }

    @Override
    public ItemStack getGUIItem() {
        List<String> lore = item.getWrapped().getItemMeta() == null ? null : item.getWrapped().getItemMeta().getLore();
        lore = lore == null ? Lists.newArrayList() : lore;
        lore.add(MessageUtils.color("&6&lName: &c" + name));
        lore.addAll(Libs.getLocale().getList("gui.item.description"));
        return ItemUtils.getNamedItem(item.getWrapped().clone(), item.getWrapped().getItemMeta().getDisplayName(), lore);
    }

    public void defaultClickConsumer(JavaPlugin plugin, PluginInventoryHolder parent, ModelPath<?> path, Field field,
                                     InventoryClickEvent e) throws IllegalAccessException {
        if (e.getClick().equals(ClickType.LEFT)) {
            path.addModel(this);
            e.getWhoClicked().openInventory(new ModelObjectInventoryHolder(plugin, parent, path).getInventory());
        } else if (e.getClick().equals(ClickType.RIGHT)) {
            ((Collection<Item>) FieldUtils.readField(field,
                    path.getLast() != null ? path.getLast() : path.getModelManager(), true)).remove(this);
            path.addModel(this);
            path.deleteModel();
            path.popModel();
            onRemoval();
            e.getWhoClicked().openInventory(parent.getInventory());
        } else if (e.getClick().equals(ClickType.MIDDLE)) {
            e.getWhoClicked().getInventory().addItem(getUsableItem());
        }
    }
}

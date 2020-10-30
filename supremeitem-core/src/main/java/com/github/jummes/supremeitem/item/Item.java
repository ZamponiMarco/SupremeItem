package com.github.jummes.supremeitem.item;

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
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.database.NamedModel;
import com.github.jummes.supremeitem.skill.Skill;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.*;

@Getter
@Setter
@CustomClickable(customCollectionClickConsumer = "defaultClickConsumer")
public class Item extends NamedModel {

    private static final boolean CONSUMABLE_DEFAULT = false;

    private static final String SKILL_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmJiMTI1NmViOWY2NjdjMDVmYjIxZTAyN2FhMWQ1MzU1OGJkYTc0ZTI0MGU0ZmE5ZTEzN2Q4NTFjNDE2ZmU5OCJ9fX0=";
    private static final String CONSUMABLE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTg0YTY4ZmQ3YjYyOGQzMDk2NjdkYjdhNTU4NTViNTRhYmMyM2YzNTk1YmJlNDMyMTYyMTFiZTVmZTU3MDE0In19fQ==";

    private static int counter = 1;

    @Serializable(stringValue = true)
    private UUID id;
    @Serializable(description = "gui.item.item", displayItem = "getUsableItem")
    private ItemStackWrapper item;
    @Serializable(headTexture = SKILL_HEAD, description = "gui.item.skill-set")
    private Set<Skill> skillSet;
    @Serializable(headTexture = CONSUMABLE_HEAD, description = "gui.item.consumable")
    @Serializable.Optional(defaultValue = "CONSUMABLE_DEFAULT")
    private boolean consumable;

    public Item() {
        this(UUID.randomUUID(), "item" + counter, new ItemStackWrapper(true),
                Sets.newHashSet(), CONSUMABLE_DEFAULT);
    }

    public Item(UUID id, String name, ItemStackWrapper item, Set<Skill> skillSet, boolean consumable) {
        super(name);
        counter++;
        this.id = id;
        this.item = item;
        this.skillSet = skillSet;
        this.consumable = consumable;
    }

    public static Item deserialize(Map<String, Object> map) {
        UUID id = UUID.fromString((String) map.get("id"));
        String name = (String) map.get("name");
        ItemStackWrapper item = (ItemStackWrapper) map.get("item");
        Set<Skill> skillSet = new HashSet<>((List<Skill>) map.get("skillSet"));
        boolean consumable = (boolean) map.getOrDefault("consumable", CONSUMABLE_DEFAULT);
        return new Item(id, name, item, skillSet, consumable);
    }

    public ItemStack getUsableItem() {
        ItemStack item = this.item.getWrapped().clone();
        item.setAmount(1);
        return Libs.getWrapper().addTagToItem(item, "supreme-item", getId().toString());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("==", getClass().getName());
        map.put("id", id.toString());
        map.put("name", name);
        map.put("item", item);
        map.put("skillSet", new ArrayList<>(skillSet));
        if (consumable != CONSUMABLE_DEFAULT) {
            map.put("consumable", consumable);
        }
        return map;
    }

    @Override
    public ItemStack getGUIItem() {
        List<String> lore = item.getWrapped().getItemMeta() == null ? null : item.getWrapped().getItemMeta().getLore();
        lore = lore == null ? Lists.newArrayList() : lore;
        lore.add(MessageUtils.color("&6&lName: &c" + name));
        lore.addAll(Libs.getLocale().getList("gui.item.description"));
        ItemStack itemStack = item.getWrapped().clone();
        itemStack.setAmount(1);
        return ItemUtils.getNamedItem(itemStack, item.getWrapped().getItemMeta().getDisplayName(), lore);
    }

    public void defaultClickConsumer(JavaPlugin plugin, PluginInventoryHolder parent, ModelPath<?> path, Field field,
                                     InventoryClickEvent e) {
        if (e.getClick().equals(ClickType.LEFT)) {
            path.addModel(this);
            e.getWhoClicked().openInventory(new ModelObjectInventoryHolder(plugin, parent, path).getInventory());
        } else if (e.getClick().equals(ClickType.RIGHT)) {
            e.getWhoClicked().openInventory(new RemoveConfirmationInventoryHolder(plugin, parent, path, this,
                    field).getInventory());
        } else if (e.getClick().equals(ClickType.MIDDLE)) {
            e.getWhoClicked().getInventory().addItem(getUsableItem());
        }
    }
}

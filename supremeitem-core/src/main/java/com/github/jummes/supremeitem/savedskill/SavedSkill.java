package com.github.jummes.supremeitem.savedskill;

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
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.database.NamedModel;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@CustomClickable(customCollectionClickConsumer = "getCustomClickConsumer")
public class SavedSkill extends NamedModel {

    private static final List<Action> ACTIONS_DEFAULT = Lists.newArrayList();

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";

    private static int counter = 1;

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.saved-skill.actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> actions;
    @Serializable
    private ItemStackWrapper item;

    public SavedSkill() {
        this(nextAvailableName(), Lists.newArrayList(), new ItemStackWrapper(), true);
    }

    public SavedSkill(String name, List<Action> actions, ItemStackWrapper item) {
        this(name, actions, item, true);
        counter++;
    }

    protected SavedSkill(String name, List<Action> actions, ItemStackWrapper item, boolean increasedCounter) {
        super(name);
        this.actions = actions;
        this.item = item;
    }

    private static String nextAvailableName() {
        String name;
        do {
            name = "skill" + counter;
            counter++;
        } while (SupremeItem.getInstance().getSavedSkillManager().getByName(name) != null);
        return name;
    }

    public static SavedSkill deserialize(Map<String, Object> map) {
        String name = (String) map.get("name");
        List<Action> actions = (List<Action>) map.getOrDefault("actions", Lists.newArrayList());
        ItemStackWrapper item = (ItemStackWrapper) map.getOrDefault("item", new ItemStackWrapper());
        return new SavedSkill(name, actions, item);
    }

    public static void addSkillsFromActionsList(Set<SavedSkill> skills, List<Action> actions) {
        skills.addAll(actions.stream().reduce(Lists.newArrayList(), (list, action) -> {
            list.addAll(action.getUsedSavedSkills());
            return list;
        }, (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        }));
    }

    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(item.getWrapped(), "&6&lName: &c" + name,
                Libs.getLocale().getList("gui.saved-skill.description"));
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

package com.github.jummes.supremeitem.gui;

import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelCollectionInventoryHolder;
import com.github.jummes.libs.gui.model.ModelObjectInventoryHolder;
import com.github.jummes.libs.gui.model.RemoveConfirmationInventoryHolder;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.meta.SkillAction;
import com.github.jummes.supremeitem.action.meta.WrapperAction;
import com.github.jummes.supremeitem.savedskill.SavedSkill;
import lombok.SneakyThrows;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ActionCollectionInventoryHolder extends ModelCollectionInventoryHolder<Action> {

    private static final boolean TARGET_DEFAULT = true;

    private static final String SELECTED_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQ3OGNjMzkxYWZmYjgwYjJiMzVlYjczNjRmZjc2MmQzODQyNGMwN2U3MjRiOTkzOTZkZWU5MjFmYmJjOWNmIn19fQ==";

    private final List<Action> selected;

    public ActionCollectionInventoryHolder(JavaPlugin plugin, PluginInventoryHolder parent, ModelPath<Action> path,
                                           Field field, int page, Predicate<Action> filter) {
        super(plugin, parent, path, field, page, filter);
        this.selected = new ArrayList<>();
    }

    @Override
    protected void initializeInventory() {
        try {
            List<Action> models = ((Collection<Action>) FieldUtils.readField(field,
                    path.getLast() != null ? path.getLast() : path.getModelManager(), true)).stream()
                    .filter(model -> model.getGUIItem() != null && filter.test(model)).collect(Collectors.toList());
            List<Action> toList = models.stream().filter(model -> models.indexOf(model) >= (page - 1) * MODELS_NUMBER
                    && models.indexOf(model) <= page * MODELS_NUMBER - 1).collect(Collectors.toList());
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

    public ItemStack getGlintedItem(Action action) {
        List<String> lore = Libs.getLocale().getList("gui.action.description");
        lore.set(0, MessageUtils.color("&6&lApply actions &eto all selected actions:"));
        lore.add(3 + WrapperAction.WRAPPERS_MAP.size(), MessageUtils.color("&6&l- [9] &eto wrap in a new skill"));
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(SELECTED_HEAD), action.getName(), lore);
    }

    @SneakyThrows
    protected void defaultClickConsumer(Action model, InventoryClickEvent e) {
        Collection<Action> actions = ((Collection<Action>) FieldUtils.readField(field,
                path.getLast() != null ? path.getLast() : path.getModelManager(), true));
        if (e.getClick().equals(ClickType.LEFT)) {
            path.addModel(model);
            e.getWhoClicked().openInventory(new ModelObjectInventoryHolder(plugin, this, path).getInventory());
        } else if (e.getClick().equals(ClickType.RIGHT)) {
            if (selected.contains(model)) {
                e.getWhoClicked().openInventory(new RemoveConfirmationInventoryHolder(plugin, this, path,
                        new ArrayList<>(selected), field).getInventory());
            } else {
                e.getWhoClicked().openInventory(new RemoveConfirmationInventoryHolder(plugin, this, path, model,
                        field).getInventory());
            }
        } else if (e.getClick().equals(ClickType.MIDDLE)) {
            if (selected.contains(model)) {
                actions.addAll(selected.stream().map(Action::clone).collect(Collectors.toList()));
            } else {
                actions.add(model.clone());
            }
            selected.clear();
            path.saveModel();
            e.getWhoClicked().openInventory(getInventory());
        } else if (e.getClick().equals(ClickType.NUMBER_KEY) && (model instanceof WrapperAction) &&
                e.getHotbarButton() == WrapperAction.WRAPPERS_MAP.get(model.getClass())) {
            actions.remove(model);
            path.addModel(model);
            path.deleteModel();
            path.popModel();
            model.onRemoval();
            actions.addAll(((WrapperAction) model).getWrappedActions());
            path.saveModel();
            e.getWhoClicked().openInventory(getInventory());
        } else if (e.getClick().equals(ClickType.NUMBER_KEY) && WrapperAction.WRAPPERS_MAP.containsValue(e.getHotbarButton())) {
            wrapActions(e, model, actions);
        } else if (e.getClick().equals(ClickType.NUMBER_KEY) && e.getHotbarButton() == 8) {
            if (!selected.isEmpty()) {
                if (selected.contains(model)) {
                    SavedSkill newSkill = new SavedSkill();
                    actions.removeAll(selected);
                    newSkill.getActions().addAll(selected);
                    SupremeItem.getInstance().getSavedSkillManager().getSkills().add(newSkill);
                    SupremeItem.getInstance().getSavedSkillManager().saveModel(newSkill);
                    selected.clear();
                    actions.add(new SkillAction(TARGET_DEFAULT, newSkill.getName()));
                    path.saveModel();
                    e.getWhoClicked().openInventory(getInventory());
                }
            } else if (model instanceof SkillAction) {
                SavedSkill skill = SupremeItem.getInstance().getSavedSkillManager().getByName(((SkillAction) model).getSkillName());
                if (skill != null) {
                    path.addModel(skill);
                    e.getWhoClicked().openInventory(new ModelObjectInventoryHolder(SupremeItem.getInstance(),
                            this, path).getInventory());
                }
            }
        } else if (e.getClick().equals(ClickType.DROP)) {
            if (!selected.contains(model)) {
                selected.add(model);
            } else {
                selected.remove(model);
            }
            e.getWhoClicked().openInventory(getInventory());
        } else if (e.getClick().equals(ClickType.CONTROL_DROP)) {
            if (selected.isEmpty()) {
                selected.addAll(actions);
            } else {
                selected.clear();
            }
            e.getWhoClicked().openInventory(getInventory());
        } else {
            selected.clear();
        }
    }

    @SneakyThrows
    protected void wrapActions(InventoryClickEvent e, Action model, Collection<Action> superActions) {
        WrapperAction wrapperAction = WrapperAction.WRAPPERS_MAP.inverse().get(e.getHotbarButton()).newInstance();
        superActions.add(wrapperAction);
        if (selected.contains(model)) {
            superActions.removeAll(selected);
            wrapperAction.getWrappedActions().addAll(selected);
            selected.clear();
        } else {
            superActions.remove(model);
            wrapperAction.getWrappedActions().add(model);
        }
        path.saveModel();
        e.getWhoClicked().openInventory(getInventory());
    }
}

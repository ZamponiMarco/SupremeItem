package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelObjectInventoryHolder;
import com.github.jummes.libs.gui.model.RemoveConfirmationInventoryHolder;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.supremeitem.action.Action;
import com.google.common.collect.Lists;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

@Enumerable.Parent(classArray = {DelayedAction.class, ProjectileAction.class, AreaEntitiesAction.class,
        SkillAction.class, TimerAction.class, CancelEventAction.class, ConditionAction.class, CommandAction.class,
        RepeatUntilAction.class})
@Enumerable.Displayable(name = "&9&lAction &6» &cMeta", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjgzODRjYjFiYmEyNWE1NzE5YjQyOTkyMzFhNWI1NDEzZTQzYjU3MDk5YzMyNzk5ZTczYTUxMTM2OTE3YWY4MyJ9fX0=")
public abstract class MetaAction extends Action {

    protected static final List<Action> ACTIONS_DEFAULT = Lists.newArrayList();

    protected void getExtractConsumer(JavaPlugin plugin, PluginInventoryHolder parent, ModelPath<?> path, Field field,
                                      InventoryClickEvent e, List<Action> actions) throws IllegalAccessException {
        Collection<Action> superActions = ((Collection<Action>) FieldUtils.readField(field,
                path.getLast() != null ? path.getLast() : path.getModelManager(), true));
        if (e.getClick().equals(ClickType.LEFT)) {
            path.addModel(this);
            e.getWhoClicked().openInventory(new ModelObjectInventoryHolder(plugin, parent, path).getInventory());
        } else if (e.getClick().equals(ClickType.RIGHT)) {
            e.getWhoClicked().openInventory(new RemoveConfirmationInventoryHolder(plugin, parent, path, this,
                    field).getInventory());
        } else if (e.getClick().equals(ClickType.MIDDLE)) {
            actions.add(clone());
            path.saveModel();
            e.getWhoClicked().openInventory(parent.getInventory());
        } else if (e.getClick().equals(ClickType.NUMBER_KEY) && (e.getHotbarButton() == 0)) {
            superActions.remove(this);
            path.addModel(this);
            path.deleteModel();
            path.popModel();
            onRemoval();
            if (e.getHotbarButton() == 0) {
                superActions.addAll(actions);
                path.saveModel();
            }
            e.getWhoClicked().openInventory(parent.getInventory());
        }
    }
}

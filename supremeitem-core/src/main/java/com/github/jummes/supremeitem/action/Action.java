package com.github.jummes.supremeitem.action;

import com.github.jummes.libs.annotation.CustomClickable;
import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelObjectInventoryHolder;
import com.github.jummes.libs.gui.model.RemoveConfirmationInventoryHolder;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.entity.EntityAction;
import com.github.jummes.supremeitem.action.location.LocationAction;
import com.github.jummes.supremeitem.action.meta.DelayedAction;
import com.github.jummes.supremeitem.action.meta.MetaAction;
import com.github.jummes.supremeitem.action.meta.TimerAction;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

@CustomClickable(customCollectionClickConsumer = "getCustomConsumer")
@Enumerable.Parent(classArray = {EntityAction.class,
        LocationAction.class, MetaAction.class})
public abstract class Action implements Model, Cloneable {

    protected transient SupremeItem plugin;

    public Action() {
        this.plugin = SupremeItem.getInstance();
    }

    /**
     * Executes the action.
     *
     * @param target The target of the action.
     * @param source The source of the action.
     * @return The ActionResult that describes how the action went.
     */
    public ActionResult executeAction(Target target, Source source) {
        if (getPossibleTargets().stream().anyMatch(clazz -> ClassUtils.isAssignable(target.getClass(), clazz))) {
            return execute(target, source);
        }
        return ActionResult.FAILURE;
    }

    protected abstract ActionResult execute(Target target, Source source);

    public abstract List<Class<? extends Target>> getPossibleTargets();

    public void getCustomConsumer(JavaPlugin plugin, PluginInventoryHolder parent, ModelPath<?> path, Field field,
                                  InventoryClickEvent e) throws IllegalAccessException {
        Collection<Action> actions = ((Collection<Action>) FieldUtils.readField(field,
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
        } else if (e.getClick().equals(ClickType.NUMBER_KEY) && e.getHotbarButton() == 0) {
            actions.remove(this);
            path.addModel(this);
            path.deleteModel();
            path.popModel();
            onRemoval();
            actions.add(new DelayedAction(Lists.newArrayList(this), new NumericValue(10)));
            path.saveModel();
            e.getWhoClicked().openInventory(parent.getInventory());
        } else if (e.getClick().equals(ClickType.NUMBER_KEY) && e.getHotbarButton() == 1) {
            actions.remove(this);
            path.addModel(this);
            path.deleteModel();
            path.popModel();
            onRemoval();
            actions.add(new TimerAction(5, 10, Lists.newArrayList(this)));
            path.saveModel();
            e.getWhoClicked().openInventory(parent.getInventory());
        }
    }

    @Override
    public ItemStack getGUIItem() {
        return new ItemStack(Material.PAPER);
    }

    @Override
    public abstract Action clone();


    public enum ActionResult {
        /**
         * An action concluded with success.
         */
        SUCCESS,
        /**
         * An action that has been cancelled.
         */
        CANCELLED,
        FAILURE
    }
}

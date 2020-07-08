package com.github.jummes.supremeitem.action;

import com.github.jummes.libs.annotation.CustomClickable;
import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelObjectInventoryHolder;
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
public abstract class Action implements Model {

    protected SupremeItem plugin;

    public Action() {
        this.plugin = SupremeItem.getInstance();
    }

    public void executeAction(Target target, Source source) {
        if (getPossibleTargets().stream().anyMatch(clazz -> ClassUtils.isAssignable(target.getClass(), clazz))) {
            execute(target, source);
        }
    }

    protected abstract void execute(Target target, Source source);

    public abstract List<Class<? extends Target>> getPossibleTargets();

    public void getCustomConsumer(JavaPlugin plugin, PluginInventoryHolder parent, ModelPath<?> path, Field field,
                                  InventoryClickEvent e) throws IllegalAccessException {
        Collection<Action> actions = ((Collection<Action>) FieldUtils.readField(field,
                path.getLast() != null ? path.getLast() : path.getModelManager(), true));
        if (e.getClick().equals(ClickType.LEFT)) {
            path.addModel(this);
            e.getWhoClicked().openInventory(new ModelObjectInventoryHolder(plugin, parent, path).getInventory());
        } else if (e.getClick().equals(ClickType.RIGHT)
                || (e.getClick().equals(ClickType.NUMBER_KEY) && (e.getHotbarButton() == 0))) {
            actions.remove(this);
            path.addModel(this);
            path.deleteModel();
            path.popModel();
            onRemoval();
            if (e.getHotbarButton() == 0) {
                wrapAction(path, actions, new DelayedAction(this, 10));
            } else if (e.getHotbarButton() == 1) {
                wrapAction(path, actions, new TimerAction(5, 10, this));
            }
            e.getWhoClicked().openInventory(parent.getInventory());
        }
    }

    private void wrapAction(ModelPath<?> path, Collection<Action> actions, Action action) {
        actions.add(action);
        path.saveModel();
    }

    @Override
    public ItemStack getGUIItem() {
        return new ItemStack(Material.PAPER);
    }
}

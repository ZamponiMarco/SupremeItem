package com.github.jummes.supremeitem.action;

import com.github.jummes.libs.annotation.CustomClickable;
import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelObjectInventoryHolder;
import com.github.jummes.libs.gui.model.RemoveConfirmationInventoryHolder;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
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
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collection;

@CustomClickable(customCollectionClickConsumer = "getCustomConsumer")
@Enumerable.Parent(classArray = {EntityAction.class,
        LocationAction.class, MetaAction.class})
public abstract class Action implements Model, Cloneable {

    protected static final boolean TARGET_DEFAULT = true;

    private static final String TARGET_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzc4N2I3YWZiNWE1OTk1Mzk3NWJiYTI0NzM3NDliNjAxZDU0ZDZmOTNjZWFjN2EwMmFjNjlhYWU3ZjliOCJ9fX0==";

    protected transient SupremeItem plugin;

    @Serializable(displayItem = "targetItem", description = "gui.action.target")
    @Serializable.Optional(defaultValue = "TARGET_DEFAULT")
    protected boolean target;

    public Action(boolean target) {
        this.plugin = SupremeItem.getInstance();
        this.target = target;
    }

    /**
     * Executes the action.
     *
     * @param target The target of the action.
     * @param source The source of the action.
     * @return The ActionResult that describes how the action went.
     */
    public ActionResult executeAction(Target target, Source source) {
        return execute(target, source);
    }

    protected abstract ActionResult execute(Target target, Source source);

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
            actions.add(new DelayedAction(TARGET_DEFAULT, Lists.newArrayList(this), new NumericValue(10)));
            path.saveModel();
            e.getWhoClicked().openInventory(parent.getInventory());
        } else if (e.getClick().equals(ClickType.NUMBER_KEY) && e.getHotbarButton() == 1) {
            actions.remove(this);
            path.addModel(this);
            path.deleteModel();
            path.popModel();
            onRemoval();
            actions.add(new TimerAction(TARGET_DEFAULT, 5, 10, Lists.newArrayList(this)));
            path.saveModel();
            e.getWhoClicked().openInventory(parent.getInventory());
        }
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(getClass().getAnnotation(Enumerable.Displayable.class).headTexture()),
                getName(), Libs.getLocale().getList("gui.action.description"));
    }

    @Override
    public abstract Action clone();

    public ItemStack targetItem() {
        return Libs.getWrapper().skullFromValue(TARGET_HEAD);
    }

    public abstract String getName();

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

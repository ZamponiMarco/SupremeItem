package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.CustomClickable;
import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.gui.model.ModelObjectInventoryHolder;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.entity.DamageAction;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@Enumerable.Child
@CustomClickable(customCollectionClickConsumer = "getCustomConsumer")
public class DelayedAction extends Action {

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = HEAD)
    private Action action;
    @Serializable(headTexture = HEAD)
    private int delay;

    public DelayedAction() {
        this(new DamageAction(), 10);
    }

    public static DelayedAction deserialize(Map<String, Object> map) {
        Action action = (Action) map.getOrDefault("action", new DamageAction());
        int delay = (int) map.getOrDefault("delay", 10);
        return new DelayedAction(action, delay);
    }

    @Override
    protected void execute(Target target, Source source) {
        Bukkit.getScheduler().runTaskLater(SupremeItem.getInstance(), () -> action.executeAction(target, source), delay);
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(EntityTarget.class, LocationTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(new ItemStack(Material.CLOCK), "&cDelayed", Lists.newArrayList());
    }

    public void getCustomConsumer(JavaPlugin plugin, PluginInventoryHolder parent, ModelPath<?> path, Field field,
                                  InventoryClickEvent e) throws IllegalAccessException {
        Collection<Action> actions = ((Collection<Action>) FieldUtils.readField(field,
                path.getLast() != null ? path.getLast() : path.getModelManager(), true));
        if (e.getClick().equals(ClickType.LEFT)) {
            path.addModel(this);
            e.getWhoClicked().openInventory(new ModelObjectInventoryHolder(plugin, parent, path).getInventory());
        } else if (e.getClick().equals(ClickType.RIGHT)
                || (e.getClick().equals(ClickType.NUMBER_KEY) && (e.getHotbarButton() == 0 || e.getHotbarButton() == 1))) {
            actions.remove(this);
            path.addModel(this);
            path.deleteModel();
            path.popModel();
            onRemoval();
            if (e.getHotbarButton() == 0) {
                actions.add(action);
                path.saveModel();
            }
            e.getWhoClicked().openInventory(parent.getInventory());
        }
    }
}

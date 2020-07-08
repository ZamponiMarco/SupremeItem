package com.github.jummes.supremeitem.action.meta;

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
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@Enumerable.Child
public class TimerAction extends MetaAction {

    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(headTexture = HEAD)
    private int timer;
    @Serializable(headTexture = HEAD)
    private int repetitions;
    @Serializable(headTexture = HEAD)
    private List<Action> actions;

    public TimerAction() {
        this(5, 10, Lists.newArrayList());
    }

    public static TimerAction deserialize(Map<String, Object> map) {
        int timer = (int) map.getOrDefault("timer", 5);
        int repetitions = (int) map.getOrDefault("repetitions", 10);
        List<Action> actions = (List<Action>) map.getOrDefault("actions", Lists.newArrayList());
        return new TimerAction(timer, repetitions, actions);
    }

    @Override
    protected void execute(Target target, Source source) {
        BukkitRunnable runnable = new BukkitRunnable() {
            private int counter = 0;

            @Override
            public void run() {
                if (counter >= repetitions) {
                    this.cancel();
                }
                actions.forEach(action -> action.executeAction(target, source));
                counter++;
            }
        };
        runnable.runTaskTimer(SupremeItem.getInstance(), 0, timer);
    }

    public void getCustomConsumer(JavaPlugin plugin, PluginInventoryHolder parent, ModelPath<?> path, Field field,
                                  InventoryClickEvent e) throws IllegalAccessException {
        Collection<Action> actions = ((Collection<Action>) FieldUtils.readField(field,
                path.getLast() != null ? path.getLast() : path.getModelManager(), true));
        if (e.getClick().equals(ClickType.LEFT)) {
            path.addModel(this);
            e.getWhoClicked().openInventory(new ModelObjectInventoryHolder(plugin, parent, path).getInventory());
        } else if (e.getClick().equals(ClickType.RIGHT)
                || (e.getClick().equals(ClickType.NUMBER_KEY) && (e.getHotbarButton() == 1))) {
            actions.remove(this);
            path.addModel(this);
            path.deleteModel();
            path.popModel();
            onRemoval();
            if (e.getHotbarButton() == 0) {
                actions.addAll(this.actions);
                path.saveModel();
            }
            e.getWhoClicked().openInventory(parent.getInventory());
        }
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(LocationTarget.class, EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(new ItemStack(Material.CLOCK), "&cTimer", Lists.newArrayList());
    }
}

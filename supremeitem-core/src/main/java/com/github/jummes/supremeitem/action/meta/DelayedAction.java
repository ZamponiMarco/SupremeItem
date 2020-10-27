package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.CustomClickable;
import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.gui.PluginInventoryHolder;
import com.github.jummes.libs.model.ModelPath;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lDelayed Action", description = "gui.action.delayed.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=")
@CustomClickable(customCollectionClickConsumer = "getCustomConsumer")
public class DelayedAction extends MetaAction {

    private static final NumericValue DELAY_DEFAULT = new NumericValue(10);

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    private static final String DELAY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.delayed.actions", additionalDescription = {"gui.additional-tooltips.value"})
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> actions;

    @Serializable(headTexture = DELAY_HEAD, description = "gui.action.delayed.delay")
    @Serializable.Number(minValue = 0, scale = 1)
    @Serializable.Optional(defaultValue = "DELAY_DEFAULT")
    private NumericValue delay;

    public DelayedAction() {
        this(TARGET_DEFAULT, Lists.newArrayList(), DELAY_DEFAULT.clone());
    }

    public DelayedAction(boolean target, List<Action> actions, NumericValue delay) {
        super(target);
        this.actions = actions;
        this.delay = delay;
    }

    public static DelayedAction deserialize(Map<String, Object> map) {
        List<Action> actions = (List<Action>) map.getOrDefault("actions", Lists.newArrayList());
        NumericValue delay;
        try {
            delay = (NumericValue) map.getOrDefault("delay", DELAY_DEFAULT.clone());
        } catch (ClassCastException e) {
            delay = new NumericValue(((Number) map.getOrDefault("delay", DELAY_DEFAULT.getValue())));
        }
        return new DelayedAction(TARGET_DEFAULT, actions, delay);
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        Bukkit.getScheduler().runTaskLater(SupremeItem.getInstance(), () ->
                actions.forEach(action -> action.executeAction(target, source)), delay.getRealValue(target, source).longValue());
        return ActionResult.SUCCESS;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(EntityTarget.class, LocationTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0="),
                "&6&lDelay: &c" + delay.getName(), Libs.getLocale().getList("gui.action.delayed.item-description"));
    }

    @Override
    public Action clone() {
        return new DelayedAction(TARGET_DEFAULT, actions.stream().map(Action::clone).collect(Collectors.toList()), delay.clone());
    }

    public void getCustomConsumer(JavaPlugin plugin, PluginInventoryHolder parent, ModelPath<?> path, Field field,
                                  InventoryClickEvent e) throws IllegalAccessException {
        getExtractConsumer(plugin, parent, path, field, e, this.actions);
    }

    @Override
    public ItemStack targetItem() {
        return null;
    }
}

package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lDelayed Action", description = "gui.action.meta.wrapper.delayed.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=")
public class DelayedAction extends WrapperAction {
    private static final NumericValue DELAY_DEFAULT = new NumericValue(10);

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    private static final String DELAY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.meta.wrapper.delayed.actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> actions;

    @Serializable(headTexture = DELAY_HEAD, description = "gui.action.meta.wrapper.delayed.delay")
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

    public DelayedAction(Map<String, Object> map) {
        super(map);
        this.actions = (List<Action>) map.getOrDefault("actions", Lists.newArrayList());
        this.actions.removeIf(Objects::isNull);
        this.delay = (NumericValue) map.getOrDefault("delay", DELAY_DEFAULT.clone());
    }

    @Override
    public ActionResult execute(Target target, Source source, Map<String, Object> map) {
        Bukkit.getScheduler().runTaskLater(SupremeItem.getInstance(), () ->
                        actions.forEach(action -> action.execute(target, source, new HashMap<>())),
                delay.getRealValue(target, source).longValue());
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new DelayedAction(TARGET_DEFAULT, actions.stream().map(Action::clone).collect(Collectors.toList()),
                delay.clone());
    }

    @Override
    public ItemStack targetItem() {
        return null;
    }

    @Override
    public String getName() {
        return "&6&lDelay: &c" + delay.getName();
    }

    @Override
    public List<Action> getWrappedActions() {
        return actions;
    }

}

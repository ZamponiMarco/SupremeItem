package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.SupremeItem;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.condition.bool.BooleanCondition;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lExecute until condition is true", description = "gui.action.meta.wrapper.repeat-until.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTIzMjI5ZjZlNjA2ZDkxYjdlNjdhMmJjZjNlMmEzMzMzYmE2MTNiNmQ2NDA5MTk5YjcxNjljMDYzODliMCJ9fX0=")
public class RepeatUntilAction extends WrapperAction {

    private static final int TIMER_DEFAULT = 5;

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    private static final String FINAL_ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkMWFiYTczZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYzNiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ==";
    private static final String CONDITION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmMyNzEwNTI3MTllZjY0MDc5ZWU4YzE0OTg5NTEyMzhhNzRkYWM0YzI3Yjk1NjQwZGI2ZmJkZGMyZDZiNWI2ZSJ9fX0=";
    private static final String TIMER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmZlOGNmZjc1ZjdkNDMzMjYwYWYxZWNiMmY3NzNiNGJjMzgxZDk1MWRlNGUyZWI2NjE0MjM3NzlhNTkwZTcyYiJ9fX0=";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.meta.wrapper.repeat-until.actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> actions;

    @Serializable(headTexture = FINAL_ACTIONS_HEAD, description = "gui.action.meta.wrapper.repeat-until.final-actions")
    private List<Action> finalActions;

    @Serializable(headTexture = CONDITION_HEAD, description = "gui.action.meta.wrapper.repeat-until.condition")
    private Condition condition;

    @Serializable(headTexture = TIMER_HEAD, description = "gui.action.meta.wrapper.repeat-until.timer")
    @Serializable.Number(minValue = 0, scale = 1)
    @Serializable.Optional(defaultValue = "TIMER_DEFAULT")
    private int timer;

    public RepeatUntilAction() {
        this(TARGET_DEFAULT, Lists.newArrayList(), Lists.newArrayList(), new BooleanCondition(), TIMER_DEFAULT);
    }

    public RepeatUntilAction(boolean target, List<Action> actions, List<Action> finalActions, Condition condition, int timer) {
        super(target);
        this.actions = actions;
        this.finalActions = finalActions;
        this.condition = condition;
        this.timer = timer;
    }

    public RepeatUntilAction(Map<String, Object> map) {
        super(map);
        this.actions = (List<Action>) map.getOrDefault("actions", Lists.newArrayList());
        this.actions.removeIf(Objects::isNull);
        this.finalActions = (List<Action>) map.getOrDefault("finalActions", Lists.newArrayList());
        this.finalActions.removeIf(Objects::isNull);
        this.condition = (Condition) map.getOrDefault("condition", new BooleanCondition());
        this.timer = (int) map.getOrDefault("timer", TIMER_DEFAULT);
    }

    @Override
    public List<Action> getWrappedActions() {
        return actions;
    }

    @Override
    public ActionResult execute(Target target, Source source, Map<String, Object> map) {
        getRunnable(target, source).runTaskTimer(SupremeItem.getInstance(), 0, timer);
        return ActionResult.SUCCESS;
    }

    private BukkitRunnable getRunnable(Target target, Source source) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (condition.checkCondition(target, source)) {
                    finalActions.forEach(action -> action.execute(target, source, new HashMap<>()));
                    this.cancel();
                    return;
                }

                actions.forEach(action -> action.execute(target, source, new HashMap<>()));
            }
        };
    }

    @Override
    public Action clone() {
        return new RepeatUntilAction(TARGET_DEFAULT, actions.stream().map(Action::clone).collect(Collectors.toList()),
                finalActions.stream().map(Action::clone).collect(Collectors.toList()), condition.clone(), timer);
    }

    @Override
    public ItemStack targetItem() {
        return null;
    }

    @Override
    public String getName() {
        return String.format("&6&lRepeat Until: %s", condition.getName());
    }
}

package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.condition.bool.BooleanCondition;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lCondition Action", description = "gui.action.condition.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmMyNzEwNTI3MTllZjY0MDc5ZWU4YzE0OTg5NTEyMzhhNzRkYWM0YzI3Yjk1NjQwZGI2ZmJkZGMyZDZiNWI2ZSJ9fX0=")
public class ConditionAction extends MetaAction {

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";
    private static final String CONDITION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmMyNzEwNTI3MTllZjY0MDc5ZWU4YzE0OTg5NTEyMzhhNzRkYWM0YzI3Yjk1NjQwZGI2ZmJkZGMyZDZiNWI2ZSJ9fX0=";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.condition.actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> actions;

    @Serializable(headTexture = CONDITION_HEAD, description = "gui.action.condition.condition")
    private Condition condition;

    public ConditionAction() {
        this(TARGET_DEFAULT, Lists.newArrayList(), new BooleanCondition());
    }

    public ConditionAction(boolean target, List<Action> actions, Condition condition) {
        super(target);
        this.actions = actions;
        this.condition = condition;
    }

    public static ConditionAction deserialize(Map<String, Object> map) {
        List<Action> actions = (List<Action>) map.getOrDefault("actions", Lists.newArrayList());
        Condition condition = (Condition) map.get("condition");
        return new ConditionAction(TARGET_DEFAULT, actions, condition);
    }

    @Override
    public ActionResult execute(Target target, Source source) {
        if (condition.checkCondition(target, source)) {
            if (actions.stream().anyMatch(action -> action.execute(target, source).equals(ActionResult.CANCELLED))) {
                return ActionResult.CANCELLED;
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public Action clone() {
        return new ConditionAction(TARGET_DEFAULT, actions.stream().map(Action::clone).collect(Collectors.toList()), condition.clone());
    }

    @Override
    public ItemStack targetItem() {
        return null;
    }

    @Override
    public String getName() {
        return String.format("&6&lCondition: %s", condition.getName());
    }
}

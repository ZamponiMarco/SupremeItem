package com.github.jummes.supremeitem.action.meta;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.LocationTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.condition.LessThanCondition;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Enumerable.Child
public class ConditionAction extends MetaAction {

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.area-entities.actions")
    private List<Action> actions;
    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.area-entities.actions")
    private Condition condition;

    public ConditionAction() {
        this(Lists.newArrayList(), new LessThanCondition());
    }

    public static ConditionAction deserialize(Map<String, Object> map) {
        List<Action> actions = (List<Action>) map.get("actions");
        Condition condition = (Condition) map.get("condition");
        return new ConditionAction(actions, condition);
    }

    @Override
    protected ActionResult execute(Target target, Source source) {
        if (condition.testCondition(target, source)) {
            if (actions.stream().anyMatch(action -> action.executeAction(target, source).equals(ActionResult.CANCELLED))) {
                return ActionResult.CANCELLED;
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public List<Class<? extends Target>> getPossibleTargets() {
        return Lists.newArrayList(LocationTarget.class, EntityTarget.class);
    }

    @Override
    public ItemStack getGUIItem() {
        return super.getGUIItem();
    }
}

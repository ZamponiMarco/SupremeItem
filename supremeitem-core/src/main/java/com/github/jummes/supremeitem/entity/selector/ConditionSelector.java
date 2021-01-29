package com.github.jummes.supremeitem.entity.selector;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.AlwaysTrueCondition;
import com.github.jummes.supremeitem.condition.Condition;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.function.Predicate;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lFilter entities with Condition", description = "gui.entity.selector.condition.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmMyNzEwNTI3MTllZjY0MDc5ZWU4YzE0OTg5NTEyMzhhNzRkYWM0YzI3Yjk1NjQwZGI2ZmJkZGMyZDZiNWI2ZSJ9fX0")
public class ConditionSelector extends EntitySelector {

    private static final String CONDITION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmMyNzEwNTI3MTllZjY0MDc5ZWU4YzE0OTg5NTEyMzhhNzRkYWM0YzI3Yjk1NjQwZGI2ZmJkZGMyZDZiNWI2ZSJ9fX0";

    @Serializable(headTexture = CONDITION_HEAD, description = "gui.entity.selector.condition.condition")
    private Condition condition;

    public ConditionSelector() {
        this(DENY_DEFAULT, new AlwaysTrueCondition());
    }

    public ConditionSelector(Map<String, Object> map) {
        super(map);
        this.condition = (Condition) map.getOrDefault("condition", new AlwaysTrueCondition());
    }

    public ConditionSelector(boolean deny, Condition condition) {
        super(deny);
        this.condition = condition;
    }

    @Override
    protected Predicate<LivingEntity> getAbstractFilter(Source source, Target target) {
        return e -> condition.checkCondition(new EntityTarget(e), source);
    }

    @Override
    public EntitySelector clone() {
        return new ConditionSelector(deny, condition.clone());
    }

    @Override
    protected String getAbstractName() {
        return "&6&lCondition Selector: &c" + condition.getName();
    }
}

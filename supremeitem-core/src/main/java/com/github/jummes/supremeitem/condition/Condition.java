package com.github.jummes.supremeitem.condition;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.numeric.NumericCondition;

@Enumerable.Parent(classArray = {NumericCondition.class, BooleanCondition.class})
public abstract class Condition implements Model {

    protected static final boolean NEGATE_DEFAULT = false;

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.condition.negate")
    @Serializable.Optional(defaultValue = "NEGATE_DEFAULT")
    private boolean negate;

    public Condition(boolean negate) {
        this.negate = negate;
    }

    public boolean checkCondition(Target target, Source source) {
        if (negate) {
            return !testCondition(target, source);
        }
        return testCondition(target, source);
    }

    public abstract boolean testCondition(Target target, Source source);

}
package com.github.jummes.supremeitem.condition.numeric;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.ConstantNumberPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;

import java.util.Map;

@Enumerable.Child
public class EqualsCondition extends NumericCondition {

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.area-entities.actions")
    private NumericPlaceholder operandOne;
    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.area-entities.actions")
    private NumericPlaceholder operandTwo;

    public EqualsCondition() {
        this(false, new ConstantNumberPlaceholder(), new ConstantNumberPlaceholder());
    }

    public EqualsCondition(boolean negate, NumericPlaceholder operandOne, NumericPlaceholder operandTwo) {
        super(negate);
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
    }

    public static EqualsCondition deserialize(Map<String, Object> map) {
        boolean negate = (boolean) map.get("negate");
        NumericPlaceholder operandOne = (NumericPlaceholder) map.get("operandOne");
        NumericPlaceholder operandTwo = (NumericPlaceholder) map.get("operandTwo");
        return new EqualsCondition(negate, operandOne, operandTwo);
    }

    @Override
    public boolean testCondition(Target target, Source source) {
        return operandOne.computePlaceholder(target, source) < operandTwo.computePlaceholder(target, source);
    }
}

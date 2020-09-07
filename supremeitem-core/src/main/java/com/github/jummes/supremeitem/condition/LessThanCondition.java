package com.github.jummes.supremeitem.condition;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.ConstantNumberPlaceholder;
import com.github.jummes.supremeitem.placeholder.NumericPlaceholder;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@Enumerable.Child
public class LessThanCondition extends Condition {

    private static final String ACTIONS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIxNmVlNDA1OTNjMDk4MWVkMjhmNWJkNjc0ODc5NzgxYzQyNWNlMDg0MWI2ODc0ODFjNGY3MTE4YmI1YzNiMSJ9fX0=";

    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.area-entities.actions")
    private NumericPlaceholder operandOne;
    @Serializable(headTexture = ACTIONS_HEAD, description = "gui.action.area-entities.actions")
    private NumericPlaceholder operandTwo;

    public LessThanCondition() {
        this(new ConstantNumberPlaceholder(), new ConstantNumberPlaceholder());
    }

    public static LessThanCondition deserialize(Map<String, Object> map) {
        NumericPlaceholder operandOne = (NumericPlaceholder) map.get("operandOne");
        NumericPlaceholder operandTwo = (NumericPlaceholder) map.get("operandTwo");
        return new LessThanCondition(operandOne, operandTwo);
    }

    @Override
    public boolean testCondition(Target target, Source source) {
        return operandOne.computePlaceholder(target, source) < operandTwo.computePlaceholder(target, source);
    }
}

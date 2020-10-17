package com.github.jummes.supremeitem.placeholder.numeric.operator;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import com.github.jummes.supremeitem.placeholder.numeric.NumericValue;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lNumber Sum Placeholder", description = "gui.placeholder.double.operator.sum.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdhMGZjNmRjZjczOWMxMWZlY2U0M2NkZDE4NGRlYTc5MWNmNzU3YmY3YmQ5MTUzNmZkYmM5NmZhNDdhY2ZiIn19fQ==")
public class SumPlaceholder extends NumberOperatorPlaceholder {

    @Serializable(headTexture = ONE_HEAD, description = "gui.placeholder.double.operator.operand-one")
    private NumericValue operandOne;
    @Serializable(headTexture = TWO_HEAD, description = "gui.placeholder.double.operator.operand-two")
    private NumericValue operandTwo;

    public SumPlaceholder() {
        this(TARGET_DEFAULT, OPERAND_ONE_DEFAULT.clone(), OPERAND_TWO_DEFAULT.clone());
    }

    public SumPlaceholder(boolean target, NumericValue operandOne, NumericValue operandTwo) {
        super(target);
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
    }

    public static SumPlaceholder deserialize(Map<String, Object> map) {
        return new SumPlaceholder();
    }

    @Override
    public NumericPlaceholder clone() {
        return new SumPlaceholder(target, operandOne.clone(), operandTwo.clone());
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        return operandOne.getRealValue(target, source) + operandTwo.getRealValue(target, source);
    }

    @Override
    public String getName() {
        return operandOne.getName() + " &6&l+&c " + operandTwo.getName();
    }
}

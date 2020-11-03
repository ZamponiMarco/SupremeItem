package com.github.jummes.supremeitem.placeholder.numeric.operator;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import com.github.jummes.supremeitem.value.NumericValue;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lNumber Difference Placeholder", description = "gui.placeholder.double.operator.difference.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ2YjEyOTNkYjcyOWQwMTBmNTM0Y2UxMzYxYmJjNTVhZTVhOGM4ZjgzYTE5NDdhZmU3YTg2NzMyZWZjMiJ9fX0===")
public class DifferencePlaceholder extends NumberOperatorPlaceholder {

    @Serializable(headTexture = ONE_HEAD, description = "gui.placeholder.double.operator.operand-one")
    private NumericValue operandOne;
    @Serializable(headTexture = TWO_HEAD, description = "gui.placeholder.double.operator.operand-two")
    private NumericValue operandTwo;

    public DifferencePlaceholder() {
        this(TARGET_DEFAULT, OPERAND_ONE_DEFAULT.clone(), OPERAND_TWO_DEFAULT.clone());
    }

    public DifferencePlaceholder(boolean target, NumericValue operandOne, NumericValue operandTwo) {
        super(target);
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
    }

    public static DifferencePlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        NumericValue operandOne = (NumericValue) map.getOrDefault("operandOne", OPERAND_ONE_DEFAULT.clone());
        NumericValue operandTwo = (NumericValue) map.getOrDefault("operandTwo", OPERAND_TWO_DEFAULT.clone());
        return new DifferencePlaceholder(target, operandOne, operandTwo);
    }

    @Override
    public NumericPlaceholder clone() {
        return new DifferencePlaceholder(target, operandOne.clone(), operandTwo.clone());
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        return operandOne.getRealValue(target, source) -
                operandTwo.getRealValue(target, source);
    }

    @Override
    public String getName() {
        return operandOne.getName() + " &6&l-&c " + operandTwo.getName();
    }
}
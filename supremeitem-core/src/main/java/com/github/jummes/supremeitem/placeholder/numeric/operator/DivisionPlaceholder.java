package com.github.jummes.supremeitem.placeholder.numeric.operator;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import com.github.jummes.supremeitem.value.NumericValue;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Setter
@Getter
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lNumber Division Placeholder", description = "gui.placeholder.double.operator.division.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RmMDdhYWZlMWExZmJlNzgwNDgzNGU4ZDYxN2NiZGZjNjY0OTNhOTQxYzExZDg3ZmI3MTQxZTU3ODk3ZjgzIn19fQ==")
public class DivisionPlaceholder extends NumberOperatorPlaceholder {

    @Serializable(headTexture = ONE_HEAD, description = "gui.placeholder.double.operator.operand-one", additionalDescription = {"gui.additional-tooltips.value"})
    private NumericValue operandOne;
    @Serializable(headTexture = TWO_HEAD, description = "gui.placeholder.double.operator.operand-two", additionalDescription = {"gui.additional-tooltips.value"})
    private NumericValue operandTwo;

    public DivisionPlaceholder() {
        this(TARGET_DEFAULT, OPERAND_ONE_DEFAULT.clone(), OPERAND_TWO_DEFAULT.clone());
    }

    public DivisionPlaceholder(boolean target, NumericValue operandOne, NumericValue operandTwo) {
        super(target);
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
    }

    public static DivisionPlaceholder deserialize(Map<String, Object> map) {
        boolean target = (boolean) map.getOrDefault("target", TARGET_DEFAULT);
        NumericValue operandOne = (NumericValue) map.getOrDefault("operandOne", OPERAND_ONE_DEFAULT.clone());
        NumericValue operandTwo = (NumericValue) map.getOrDefault("operandTwo", OPERAND_TWO_DEFAULT.clone());
        return new DivisionPlaceholder(target, operandOne, operandTwo);
    }

    @Override
    public NumericPlaceholder clone() {
        return new DivisionPlaceholder(target, operandOne.clone(), operandTwo.clone());
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        return operandOne.getRealValue(target, source) /
                operandTwo.getRealValue(target, source);
    }

    @Override
    public String getName() {
        return operandOne.getName() + " &6&l/&c " + operandTwo.getName();
    }
}
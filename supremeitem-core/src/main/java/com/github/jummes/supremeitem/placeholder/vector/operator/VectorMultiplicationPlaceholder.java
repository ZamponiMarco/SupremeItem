package com.github.jummes.supremeitem.placeholder.vector.operator;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.math.Vector;
import com.github.jummes.supremeitem.placeholder.vector.VectorPlaceholder;
import com.github.jummes.supremeitem.value.NumericValue;
import com.github.jummes.supremeitem.value.VectorValue;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lVector Multiplication Placeholder", description = "gui.placeholder.vector.operator.multiplication.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxZDZlZGE4M2VkMmMyNGRjZGNjYjFlMzNkZjM2OTRlZWUzOTdhNTcwMTIyNTViZmM1NmEzYzI0NGJjYzQ3NCJ9fX0=")
public class VectorMultiplicationPlaceholder extends VectorOperatorPlaceholder {

    @Serializable(headTexture = ONE_HEAD, description = "gui.placeholder.vector.operator.operand-one",
            additionalDescription = {"gui.additional-tooltips.value"})
    private VectorValue operandOne;
    @Serializable(headTexture = TWO_HEAD, description = "gui.placeholder.vector.operator.operand-two",
            additionalDescription = {"gui.additional-tooltips.value"})
    private NumericValue operandTwo;

    public VectorMultiplicationPlaceholder() {
        this(TARGET_DEFAULT, new VectorValue(), new NumericValue());
    }

    public VectorMultiplicationPlaceholder(boolean target, VectorValue operandOne, NumericValue operandTwo) {
        super(target);
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
    }

    public VectorMultiplicationPlaceholder(Map<String, Object> map) {
        super(map);
        this.operandOne = (VectorValue) map.getOrDefault("operandOne", new VectorValue());
        this.operandTwo = (NumericValue) map.getOrDefault("operandTwo", new NumericValue());
    }

    @Override
    public Vector computePlaceholder(Target target, Source source) {
        return new Vector(operandOne.getRealValue(target, source).computeVector(target, source).multiply(operandTwo.
                getRealValue(target, source)));
    }

    @Override
    public String getName() {
        return operandOne.getName() + " &6&lx&c " + operandTwo.getName();
    }

    @Override
    public VectorPlaceholder clone() {
        return new VectorMultiplicationPlaceholder(TARGET_DEFAULT, operandOne.clone(), operandTwo.clone());
    }
}

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
@Enumerable.Displayable
public class VectorMultiplicationPlaceholder extends VectorOperatorPlaceholder {
    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";

    @Serializable(headTexture = NAME_HEAD, description = "gui.placeholder.double.variable.name")
    private VectorValue operandOne;
    @Serializable(headTexture = NAME_HEAD, description = "gui.placeholder.double.variable.name")
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
        return "Vector multiply";
    }

    @Override
    public VectorPlaceholder clone() {
        return new VectorMultiplicationPlaceholder(TARGET_DEFAULT, operandOne.clone(), operandTwo.clone());
    }
}

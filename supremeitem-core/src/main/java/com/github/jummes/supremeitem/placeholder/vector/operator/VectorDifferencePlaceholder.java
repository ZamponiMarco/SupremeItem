package com.github.jummes.supremeitem.placeholder.vector.operator;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.math.Vector;
import com.github.jummes.supremeitem.placeholder.vector.VectorPlaceholder;
import com.github.jummes.supremeitem.value.VectorValue;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&c&lVector Difference Placeholder", description = "gui.placeholder.vector.operator.difference.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWQ2YjEyOTNkYjcyOWQwMTBmNTM0Y2UxMzYxYmJjNTVhZTVhOGM4ZjgzYTE5NDdhZmU3YTg2NzMyZWZjMiJ9fX0")
public class VectorDifferencePlaceholder extends VectorOperatorPlaceholder {
    @Serializable(headTexture = ONE_HEAD, description = "gui.placeholder.vector.operator.operand-one",
            additionalDescription = {"gui.additional-tooltips.value"})
    private VectorValue operandOne;
    @Serializable(headTexture = TWO_HEAD, description = "gui.placeholder.vector.operator.operand-two",
            additionalDescription = {"gui.additional-tooltips.value"})
    private VectorValue operandTwo;

    public VectorDifferencePlaceholder() {
        this(TARGET_DEFAULT, new VectorValue(), new VectorValue());
    }

    public VectorDifferencePlaceholder(boolean target, VectorValue operandOne, VectorValue operandTwo) {
        super(target);
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
    }

    public VectorDifferencePlaceholder(Map<String, Object> map) {
        super(map);
        this.operandOne = (VectorValue) map.getOrDefault("operandOne", new VectorValue());
        this.operandTwo = (VectorValue) map.getOrDefault("operandTwo", new VectorValue());
    }

    @Override
    public Vector computePlaceholder(Target target, Source source) {
        return new Vector(operandOne.getRealValue(target, source).computeVector(target, source).subtract(operandTwo.
                getRealValue(target, source).computeVector(target, source)));
    }

    @Override
    public String getName() {
        return operandOne.getName() + " &6&l-&c " + operandTwo.getName();
    }

    @Override
    public VectorPlaceholder clone() {
        return new VectorDifferencePlaceholder(TARGET_DEFAULT, operandOne.clone(), operandTwo.clone());
    }
}

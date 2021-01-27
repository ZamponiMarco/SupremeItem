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
@Enumerable.Displayable(name = "&c&lVector Sum Placeholder", description = "gui.placeholder.vector.operator.sum.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdhMGZjNmRjZjczOWMxMWZlY2U0M2NkZDE4NGRlYTc5MWNmNzU3YmY3YmQ5MTUzNmZkYmM5NmZhNDdhY2ZiIn19fQ==")
public class VectorSumPlaceholder extends VectorOperatorPlaceholder {
    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";

    @Serializable(headTexture = ONE_HEAD, description = "gui.placeholder.vector.operator.operand-one",
            additionalDescription = {"gui.additional-tooltips.value"})
    private VectorValue operandOne;
    @Serializable(headTexture = TWO_HEAD, description = "gui.placeholder.vector.operator.operand-two",
            additionalDescription = {"gui.additional-tooltips.value"})
    private VectorValue operandTwo;

    public VectorSumPlaceholder() {
        this(TARGET_DEFAULT, new VectorValue(), new VectorValue());
    }

    public VectorSumPlaceholder(boolean target, VectorValue operandOne, VectorValue operandTwo) {
        super(target);
        this.operandOne = operandOne;
        this.operandTwo = operandTwo;
    }

    public VectorSumPlaceholder(Map<String, Object> map) {
        super(map);
        this.operandOne = (VectorValue) map.getOrDefault("operandOne", new VectorValue());
        this.operandTwo = (VectorValue) map.getOrDefault("operandTwo", new VectorValue());
    }

    @Override
    public Vector computePlaceholder(Target target, Source source) {
        return new Vector(operandOne.getRealValue(target, source).computeVector(target, source).add(operandTwo.
                getRealValue(target, source).computeVector(target, source)));
    }

    @Override
    public String getName() {
        return operandOne.getName() + " &6&l+&c " + operandTwo.getName();
    }

    @Override
    public VectorPlaceholder clone() {
        return new VectorSumPlaceholder(TARGET_DEFAULT, operandOne.clone(), operandTwo.clone());
    }
}

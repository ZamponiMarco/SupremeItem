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
@Enumerable.Displayable(name = "&c&lNormalize Vector Placeholder", description = "gui.placeholder.vector.operator.normalize.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==")
public class VectorNormalizePlaceholder extends VectorOperatorPlaceholder {

    @Serializable(headTexture = ONE_HEAD, description = "gui.placeholder.vector.operator.operand-one",
            additionalDescription = {"gui.additional-tooltips.value"})
    private VectorValue operandOne;

    public VectorNormalizePlaceholder() {
        this(TARGET_DEFAULT, new VectorValue());
    }

    public VectorNormalizePlaceholder(boolean target, VectorValue operandOne) {
        super(target);
        this.operandOne = operandOne;
    }

    public VectorNormalizePlaceholder(Map<String, Object> map) {
        super(map);
        this.operandOne = (VectorValue) map.getOrDefault("operandOne", new Vector());
    }

    @Override
    public Vector computePlaceholder(Target target, Source source) {
        return new Vector(operandOne.getRealValue(target, source).computeVector(target, source).normalize());
    }

    @Override
    public String getName() {
        return operandOne.getName() + " &6&l Normalized&c ";
    }

    @Override
    public VectorPlaceholder clone() {
        return new VectorNormalizePlaceholder(TARGET_DEFAULT, operandOne.clone());
    }
}

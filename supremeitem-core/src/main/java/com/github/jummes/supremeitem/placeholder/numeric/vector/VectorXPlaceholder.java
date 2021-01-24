package com.github.jummes.supremeitem.placeholder.numeric.vector;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import com.github.jummes.supremeitem.placeholder.vector.VectorPlaceholder;
import com.github.jummes.supremeitem.placeholder.vector.entity.DirectionPlaceholder;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable
public class VectorXPlaceholder extends NumericVectorPlaceholder {

    public VectorXPlaceholder() {
        this(TARGET_DEFAULT, new DirectionPlaceholder());
    }

    public VectorXPlaceholder(boolean target, VectorPlaceholder placeholder) {
        super(target, placeholder);
    }

    public VectorXPlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        return placeholder.computePlaceholder(target, source).getX().getRealValue(target, source);
    }

    @Override
    public String getName() {
        return "Vector X";
    }

    @Override
    public NumericPlaceholder clone() {
        return new VectorXPlaceholder(target, placeholder.clone());
    }
}

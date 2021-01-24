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
public class VectorYPlaceholder extends NumericVectorPlaceholder {

    public VectorYPlaceholder() {
        this(TARGET_DEFAULT, new DirectionPlaceholder());
    }

    public VectorYPlaceholder(boolean target, VectorPlaceholder placeholder) {
        super(target, placeholder);
    }

    public VectorYPlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        return placeholder.computePlaceholder(target, source).getY().getRealValue(target, source);
    }

    @Override
    public String getName() {
        return "Vector Y";
    }

    @Override
    public NumericPlaceholder clone() {
        return new VectorYPlaceholder(target, placeholder.clone());
    }
}
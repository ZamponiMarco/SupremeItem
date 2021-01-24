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
public class VectorZPlaceholder extends NumericVectorPlaceholder {

    public VectorZPlaceholder() {
        this(TARGET_DEFAULT, new DirectionPlaceholder());
    }

    public VectorZPlaceholder(boolean target, VectorPlaceholder placeholder) {
        super(target, placeholder);
    }

    public VectorZPlaceholder(Map<String, Object> map) {
        super(map);
    }

    @Override
    public Double computePlaceholder(Target target, Source source) {
        return placeholder.computePlaceholder(target, source).getZ().getRealValue(target, source);
    }

    @Override
    public String getName() {
        return "Vector Z";
    }

    @Override
    public NumericPlaceholder clone() {
        return new VectorZPlaceholder(target, placeholder.clone());
    }
}
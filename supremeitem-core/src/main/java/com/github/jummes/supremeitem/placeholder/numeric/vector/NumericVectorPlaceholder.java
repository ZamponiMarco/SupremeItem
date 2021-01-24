package com.github.jummes.supremeitem.placeholder.numeric.vector;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.placeholder.numeric.NumericPlaceholder;
import com.github.jummes.supremeitem.placeholder.vector.VectorPlaceholder;
import com.github.jummes.supremeitem.placeholder.vector.entity.DirectionPlaceholder;

import java.util.Map;

@Enumerable.Displayable
@Enumerable.Parent(classArray = {VectorXPlaceholder.class, VectorYPlaceholder.class, VectorZPlaceholder.class})
public abstract class NumericVectorPlaceholder extends NumericPlaceholder {

    protected VectorPlaceholder placeholder;

    public NumericVectorPlaceholder(boolean target, VectorPlaceholder placeholder) {
        super(target);
        this.placeholder = placeholder;
    }

    public NumericVectorPlaceholder(Map<String, Object> map) {
        super(map);
        this.placeholder = (VectorPlaceholder) map.getOrDefault("placeholder", new DirectionPlaceholder());
    }
}

package com.github.jummes.supremeitem.placeholder.vector;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.math.Vector;
import com.github.jummes.supremeitem.placeholder.Placeholder;
import com.github.jummes.supremeitem.placeholder.vector.entity.EntityVectorPlaceholder;
import com.github.jummes.supremeitem.placeholder.vector.operator.VectorOperatorPlaceholder;

import java.util.Map;


@Enumerable.Displayable
@Enumerable.Parent(classArray = {EntityVectorPlaceholder.class, VectorOperatorPlaceholder.class,
        VectorVariablePlaceholder.class, VectorFromLocationPlaceholder.class})
public abstract class VectorPlaceholder extends Placeholder<Vector> {

    public VectorPlaceholder(boolean target) {
        super(target);
    }

    public VectorPlaceholder(Map<String, Object> map) {
        super(map);
    }

    public abstract VectorPlaceholder clone();
}
